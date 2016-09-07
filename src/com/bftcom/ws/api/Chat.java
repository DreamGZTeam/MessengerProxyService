package com.bftcom.ws.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Chat {

    private Map<History, Message> history = new HashMap<>();

    public Map<History, Message> getHistory() {
        return history;
    }

    public void addMessage(Message msg){
        history.put(new History(), msg);
    }


}
