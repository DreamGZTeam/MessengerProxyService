package com.bftcom.bots;

import com.bftcom.bots.intf.iBot;
import com.bftcom.bots.intf.iMessenger;
import com.bftcom.ws.api.Chat;
import com.bftcom.ws.api.Contact;
import com.bftcom.ws.api.Messenger;
import com.bftcom.ws.api.TextMessage;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.Date;

import static com.bftcom.ws.api.History.DIRECTION_MESSAGE_INCOMING;
import static com.bftcom.ws.api.Message.MESSAGE_TYPE_TEXT;

public class TelegramBot extends TelegramLongPollingBot implements iBot {
  public static final String BOT_USERNAME = "GZGZBot";
  public static final String BOT_TOKEN = "181000542:AAHLrSjDPKhJQUe8AWrC3RZjQcXIT46_Y2E";

  private iMessenger msgr;
  private static TelegramBot telegramBot;

  public static void main(String[] args) {
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      TelegramBot telegramBot = new TelegramBot();
      telegramBotsApi.registerBot(telegramBot);
      telegramBot.sendMessage("123");
    } catch (TelegramApiException e) {
      BotLogger.error("Bot register error", e);
    }
  }

  public static TelegramBot getInstance() {
    if (telegramBot == null) {
      telegramBot = new TelegramBot();
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
    sendMessageRequest.setChatId("xxx");
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

  private com.bftcom.ws.api.Update transformUpdateObject(Update update){
    if (update.getMessage().getText() == null || update.getMessage().getText().equals("") ||
        update.getMessage().getFrom().getId() == null)
      return null;
    Contact contact = new Contact(update.getMessage().getFrom().getId().toString(),
                                  update.getMessage().getFrom().getFirstName(),
                                  update.getMessage().getFrom().getLastName(),
                                  update.getMessage().getFrom().getUserName(),
                                  new Chat(update.getMessage().getChatId().toString()));
    contact.getChat().addMessage(new com.bftcom.ws.api.TextMessage(update.getMessage().getText()),
                                 new Date(update.getMessage().getDate()),
                                 DIRECTION_MESSAGE_INCOMING);
    return new com.bftcom.ws.api.Update(contact);
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
  public void sendMessage(String chatId, com.bftcom.ws.api.Message msg) {
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
