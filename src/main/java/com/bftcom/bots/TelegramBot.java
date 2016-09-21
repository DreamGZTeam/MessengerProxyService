package com.bftcom.bots;

import com.bftcom.intf.IBot;
import com.bftcom.intf.IMessenger;
import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.objmodel.TextMessage;
import com.sun.jersey.core.util.Base64;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.BotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import static com.bftcom.ws.objmodel.Message.MESSAGE_TYPE_TEXT;

public class TelegramBot extends TelegramLongPollingBot implements IBot {

  private IMessenger msgr;
  //Токен закодирован в base64, чтобы ботов не банили после заливки кода на github
  private String bot_token;
  private String bot_userName;
  private String bot_protocol;

  public TelegramBot(BotOptions options) {
    super(options);
  }

  public TelegramBot() {
    super();
  }

//  public static TelegramBot getInstance() {
//    if (telegramBot == null) {
//      Configurator.Config cfg = Configurator.getInstance().getConfig(BOT_USERNAME);
//      BotOptions options = null;
//      if (cfg != null){
//        setBotToken(cfg.getParam("token"));
//        if (Boolean.parseBoolean(cfg.getParam("useProxy"))){
//          options = new BotOptions();
//          options.setProxyHost(cfg.getParam("proxyHost"));
//          options.setProxyPort(Integer.parseInt(cfg.getParam("proxyPort")));
//        }
//      }
//      telegramBot = options == null ? new TelegramBot() : new TelegramBot(options);
//
//      TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//      try {
//        telegramBotsApi.registerBot(telegramBot);
//      } catch (TelegramApiException e) {
//        BotLogger.error("Bot register error", e);
//      }
//    }
//    return telegramBot;
//  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      msgr.onUpdate(transformUpdateObject(update));
    }
  }

  private com.bftcom.ws.objmodel.Update transformUpdateObject(Update update) {
    if (update.getMessage().getText() == null || update.getMessage().getText().equals("") ||
        update.getMessage().getFrom().getId() == null)
      return null;
    com.bftcom.ws.objmodel.Update wsUpdate = new com.bftcom.ws.objmodel.Update();
    Message message = update.getMessage();

    wsUpdate.setContactId(message.getFrom().getId().toString());
    wsUpdate.setFirstName(message.getFrom().getFirstName());
    wsUpdate.setLastName(message.getFrom().getLastName());
    wsUpdate.setUserName(message.getFrom().getUserName());

    wsUpdate.setChatId(message.getChatId().toString());
    wsUpdate.setIsGroupChat(message.getChat().isGroupChat());
    if (wsUpdate.isIsGroupChat())
      wsUpdate.setChatName(message.getChat().getTitle());
    else
      wsUpdate.setChatName(message.getChat().getFirstName() + " " + message.getChat().getLastName());

    wsUpdate.setText(message.getText());
    wsUpdate.setDate(message.getDate().longValue());

    return wsUpdate;
  }

  @Override
  public String getBotUsername() {
    return bot_userName;
  }

  @Override
  public void init(Configurator.Config cfg) {
    bot_token = cfg.getParam("token");
    bot_userName = cfg.getParam("name");
    bot_protocol = cfg.getParam("protocol");
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      telegramBotsApi.registerBot(this);
    } catch (TelegramApiException e) {
      BotLogger.error("Bot register error", e);
    }
  }

  @Override
  public void setMessenger(IMessenger msgr) {
    this.msgr = msgr;
  }

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
  public void sendMessage(String chatId, com.bftcom.ws.objmodel.Message msg) {
    SendMessage sendMessageRequest = new SendMessage();
    sendMessageRequest.setChatId(chatId);
    switch (msg.getMessageType()) {
      case MESSAGE_TYPE_TEXT:
        sendMessageRequest.setText(((TextMessage) msg).getText());
        break;
    }
    try {
      sendMessage(sendMessageRequest);
    } catch (TelegramApiException e) {
      //do some error handling
    }
  }


}
