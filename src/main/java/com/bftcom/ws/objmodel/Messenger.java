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

    public String id;
    private IBot bot;
    private IStorage storage = new XMLStorage();
    private Map<String, Chat> chats = new HashMap<>();
    private MessageProcessor messageProcessor = new MessageProcessor();
    private boolean interactiveMode = false;

    public String getName() {
        return bot != null ? bot.getName() : "";
    }

    public String getProtocol() {
        return bot != null ? bot.getProtocol() : "";
    }

    public Messenger(IBot bot) {
        if (bot != null) {
            this.bot = bot;
            bot.setMessenger(this);
            id = bot.getBotToken();
        }
        load();
    }

    public void addChat(String chatId, Chat chat){
        chats.put(chatId, chat);
    }

    public void save(){
        if (storage != null)
            storage.saveData(this);
    }

    public void load(){
        if (storage != null)
            storage.readData(this);
    }

    public String getId() {
        return id;
    }

    public void sendTextMessage(String chatId, String messageText){
        Chat chat = getChatById(chatId);
        TextMessage message = new TextMessage(messageText);
        chat.addMessage(message);
        bot.sendMessage(chat.getId(), message);
        save();
    }

    public List<Chat> getChats() {
        return chats.values().stream().collect(Collectors.toList());
    }

    public Chat getChatById(String chatId) {
        return getChatById(chatId, true);
    }

    public Chat getChatById(String chatId, boolean throwException) {
        if (!chats.containsKey(chatId))
            if (throwException)
                throw new RuntimeException((new StringBuilder().append("Chat with ID ").append(chatId).append(" not found!")).toString());
            else
                return null;
        return chats.get(chatId);
    }


    @Override
    public void onUpdate(Update update) {
        if (update ==  null)
            return;

        // создадим новый чат или если уже есть, возьмем из мапы
        String chatId = update.getChatId();
        Chat chat = getChatById(chatId, false);
        if (chat == null) {
            chat = new Chat(chatId,
                update.getChatName(),
                update.isIsGroupChat());
            addChat(chatId, chat);
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
        save();
    }

    public Set<TextMessage> getHistory(String chatId){
        return getChatById(chatId).getHistory();
    }

    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }

    public void setInteractiveMode(boolean interactiveMode){
        this.interactiveMode = interactiveMode;
    }
}
