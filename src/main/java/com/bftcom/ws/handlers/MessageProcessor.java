package com.bftcom.ws.handlers;

import com.bftcom.intf.IMessageHandler;
import com.bftcom.ws.objmodel.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public class MessageProcessor {

  private List<AbstractHandler> messageHandlersChain = new ArrayList<>();

  public void handleMessage(Message inMessage) {
    for (IMessageHandler handler : messageHandlersChain.stream().filter(h->h.active).collect(Collectors.toList())) {
      handler.handleMessage(inMessage);
    }
  }

  public List<AbstractHandler> getMessageHandlersChain() {
    return messageHandlersChain;
  }

  public AbstractHandler getHandler(String handlerId){
    AbstractHandler handler = messageHandlersChain.stream().filter(h->h.getId().equals(handlerId)).findFirst().orElse(null);
    if (handler == null)
      throw new RuntimeException((new StringBuilder().append("Handler with ID ").append(handlerId).append(" not found!")).toString());
    return handler;
  }

  public void clearHandlesChain() {
    messageHandlersChain.clear();
  }

  public void addHandler(IMessageHandler handler) {
    messageHandlersChain.add((AbstractHandler)handler);
  }
}
