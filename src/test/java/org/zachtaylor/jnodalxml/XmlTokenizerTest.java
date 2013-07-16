package org.zachtaylor.jnodalxml;

import junit.framework.TestCase;

public class XmlTokenizerTest extends TestCase {
	private XmlToken token;
	private XmlTokenizer tokenizer;

	public void testXmlNodeBasics() {
		tokenizer = new XmlTokenizer("< hello / >");
		assertSelfClosingNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("<hello / >");
		assertSelfClosingNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("< hello />");
		assertSelfClosingNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("< hello/ >");
		assertSelfClosingNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("<hello />");
		assertSelfClosingNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("<hello/>");
		assertSelfClosingNode("hello", tokenizer);
	}

	public void testComment() {
		tokenizer = new XmlTokenizer("<!-- x --><hello />");
		assertSelfClosingNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("<hello /> <!-- x -->");
		assertSelfClosingNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("<hello> <!-- x --></hello>");
		assertOpenNode("hello", tokenizer);
		assertCloseNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("<hello> </hello> <!-- x --> ");
		assertOpenNode("hello", tokenizer);
		assertCloseNode("hello", tokenizer);

		tokenizer = new XmlTokenizer("<!-- <foo> </foo> -->");
		assertTrue(!tokenizer.hasNext());
	}

	public void testQuotation() {
		tokenizer = new XmlTokenizer("<foo prop=\"bar\" />");
		token = tokenizer.next();
		assertEquals(XmlTokenType.OPEN_BRACKET, token.getType());
		token = tokenizer.next();
		assertTextToken("foo", token);
		token = tokenizer.next();
		assertTextToken("prop", token);
		token = tokenizer.next();
		assertEquals(XmlTokenType.EQUALS, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.QUOTE, token.getType());
		token = tokenizer.next();
		assertTextToken("bar", token);
		token = tokenizer.next();
		assertEquals(XmlTokenType.QUOTE, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.SLASH, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.CLOSE_BRACKET, token.getType());

		tokenizer = new XmlTokenizer("<foo prop=\"bar baz qux\" />");
		token = tokenizer.next();
		assertEquals(XmlTokenType.OPEN_BRACKET, token.getType());
		token = tokenizer.next();
		assertTextToken("foo", token);
		token = tokenizer.next();
		assertTextToken("prop", token);
		token = tokenizer.next();
		assertEquals(XmlTokenType.EQUALS, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.QUOTE, token.getType());
		token = tokenizer.next();
		assertTextToken("bar", token);
		token = tokenizer.next();
		assertTextToken("baz", token);
		token = tokenizer.next();
		assertTextToken("qux", token);
		token = tokenizer.next();
		assertEquals(XmlTokenType.QUOTE, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.SLASH, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.CLOSE_BRACKET, token.getType());

		tokenizer = new XmlTokenizer("<foo prop=\"bar baz qux\" />");
		token = tokenizer.next();
		assertEquals(XmlTokenType.OPEN_BRACKET, token.getType());
		token = tokenizer.next();
		assertTextToken("foo", token);
		token = tokenizer.next();
		assertTextToken("prop", token);
		token = tokenizer.next();
		assertEquals(XmlTokenType.EQUALS, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.QUOTE, token.getType());
		token = tokenizer.next();
		assertTextToken("bar", token);
		token = tokenizer.next();
		assertTextToken("baz", token);
		token = tokenizer.next();
		assertTextToken("qux", token);
		token = tokenizer.next();
		assertEquals(XmlTokenType.QUOTE, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.SLASH, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.CLOSE_BRACKET, token.getType());

		tokenizer = new XmlTokenizer("<foo prop=\"<hello />\" />");
		token = tokenizer.next();
		assertEquals(XmlTokenType.OPEN_BRACKET, token.getType());
		token = tokenizer.next();
		assertTextToken("foo", token);
		token = tokenizer.next();
		assertTextToken("prop", token);
		token = tokenizer.next();
		assertEquals(XmlTokenType.EQUALS, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.QUOTE, token.getType());
		token = tokenizer.next();
		assertTextToken("<hello", token);
		token = tokenizer.next();
		assertTextToken("/>", token);
		token = tokenizer.next();
		assertEquals(XmlTokenType.QUOTE, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.SLASH, token.getType());
		token = tokenizer.next();
		assertEquals(XmlTokenType.CLOSE_BRACKET, token.getType());
	}

	private void assertOpenNode(String expectedName, XmlTokenizer actual) {
		token = tokenizer.next();
		assertEquals(XmlTokenType.OPEN_BRACKET, token.getType());

		token = tokenizer.next();
		assertTextToken(expectedName, token);

		token = tokenizer.next();
		assertEquals(XmlTokenType.CLOSE_BRACKET, token.getType());
	}

	private void assertCloseNode(String expectedName, XmlTokenizer actual) {
		token = tokenizer.next();
		assertEquals(XmlTokenType.OPEN_BRACKET, token.getType());

		token = tokenizer.next();
		assertEquals(XmlTokenType.SLASH, token.getType());

		token = tokenizer.next();
		assertTextToken(expectedName, token);

		token = tokenizer.next();
		assertEquals(XmlTokenType.CLOSE_BRACKET, token.getType());
	}

	private void assertSelfClosingNode(String expectedName, XmlTokenizer actual) {
		token = tokenizer.next();
		assertEquals(XmlTokenType.OPEN_BRACKET, token.getType());

		token = tokenizer.next();
		assertTextToken(expectedName, token);

		token = tokenizer.next();
		assertEquals(XmlTokenType.SLASH, token.getType());

		token = tokenizer.next();
		assertEquals(XmlTokenType.CLOSE_BRACKET, token.getType());
	}

	private static void assertTextToken(String expectedName, XmlToken token) {
		assertEquals(XmlTokenType.TEXT, token.getType());
		assertEquals(expectedName, token.getValue());
	}
}