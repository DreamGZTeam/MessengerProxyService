package com.bftcom.intf;

import com.bftcom.ws.objmodel.Message;
import com.bftcom.ws.objmodel.Messenger;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public interface IBot {
    void setMessenger(Messenger msgr);
    String getBotToken();
    String getProtocol();
    void sendMessage(String chatId, Message msg);
}
