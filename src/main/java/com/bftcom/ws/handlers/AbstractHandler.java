package com.bftcom.ws.handlers;

import com.bftcom.intf.IMessageHandler;
import com.bftcom.ws.objmodel.Message;

import java.io.Serializable;

/**
 * Created by d.dyldaev on 22.09.2016.
 */
public abstract class AbstractHandler implements IMessageHandler, Serializable {
  public String name;
  public String id;

  public AbstractHandler(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  @Override
  public abstract void handleMessage(Message inMessage);
}
