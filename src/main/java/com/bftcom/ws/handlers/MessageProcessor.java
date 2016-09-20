package com.bftcom.ws.handlers;

import com.bftcom.intf.IMessageHandler;
import com.bftcom.ws.objmodel.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public class MessageProcessor {

    private List<IMessageHandler> messageHandlersChain = new ArrayList<>();

    public void handleMessage(Message inMessage){
        for (IMessageHandler handler : messageHandlersChain) {
            handler.handleMessage(inMessage);
        }
    }

    public void addHandler(IMessageHandler handler){
        messageHandlersChain.add(handler);
    }
}
