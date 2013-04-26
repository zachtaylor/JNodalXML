package org.zachtaylor.jnodalxml;

import junit.framework.TestCase;

public class XMLNodeTest extends TestCase {
  static XMLNode node;

  public void setUp() throws Exception {
    node = new XMLNode("name");
  }

  public void testValue() {
    node.setValue("value");

    assertEquals("value", node.getValue());
  }

  public void testValueEquals() {
    XMLNode other = new XMLNode("name");

    assertEquals(other, node);

    node.setValue("value");
    other.setValue("value");

    assertEquals(other, node);
  }

  public void testValueNotEquals() {
    XMLNode other = new XMLNode("name");

    assertEquals(other, node);

    other.setValue("value");

    assertTrue(!other.equals(node));

    other = new XMLNode("name");

    assertEquals(other, node);

    node.setValue("value");

    assertTrue(!other.equals(node));
  }

  public void testAttribute() {
    node.setAttribute("key", "value");

    assertEquals("value", node.getAttribute("key"));
  }

  public void testAttributeCasts() {
    node.setAttribute("int", 42);
    assertEquals(42, node.getIntAttribute("int"));
    
  }

  public void testMultipleAttributes() {
    node.setAttribute("key1", "value1").setAttribute("key2", "value2").setAttribute("key3", "value3");

    assertEquals("value1", node.getAttribute("key1"));
    assertEquals("value2", node.getAttribute("key2"));
    assertEquals("value3", node.getAttribute("key3"));
  }

  public void testAttributeEquals() {
    XMLNode other = new XMLNode("name");

    assertEquals(other, node);

    node.setAttribute("key", "value");
    other.setAttribute("key", "value");

    assertEquals(other, node);
  }

  public void testAttributeNotEquals() {
    XMLNode other = new XMLNode("name");

    assertEquals(other, node);

    other.setAttribute("key", "value");

    assertFalse(other.equals(node));

    other = new XMLNode("name");

    assertEquals(other, node);

    node.setAttribute("key", "value");

    assertFalse(other.equals(node));
  }

  public void testChild() {
    XMLNode child = new XMLNode("child");

    node.addChild(child);

    assertEquals(child, node.getChildren("child").get(0));
  }

  public void testCannotResetAttribute() {
    node.setAttribute("key", "value");

    try {
      node.setAttribute("key", "newValue");
      fail("Should not allow reset of key \"key\"");
    } catch (XMLException e) {
    }
  }

  public void testChildren() {
    XMLNode child1 = new XMLNode("child1");
    XMLNode child2 = new XMLNode("child2");

    node.addChild(child1).addChild(child2);

    assertEquals(child1, node.getChildren("child1").get(0));
    assertEquals(child2, node.getChildren("child2").get(0));
  }

  public void testCannotSetValueOfSelfClosing() {
    node.setSelfClosing(true);

    try {
      node.setValue("value");
      fail("This should die");
    } catch (Exception e) {
      assertTrue(e instanceof XMLException);
    }
  }
}