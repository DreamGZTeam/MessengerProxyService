package com.bftcom.ws.handlers.eliza;

import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.handlers.AbstractHandler;
import com.bftcom.ws.objmodel.Message;
import com.bftcom.ws.objmodel.TextMessage;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public class ElizaMessageHandler extends AbstractHandler {

  Eliza eliza;

  public ElizaMessageHandler() {
    super();
    eliza = new Eliza();

  }

  @Override
  public boolean handleMessage(Message inMessage) {
    if (inMessage.getMessageType() == Message.MESSAGE_TYPE_TEXT) {
      String text = eliza.processInput(((TextMessage) inMessage).getText());
      ((TextMessage) inMessage).setText(text);
    }
    return true;
  }


}
