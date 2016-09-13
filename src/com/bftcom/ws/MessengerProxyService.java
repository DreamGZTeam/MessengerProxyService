package com.bftcom.ws;

import com.bftcom.ws.api.Contact;
import com.bftcom.ws.api.Messenger;

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
    <name>Имя messenger'a</name>
    <id>Идентификатор messenger'a</id>
  */
  List<Messenger> getMessengers(String protocol);

  /* Возвращает список доступных протоколов */
  Set<String> getProtocols();

  /* Возвращает список контактов для заданного messenger'а в формате:
    <id>Идентификатор контакта</id>
    <userName>userName</userName>
    <firstName>firstName</firstName>
    <lastName>lastName</lastName>
  */
  List<Contact> getContacts(String messengerId);

  /* Возвращает историю переписки с конкретным пользователем в формате:
  *  Время сообщения;направление сообщения(исходящее\входящее);текс сообщения */
  List<String> getHistory(String messengerId, String contactId);
}
