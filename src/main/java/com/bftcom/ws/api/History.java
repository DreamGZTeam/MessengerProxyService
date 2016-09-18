package com.bftcom.ws.api;

import java.util.Date;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class History implements Comparable {

  public static final int DIRECTION_MESSAGE_INCOMING = 0;
  public static final int DIRECTION_MESSAGE_OUTGOING = 1;

  private Date date;
  private int direction;

  public History(Date date, int direction) {
    this.direction = direction;
    this.date = date;
  }

  public History() {
    this.date = new Date();
    this.direction = DIRECTION_MESSAGE_OUTGOING;
  }

  @Override
  public int compareTo(Object o) {
    return ((History) o).date.compareTo(this.date);
  }

  public Date getDate() {
    return date;
  }

  public int getDirection() {
    return direction;
  }
}
