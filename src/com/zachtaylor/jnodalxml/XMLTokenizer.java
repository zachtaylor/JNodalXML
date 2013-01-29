package com.zachtaylor.jnodalxml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class XMLTokenizer {
  public XMLTokenizer(File f) throws IOException {
    Scanner scan = new Scanner(new FileReader(f));
    parse(scan);
    scan.close();
  }

  public XMLTokenizer(String s) {
    Scanner scan = new Scanner(s);
    parse(scan);
    scan.close();
  }

  public boolean hasNext() {
    return !tokens.isEmpty();
  }

  public XMLToken next() {
    return tokens.remove();
  }

  private void parse(Scanner scan) {
    String string;
    boolean hasClosing;
    int equalsIndex, slashIndex;

    while (scan.hasNext()) {
      hasClosing = false;
      string = scan.next();

      if (string.charAt(0) == '<') {
        tokens.add(new XMLToken(XMLTokenType.OPEN_BRACKET));
        string = string.substring(1);
      }
      if (string.endsWith(">")) {
        hasClosing = true;
        string = string.substring(0, string.lastIndexOf('>'));
      }

      equalsIndex = string.indexOf('=');
      slashIndex = string.indexOf("\"");

      if (equalsIndex > 0 && (slashIndex == -1 || slashIndex > equalsIndex)) {
        tokens.add(new XMLToken(XMLTokenType.ATTRIBUTE_KEY, string.substring(0, equalsIndex)));
        tokens.add(new XMLToken(XMLTokenType.EQUALS));
        string = string.substring(equalsIndex + 1);
      }
      if (string.startsWith("/")) {
        tokens.add(new XMLToken(XMLTokenType.SLASH));
        string = string.substring(1);
      }
      if (string.startsWith("\"") && string.endsWith("\"")) {
        string = string.substring(0, string.length() - 1);
        tokens.add(new XMLToken(XMLTokenType.ATTRIBUTE_KEY, string));
      }
      if (hasClosing) {
        tokens.add(new XMLToken(XMLTokenType.CLOSE_BRACKET));
      }
    }
  }

  private Queue<XMLToken> tokens = new LinkedList<XMLToken>();
}