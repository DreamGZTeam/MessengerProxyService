package com.bftcom.ws.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Chat {

    public Chat(String id) {
        this.id = id;
    }

    private String id;

    private String contactId;

    public String getContactId() {
        return contactId;
    }

    private Map<History, Message> history = new HashMap<>();

    public Map<History, Message> getHistory() {
        return history;
    }

    public void addMessage(Message msg){
        history.put(new History(), msg);
    }

    public void addMessage(Message msg, Date date, int direction){
        history.put(new History(date, direction), msg);
    }

    public String getId() {
        return id;
    }
}
