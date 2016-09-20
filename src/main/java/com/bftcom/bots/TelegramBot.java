package com.bftcom.bots;

import com.bftcom.intf.IBot;
import com.bftcom.intf.IMessenger;
import com.bftcom.ws.objmodel.Messenger;
import com.bftcom.ws.objmodel.TextMessage;
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
  public static final String BOT_USERNAME = "GZGZ2Bot";
  public static final String BOT_TOKEN = "263209527:AAETQX4khnVcbd5xSw9uQCBRXJ8N_vuVuYU";

  private IMessenger msgr;
  private static TelegramBot telegramBot;

  public TelegramBot(BotOptions options) {
    super(options);
  }

  public TelegramBot() {
    super();
  }

  public static void main(String[] args) {
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      BotOptions options = new BotOptions();
      options.setProxyHost("proxy.bftcom.com");
      options.setProxyPort(8080);
      TelegramBot telegramBot = new TelegramBot(options) {
        @Override
        public void onUpdateReceived(Update update) {
          //check if the update has a message
          if (update.hasMessage()) {
            Message message = update.getMessage();

            //check if the message has text. it could also contain for example a location ( message.hasLocation() )
            if (message.hasText()) {
              //create an object that contains the information to send back the message
              SendMessage sendMessageRequest = new SendMessage();
              sendMessageRequest.setChatId(message.getChatId().toString()); //who should get from the message the sender that sent it.
              sendMessageRequest.setText("you said: " + message.getText());
              try {
                sendMessage(sendMessageRequest); //at the end, so some magic and send the message ;)
              } catch (TelegramApiException e) {
                //do some error handling
              }
            }
          }
        }
      };
      telegramBotsApi.registerBot(telegramBot);

//      telegramBot.sendMessage("Hello, I'm Bot");

    } catch (TelegramApiException e) {
      BotLogger.error("Bot register error", e);
    }
  }

  public static TelegramBot getInstance() {
    if (telegramBot == null) {
      BotOptions options = new BotOptions();
      options.setProxyHost("proxy.bftcom.com");
      options.setProxyPort(8080);
      telegramBot = new TelegramBot(options);
      TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
      try {
        telegramBotsApi.registerBot(telegramBot);
      } catch (TelegramApiException e) {
        BotLogger.error("Bot register error", e);
      }
    }
    return telegramBot;
  }

  public void sendMessage(String text) {
    SendMessage sendMessageRequest = new SendMessage();
    sendMessageRequest.setChatId("-179689831");
    sendMessageRequest.setText(text);
    try {
      sendMessage(sendMessageRequest);
    } catch (TelegramApiException e) {
      //do some error handling
    }
  }

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
    wsUpdate.setDate(message.getDate());

    return wsUpdate;
  }

  @Override
  public String getBotUsername() {
    return BOT_USERNAME;
  }

  @Override
  public void setMessenger(Messenger msgr) {
    this.msgr = msgr;
  }

  @Override
  public String getBotToken() {
    return BOT_TOKEN;
  }

  @Override
  public String getProtocol() {
    return "Telegram";
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
