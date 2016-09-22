package com.bftcom.ws.objmodel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public abstract class Message implements Comparable, Serializable {
  public static final int MESSAGE_TYPE_TEXT = 0;
  public static final int DIRECTION_MESSAGE_INCOMING = 0;
  public static final int DIRECTION_MESSAGE_OUTGOING = 1;

  private int messageType;

  public Date date;
  public int direction;
  private long uid;
  private String contactId;

  public Message(int messageType, Date date, int direction, String contactId) {
    this.messageType = messageType;
    this.date = date;
    this.direction = direction;
    this.uid = new Date().getTime();
    this.contactId = contactId;
  }

  public int getMessageType() {
    return messageType;
  }

  public Date getDate() {
    return date;
  }

  public int getDirection() {
    return direction;
  }

  public long getUid() {
    return uid;
  }

  public String getContactId() {
    return contactId;
  }

  @Override
  public int compareTo(Object o) {
    if (((Message) o).date.compareTo(this.date) == 0)
      return Long.compare(((Message) o).getUid(), this.getUid());
    else
      return ((Message) o).date.compareTo(this.date);
  }
}
