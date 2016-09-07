package com.bftcom.ws.api;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Contact {

    private String id;

    private String alias;

    private Chat chat;

    public Contact(String id) {
        this.id = id;
        chat = new Chat();
    }
}
