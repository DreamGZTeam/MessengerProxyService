package com.bftcom.ws.api;

/**
 * Created by d.dyldaev on 08.09.16.
 */
public class Update {

    Contact contact;

    public Update(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
