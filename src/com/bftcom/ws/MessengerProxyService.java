package com.bftcom.ws;

import javax.jws.WebService;

/**
 * Created by Artem on 06.09.2016.
 */
@WebService
public interface MessengerProxyService {
  void sendMessage(String text);
}
