package com.bftcom.ws.api;

import com.bftcom.bots.intf.iBot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Messenger {

    private String name;

    private String protocol;

    private iBot bot;

    private Map<String, Chat> chats = new HashMap<>();

    private Map<String, Contact> contacts = new HashMap<>();

    public Chat getChat(String chatId) {
        return chats.get(chatId);
    }

    public String getName() {
        return name;
    }

    public String getProtocol() {
        return protocol;
    }

    public Messenger(iBot bot, String name) {
        this.bot = bot;
        this.protocol = bot.getProtocol();
        this.name = name;
    }

    public String getId() {
        return bot.getBotToken();
    }

    public void sendTextMessage(String chatId, String messageText){
        if (chats.isEmpty()){
            //todo get Contacts from bot
            //todo get Chats from bot
        }
        Chat cht = getChat(chatId);
        if (cht == null)
            throw new RuntimeException((new StringBuilder().append("Conversation with ID ").append(chatId).append(" not found!")).toString());
        Message message = new TextMessage(messageText);
        cht.addMessage(message);
        bot.sendMessage(chatId, message);
    }

}
