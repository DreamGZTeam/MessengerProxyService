package com.bftcom.bots;

import com.bftcom.intf.IBot;
import com.bftcom.ws.objmodel.Message;
import com.bftcom.ws.objmodel.Messenger;
import com.bftcom.ws.objmodel.TextMessage;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;

import static com.bftcom.ws.objmodel.Message.MESSAGE_TYPE_TEXT;

public class SlackBot implements IBot {
  public static final String BOT_TOKEN = "xoxb-81138594657-6w5wBkII5CkOyImtowilOZ71";

  static SlackSession session;
  static SlackBot slackBot;

  public static SlackBot getInstance() {
    if (slackBot == null) {
      slackBot = new SlackBot();
    }
    return slackBot;
  }

  /** Тест */
  public static void main(String args[]) {
    session = SlackSessionFactory.createWebSocketSlackSession(BOT_TOKEN);
    session.refetchUsers();
    SlackBot bot = SlackBot.getInstance();
    bot.sendDirectMessageToAUser(session, "a.frolovsky", "test message");
  }

  @Override
  public void setMessenger(Messenger msgr) {
    session = SlackSessionFactory.createWebSocketSlackSession(BOT_TOKEN);
    try {
      session.connect();
    } catch (IOException e) {
      BotLogger.error("Bot register error", e);
    }
  }

  @Override
  public String getBotToken() {
    return BOT_TOKEN;
  }

  @Override
  public String getProtocol() {
    return "Slack";
  }

  @Override
  public void sendMessage(String username, Message msg) {
    SendMessage sendMessageRequest = new SendMessage();
    sendMessageRequest.setChatId(username);
    switch (msg.getMessageType()) {
      case MESSAGE_TYPE_TEXT:
        sendMessageRequest.setText(((TextMessage) msg).getText());
        break;
    }
    sendDirectMessageToAUser(session, username, ((TextMessage) msg).getText());
  }

  public void sendDirectMessageToAUser(SlackSession session, String userName, String message) {
    SlackUser user = session.findUserByUserName(userName);
    session.sendMessageToUser(user, message, null);
  }
}
