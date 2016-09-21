package com.bftcom.ws.objmodel;

/**
 * Created by d.dyldaev on 08.09.16.
 */
public class Update {

  private String contactId;
  private String userName;
  private String firstName;
  private String lastName;

  private String chatId;
  private String chatName;
  private boolean isGroupChat;

  private String text;
  private Long date;

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getChatId() {
    return chatId;
  }

  public void setChatId(String chatId) {
    this.chatId = chatId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getDate() {
    return date;
  }

  public void setDate(Long date) {
    this.date = date;
  }

  public String getChatName() {
    return chatName;
  }

  public void setChatName(String chatName) {
    this.chatName = chatName;
  }

  public boolean isIsGroupChat() {
    return isGroupChat;
  }

  public void setIsGroupChat(boolean groupChat) {
    isGroupChat = groupChat;
  }
}
