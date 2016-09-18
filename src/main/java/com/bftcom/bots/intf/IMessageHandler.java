package com.bftcom.bots.intf;

import com.bftcom.ws.api.Message;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public interface IMessageHandler {
    void handleMessage(Message inMessage);
}
