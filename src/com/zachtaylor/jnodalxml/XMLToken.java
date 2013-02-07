package com.zachtaylor.jnodalxml;

public class XMLToken {
  public XMLToken(XMLTokenType t, String s) {
    type = t;
    value = s;
  }

  public XMLToken(XMLTokenType t) {
    this(t, null);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String v) {
    value = v;
  }

  public XMLTokenType getType() {
    return type;
  }

  public String toString() {
    if (value != null)
      return type.name() + " : " + value;
    return type.name();
  }

  private String value;
  private XMLTokenType type;
}
