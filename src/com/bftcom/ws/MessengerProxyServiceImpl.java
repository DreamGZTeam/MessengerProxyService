package com.bftcom.ws;

import com.bftcom.bots.TelegramBot;
import com.bftcom.ws.api.Messenger;

import javax.jws.WebService;
import java.util.List;

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

  @Override
  public List<Messenger> getMessangers() {
    return null;
  }

}
