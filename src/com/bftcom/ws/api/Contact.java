package com.bftcom.ws.api;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Contact {

    private String id;

    private String userName;

    private Chat chat;

    public Contact(String id, String userName, Chat chat) {
        this.id = id;
        this.userName = userName;
        this.chat = chat;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Chat getChat() {
        return chat;
    }
}
