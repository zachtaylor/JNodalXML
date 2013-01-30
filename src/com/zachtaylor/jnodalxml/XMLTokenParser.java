package com.zachtaylor.jnodalxml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class XMLTokenParser {
  public static List<XMLNode> parse(File f) throws IOException {
    return parse(new XMLTokenizer(f));
  }

  public static List<XMLNode> parse(String s) {
    return parse(new XMLTokenizer(s));
  }

  public static List<XMLNode> parse(XMLTokenizer tokens) {
    List<XMLNode> topLevel = new ArrayList<XMLNode>();
    Stack<XMLNode> nodes = new Stack<XMLNode>();
    Stack<String> attributes = new Stack<String>();

    boolean slash = false, closingTag = false;
    XMLToken token;

    while (tokens.hasNext()) {
      token = tokens.next();

      switch (token.getType()) {
      case NODE_NAME:
        if (slash)
          closingTag = true;
        else
          nodes.push(new XMLNode(token.getValue()));
        continue;
      case ATTRIBUTE_KEY:
        attributes.push(token.getValue());
        continue;
      case OPEN_BRACKET:
      case EQUALS:
        continue;
      case ATTRIBUTE_VALUE:
        nodes.peek().setAttribute(attributes.pop(), token.getValue());
        continue;
      case NODE_VALUE:
        nodes.peek().setValue(token.getValue());
        continue;
      case SLASH:
        slash = true;
        continue;
      }

      // CLOSE_BRACKET
      if (slash) {
        XMLNode done = nodes.pop();
        if (!closingTag) {
          done.setSelfClosing(true);
        }
        if (nodes.isEmpty()) {
          topLevel.add(done);
        }
        else {
          nodes.peek().addChild(done);
        }
      }

      slash = closingTag = false;
    }

    return topLevel;
  }
}