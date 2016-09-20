package com.bftcom.ws.storages;


import com.bftcom.intf.IStorage;
import com.bftcom.ws.objmodel.Messenger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public class XMLStorage implements IStorage {
    @Override
    public void saveData(Messenger msgr) {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement(msgr.getName());
            document.appendChild(root);
            root.appendChild(document.createTextNode("Some"));
            root.appendChild(document.createTextNode(" "));
            root.appendChild(document.createTextNode("text"));

            File xmlFile = new File(msgr.getName() + ".xml");

            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(xmlFile));

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void readData(Messenger msgr) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            File fXml = new File(msgr.getName() + ".xml");
            Document doc = db.parse(fXml);
            doc.getDocumentElement().normalize();

        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }

    }
}
