package com.bftcom.ws;

import com.bftcom.intf.IBot;
import com.bftcom.intf.IMessageHandler;
import com.bftcom.intf.IStorage;
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
public class MessengerProxyServiceImpl implements MessengerProxyService {

  Map<String, Messenger> messengers = new HashMap<>();

  public MessengerProxyServiceImpl() {
    //register bots as messengers

    for (Configurator.MessengerConfig messengerConfig : Configurator.getInstance().getMessengerFactory()) {
      try {
        //Create messenger and bot
        ClassLoader botClassLoader = IBot.class.getClassLoader();
        Class botClass = botClassLoader.loadClass(messengerConfig.getBotConfig().getParam("javaclass"));
        Constructor<IBot> botConstructor = botClass.getConstructor();
        IBot bot = botConstructor.newInstance();
        bot.init(messengerConfig.getBotConfig());
        Messenger msgr = new Messenger(bot);
        messengers.put(msgr.getId(), msgr);
        //Create message handles chain
        msgr.getMessageProcessor().clearHandlesChain();
        for (Configurator.Config handlerConfig : messengerConfig.getHandlers()) {
          ClassLoader handlerClassLoader = IMessageHandler.class.getClassLoader();
          Class handlerClass = handlerClassLoader.loadClass(handlerConfig.getParam("javaclass"));
          Constructor<IMessageHandler> handlerConstructor = handlerClass.getConstructor();
          IMessageHandler handler = handlerConstructor.newInstance();
          handler.init(handlerConfig);
          msgr.getMessageProcessor().addHandler(handler);
        }
        //Create storage
        if (messengerConfig.getStorage() != null) {
          ClassLoader storageClassLoader = IStorage.class.getClassLoader();
          Class storageClass = storageClassLoader.loadClass(messengerConfig.getStorage().getParam("javaclass"));
          Constructor<IStorage> storageConstructor = storageClass.getConstructor();
          IStorage storage = storageConstructor.newInstance();
          storage.init(messengerConfig.getStorage());
          msgr.setStorage(storage);
          //Load contacts and history
          msgr.load();
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Set<String> getProtocols() {
    return messengers.values().stream().map(Messenger::getProtocol).collect(Collectors.toSet());
  }

  @Override
  public List<Messenger> getMessengers(String protocol) {
    return messengers.values().stream().filter(m -> m.getProtocol().equals(protocol)).collect(Collectors.toList());
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
  public List<AbstractHandler> getHandlers(String messengerId) {
    return getMessenger(messengerId).getMessageProcessor().getMessageHandlersChain();
  }

  @Override
  public void setHandlerMode(String messengerId, String handlerId, boolean mode) {
    getMessenger(messengerId).getMessageProcessor().getHandler(handlerId).setActive(mode);
  }

  private Messenger getMessenger(String messengerId) {
    if (!messengers.containsKey(messengerId))
      throw new RuntimeException("Messenger with ID " + messengerId + " not found!");
    return messengers.get(messengerId);
  }
}
