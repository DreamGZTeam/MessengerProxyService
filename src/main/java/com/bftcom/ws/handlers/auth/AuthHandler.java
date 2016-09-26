package com.bftcom.ws.handlers.auth;

import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.handlers.AbstractHandler;
import com.bftcom.ws.objmodel.Message;
import com.bftcom.ws.objmodel.TextMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Artem on 26.09.2016.
 */
public class AuthHandler extends AbstractHandler {

  Set<String> cachedAuthTokens = new HashSet<>();
  Set<String> authContactIds = new HashSet<>();

  @Override
  public boolean handleMessage(Message inMessage) {
    if (inMessage.getMessageType() == Message.MESSAGE_TYPE_TEXT) {
      TextMessage textMessage = (TextMessage) inMessage;
      String text = textMessage.getText();
      if (authContactIds.contains(inMessage.getContactId())) {
        return false;
      } else if (text.startsWith("/register ")) {
        String key = text.substring(10);
        if (cachedAuthTokens.contains(key)) {
          cachedAuthTokens.remove(key);
          authContactIds.add(inMessage.getContactId());
          textMessage.setText("You are registered");
        } else {
          textMessage.setText("Wrong token");
        }
        return true;
      } else {
        textMessage.setText("You are not registered");
        return true;
      }
    }
    return false;
  }

  public String generateAuthToken() {
    String token = UUID.randomUUID().toString();
    cachedAuthTokens.add(token);
    return token;
  }

  @Override
  public void init(Configurator.Config cfg) {
    if (cfg.getParam("auth") != null)
    setAuthFlag(Boolean.parseBoolean(cfg.getParam("auth")));
  }
}
