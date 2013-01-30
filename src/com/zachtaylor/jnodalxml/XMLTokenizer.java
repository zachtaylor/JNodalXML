package com.zachtaylor.jnodalxml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    String string = "";
    boolean inBrackets = false, hasName = false;

    while (scan.hasNext() || string.length() > 0) {
      if (string.length() == 0) {
        string = scan.next();
      }
      if (string.startsWith("/")) {
        tokens.add(new XMLToken(XMLTokenType.SLASH));
        string = string.substring(1);
      }
      if (string.startsWith(">")) {
        tokens.add(new XMLToken(XMLTokenType.CLOSE_BRACKET));
        inBrackets = false;
        string = string.substring(1);
      }
      if (string.startsWith("<")) {
        tokens.add(new XMLToken(XMLTokenType.OPEN_BRACKET));
        inBrackets = true;
        hasName = false;
        string = string.substring(1);
      }
      if (string.startsWith("=")) {
        tokens.add(new XMLToken(XMLTokenType.EQUALS));
        string = string.substring(1);
      }
      if (string.startsWith("\"")) {
        string = string.substring(1, string.length());

        while (!string.contains("\""))
          string = string.concat(" ").concat(scan.next());

        int quoteIndex = string.indexOf("\"");
        tokens.add(new XMLToken(XMLTokenType.ATTRIBUTE_VALUE, string.substring(
            0, quoteIndex)));
        string = string.substring(quoteIndex + 1);
        continue;
      }
      if (string.length() > 0) {
        String usablePart;
        int specialCharacterIndex = specialCharacterIndex(string);

        if (specialCharacterIndex == 0) {
          continue;
        }
        else if (specialCharacterIndex > 0) {
          usablePart = string.substring(0, specialCharacterIndex);
          string = string.substring(specialCharacterIndex);
        }
        else {
          usablePart = string;
          string = "";
        }

        if (inBrackets) {
          if (hasName) {
            tokens.add(new XMLToken(XMLTokenType.ATTRIBUTE_KEY, usablePart));
          }
          else {
            tokens.add(new XMLToken(XMLTokenType.NODE_NAME, usablePart));
            hasName = true;
          }
        }
        else {
          tokens.add(new XMLToken(XMLTokenType.NODE_VALUE, usablePart));
        }
      }
    }
  }

  private int specialCharacterIndex(String string) {
    int lowestIndex = -1;
    char[] chars = { '/', '>', '<', '=' };

    for (char c : chars) {
      int index = string.indexOf(c);

      if (index >= 0 && (lowestIndex == -1 || index < lowestIndex)) {
        lowestIndex = index;
      }
    }

    return lowestIndex;
  }

  private Queue<XMLToken> tokens = new LinkedList<XMLToken>();
}