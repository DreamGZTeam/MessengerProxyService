package com.bftcom.bots.intf;

import com.bftcom.ws.api.Update;

/**
 * Created by d.dyldaev on 08.09.16.
 */
public interface IMessenger {

    void onUpdate(Update update);
}
