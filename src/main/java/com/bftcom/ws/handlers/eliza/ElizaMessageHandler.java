package com.bftcom.ws.handlers.eliza;

import com.bftcom.intf.IMessageHandler;
import com.bftcom.ws.objmodel.Message;
import com.bftcom.ws.objmodel.TextMessage;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public class ElizaMessageHandler implements IMessageHandler {

    Eliza eliza = new Eliza();

    @Override
    public void handleMessage(Message inMessage) {
        if (inMessage.getMessageType() == Message.MESSAGE_TYPE_TEXT) {
            String text = eliza.processInput(((TextMessage) inMessage).getText());
            ((TextMessage) inMessage).setText(text);
        }
    }

}
