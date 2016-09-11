package com.bftcom.bots.intf;

import com.bftcom.ws.api.Update;

import java.util.List;

/**
 * Created by d.dyldaev on 08.09.16.
 */
public interface iMessenger {

    void onUpdate(Update update);
}
