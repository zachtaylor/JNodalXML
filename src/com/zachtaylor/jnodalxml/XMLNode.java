package com.zachtaylor.jnodalxml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLNode {
  public XMLNode(String nodeName) {
    this.nodeName = nodeName;
  }

  public void addChild(XMLNode child) {
    children.add(child);
  }

  public List<XMLNode> getChildren(String nodeName) {
    List<XMLNode> val = new ArrayList<XMLNode>();

    for (XMLNode node : children) {
      if (node.nodeName.equals(nodeName))
        val.add(node);
    }

    return val;
  }

  public void addAttribute(String key, String value) {
    if (attributes.get(key) != null)
      throw new IllegalArgumentException();

    attributes.put(key, value);
  }

  public String getAttribute(String key) {
    return attributes.get(key);
  }

  public boolean isSelfClosing() {
    return children.isEmpty();
  }

  public String toString() {
    return doToString(0);
  }

  private String doToString(int depth) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < depth; i++)
      sb.append("\t");

    sb.append("<");
    sb.append(nodeName);

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
      sb.append(nodeName);
      sb.append(">\n");
    }
    else {
      sb.append(" />\n");
    }

    return sb.toString();
  }

  //@preformat
  public static XMLNode fromFile(String fileName) throws FileNotFoundException, IOException, XMLException {
    //@format
    return doFromFile(new BufferedReader(new FileReader(fileName)));
  }

  //@preformat
  public static XMLNode doFromFile(BufferedReader br) throws IOException, XMLException {
    //@format
    String line = br.readLine().trim();

    if (line.startsWith("</") && line.endsWith(">"))
      return null;

    if (line.charAt(0) != '<' || !line.endsWith(">"))
      throw new XMLException("Illegal format for line: " + line);

    line = line.substring(1, line.length() - 1);

    String[] pieces = line.split(" ");
    String[] attrPieces;
    String attr, val;

    XMLNode node = new XMLNode(pieces[0]);

    for (int i = 1; i < pieces.length; i++) {
      if (pieces[i].equals("/"))
        return node;

      attrPieces = pieces[i].split("=");
      attr = attrPieces[0];
      val = attrPieces[1];

      if (!val.startsWith("\"") || !val.endsWith("\""))
        throw new XMLException("Illegal format for line: " + line);
      val = val.substring(1, val.length() - 1);

      node.addAttribute(attr, val);
    }

    XMLNode child = XMLNode.doFromFile(br);
    while (child != null) {
      node.addChild(child);
      child = XMLNode.doFromFile(br);
    }

    return node;
  }

  public boolean equals(Object o) {
    if (!(o instanceof XMLNode))
      return false;

    XMLNode node = (XMLNode) o;

    if (!nodeName.equals(node.nodeName))
      return false;
    if (!children.equals(node.children))
      return false;
    if (!attributes.equals(node.attributes))
      return false;

    return true;
  }

  public int hashCode() {
    return nodeName.hashCode();
  }

  private String nodeName;
  private List<XMLNode> children = new ArrayList<XMLNode>();
  private Map<String, String> attributes = new HashMap<String, String>();
}