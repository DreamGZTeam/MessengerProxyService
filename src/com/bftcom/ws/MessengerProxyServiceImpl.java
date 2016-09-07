package com.bftcom.ws;

import com.bftcom.bots.TelegramBot;
import com.bftcom.bots.intf.iBot;
import com.bftcom.ws.api.Messenger;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Artem on 06.09.2016.
 */
@WebService(endpointInterface = "com.bftcom.ws.MessengerProxyService")
public class MessengerProxyServiceImpl implements MessengerProxyService {

  Map<String, Messenger> messengers = new HashMap<>();

  public MessengerProxyServiceImpl() {
    initService();
  }

  private void initService(){
    //register bots as messengers

    //Telegramm bot
    iBot tlBot = TelegramBot.getInstance();
    messengers.put(tlBot.getBotToken(), new Messenger(tlBot, "TelegramBot_1"));

    //@todo register Another bot...
  }

//  @Override
//  public void sendMessage(String text) {
//    TelegramBot.getInstance().sendMessage(text);
//    System.out.println("Text sended: " + text);
//  }

  @Override
  public void sendTextMessage(String messengerId, String chatId, String text) {
    Messenger msngr = messengers.get(messengerId);
    if (msngr == null)
      throw new RuntimeException((new StringBuilder().append("Messenger with ID ").append(messengerId).append(" not found!")).toString());
    msngr.sendTextMessage(chatId, text);
  }

  @Override
  public Set<String> getMessengers() {
    return messengers.keySet();

  }

}
