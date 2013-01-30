package com.zachtaylor.jnodalxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLNode {
  public XMLNode(String nodeName) {
    name = nodeName;

    value = null;
    children = null;
    selfClosing = false;
  }

  public Collection<XMLNode> getAllChildren() {
    return Collections.unmodifiableCollection(children);
  }

  public List<XMLNode> getChildren(String nodeName) {
    List<XMLNode> val = new ArrayList<XMLNode>();

    if (children == null)
      return val;

    for (XMLNode node : children) {
      if (node.name.equals(nodeName))
        val.add(node);
    }

    return val;
  }

  public XMLNode addChild(String childName) throws XMLException {
    return addChild(new XMLNode(childName));
  }

  public XMLNode addChild(XMLNode n) throws XMLException {
    if (selfClosing)
      throw new XMLException("Cannot add children to self-closing XMLNode");
    if (children == null)
      children = new ArrayList<XMLNode>();

    children.add(n);
    return this;
  }

  public String getAttribute(String key) {
    return attributes.get(key);
  }

  public XMLNode setAttribute(String key, String value) throws XMLException {
    if (attributes.get(key) != null)
      throw new XMLException("Cannot reset attribute value");

    attributes.put(key, value);
    return this;
  }

  public XMLNode setSelfClosing(boolean b) throws XMLException {
    if (children != null && b)
      throw new XMLException("Cannot set self closing of XMLNode with children");
    if (value != null && b)
      throw new XMLException("Cannot set self closing of XMLNode with value");

    selfClosing = b;
    return this;
  }

  public boolean isSelfClosing() {
    return selfClosing;
  }

  public String getValue() {
    return value;
  }

  public XMLNode setValue(String s) throws XMLException {
    if (selfClosing)
      throw new XMLException("Cannot set value of self closing XMLNode");
    if (children != null)
      throw new XMLException("Cannot set value of XMLNode which has children");

    value = s;
    return this;
  }

  public String toString() {
    return doToString(0);
  }

  private String doToString(int depth) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < depth; i++)
      sb.append("\t");

    sb.append("<");
    sb.append(name);

    for (Map.Entry<String, String> entry : attributes.entrySet()) {
      sb.append(" ");
      sb.append(entry.getKey());
      sb.append("=\"");
      sb.append(entry.getValue());
      sb.append("\"");
    }

    if (!isSelfClosing()) {
      sb.append(">\n");

      for (XMLNode node : children) {
        sb.append(node.doToString(depth + 1));
      }

      for (int i = 0; i < depth; i++)
        sb.append("\t");

      sb.append("</");
      sb.append(name);
      sb.append(">\n");
    }
    else {
      sb.append(" />\n");
    }

    return sb.toString();
  }

  public boolean equals(Object o) {
    if (!(o instanceof XMLNode))
      return false;

    XMLNode node = (XMLNode) o;

    if (!name.equals(node.name))
      return false;
    if (value != null && !value.equals(node.value))
      return false;
    if (value == null && node.value != null)
      return false;
    if (children != null && !children.equals(node.children))
      return false;
    if (children == null && node.children != null)
      return false;
    if (!attributes.equals(node.attributes))
      return false;

    return true;
  }

  public int hashCode() {
    return name.hashCode();
  }

  private boolean selfClosing;
  private String name, value;
  private List<XMLNode> children;
  private Map<String, String> attributes = new HashMap<String, String>();
}