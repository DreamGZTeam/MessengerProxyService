package com.bftcom.ws.api;

import com.bftcom.bots.intf.IBot;
import com.bftcom.bots.intf.IMessenger;
import com.bftcom.bots.intf.IStorage;
import com.bftcom.ws.handlers.MessageProcessor;
import com.bftcom.ws.storages.XMLStorage;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.bftcom.ws.api.Message.DIRECTION_MESSAGE_INCOMING;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Messenger implements IMessenger, Serializable{

    public String name;
    public String id;
    private IBot bot;
    private IStorage storage = new XMLStorage();
    private Map<String, Contact> contacts = new HashMap<>();
    private MessageProcessor messageProcessor = new MessageProcessor();
    private boolean interactiveMode = false;

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
            id = bot.getBotToken();
        }
        storage.readData(this);

    }

    public void save(){
        if (storage != null)
            storage.saveData(this);
    }

    public String getId() {
        return id;
    }

    public void sendTextMessage(String contactId, String messageText){
        Chat chat = getChatForContact(contactId);
        TextMessage message = new TextMessage(messageText);
        chat.addMessage(message);
        bot.sendMessage(chat.getId(), message);
    }

    public List<Contact> getContacts() {
        return contacts.values().stream().collect(Collectors.toList());
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
        TextMessage incomingMessage = new TextMessage(Date.from(Instant.ofEpochSecond(update.getDate().longValue())),
                                                      DIRECTION_MESSAGE_INCOMING,
                                                      update.getText());
        if (contact == null) {
            contact = new Contact(update.getContactId().toString(),
                update.getFirstName(),
                update.getLastName(),
                update.getUserName(),
                update.getChatId());
            contacts.put(update.getContactId().toString(), contact);
            contact.getChat().addMessage(incomingMessage);
        } else {
            contact.getChat().addMessage(incomingMessage);
        }
        if(interactiveMode) {
            TextMessage outgoingMessage = new TextMessage(incomingMessage.getText());
            messageProcessor.handleMessage(outgoingMessage);
            sendTextMessage(contact.getId(), outgoingMessage.getText());
        }
    }

    public Set<TextMessage> getHistory(String contactId){
        return getChatForContact(contactId).getHistory();
    }

    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    public void setInteractiveMode(boolean interactiveMode){
        this.interactiveMode = interactiveMode;
    }
}
