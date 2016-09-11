package com.bftcom.ws;

import javax.jws.WebService;
import java.util.List;
import java.util.Set;

/**
 * Created by Artem on 06.09.2016.
 */
@WebService
public interface MessengerProxyService {

  /* Отсылает сообщение выбранному контакту через выбранный messenger */
  void sendTextMessage(String messengerId, String contactId, String text);

  /* Возвращает список доступных messenger'ов для выбранного протокола в формате:
  * messengerId;messengerName */
  Set<String> getMessengers(String protocol);

  /* Возвращает список доступных протоколов */
  Set<String> getProtocols();

  /* Возвращает список контактов для заданного messrnger'а в формате:
  *  userId;userName */
  Set<String> getContacts(String messengerId);

  /* Возвращает историю переписки с конкретным пользователем в формате:
  *  Время сообщения;направление сообщения(исходящее\входящее);текс сообщения */
  List<String> getHistory(String messengerId, String contactId);
}
