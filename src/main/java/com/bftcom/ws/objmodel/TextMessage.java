package com.bftcom.ws.objmodel;

import java.util.Date;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class TextMessage extends Message {

    private String text;

    public TextMessage(String messageText) {
        super(MESSAGE_TYPE_TEXT, new Date(), DIRECTION_MESSAGE_OUTGOING, null);
        this.text = messageText;
    }

    public TextMessage(Date date, int direction, String text, String contactId) {
        super(MESSAGE_TYPE_TEXT, date, direction, contactId);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
