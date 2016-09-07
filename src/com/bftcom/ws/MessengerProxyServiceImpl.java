package com.bftcom.ws;

import com.bftcom.bots.TelegramBot;

import javax.jws.WebService;

/**
 * Created by Artem on 06.09.2016.
 */
@WebService(endpointInterface = "com.bftcom.ws.MessengerProxyService")
public class MessengerProxyServiceImpl implements MessengerProxyService {
  @Override
  public void sendMessage(String text) {
    TelegramBot.getInstance().sendMessage(text);
    System.out.println("Text sended: " + text);
  }
}
