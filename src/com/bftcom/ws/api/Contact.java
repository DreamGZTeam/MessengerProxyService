package com.bftcom.ws.api;

import java.io.Serializable;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Contact implements Serializable {

    public String id;
    public String userName;
    public String firstName;
    public String lastName;
    private Chat chat;

    public Contact(String id, String firstName, String lastName, String userName, Long chatId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.chat = new Chat(chatId.toString());
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Chat getChat() {
        return chat;
    }
}
