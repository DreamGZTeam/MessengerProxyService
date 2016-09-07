package com.bftcom.ws.api;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class TextMessage extends Message {

    private String messageBody;

    public TextMessage(String messageText) {
        super(MESSAGE_TYPE_TEXT);
        this.messageBody = messageText;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
