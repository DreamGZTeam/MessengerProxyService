package com.bftcom.ws.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.dyldaev on 21.09.16.
 */
public class Configurator {

  private static final String paramsFileName = "serviceparams.xml";
  private static final String messengerParamsTag = "Messenger";
  private static final String storageParamsTag = "Storage";
  private static final String handlersParamsTag = "Handlers";
  private static final String handlerParamsTag = "Handler";
  private static Configurator configurator;

  private List<MessengerConfig> messengerFactory = new ArrayList<>();

  public static Configurator getInstance() {
    if (configurator == null) {
      configurator = new Configurator();
    }
    return configurator;
  }

  public Configurator() {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder db = dbf.newDocumentBuilder();
      File fXml = new File(paramsFileName);
      Document doc = db.parse(fXml);
      doc.getDocumentElement().normalize();
      Element rootCfgElement = doc.getDocumentElement();
      for (int i = 0; i < rootCfgElement.getElementsByTagName(messengerParamsTag).getLength(); i++) {

        Element botConfig = (Element) rootCfgElement.getElementsByTagName(messengerParamsTag).item(i);
        MessengerConfig messengerConfig = new MessengerConfig(new Config(botConfig));
        messengerFactory.add(messengerConfig);

        Element storageConfig = (Element) botConfig.getElementsByTagName(storageParamsTag).item(0);
        if (storageConfig != null)
          messengerConfig.setStorage(new Config(storageConfig));

        //if (botConfig.getElementsByTagName(handlersParamsTag).getLength() > 0) {
          //Element handlersConfig = (Element) botConfig.getElementsByTagName(handlersParamsTag).item(0);
          for (int j = 0; j < botConfig.getElementsByTagName(handlerParamsTag).getLength(); j++) {
            Element handlerConfig = (Element) botConfig.getElementsByTagName(handlerParamsTag).item(j);
            messengerConfig.addHandler(new Config(handlerConfig));
          }
        //}
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<MessengerConfig> getMessengerFactory() {
    return messengerFactory;
  }

  public class Config {
    private Element cfgElement;

    Config(Element cfgElement) {
      this.cfgElement = cfgElement;
    }

    public String getParam(String paramName) {
      NodeList param = cfgElement.getElementsByTagName(paramName);
      if (param == null || param.getLength() == 0)
        return null;
      return cfgElement.getElementsByTagName(paramName).item(0).getFirstChild().getNodeValue();
    }
  }

  public class MessengerConfig {
    Config botConfig;
    List<Config> handlers = new ArrayList<>();
    Config storage = null;

    void setStorage(Config storage) {
      this.storage = storage;
    }

    void addHandler(Config handlerConfig) {
      handlers.add(handlerConfig);
    }

    public Config getBotConfig() {
      return botConfig;
    }

    public List<Config> getHandlers() {
      return handlers;
    }

    public Config getStorage() {
      return storage;
    }

    public MessengerConfig(Config botConfig) {
      this.botConfig = botConfig;
    }
  }
}
