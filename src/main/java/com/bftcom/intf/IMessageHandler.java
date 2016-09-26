package com.bftcom.intf;

import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.objmodel.Message;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public interface IMessageHandler {
  boolean handleMessage(Message inMessage);

  void init(Configurator.Config cfg);
}
