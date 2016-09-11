package com.bftcom.bots.intf;

import com.bftcom.ws.api.Message;
import com.bftcom.ws.api.Messenger;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public interface iBot {
    void setMessenger(Messenger msgr);
    String getBotToken();
    String getProtocol();
    void sendMessage(String chatId, Message msg);
}
