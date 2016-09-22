package com.bftcom.ws;

import com.bftcom.intf.IBot;
import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.handlers.AbstractHandler;
import com.bftcom.ws.objmodel.Chat;
import com.bftcom.ws.objmodel.Contact;
import com.bftcom.ws.objmodel.MessageInfo;
import com.bftcom.ws.objmodel.Messenger;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import java.lang.reflect.Constructor;
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

    try {
        for (Configurator.MessengerConfig messengerConfig : Configurator.getInstance().getMessengerFactory()) {
          ClassLoader classLoader = IBot.class.getClassLoader();
          Class botClass = classLoader.loadClass(messengerConfig.getBotConfig().getParam("javaclass"));
          Constructor<IBot> botConstructor = botClass.getConstructor();
          IBot bot = botConstructor.newInstance();
          bot.init(messengerConfig.getBotConfig());
          Messenger msgr = new Messenger(bot);
          messengers.put(msgr.getId(), msgr);
        }
    }
    catch (Exception e){
        e.printStackTrace();
    }

    //Telegramm bot
//    Messenger msgr = new Messenger(TelegramBot.getInstance());
//    //Интерактивный обработчик сообщений
//    msgr.getMessageProcessor().addHandler(new ElizaMessageHandler());
//    messengers.put(msgr.getId(), msgr);
//
//    Messenger slackMessenger = new Messenger(SlackBot.getInstance());
//    messengers.put(slackMessenger.getId(), slackMessenger);
  }

  @Override
  public Set<String> getProtocols() {
    return messengers.values().stream().map(Messenger::getProtocol).collect(Collectors.toSet());
  }

  @Override
  public List<Messenger> getMessengers(String protocol) {
    return messengers.values().stream().filter(m->m.getProtocol().equals(protocol)).collect(Collectors.toList());
  }

  @Override
  public List<Chat> getChats(String messengerId) {
    return getMessenger(messengerId).getChats();
  }

  @Override
  public void sendTextMessage(String messengerId, String chatId, String text) {
    getMessenger(messengerId).sendTextMessage(chatId, text);
  }

  @Override
  public Set<MessageInfo> getHistory(String messengerId, String chatId) {
    return getMessenger(messengerId).getHistory(chatId);
  }

  @Override
  public Contact getContact(String messengerId, String contactId) {
    return getMessenger(messengerId).getContact(contactId);
  }

  @Override
  public void setInteractive(String messengerId, boolean interactive) {
    getMessenger(messengerId).setInteractiveMode(interactive);
  }

  @Override
  public Set<AbstractHandler> getHandlers(String messengerId) {
    return null;
  }

  @Override
  public void setHandlerMode(String messenderId, String handlerId, boolean mode) {

  }

  private Messenger getMessenger(String messengerId) {
    if (!messengers.containsKey(messengerId))
      throw new RuntimeException("Messenger with ID " + messengerId + " not found!");
    return  messengers.get(messengerId);
  }
}
