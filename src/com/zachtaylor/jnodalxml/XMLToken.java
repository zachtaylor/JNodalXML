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

  public XMLTokenType getType() {
    return type;
  }

  private String value;
  private XMLTokenType type;
}
