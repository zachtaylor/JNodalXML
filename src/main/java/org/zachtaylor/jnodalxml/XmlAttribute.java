package org.zachtaylor.jnodalxml;

public class XmlAttribute {
  /**
   * Constructor for the XmlAttribute
   * 
   * @param keyString The key for the XmlAttribute
   * @param valueString The value for the XmlAttribute
   */
  public XmlAttribute(String keyString, String valueString) {
    key = keyString;
    value = valueString;
  }

  /**
   * Retrieves the XmlAttribute Key
   * @return The Key
   */
  public String getKey() {
    return key;
  }

  /**
   * Retrieves the XmlAttribute Value
   * @return The Value
   */
  public String getValue() {
    return value;
  }

  /**
   * Gets value and calls Integer.parseInt for convenience
   * 
   * @return Value assigned to the attribute as Integer
   */
  public int getIntValue() {
    return Integer.parseInt(value);
  }

  /**
   * Gets value and calls Double.parseDouble for convenience
   * 
   * @return Value assigned to the attribute name as Double
   */
  public double getDoubleValue() {
    return Double.parseDouble(value);
  }

  /**
   * Gets value and calls Boolean.parseBoolean for convenience
   * 
   * @return Value assigned to the attribute name as Boolean
   */
  public boolean getBoolValue() {
    return Boolean.parseBoolean(value);
  }

  public String toString() {
    return String.format("%s = %s", key, value);
  }

  public String printToString() {
    return String.format("%s=\"%s\"", key, value);
  }

  public boolean equals(Object o) {
    if (!(o instanceof XmlAttribute)) {
      return false;
    }

    XmlAttribute attribute = (XmlAttribute) o;

    return attribute.key.equals(key) && attribute.value.equals(value);
  }

  private final String key;
  private final String value;
}
