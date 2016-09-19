package com.bftcom.ws;

import com.bftcom.bots.SlackBot;
import com.bftcom.bots.TelegramBot;
import com.bftcom.ws.api.Contact;
import com.bftcom.ws.api.Messenger;
import com.bftcom.ws.api.TextMessage;
import com.bftcom.ws.handlers.eliza.ElizaMessageHandler;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Artem on 06.09.2016.
 */
@WebService(endpointInterface = "com.bftcom.ws.MessengerProxyService")
public class MessengerProxyServiceImpl implements MessengerProxyService {

  Map<String, Messenger> messengers = new HashMap<>();

  @PostConstruct
  private void initService() {
    //register bots as messengers

    //Telegramm bot
    Messenger msgr = new Messenger(TelegramBot.getInstance(), "TelegramBot_1");
    //Интерактивный обработчик сообщений
    msgr.getMessageProcessor().addHandler(new ElizaMessageHandler());
    messengers.put(msgr.getId(), msgr);

    Messenger slackMessenger = new Messenger(SlackBot.getInstance(), "SlackBot_1");
    messengers.put(slackMessenger.getId(), slackMessenger);
  }

  @Override
  public void sendTextMessage(String messengerId, String contactId, String text) {
    getMessenger(messengerId).sendTextMessage(contactId, text);
  }

  @Override
  public List<Messenger> getMessengers(String protocol) {
    return messengers.values().stream().collect(Collectors.toList());
  }

  @Override
  public Set<String> getProtocols() {
    return messengers.values().stream().map(Messenger::getProtocol).collect(Collectors.toSet());
  }

  @Override
  public List<Contact> getContacts(String messengerId) {
    return getMessenger(messengerId).getContacts();

  }

  @Override
  public Set<TextMessage> getHistory(String messengerId, String contactId) {
    return getMessenger(messengerId).getHistory(contactId);
  }

  @Override
  public void save() {
    messengers.values().forEach(Messenger::save);
  }

  @Override
  public void setInteractive(String messengerId, String interactive) {
  getMessenger(messengerId).setInteractiveMode(interactive.equals("1"));

  }

  private Messenger getMessenger(String messengerId){
    Messenger msgr = messengers.get(messengerId);
    if (msgr == null)
      throw new RuntimeException((new StringBuilder().append("Messenger with ID ").append(messengerId).append(" not found!")).toString());
    return msgr;
  }

}
