package com.bftcom.bots;

import com.bftcom.intf.IBot;
import com.bftcom.intf.IMessenger;
import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.objmodel.Message;
import com.bftcom.ws.objmodel.TextMessage;
import com.sun.jersey.core.util.Base64;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;
import java.net.Proxy;

import static com.bftcom.ws.objmodel.Message.MESSAGE_TYPE_TEXT;

public class SlackBot implements IBot {
  //Токен закодирован в base64, чтобы ботов не банили после заливки кода на github
  private String bot_token;
  private String bot_userName;
  private String bot_protocol;

  private static SlackSession session;
  private IMessenger msgr;

  @Override
  public void init(Configurator.Config cfg) {
    bot_token = cfg.getParam("token");
    bot_userName = cfg.getParam("name");
    bot_protocol = cfg.getParam("protocol");
    session = SlackSessionFactory.createWebSocketSlackSession(getBotToken());
    try {
      session.connect();
      session.addMessagePostedListener(messageListener);
    } catch (IOException e) {
      BotLogger.error("Bot register error", e);
    }
  }

  @Override
  public void setMessenger(IMessenger msgr) {
    this.msgr = msgr;
  }

  private SlackMessagePostedListener messageListener = new SlackMessagePostedListener() {
    @Override
    public void onEvent(SlackMessagePosted event, SlackSession session) {
      //Сообщения от ботов не принимаем
      if (event.getSender().isBot())
        return;

      com.bftcom.ws.objmodel.Update wsUpdate = new com.bftcom.ws.objmodel.Update();

      wsUpdate.setContactId(event.getSender().getId());
      wsUpdate.setFirstName(event.getSender().getRealName());
      wsUpdate.setLastName("");
      wsUpdate.setUserName(event.getSender().getUserName());

      wsUpdate.setChatId(event.getChannel().getId());
      wsUpdate.setIsGroupChat(!event.getChannel().isDirect());
      if (wsUpdate.isIsGroupChat())
        wsUpdate.setChatName(event.getChannel().getName());
      else
        wsUpdate.setChatName(event.getSender().getUserName());

      wsUpdate.setText(event.getMessageContent());
      wsUpdate.setDate(((Double) Double.parseDouble(event.getTimeStamp())).longValue());

      msgr.onUpdate(wsUpdate);
    }
  };

  @Override
  public String getBotToken() {
    return Base64.base64Decode(bot_token);
  }

  @Override
  public String getName() {
    return bot_userName;
  }

  @Override
  public String getProtocol() {
    return bot_protocol;
  }

  @Override
  public void sendMessage(String channelId, Message msg) {
    switch (msg.getMessageType()) {
      case MESSAGE_TYPE_TEXT:
        sendMessageToChannel(session, channelId, ((TextMessage) msg).getText());
        break;
    }
  }

  private void sendMessageToChannel(SlackSession session, String channelId, String message){
    SlackChannel channel = session.findChannelById(channelId);
    session.sendMessage(channel, message);
  }
}
