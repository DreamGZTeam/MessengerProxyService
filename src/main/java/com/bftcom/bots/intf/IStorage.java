package com.bftcom.bots.intf;

import com.bftcom.ws.api.Messenger;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public interface IStorage {

    void saveData(Messenger msgr);

    void readData(Messenger msgr);
}
