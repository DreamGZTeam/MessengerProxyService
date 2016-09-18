package com.bftcom.ws.api;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Chat {

    private String id;
    private Set<TextMessage> history = new TreeSet<>();

    public Chat(String id) {
        this.id = id;
    }

    public Set<TextMessage> getHistory() {
        return history;
    }

    public void addMessage(TextMessage msg){
        history.add(msg);
    }

    public String getId() {
        return id;
    }

}
