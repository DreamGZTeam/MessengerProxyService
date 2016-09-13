package com.bftcom.ws.api;

import java.util.*;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Chat {

    public Chat(String id) {
        this.id = id;
    }

    private String id;

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

    public void mergeChats(Chat aChat){
        history.putAll(aChat.getHistory());
    }

    public List<String> toList(){
        List<String> retVal = new ArrayList<>();
        history.keySet().forEach(h -> {
                retVal.add(new StringBuilder()
                    .append(h.getDate().toString())
                    .append(";")
                    .append(h.getDirection())
                    .append(";")
                    .append(((TextMessage)history.get(h)).getText()).toString());
            });
        return retVal;
    }
}
