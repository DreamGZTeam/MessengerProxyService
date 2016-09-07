package com.bftcom.ws.api;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public abstract class Message {
    public static final int MESSAGE_TYPE_TEXT = 0;

    private int messageType;

    public Message(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageType() {
        return messageType;
    }

}
