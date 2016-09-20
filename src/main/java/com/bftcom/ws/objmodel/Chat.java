package com.bftcom.ws.objmodel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Chat implements Serializable {

  public String id;
  public String name;
  public boolean isGroup;
  private Set<Contact> contacts = new HashSet<>();
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

  public void addMessage(TextMessage msg) {
    history.add(msg);
  }


  public Set<Contact> getContacts() {
    return contacts;
  }

  public void addContact(Contact contact) {
    contacts.add(contact);
  }
}
