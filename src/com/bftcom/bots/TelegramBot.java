package com.bftcom.bots;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

public class TelegramBot extends TelegramLongPollingBot {
  public static final String BOT_USERNAME = "GZGZBot";
  public static final String BOT_TOKEN = "181000542:AAHLrSjDPKhJQUe8AWrC3RZjQcXIT46_Y2E";

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
      Message message = update.getMessage();
      SendMessage sendMessageRequest = new SendMessage();
      sendMessageRequest.setChatId(message.getChatId().toString());
      sendMessageRequest.setText("ЗБС");
      try {
        sendMessage(sendMessageRequest); //at the end, so some magic and send the message ;)
      } catch (TelegramApiException e) {
        //do some error handling
      }
    }
  }

  @Override
  public String getBotUsername() {
    return BOT_USERNAME;
  }

  @Override
  public String getBotToken() {
    return BOT_TOKEN;
  }
}
