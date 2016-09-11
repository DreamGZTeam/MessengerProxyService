package com.bftcom.ws.api;

import com.bftcom.bots.intf.iBot;
import com.bftcom.bots.intf.iMessenger;

import java.util.*;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Messenger implements iMessenger {

    private String name;

    private iBot bot;

    private Map<String, Contact> contacts = new HashMap<>();

    public String getName() {
        return name;
    }

    public String getProtocol() {
        return bot != null ? bot.getProtocol() : "";
    }

    public Messenger(iBot bot, String name) {
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
            .append(c.getUserName()).toString());
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
        Contact contact = contacts.get(update.getContact().getId());
        if (contact == null) {
            contacts.put(update.getContact().getId(),
                            new Contact(update.getContact().getId(),
                                        update.getContact().getUserName(),
                                        update.getContact().getChat()));
        }
        else{
            contact.getChat().mergeChats(update.getContact().getChat());
        }
    }

    public List<String> getHistory(String contactId){
        return getChatForContact(contactId).toList();
    }
}
