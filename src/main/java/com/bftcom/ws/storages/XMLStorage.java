package com.bftcom.ws.storages;


import com.bftcom.intf.IStorage;
import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.objmodel.*;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Date;

import static com.bftcom.ws.objmodel.Message.MESSAGE_TYPE_TEXT;

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
      root.setAttribute("id", msgr.getId());
      //Chats
      Element chats = document.createElement("Chats");
      for (Chat c : msgr.getChats()) {
        Element chat = document.createElement("Chat");
        chat.setAttribute("id", c.getId());
        chat.setAttribute("name", c.getName());
        chat.setAttribute("isGroup", c.getIsGroup() ? "1" : "0");
        //History
        Element messageHistory = document.createElement("MessageHistory");
        for (Message h : c.getHistory()) {
          Element history = document.createElement("History");
          history.setAttribute("messageType", String.valueOf(h.getMessageType()));
          history.setAttribute("direction", String.valueOf(h.getDirection()));
          history.setAttribute("uid", String.valueOf(h.getUid()));
          history.setAttribute("date", String.valueOf(h.getDate().getTime()));
          if (h.getContactId() != null && !h.getContactId().isEmpty())
            history.setAttribute("contactId", h.getContactId());
          switch (h.getMessageType()) {
            case MESSAGE_TYPE_TEXT:
              Element messageBody = document.createElement("MessageBody");
              CDATASection message = document.createCDATASection(((TextMessage) h).getText());
              messageBody.appendChild(message);
              history.appendChild(messageBody);
              break;
            default:
          }
          messageHistory.appendChild(history);
        }
        chat.appendChild(messageHistory);
        //Contacts
        Element contacts = document.createElement("Contacts");
        for (Contact cn : c.getContacts().values()) {
          Element contact = document.createElement("Contact");
          contact.setAttribute("id", cn.getId());
          contact.setAttribute("userName", cn.getUserName());
          contact.setAttribute("firstName", cn.getFirstName());
          contact.setAttribute("lastName", cn.getLastName());
          contacts.appendChild(contact);
        }
        chat.appendChild(contacts);
        chats.appendChild(chat);
      }
      root.appendChild(chats);
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

      Element root = doc.getDocumentElement();
      //Файл не для этого messenger'a, ничего не делаем
      if (!root.getAttribute("id").equals(msgr.getId()))
        return;
      NodeList chats = root.getElementsByTagName("Chat");
      for (int i = 0; i < chats.getLength(); i++) {
        Element chat = (Element) chats.item(i);
        //Ищем, возможно чат который загружаем уже существует
        Chat c = msgr.getChatById(chat.getAttribute("id"), false);
        //Если такого чата нет, то создадим его и загрузим в него всю информацию (историю и контакты)
        //Если же такой час существует, то только обновим контакты
        if (c == null) {
          c = new Chat(chat.getAttribute("id"),
              chat.getAttribute("name"),
              chat.getAttribute("isGroup").equals("1"));
          //Загружаем историю сообщений
          NodeList messageHistory = root.getElementsByTagName("History");
          for (int j = 0; j < messageHistory.getLength(); j++) {
            Element history = (Element) messageHistory.item(j);
            int messageType = Integer.parseInt(history.getAttribute("messageType"));
            switch (messageType) {
              case MESSAGE_TYPE_TEXT:
                c.addMessage(new TextMessage(
                    new Date(Long.parseLong(history.getAttribute("date"))),
                    Integer.parseInt(history.getAttribute("direction")),
                    (history.getElementsByTagName("MessageBody").item(0)).getFirstChild().getNodeValue(),
                    history.hasAttribute("contactId") ? history.getAttribute("contactId") : null));
                break;
              default:
            }
          }
          msgr.addChat(c.getId(), c);
        }
        //Загружаем контакты
        NodeList contacts = root.getElementsByTagName("Contact");
        for (int n = 0; n < contacts.getLength(); n++) {
          Element contact = (Element) contacts.item(n);
          String contactId = contact.getAttribute("id");
          //Такой контакт уже есть, пропускаем
          if (msgr.getChatById(c.getId()).getContacts().containsKey(contactId))
            continue;
          c.addContact(new Contact(contactId,
              contact.getAttribute("firstName"),
              contact.getAttribute("lastName"),
              contact.getAttribute("userName")));
        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void init(Configurator.Config cfg) {

  }
}
