package com.bftcom.ws;

import com.bftcom.ws.api.Messenger;

import javax.jws.WebService;
import java.util.Set;

/**
 * Created by Artem on 06.09.2016.
 */
@WebService
public interface MessengerProxyService {
  //void sendMessage(String text);

  void sendTextMessage(String messengerId, String chatId, String text);

  Set<String> getMessengers();
}
