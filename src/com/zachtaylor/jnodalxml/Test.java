package com.zachtaylor.jnodalxml;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Test {
  public static void main(String[] args) {
    XMLNode n1 = new XMLNode("root");
    n1.addAttribute("n1prop1", "n1prop1val");
    n1.addAttribute("n1prop2", "n1prop2val");

    XMLNode n2 = new XMLNode("child1");
    n2.addAttribute("n2prop1", "n2prop1val");
    n2.addAttribute("n2prop2", "n2prop2val");
    XMLNode n3 = new XMLNode("child2");
    n3.addAttribute("n3prop1", "n3prop1val");
    XMLNode n4 = new XMLNode("child3");

    n1.addChild(n2);
    n1.addChild(n3);
    n2.addChild(n4);

    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter("myxml.xml"));
      bw.write(n1.toString());
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      System.out.print(XMLNode.fromFile("myxml.xml"));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (XMLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}