package com.bftcom.ws.objmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Chat implements Serializable {

  public String id;
  public String name;
  public boolean isGroup;
  private Map<String, Contact> contacts = new HashMap<>();
  private Set<TextMessage> history = new TreeSet<>();

  public Chat(String id, String name, boolean isGroup) {
    this.id = id;
    this.name = name;
    this.isGroup = isGroup;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean getIsGroup() {
    return isGroup;
  }

  public Set<TextMessage> getHistory() {
    return history;
  }

  public Set<MessageInfo> getFullHistory() {
    return history.stream().map(e -> new MessageInfo(e, contacts.get(e.getContactId()))).collect(Collectors.toCollection(TreeSet::new));
  }

  public void addMessage(TextMessage msg) {
    history.add(msg);
  }


  public Map<String, Contact> getContacts() {
    return contacts;
  }

  public void addContact(Contact contact) {
    contacts.put(contact.getId(), contact);
  }
}
