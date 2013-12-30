package org.zachtaylor.jnodalxml;

public class XmlToken {
  /** Indicates the textual value of the node */
  private String value;

  /** Indicates the {@link XmlTokenType} of this token */
  private XmlTokenType type;

  public XmlToken(XmlTokenType t, String s) {
    type = t;
    value = s;
  }

  public XmlToken(XmlTokenType t) {
    this(t, null);
  }

  public String getValue() {
    if (value == null) {
      return type.toString();
    }

    return value;
  }

  public XmlToken setValue(String v) {
    value = v;
    return this;
  }

  public XmlTokenType getType() {
    return type;
  }

  @Override
  public String toString() {
    if (value != null) {
      return type.name() + " : " + value;
    }

    return type.name();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof XmlToken)) {
      return false;
    }

    XmlToken t = (XmlToken) o;

    if (this.type != t.type) {
      return false;
    }
    if (!this.getValue().equals(t.getValue())) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return getValue().hashCode();
  }
}