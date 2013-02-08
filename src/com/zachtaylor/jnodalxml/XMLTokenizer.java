package com.zachtaylor.jnodalxml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class XMLTokenizer {
  public XMLTokenizer(File f) throws FileNotFoundException {
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

  public XMLToken peek() {
    return tokens.peek();
  }

  private void parse(Scanner scan) {
    String string = "";
    boolean inBrackets = false, inQuotes = false;

    while (scan.hasNext() || string.length() > 0) {
      if (string.length() == 0) {
        string = scan.next();
      }

      if (inQuotes) {
        int qChar = string.indexOf('"');

        if (qChar < 0) {
          add(new XMLToken(XMLTokenType.TEXT, string));
          string = "";
        }
        else {
          if (qChar > 0) {
            add(new XMLToken(XMLTokenType.TEXT, string.substring(0, qChar)));
            string = string.substring(qChar);
          }
          add(new XMLToken(XMLTokenType.QUOTE));
          string = string.substring(1);
          inQuotes = false;
        }

      }
      else if (string.startsWith("<!--")) {
        // Throw away comments in the Scanner
        string = string.substring(4);

        int endChar = string.indexOf("-->");
        while (endChar == -1) {
          string = string.concat(" ").concat(scan.next());
        }
        string = string.substring(endChar + 3);
      }
      else if (inBrackets) {
        if (string.startsWith("\"")) {
          add(new XMLToken(XMLTokenType.QUOTE));
          string = string.substring(1);
          inQuotes = true;
        }
        else if (string.startsWith("/")) {
          add(new XMLToken(XMLTokenType.SLASH));
          string = string.substring(1);
        }
        else if (string.startsWith("=")) {
          add(new XMLToken(XMLTokenType.EQUALS));
          string = string.substring(1);
        }
        else if (string.startsWith(">")) {
          add(new XMLToken(XMLTokenType.CLOSE_BRACKET));
          string = string.substring(1);
          inBrackets = false;
        }
        else {
          int sChar = specialCharacterIndex(string);

          if (sChar < 0) {
            add(new XMLToken(XMLTokenType.TEXT, string));
            string = "";
          }
          else if (sChar > 0) {
            add(new XMLToken(XMLTokenType.TEXT, string.substring(0, sChar)));
            string = string.substring(sChar);
          }
        }
      }
      else if (string.startsWith("<")) {
        add(new XMLToken(XMLTokenType.OPEN_BRACKET));
        string = string.substring(1);
        inBrackets = true;
      }
      else {
        int sChar = string.indexOf('<');

        if (sChar < 0) {
          add(new XMLToken(XMLTokenType.TEXT, string));
          string = "";
        }
        else if (sChar > 0) {
          add(new XMLToken(XMLTokenType.TEXT, string.substring(0, sChar)));
          string = string.substring(sChar);
        }
      }
    }
  }

  private void add(XMLToken token) {
    tokens.add(token);
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