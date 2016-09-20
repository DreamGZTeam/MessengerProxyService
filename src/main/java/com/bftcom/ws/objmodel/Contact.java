package com.bftcom.ws.objmodel;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by d.dyldaev on 07.09.16.
 */
public class Contact implements Serializable {

  public String id;
  public String userName;
  public String firstName;
  public String lastName;

  public Contact(String id, String firstName, String lastName, String userName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
  }

  public String getId() {
    return id;
  }

  public String getUserName() {
    return userName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Contact contact = (Contact) o;
    return Objects.equals(id, contact.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
