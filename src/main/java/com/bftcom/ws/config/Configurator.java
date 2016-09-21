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
            for (int i = 0; i < rootCfgElement.getElementsByTagName(messengerParamsTag).getLength(); i++){
                Element botConfig = (Element) rootCfgElement.getElementsByTagName(messengerParamsTag).item(i);
                messengerFactory.add(new MessengerConfig(new Config(botConfig)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MessengerConfig> getMessengerFactory() {
        return messengerFactory;
    }

    public class Config{
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

    public class MessengerConfig{
        Config botConfig;
        List<Config> handlers;
        Config storage;

        public Config getBotConfig() {
            return botConfig;
        }

        public MessengerConfig(Config botConfig) {
            this.botConfig = botConfig;
        }
    }
}
