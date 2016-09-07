package com.bftcom.ws;

import com.bftcom.ws.api.Message;
import com.bftcom.ws.api.Messenger;

import javax.jws.WebService;
import java.util.List;

/**
 * Created by Artem on 06.09.2016.
 */
@WebService
public interface MessengerProxyService {
  void sendMessage(String text);

  List<Messenger> getMessagers();

  int sendMessage(String messagerId, String contactId, Message message);
}
