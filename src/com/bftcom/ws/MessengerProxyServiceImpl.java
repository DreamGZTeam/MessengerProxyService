package com.bftcom.ws;

import com.bftcom.bots.TelegramBot;
import com.bftcom.bots.intf.iBot;
import com.bftcom.ws.api.Messenger;

import javax.jws.WebService;
import java.util.*;
import java.util.stream.Collectors;

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
    Messenger msgr = new Messenger(TelegramBot.getInstance(), "TelegramBot_1");
    messengers.put(msgr.getId(), msgr);

    //@todo register Another bot...

  }

  @Override
  public void sendTextMessage(String messengerId, String contactId, String text) {
    getMessenger(messengerId).sendTextMessage(contactId, text);
  }

  @Override
  public Set<String> getMessengers(String protocol) {
    Set<String> retVal = new HashSet<>();
     messengers.values().stream()
      .filter(m -> m.getProtocol().equals(protocol))
      .collect(Collectors.toList())
      .forEach(msgr -> {
        retVal.add(
                new StringBuilder().append(msgr.getId())
                        .append(";")
                        .append(msgr.getName()).toString());
      });
     return retVal;
  }

  @Override
  public Set<String> getProtocols() {
    return messengers.values().stream().map(Messenger::getProtocol).collect(Collectors.toSet());
  }

  @Override
  public Set<String> getContacts(String messengerId) {
    return getMessenger(messengerId).getContacts();

  }

  @Override
  public List<String> getHistory(String messengerId, String contactId) {
    return getMessenger(messengerId).getHistory(contactId);
  }

  private Messenger getMessenger(String messengerId){
    Messenger msgr = messengers.get(messengerId);
    if (msgr == null)
      throw new RuntimeException((new StringBuilder().append("Messenger with ID ").append(messengerId).append(" not found!")).toString());
    return msgr;
  }

}
