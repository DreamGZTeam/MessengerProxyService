package com.bftcom.ws.objmodel;

import com.bftcom.intf.IBot;
import com.bftcom.intf.IMessenger;
import com.bftcom.intf.IStorage;
import com.bftcom.ws.handlers.MessageProcessor;
import com.bftcom.ws.storages.XMLStorage;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.bftcom.ws.objmodel.Message.DIRECTION_MESSAGE_INCOMING;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Messenger implements IMessenger, Serializable{

    public String name;
    public String id;
    private IBot bot;
    private IStorage storage = new XMLStorage();
    private Map<String, Chat> chats = new HashMap<>();
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

    public void sendTextMessage(String chatId, String messageText){
        Chat chat = chats.get(chatId);
        TextMessage message = new TextMessage(messageText);
        chat.addMessage(message);
        bot.sendMessage(chat.getId(), message);
    }

    public List<Chat> getChats() {
        return chats.values().stream().collect(Collectors.toList());
    }

    public Chat getChatById(String chatId) {
        if (chats.containsKey(chatId))
            throw new RuntimeException((new StringBuilder().append("Chat with ID ").append(chatId).append(" not found!")).toString());
        return chats.get(chatId);
    }


    @Override
    public void onUpdate(Update update) {
        if (update ==  null)
            return;

        // создадим новый чат или если уже есть, возьмем из мапы
        Chat chat;
        String chatId = update.getChatId().toString();
        if (!chats.containsKey(chatId)) {
            chat = new Chat(update.getChatId().toString(),
                update.getChatName(),
                update.isIsGroupChat());
            chats.put(update.getChatId().toString(), chat);
        } else {
            chat = chats.get(chatId);
        }

        // добавим в чат новое сообщение и пользователя
        TextMessage incomingMessage = new TextMessage(Date.from(Instant.ofEpochSecond(update.getDate().longValue())),
            DIRECTION_MESSAGE_INCOMING,
            update.getText());
        chat.addMessage(incomingMessage);
        chat.addContact(new Contact(update.getContactId(), update.getFirstName(), update.getLastName(), update.getUserName()));

        if (interactiveMode) {
            TextMessage outgoingMessage = new TextMessage(incomingMessage.getText());
            messageProcessor.handleMessage(outgoingMessage);
            sendTextMessage(chat.getId(), outgoingMessage.getText());
        }
    }

    public Set<TextMessage> getHistory(String chatId){
        return chats.get(chatId).getHistory();
    }

    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    public void setInteractiveMode(boolean interactiveMode){
        this.interactiveMode = interactiveMode;
    }
}
