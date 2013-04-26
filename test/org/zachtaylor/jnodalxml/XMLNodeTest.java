package org.zachtaylor.jnodalxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

public class XMLNodeTest extends TestCase {
  static XMLNode node;

  static String NODE_NAME = "name";

  public void setUp() throws Exception {
    node = new XMLNode(NODE_NAME);
  }

  public void testName() {
    assertEquals(NODE_NAME, node.getName());
  }

  public void testValue() {
    node.setValue("value");

    assertEquals("value", node.getValue());
  }

  public void testValueEquals() {
    XMLNode other = new XMLNode(NODE_NAME);

    assertEquals(other, node);

    node.setValue("value");
    other.setValue("value");

    assertEquals(other, node);
  }

  public void testValueNotEquals() {
    XMLNode other = new XMLNode(NODE_NAME);

    assertEquals(other, node);

    other.setValue("value");

    assertTrue(!other.equals(node));

    other = new XMLNode(NODE_NAME);

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
      assertTrue(e instanceof XMLException);
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
      assertTrue(e instanceof XMLException);
    }

    try {
      node.setAttribute("key", null);
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }

    try {
      node.removeAttribute("key");
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }
  }

  public void testCannotResetAttribute() {
    node.setAttribute("key", "value");

    try {
      node.setAttribute("key", "newValue");
      fail("Should not allow reset of key \"key\"");
    } catch (XMLException e) {
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
    XMLNode other = new XMLNode(NODE_NAME);

    assertEquals(other, node);

    node.setAttribute("key", "value");
    other.setAttribute("key", "value");

    assertEquals(other, node);
  }

  public void testAttributeNotEquals() {
    XMLNode other = new XMLNode(NODE_NAME);

    assertEquals(other, node);

    other.setAttribute("key", "value");

    assertFalse(other.equals(node));

    other = new XMLNode(NODE_NAME);

    assertEquals(other, node);

    node.setAttribute("key", "value");

    assertFalse(other.equals(node));
  }

  public void testChild() {
    XMLNode child = new XMLNode("child");

    node.addChild(child);

    assertEquals(child, node.getChildren("child").get(0));
  }

  public void testChildren() {
    XMLNode child1 = new XMLNode("child1");
    XMLNode child2 = new XMLNode("child2");

    node.addChild(child1).addChild(child2);

    assertEquals(child1, node.getChildren("child1").get(0));
    assertEquals(child2, node.getChildren("child2").get(0));
  }

  public void testTwins() {
    List<XMLNode> nodes = new ArrayList<XMLNode>();

    nodes.add(new XMLNode("child"));
    nodes.add(new XMLNode("child"));
    nodes.add(new XMLNode("child"));

    for (XMLNode current_node : nodes) {
      node.addChild(current_node);
    }

    List<XMLNode> children = node.getChildren("child");

    assertEquals(nodes.size(), children.size());
    assertTrue(children.containsAll(nodes));
  }

  public void testChildrenNullKey() {
    assertTrue(node.getChildren(null).isEmpty());
  }

  public void testGetAllChildren() {
    List<XMLNode> nodes = new ArrayList<XMLNode>();

    nodes.add(new XMLNode("foo"));
    nodes.add(new XMLNode("bar"));
    nodes.add(new XMLNode("baz"));

    for (XMLNode current_node : nodes) {
      node.addChild(current_node);
    }

    Collection<XMLNode> children = node.getAllChildren();

    assertEquals(nodes.size(), children.size());
    assertTrue(children.containsAll(nodes));
  }

  public void testSetValueConflicts() {
    node = new XMLNode(NODE_NAME);
    node.setSelfClosing(true);

    try {
      node.setValue("value");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }

    node = new XMLNode(NODE_NAME);
    node.addChild("foo");

    try {
      node.setValue("value");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }
  }

  public void testAddChildrenConflicts() {
    node = new XMLNode(NODE_NAME);
    node.setSelfClosing(true);

    try {
      node.addChild("child");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }

    node = new XMLNode(NODE_NAME);
    node.setValue("foo");

    try {
      node.addChild("child");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }
  }

  public void testSetSelfClosingConflicts() {
    node = new XMLNode(NODE_NAME);
    node.addChild("foo");

    // In a way, assert no exception
    node.setSelfClosing(false);

    try {
      node.setSelfClosing(true);
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }

    node = new XMLNode(NODE_NAME);
    node.setValue("foo");

    // In a way, assert no exception
    node.setSelfClosing(false);

    try {
      node.setSelfClosing(true);
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }

    node = new XMLNode(NODE_NAME);

    node.setSelfClosing(true);

    assertTrue(node.isSelfClosing());
  }
}