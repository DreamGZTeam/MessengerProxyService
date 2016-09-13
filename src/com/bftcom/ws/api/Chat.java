package com.bftcom.ws.api;

import java.util.*;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Chat {

    private String id;
    private Map<History, Message> history = new TreeMap<>();

    public Chat(String id) {
        this.id = id;
    }

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

    public List<String> toList(){
        List<String> retVal = new ArrayList<>();
        history.keySet().forEach(h -> {
                retVal.add(h.getDate().toString() +
                    ";" +
                    h.getDirection() +
                    ";" +
                    ((TextMessage) history.get(h)).getText());
            });
        return retVal;
    }
}
