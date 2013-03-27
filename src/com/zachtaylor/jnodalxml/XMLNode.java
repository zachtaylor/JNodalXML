package com.zachtaylor.jnodalxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLNode {
  /**
   * Constructor for XMLNode
   * 
   * @param nodeName Name of the node
   */
  public XMLNode(String nodeName) {
    name = nodeName;

    value = null;
    children = null;
    selfClosing = false;
  }

  /**
   * Getter for name of the node
   * 
   * @return The tag name
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for all of the children
   * 
   * @return An unmodifiable collection of nodes
   */
  public Collection<XMLNode> getAllChildren() {
    return Collections.unmodifiableCollection(children);
  }

  /**
   * Getter for children with the specified name
   * 
   * @param nodeName Name to search for among child nodes
   * @return A newly constructed list of child nodes with the specified name
   */
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

  /**
   * Shorthand for addChild(XMLNode n)
   * 
   * @param childName Name of the child node to add
   * @return This node
   * @throws XMLException If the child cannot be added, for instance if this
   *           node is self-closing or has value
   */
  public XMLNode addChild(String childName) throws XMLException {
    return addChild(new XMLNode(childName));
  }

  /**
   * Adds a child to this XMLNode
   * 
   * @param n Child node to add
   * @return This node
   * @throws XMLException If the child cannot be added, for instance if this
   *           node is self-closing or has value
   */
  public XMLNode addChild(XMLNode n) throws XMLException {
    if (selfClosing)
      throw new XMLException("Cannot add children to self-closing XMLNode");
    if (children == null)
      children = new ArrayList<XMLNode>();

    children.add(n);
    return this;
  }

  /**
   * Getter for an attribute of this node
   * 
   * @param key Attribute name
   * @return Value assigned to the attribute name
   */
  public String getAttribute(String key) {
    return attributes.get(key);
  }

  /**
   * Adds a new attribute to this node
   * 
   * @param key Attribute name
   * @param value Attribute value
   * @return This node
   * @throws XMLException If the key was previously assigned to another value
   */
  public XMLNode setAttribute(String key, String value) throws XMLException {
    if (attributes.get(key) != null)
      throw new XMLException("Cannot reset attribute value");

    attributes.put(key, value);
    return this;
  }

  /**
   * Sets this node to be self-closing or not
   * 
   * @param b Whether the node should be self-closing
   * @return This node
   * @throws XMLException If this node has children, or a value, and b is true
   */
  public XMLNode setSelfClosing(boolean b) throws XMLException {
    if (children != null && b)
      throw new XMLException("Cannot set self closing of XMLNode with children");
    if (value != null && b)
      throw new XMLException("Cannot set self closing of XMLNode with value");

    selfClosing = b;
    return this;
  }

  /**
   * Tells whether this node is self-closing
   * 
   * @return Whether this node is self-closing
   */
  public boolean isSelfClosing() {
    return selfClosing;
  }

  /**
   * Getter for the value of this node
   * 
   * @return The value, or null if one has not been assigned
   */
  public String getValue() {
    return value;
  }

  /**
   * Setter for the value of this node
   * 
   * @param s Value to set
   * @return This node
   * @throws XMLException If this node is self-closing or has children
   */
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

    if (isSelfClosing()) {
      sb.append(" />\n");
    }
    else if (children != null) {
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
      sb.append("> ");

      if (value != null) {
        sb.append(value);
        sb.append(" ");
      }

      sb.append("</");
      sb.append(name);
      sb.append(">\n");
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