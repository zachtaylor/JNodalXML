package org.zachtaylor.jnodalxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

public class XmlNodeTest extends TestCase {
  static XmlNode node;

  static String NODE_NAME = "name";

  public void setUp() throws Exception {
    node = new XmlNode(NODE_NAME);
  }

  public void testName() {
    assertEquals(NODE_NAME, node.getName());
  }

  public void testValue() {
    node.setValue("value");

    assertEquals("value", node.getValue());
  }

  public void testValueEquals() {
    XmlNode other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    node.setValue("value");
    other.setValue("value");

    assertEquals(other, node);
  }

  public void testValueNotEquals() {
    XmlNode other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    other.setValue("value");

    assertTrue(!other.equals(node));

    other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    node.setValue("value");

    assertTrue(!other.equals(node));
  }

  public void testAttribute() {
    node.setAttribute("key", "value");

    assertEquals("value", node.getAttribute("key"));
  }

  public void testAttributeHasAndRemove() {
    node.setAttribute("key1", "val1");

    assertTrue(node.hasAttribute("key1"));

    node.removeAttribute("key1");

    assertTrue(!node.hasAttribute("key1"));

    try {
      node.removeAttribute("key1");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }
  }

  public void testAttributeSet() {
    List<String> keys = new ArrayList<String>();

    keys.add("key1");
    keys.add("key2");
    keys.add("key3");

    for (String key : keys) {
      node.setAttribute(key, "value");
    }

    Collection<String> attributeKeys = node.attributeKeys();

    assertEquals(keys.size(), attributeKeys.size());
    assertTrue(attributeKeys.containsAll(keys));
  }

  public void testAttributeNulls() {
    try {
      node.setAttribute(null, "value");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    try {
      node.setAttribute("key", null);
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    try {
      node.removeAttribute("key");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }
  }

  public void testCannotResetAttribute() {
    node.setAttribute("key", "value");

    try {
      node.setAttribute("key", "newValue");
      fail("Should not allow reset of key \"key\"");
    } catch (XmlException e) {
    }
  }

  public void testAttributeCasts() {
    int my_int = 42;
    double my_double = 3.14;
    boolean my_boolean = true;

    // int
    node.setAttribute("int", my_int);
    assertEquals(my_int, node.getIntAttribute("int"));
    node.removeAttribute("int");
    node.setAttribute("int", my_int + "");
    assertEquals(my_int, node.getIntAttribute("int"));
    assertEquals(my_int, node.removeIntAttribute("int"));
    assertTrue(!node.hasAttribute("int"));

    // double
    node.setAttribute("double", my_double);
    assertEquals(my_double, node.getDoubleAttribute("double"));
    node.removeAttribute("double");
    node.setAttribute("double", my_double + "");
    assertEquals(my_double, node.getDoubleAttribute("double"));
    assertEquals(my_double, node.removeDoubleAttribute("double"));
    assertTrue(!node.hasAttribute("double"));

    // boolean
    node.setAttribute("boolean", my_boolean);
    assertEquals(my_boolean, node.getBoolAttribute("boolean"));
    node.removeAttribute("boolean");
    node.setAttribute("boolean", my_boolean + "");
    assertEquals(my_boolean, node.getBoolAttribute("boolean"));
    assertEquals(my_boolean, node.removeBoolAttribute("boolean"));
    assertTrue(!node.hasAttribute("boolean"));
  }

  public void testMultipleAttributes() {
    node.setAttribute("key1", "value1").setAttribute("key2", "value2").setAttribute("key3", "value3");

    assertEquals("value1", node.getAttribute("key1"));
    assertEquals("value2", node.getAttribute("key2"));
    assertEquals("value3", node.getAttribute("key3"));
  }

  public void testAttributeEquals() {
    XmlNode other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    node.setAttribute("key", "value");
    other.setAttribute("key", "value");

    assertEquals(other, node);
  }

  public void testAttributeNotEquals() {
    XmlNode other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    other.setAttribute("key", "value");

    assertFalse(other.equals(node));

    other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    node.setAttribute("key", "value");

    assertFalse(other.equals(node));
  }

  public void testChild() {
    XmlNode child = new XmlNode("child");

    node.addChild(child);

    assertEquals(child, node.getChildren("child").get(0));
  }

  public void testChildren() {
    XmlNode child1 = new XmlNode("child1");
    XmlNode child2 = new XmlNode("child2");

    node.addChild(child1).addChild(child2);

    assertEquals(child1, node.getChildren("child1").get(0));
    assertEquals(child2, node.getChildren("child2").get(0));
  }

  public void testTwins() {
    List<XmlNode> nodes = new ArrayList<XmlNode>();

    nodes.add(new XmlNode("child"));
    nodes.add(new XmlNode("child"));
    nodes.add(new XmlNode("child"));

    for (XmlNode current_node : nodes) {
      node.addChild(current_node);
    }

    List<XmlNode> children = node.getChildren("child");

    assertEquals(nodes.size(), children.size());
    assertTrue(children.containsAll(nodes));
  }

  public void testChildrenNullKey() {
    assertTrue(node.getChildren(null).isEmpty());
  }

  public void testGetAllChildren() {
    List<XmlNode> nodes = new ArrayList<XmlNode>();

    nodes.add(new XmlNode("foo"));
    nodes.add(new XmlNode("bar"));
    nodes.add(new XmlNode("baz"));

    for (XmlNode current_node : nodes) {
      node.addChild(current_node);
    }

    Collection<XmlNode> children = node.getAllChildren();

    assertEquals(nodes.size(), children.size());
    assertTrue(children.containsAll(nodes));
  }

  public void testSetValueConflicts() {
    node = new XmlNode(NODE_NAME);
    node.setSelfClosing(true);

    try {
      node.setValue("value");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    node = new XmlNode(NODE_NAME);
    node.addChild("foo");

    try {
      node.setValue("value");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }
  }

  public void testAddChildrenConflicts() {
    node = new XmlNode(NODE_NAME);
    node.setSelfClosing(true);

    try {
      node.addChild("child");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    node = new XmlNode(NODE_NAME);
    node.setValue("foo");

    try {
      node.addChild("child");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }
  }

  public void testSetSelfClosingConflicts() {
    node = new XmlNode(NODE_NAME);
    node.addChild("foo");

    // In a way, assert no exception
    node.setSelfClosing(false);

    try {
      node.setSelfClosing(true);
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    node = new XmlNode(NODE_NAME);
    node.setValue("foo");

    // In a way, assert no exception
    node.setSelfClosing(false);

    try {
      node.setSelfClosing(true);
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    node = new XmlNode(NODE_NAME);

    node.setSelfClosing(true);

    assertTrue(node.isSelfClosing());
  }
}