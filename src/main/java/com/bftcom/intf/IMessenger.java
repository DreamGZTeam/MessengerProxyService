package com.bftcom.intf;

import com.bftcom.ws.objmodel.Update;

/**
 * Created by d.dyldaev on 08.09.16.
 */
public interface IMessenger {

    void onUpdate(Update update);
}
