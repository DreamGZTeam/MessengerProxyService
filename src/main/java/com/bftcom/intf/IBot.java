package com.bftcom.intf;

import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.objmodel.Message;
import com.bftcom.ws.objmodel.Messenger;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public interface IBot {
    void init(Configurator.Config cfg);
    void setMessenger(IMessenger msgr);
    String getBotToken();
    String getName();
    String getProtocol();
    void sendMessage(String chatId, Message msg);
}
