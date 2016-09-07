package com.bftcom.bots.intf;

import com.bftcom.ws.api.Message;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public interface iBot {

    String getBotToken();
    String getProtocol();
    void sendMessage(String chatId, Message msg);
}
