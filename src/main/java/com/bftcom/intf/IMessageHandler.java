package com.bftcom.intf;

import com.bftcom.ws.objmodel.Message;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public interface IMessageHandler {
    void handleMessage(Message inMessage);
}
