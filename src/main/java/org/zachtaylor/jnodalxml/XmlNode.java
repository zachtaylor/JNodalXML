package org.zachtaylor.jnodalxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlNode {
  /**
   * Constructor for XMLNode
   * 
   * @param nodeName Name of the node
   */
  public XmlNode(String nodeName) {
    name = nodeName;
  }

  /**
   * Constructor for XMLNode
   * 
   * @param nodeName Name of the node
   * @param parentNode Parent XmlNode
   */
  public XmlNode(String nodeName, XmlNode parentNode) {
    this(nodeName);

    parentNode.addChild(this);
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
   * Setter for name of the XmlNode
   * 
   * @param nodeName Name of the XmlNode
   * @return This XmlNode
   */
  public XmlNode setName(String nodeName) {
    name = nodeName;
    return this;
  }

  /**
   * Getter for parent node
   * 
   * @return The parent of this XmlNode
   */
  public XmlNode getParent() {
    return parent;
  }

  /**
   * Setter for parent node. For nodes that already had a parent,
   * this node will additionally be removed from that parent.
   * 
   * @param parentNode The parent XmlNode
   * @return This XmlNode
   * @throws XmlException If the parent cannot have a child, for instance if this parent is self-closing or has value
   */
  public XmlNode setParent(XmlNode parentNode) throws XmlException {
    if (parent != null) {
      parent.removeChild(this);
    }

    if (parentNode != null) {
      parentNode.addChild(this);
    }

    return this;
  }

  /**
   * Getter for all of the children
   * 
   * @return An unmodifiable collection of nodes
   */
  public Collection<XmlNode> getAllChildren() {
    if (children == null)
      return Collections.unmodifiableCollection(new ArrayList<XmlNode>());

    return Collections.unmodifiableCollection(children);
  }

  /**
   * Getter for children with the specified name
   * 
   * @param nodeName Name to search for among child nodes
   * @return A newly constructed list of child nodes with the specified name
   */
  public List<XmlNode> getChildren(String nodeName) {
    List<XmlNode> val = new ArrayList<XmlNode>();

    if (children == null)
      return val;

    for (XmlNode node : children) {
      if (node.name.equals(nodeName))
        val.add(node);
    }

    return val;
  }

  /**
   * Shorthand for {@link #addChild(XmlNode)}
   * 
   * @param childName Name of the child node to add
   * @return This node
   * @throws XmlException If the child cannot be added, for instance if this node is self-closing or has value
   */
  public XmlNode addChild(String childName) throws XmlException {
    return addChild(new XmlNode(childName));
  }

  /**
   * Adds a child to this XmlNode. The child's parent will be set to this XmlNode.
   * 
   * @param n Child node to add
   * @return This node
   * @throws XmlException If the child cannot be added, for instance if this node is self-closing or has value
   */
  public XmlNode addChild(XmlNode n) throws XmlException {
    if (selfClosing)
      throw new XmlException("Cannot add children to self-closing XMLNode");
    if (value != null)
      throw new XmlException("Cannot add children to XMLNode with value");

    n.parent = this;

    if (children == null) {
      children = new ArrayList<XmlNode>();
    }

    children.add(n);
    return this;
  }

  /**
   * Adds all of the specified nodes as children of this XmlNode
   * 
   * @param nodes The collection of XmlNodes to add
   * @return This XmlNode
   */
  public XmlNode addAll(Collection<XmlNode> nodes) {
    for (XmlNode node : nodes) {
      addChild(node);
    }

    return this;
  }

  /**
   * Shorthand for {@link #removeChild(XmlNode)}
   * 
   * @param childName Name of the child node to remove
   * @return The removed XmlNode
   */
  public XmlNode removeChild(String childName) throws XmlException {
    return removeChild(new XmlNode(childName));
  }

  /**
   * Removes a child from this XmlNode. The child's parent will be set to null.
   * 
   * @param node The XmlNode to remove, as equivalent by {@link #equals(Object)}
   * @return The removed XmlNode
   */
  public XmlNode removeChild(XmlNode node) {
    node.parent = null;

    children.remove(node);

    if (children.isEmpty()) {
      children = null;
    }

    return node;
  }

  /**
   * Removes all child XmlNodes from this XmlNode
   * 
   * @return This XmlNode
   */
  public XmlNode clearChildren() {
    for (XmlNode child : children) {
      child.parent = null;
    }

    children = null;

    return this;
  }

  /**
   * Tells whether an attribute has been set on this node. It is good practice to test if an attribute has been set before setting or deleting
   * 
   * @param key Key to test for
   * @return True if this attribute has been set
   */
  public boolean hasAttribute(String key) {
    return attributes.get(key) != null;
  }

  /**
   * Getter for an attribute of this node
   * 
   * @param key Attribute name
   * @return Value assigned to the attribute name. Null if attribute has not been set
   */
  public XmlAttribute getAttribute(String key) {
    return attributes.get(key);
  }

  /**
   * Getter for all attributes of this node
   * 
   * @return All attributes.
   */
  public List<XmlAttribute> getAllAttributes() {
    return new ArrayList<XmlAttribute>(attributes.values());
  }

  /**
   * Getter for all of the attribute keys
   * 
   * @return An unmodifiable collection of string keys
   */
  public Collection<String> attributeKeys() {
    return Collections.unmodifiableCollection(attributes.keySet());
  }

  /**
   * Adds a new attribute to this node
   * 
   * @param attribute The XmlAttribute to add
   * @return This node
   * @throws XmlException If the attribute key has already been assigned on this node
   */
  public XmlNode addAttribute(XmlAttribute attribute) throws XmlException {
    if (attributes.containsKey(attribute.getKey())) {
      throw new XmlException("Cannot reset attribute value");
    }

    attributes.put(attribute.getKey(), attribute);

    return this;
  }

  /**
   * Adds a new attribute to this node
   * 
   * @param key Attribute name
   * @param value Attribute value
   * @return This node
   * @throws XmlException If the key is null, value is null, or was previously assigned to another value
   */
  public XmlNode addAttribute(String key, String value) throws XmlException {
    if (key == null || value == null) {
      throw new XmlException("Cannot have null key or value");
    }

    return addAttribute(new XmlAttribute(key, value));
  }

  /**
   * Adds a new attribute to this node
   * 
   * @param key Attribute name
   * @param value Attribute value
   * @return This node
   * @throws XmlException If the key is null, value is null, or was previously assigned to another value
   */
  public XmlNode addAttribute(String key, int value) throws XmlException {
    return addAttribute(key, Integer.toString(value));
  }

  /**
   * Adds a new attribute to this node
   * 
   * @param key Attribute name
   * @param value Attribute value
   * @return This node
   * @throws XmlException If the key is null, value is null, or was previously assigned to another value
   */
  public XmlNode addAttribute(String key, double value) throws XmlException {
    return addAttribute(key, Double.toString(value));
  }

  /**
   * Adds a new attribute to this node
   * 
   * @param key Attribute name
   * @param value Attribute value
   * @return This node
   * @throws XmlException If the key is null, value is null, or was previously assigned to another value
   */
  public XmlNode addAttribute(String key, boolean value) throws XmlException {
    return addAttribute(key, Boolean.toString(value));
  }

  /**
   * Removes an attribute from this node
   * 
   * @param key Attribute name
   * @return The old value
   * @throws XmlException If there was no such attribute assigned on this node, or key is null
   */
  public XmlAttribute removeAttribute(String key) throws XmlException {
    if (key == null || attributes.get(key) == null)
      throw new XmlException("Attribute does not exist");

    return attributes.remove(key);
  }

  /**
   * Removes all attributes from this XmlNode
   * 
   * @return This XmlNode
   */
  public XmlNode clearAttributes() {
    attributes = new HashMap<String, XmlAttribute>();
    return this;
  }

  /**
   * Sets this node to be self-closing or not
   * 
   * @param b Whether the node should be self-closing
   * @return This node
   * @throws XmlException If this node has children, or a value, and b is true
   */
  public XmlNode setSelfClosing(boolean b) throws XmlException {
    if (children != null && b)
      throw new XmlException("Cannot set self closing of XMLNode with children");
    if (value != null && b)
      throw new XmlException("Cannot set self closing of XMLNode with value");

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
   * @throws XmlException If this node is self-closing or has children
   */
  public XmlNode setValue(String s) throws XmlException {
    if (selfClosing)
      throw new XmlException("Cannot set value of self closing XMLNode");
    if (children != null)
      throw new XmlException("Cannot set value of XMLNode which has children");

    value = s;
    return this;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append('<');
    sb.append(getName());

    if (!attributes.isEmpty()) {
      sb.append("( ");
      for (XmlAttribute attribute : attributes.values()) {
        sb.append(attribute.toString());
        sb.append(' ');
      }
      sb.append(") ");
    }

    if (selfClosing) {
      sb.append(" /");
    }

    sb.append('>');

    return sb.toString();
  }

  public String printToString(int depth, String tab) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < depth; i++)
      sb.append(tab);

    sb.append('<');
    sb.append(name);

    for (XmlAttribute attribute : attributes.values()) {
      sb.append(' ');
      sb.append(attribute.printToString());
    }

    if (isSelfClosing()) {
      sb.append(" />");
      return sb.toString();
    }

    sb.append(">\n");

    if (children != null) {
      for (XmlNode node : children) {
        sb.append(node.printToString(depth + 1, tab));
        sb.append('\n');
      }
    }
    else {
      for (int i = 0; i < depth + 1; i++) {
        sb.append(tab);
      }

      if (value != null) {
        sb.append(value);
        sb.append('\n');
      }
    }

    for (int i = 0; i < depth; i++) {
      sb.append(tab);
    }

    sb.append("</");
    sb.append(name);
    sb.append('>');

    return sb.toString();
  }

  public boolean equals(Object o) {
    if (!(o instanceof XmlNode))
      return false;

    XmlNode node = (XmlNode) o;

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

  private String name, value = null;
  private boolean selfClosing = false;
  private List<XmlNode> children = null;
  private XmlNode parent = null;
  private Map<String, XmlAttribute> attributes = new HashMap<String, XmlAttribute>();
}
