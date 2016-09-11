package com.bftcom.ws.api;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class TextMessage extends Message {

    private String text;

    public TextMessage(String messageText) {
        super(MESSAGE_TYPE_TEXT);
        this.text = messageText;
    }

    public String getText() {
        return text;
    }
}
