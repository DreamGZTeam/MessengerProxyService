package com.bftcom.ws.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
    static Configurator configurator;

    List<Config> botFactory = new ArrayList<>();

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
            File fXml = new File("serviceparams.xml");
            Document doc = db.parse(fXml);
            doc.getDocumentElement().normalize();
            Element rootCfgElement = doc.getDocumentElement();
            for (int i = 0; i < rootCfgElement.getElementsByTagName("Bot").getLength(); i++){
                botFactory.add(new Config((Element) rootCfgElement.getElementsByTagName("Bot").item(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Config> getBotFactory() {
        return botFactory;
    }

    public class Config{
        Element cfgElement;

        public Config(Element cfgElement) {
            this.cfgElement = cfgElement;
        }

        public String getParam(String paramName) {
            NodeList param = cfgElement.getElementsByTagName(paramName);
            if (param == null || param.getLength() == 0)
                return null;
            return cfgElement.getElementsByTagName(paramName).item(0).getFirstChild().getNodeValue();
        }
    }
}
