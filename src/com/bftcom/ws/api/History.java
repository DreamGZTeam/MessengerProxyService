package com.bftcom.ws.api;

import java.util.Date;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class History implements Comparable {

    Date date;

    public History(Date date) {
        this.date = date;
    }

    public History() {
        this(new Date());
    }

    @Override
    public int compareTo(Object o) {
       return ((History)o).date.compareTo(this.date);
    }
}
