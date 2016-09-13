package com.bftcom.ws.api;

import com.bftcom.bots.intf.IBot;
import com.bftcom.bots.intf.IMessenger;

import java.time.Instant;
import java.util.*;

import static com.bftcom.ws.api.History.DIRECTION_MESSAGE_INCOMING;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Messenger implements IMessenger {

    private String name;
    private IBot bot;
    private Map<String, Contact> contacts = new HashMap<>();

    public String getName() {
        return name;
    }

    public String getProtocol() {
        return bot != null ? bot.getProtocol() : "";
    }

    public Messenger(IBot bot, String name) {
        this.name = name;
        if (bot != null) {
            this.bot = bot;
            bot.setMessenger(this);
        }

    }

    public String getId() {
        return bot.getBotToken();
    }

    public void sendTextMessage(String contactId, String messageText){
        Chat chat = getChatForContact(contactId);
        Message message = new TextMessage(messageText);
        chat.addMessage(message);
        bot.sendMessage(chat.getId(), message);
    }

    public Set<String> getContacts() {
        Set<String> retVal = new HashSet<>();
        contacts.keySet().forEach(cKey -> {
            Contact c = contacts.get(cKey);
            retVal.add(new StringBuilder()
            .append(c.getId())
            .append(";")
            .append(c.getFirstName())
            .append(" ")
            .append(c.getLastName()).toString());
        });
        return retVal;
    }

    public Chat getChatForContact(String contactId){
        Contact contact = contacts.get(contactId);
        if (contact == null)
            throw new RuntimeException((new StringBuilder().append("Contact with ID ").append(contactId).append(" not found!")).toString());
        return contacts.get(contactId).getChat();
    }


    @Override
    public void onUpdate(Update update) {
        if (update ==  null)
            return;
        Contact contact = contacts.get(update.getContactId().toString());
        if (contact == null) {
            contact = new Contact(update.getContactId().toString(),
                update.getFirstName(),
                update.getLastName(),
                update.getUserName(),
                update.getChatId());
            contacts.put(update.getContactId().toString(), contact);
            contact.getChat().addMessage(new TextMessage(update.getText()),
                Date.from(Instant.ofEpochSecond(update.getDate().longValue())),
                DIRECTION_MESSAGE_INCOMING);
        } else {
            contact.getChat().addMessage(new TextMessage(update.getText()),
                Date.from(Instant.ofEpochSecond(update.getDate().longValue())),
                DIRECTION_MESSAGE_INCOMING);
        }
    }

    public List<String> getHistory(String contactId){
        return getChatForContact(contactId).toList();
    }
}
