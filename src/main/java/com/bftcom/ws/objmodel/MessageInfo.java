package com.bftcom.ws.objmodel;

import java.io.Serializable;

/**
 * @author: a.solonshchikov
 * Date: 22.09.2016
 */
public class MessageInfo implements Comparable<MessageInfo>, Serializable {
  public TextMessage message;
  public Contact contact;

  public MessageInfo(TextMessage message, Contact contact) {
    this.message = message;
    this.contact = contact;
  }

  @Override
  public int compareTo(MessageInfo o) {
    return this.message.compareTo(o.message);
  }
}
