package org.zachtaylor.jnodalxml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class XMLParser {
  public static List<XMLNode> parse(File f) throws FileNotFoundException {
    return parse(new XMLTokenizer(f));
  }

  public static List<XMLNode> parse(String s) {
    return parse(new XMLTokenizer(s));
  }

  public static List<XMLNode> parse(XMLTokenizer tokens) {
    List<XMLNode> topLevel = new ArrayList<XMLNode>();
    Stack<XMLNode> nodes = new Stack<XMLNode>();

    XMLToken token;

    while (tokens.hasNext()) {
      token = tokens.next();

      if (token.getType() == XMLTokenType.OPEN_BRACKET) {
        token = tokens.next();

        if (token.getType() == XMLTokenType.SLASH) {
          token = tokens.next();
          if (!token.getValue().equals(nodes.peek().getName()))
            throw new RuntimeException(" uh oh ");

          token = tokens.next();
          if (!(token.getType() == XMLTokenType.CLOSE_BRACKET))
            throw new RuntimeException(" oh no ");

          XMLNode node = nodes.pop();
          if (nodes.isEmpty()) {
            topLevel.add(node);
          }
          else {
            nodes.peek().addChild(node);
          }
        }
        else {
          nodes.push(new XMLNode(token.getValue()));

          token = tokens.next();
          while (token.getType() == XMLTokenType.TEXT) {
            String attrName = token.getValue();

            token = tokens.next();
            if (!(token.getType() == XMLTokenType.EQUALS))
              throw new RuntimeException(" awkward ");

            token = tokens.next();
            if (!(token.getType() == XMLTokenType.QUOTE))
              throw new RuntimeException(" foo ");

            token = tokens.next();
            if (!(token.getType() == XMLTokenType.TEXT))
              throw new RuntimeException(" bar ");

            String attrValue = token.getValue();
            token = tokens.next();
            while (token.getType() == XMLTokenType.TEXT) {
              attrValue = attrValue.concat(" ").concat(token.getValue());
              token = tokens.next();
            }

            if (!(token.getType() == XMLTokenType.QUOTE))
              throw new RuntimeException(" foo ");

            nodes.peek().setAttribute(attrName, attrValue);
            token = tokens.next();
          }

          if (token.getType() == XMLTokenType.SLASH) {
            nodes.peek().setSelfClosing(true);

            token = tokens.next();
            if (!(token.getType() == XMLTokenType.CLOSE_BRACKET))
              throw new RuntimeException(" bar ");

            XMLNode node = nodes.pop();
            if (nodes.isEmpty()) {
              topLevel.add(node);
            }
            else {
              nodes.peek().addChild(node);
            }
          }
        }
      }
      else {
        String val = token.getValue();

        while (tokens.peek().getType() == XMLTokenType.TEXT) {
          val = val.concat(" ").concat(tokens.next().getValue());
        }

        nodes.peek().setValue(val);
      }
    }

    return topLevel;
  }
}