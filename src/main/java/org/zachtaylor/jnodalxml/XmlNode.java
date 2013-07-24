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
	 * @throws XmlException If the child cannot be added, for instance if this
	 *           node is self-closing or has value
	 */
	public XmlNode addChild(String childName) throws XmlException {
		return addChild(new XmlNode(childName));
	}

	/**
	 * Adds a child to this XmlNode
	 * 
	 * @param n Child node to add
	 * @return This node
	 * @throws XmlException If the child cannot be added, for instance if this
	 *           node is self-closing or has value
	 */
	public XmlNode addChild(XmlNode n) throws XmlException {
		if (selfClosing)
			throw new XmlException("Cannot add children to self-closing XMLNode");
		if (value != null)
			throw new XmlException("Cannot add children to XMLNode with value");

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
		children.addAll(nodes);

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
	 * Removes a child from this XmlNode
	 * 
	 * @param node The XmlNode to remove, as equivalent by {@link #equals(Object)}
	 * @return The removed XmlNode
	 */
	public XmlNode removeChild(XmlNode node) {
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
		children = null;
		return this;
	}

	/**
	 * Tells whether an attribute has been set on this node. It is good practice
	 * to test if an attribute has been set before setting or deleting
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
	 * @return Value assigned to the attribute name. Null if attribute has not
	 *         been set
	 */
	public String getAttribute(String key) {
		return attributes.get(key);
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
	 * Gets an attribute of this node, and calls Integer.parseInt for convenience
	 * 
	 * @param key Attribute name
	 * @return Value assigned to the attribute name as Integer
	 */
	public int getIntAttribute(String key) {
		return Integer.parseInt(getAttribute(key));
	}

	/**
	 * Gets an attribute of this node, and calls Double.parseDouble for
	 * convenience
	 * 
	 * @param key Attribute name
	 * @return Value assigned to the attribute name as Double
	 */
	public double getDoubleAttribute(String key) {
		return Double.parseDouble(getAttribute(key));
	}

	/**
	 * Gets an attribute of this node, and calls Boolean.parseBoolean for
	 * convenience
	 * 
	 * @param key Attribute name
	 * @return Value assigned to the attribute name as Boolean
	 */
	public boolean getBoolAttribute(String key) {
		return Boolean.parseBoolean(getAttribute(key));
	}

	/**
	 * Adds a new attribute to this node
	 * 
	 * @param key Attribute name
	 * @param value Attribute value
	 * @return This node
	 * @throws XmlException If the key is null, value is null, or was previously
	 *           assigned to another value
	 */
	public XmlNode setAttribute(String key, String value) throws XmlException {
		if (key == null || value == null || attributes.get(key) != null)
			throw new XmlException("Cannot reset attribute value");

		attributes.put(key, value);
		return this;
	}

	/**
	 * Adds a new attribute to this node
	 * 
	 * @param key Attribute name
	 * @param value Attribute value
	 * @return This node
	 * @throws XmlException If the key is null, value is null, or was previously
	 *           assigned to another value
	 */
	public XmlNode setAttribute(String key, int value) throws XmlException {
		return setAttribute(key, Integer.toString(value));
	}

	/**
	 * Adds a new attribute to this node
	 * 
	 * @param key Attribute name
	 * @param value Attribute value
	 * @return This node
	 * @throws XmlException If the key is null, value is null, or was previously
	 *           assigned to another value
	 */
	public XmlNode setAttribute(String key, double value) throws XmlException {
		return setAttribute(key, Double.toString(value));
	}

	/**
	 * Adds a new attribute to this node
	 * 
	 * @param key Attribute name
	 * @param value Attribute value
	 * @return This node
	 * @throws XmlException If the key is null, value is null, or was previously
	 *           assigned to another value
	 */
	public XmlNode setAttribute(String key, boolean value) throws XmlException {
		return setAttribute(key, Boolean.toString(value));
	}

	/**
	 * Removes an attribute from this node
	 * 
	 * @param key Attribute name
	 * @return The old value
	 * @throws XmlException If there was no such attribute assigned on this node,
	 *           or key is null
	 */
	public String removeAttribute(String key) throws XmlException {
		if (key == null || attributes.get(key) == null)
			throw new XmlException("Attribute does not exist");

		return attributes.remove(key);
	}

	/**
	 * Removes an attribute from this node, and calls Integer.parseInt on the old
	 * value for convenience
	 * 
	 * @param key Attribute name
	 * @return The old value
	 * @throws XmlException If there was no such attribute assigned on this node,
	 *           or key is null
	 */
	public int removeIntAttribute(String key) throws XmlException {
		return Integer.parseInt(removeAttribute(key));
	}

	/**
	 * Removes an attribute from this node, and calls Double.parseDouble on the
	 * old value for convenience
	 * 
	 * @param key Attribute name
	 * @return The old value
	 * @throws XmlException If there was no such attribute assigned on this node,
	 *           or key is null
	 */
	public double removeDoubleAttribute(String key) throws XmlException {
		return Double.parseDouble(removeAttribute(key));
	}

	/**
	 * Removes an attribute from this node, and calls Boolean.parseBoolean on the
	 * old value for convenience
	 * 
	 * @param key Attribute name
	 * @return The old value
	 * @throws XmlException If there was no such attribute assigned on this node,
	 *           or key is null
	 */
	public boolean removeBoolAttribute(String key) {
		return Boolean.parseBoolean(removeAttribute(key));
	}

	/**
	 * Removes all attributes from this XmlNode
	 * 
	 * @return This XmlNode
	 */
	public XmlNode clearAttributes() {
		attributes = new HashMap<String, String>();
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
		String value = "<" + getName() + " ";

		if (!attributes.isEmpty()) {
			value += "( ";
			for (Map.Entry<String, String> attributePair : attributes.entrySet()) {
				value += attributePair.getKey() + " ";
			}
			value += ") ";
		}
		if (selfClosing) {
			value += "/";
		}

		return value + ">";
	}

	public String printToString(int depth, String tab) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < depth; i++)
			sb.append(tab);

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

			for (XmlNode node : children) {
				sb.append(node.printToString(depth + 1, tab));
				sb.append("\n");
			}

			for (int i = 0; i < depth; i++)
				sb.append(tab);

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
			sb.append(">");
		}

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
	private Map<String, String> attributes = new HashMap<String, String>();
}