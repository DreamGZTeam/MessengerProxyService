package com.bftcom.ws.handlers;

import com.bftcom.intf.IMessageHandler;
import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.objmodel.Message;

import java.io.Serializable;

/**
 * Created by d.dyldaev on 22.09.2016.
 */
public abstract class AbstractHandler implements IMessageHandler, Serializable {
  public String name;
  public String id;
  public boolean active;

  public AbstractHandler() {
    active = false;
  }

  public void init(Configurator.Config cfg){
    name = cfg.getParam("name");
    id = cfg.getParam("id");
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public abstract boolean handleMessage(Message inMessage);
}
