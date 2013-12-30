package org.zachtaylor.jnodalxml;

import java.util.ArrayList;
import java.util.Arrays;
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
    node.addAttribute("key", "value");

    XmlAttribute attr = new XmlAttribute("key", "value");

    assertEquals(attr, node.getAttribute("key"));
  }

  public void testAttributeHasAndRemove() {
    node.addAttribute("key1", "val1");

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
      node.addAttribute(key, "value");
    }

    Collection<String> attributeKeys = node.attributeKeys();

    assertEquals(keys.size(), attributeKeys.size());
    assertTrue(attributeKeys.containsAll(keys));
  }

  public void testAttributeNulls() {
    try {
      node.addAttribute(null, "value");
    } catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    try {
      node.addAttribute("key", null);
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
    node.addAttribute("key", "value");

    try {
      node.addAttribute("key", "newValue");
      fail("Should not allow reset of key \"key\"");
    } catch (XmlException e) {
    }
  }

  public void testAttributeCasts() {
    int my_int = 42;
    double my_double = 3.14;
    boolean my_boolean = true;

    // int
    node.addAttribute("int", my_int);
    XmlAttribute intAttr = node.getAttribute("int");
    assertEquals(my_int, intAttr.getIntValue());
    assertEquals(intAttr, node.removeAttribute("int"));
    node.addAttribute("int", my_int + "");
    XmlAttribute intAttrFromString = node.getAttribute("int");
    assertEquals(my_int, intAttrFromString.getIntValue());
    assertEquals(intAttrFromString, node.removeAttribute("int"));
    assertTrue(!node.hasAttribute("int"));

    // double
    node.addAttribute("double", my_double);
    XmlAttribute doubleAttr = node.getAttribute("double");
    assertEquals(my_double, doubleAttr.getDoubleValue());
    assertEquals(doubleAttr, node.removeAttribute("double"));
    node.addAttribute("double", my_double + "");
    XmlAttribute doubleAttrFromString = node.getAttribute("double");
    assertEquals(my_double, doubleAttrFromString.getDoubleValue());
    assertEquals(doubleAttrFromString, node.removeAttribute("double"));
    assertTrue(!node.hasAttribute("double"));

    // boolean
    node.addAttribute("boolean", my_boolean);
    XmlAttribute booleanAttr = node.getAttribute("boolean");
    assertEquals(my_boolean, booleanAttr.getBoolValue());
    assertEquals(booleanAttr, node.removeAttribute("boolean"));
    node.addAttribute("boolean", my_boolean + "");
    XmlAttribute booleanAttrFromString = node.getAttribute("boolean");
    assertEquals(my_boolean, booleanAttrFromString.getBoolValue());
    assertEquals(booleanAttrFromString, node.removeAttribute("boolean"));
    assertTrue(!node.hasAttribute("boolean"));
  }

  public void testMultipleAttributes() {
    node.addAttribute("key1", "value1").addAttribute("key2", "value2").addAttribute("key3", "value3");

    XmlAttribute attr1 = new XmlAttribute("key1", "value1");
    XmlAttribute attr2 = new XmlAttribute("key2", "value2");
    XmlAttribute attr3 = new XmlAttribute("key3", "value3");

    assertEquals(attr1, node.getAttribute("key1"));
    assertEquals(attr2, node.getAttribute("key2"));
    assertEquals(attr3, node.getAttribute("key3"));
  }

  public void testAttributeEquals() {
    XmlNode other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    node.addAttribute("key", "value");
    other.addAttribute("key", "value");

    assertEquals(other, node);
  }

  public void testAttributeNotEquals() {
    XmlNode other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    other.addAttribute("key", "value");

    assertFalse(other.equals(node));

    other = new XmlNode(NODE_NAME);

    assertEquals(other, node);

    node.addAttribute("key", "value");

    assertFalse(other.equals(node));
  }

  public void testConstructorSpecifiedParent() {
    XmlNode root = new XmlNode("root");
    XmlNode child = new XmlNode("child", root);

    assertEquals(child.getParent(), root);
  }

  public void testSetParent() {
    XmlNode root = new XmlNode("root");
    XmlNode child = new XmlNode("child");

    child.setParent(root);

    assertEquals(child.getParent(), root);
  }

  public void testSetNullParent() {
    XmlNode child = new XmlNode("child");

    child.setParent(null);

    assertNull(child.getParent());
  }

  public void testSetParentWithPreviousParent() {
    XmlNode root1 = new XmlNode("root");
    XmlNode root2 = new XmlNode("root");
    XmlNode child = new XmlNode("child", root1);

    child.setParent(root2);

    assertEquals(root1.getAllChildren().size(), 0);
  }

  public void testSetParentWithValue() {
    XmlNode root = new XmlNode("root");
    XmlNode child = new XmlNode("child");
    root.setValue("value");

    try {
      child.setParent(root);
      fail("Can't set a parent that has a value");
    }
    catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    assertNull(child.getParent());
  }

  public void testSetSelfClosingParent() {
    XmlNode root = new XmlNode("root");
    XmlNode child = new XmlNode("child");
    root.setSelfClosing(true);

    try {
      child.setParent(root);
      fail("Can't set a parent that is self closing");
    }
    catch (Exception e) {
      assertTrue(e instanceof XmlException);
    }

    assertNull(child.getParent());
  }

  public void testUnspecifiedParentIsNull() {
    XmlNode root = new XmlNode("root");

    assertNull(root.getParent());
  }

  public void testAddChildNodeParentIsThis() {
    XmlNode root = new XmlNode("root");
    XmlNode child = new XmlNode("child");

    root.addChild(child);

    assertEquals(child.getParent(), root);
  }

  public void testAddChildrenParentIsThis() {
    XmlNode root = new XmlNode("root");
    XmlNode child1 = new XmlNode("child1");
    XmlNode child2 = new XmlNode("child2");

    List<XmlNode> children = Arrays.asList(child1, child2);

    root.addAll(children);

    assertEquals(child1.getParent(), root);
    assertEquals(child2.getParent(), root);
  }

  public void testRemoveChildNodeParentIsNull() {
    XmlNode root = new XmlNode("root");
    XmlNode child = new XmlNode("child", root);

    root.removeChild(child);

    assertNull(child.getParent());
  }

  public void testRemoveChildrenParentIsNull() {
    XmlNode root = new XmlNode("root");
    XmlNode child1 = new XmlNode("child1", root);
    XmlNode child2 = new XmlNode("child2", root);

    root.clearChildren();

    assertNull(child1.getParent());
    assertNull(child2.getParent());
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