package de.uni_passau.sds.patterns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PatternConverterTest {

	// TODO - test de.uni_passau.sds.patterns with positive or negative lookahead
	// Possibly throw an exception.

	@Test
	public void testASCIINull() throws REException {
		assertEquals("[^\0]", PatternConverter.rewrite("^[^\\0]$"));
	}

	@Test
	public void testRepetitions() throws REException {
		assertEquals("(abc)*", PatternConverter.rewrite("^(abc)*$"));
		assertEquals("@(abc)*@", PatternConverter.rewrite("(abc)*"));
		assertEquals("a+", PatternConverter.rewrite("^a+$"));
		assertEquals("foo(bar)?", PatternConverter.rewrite("^foo(bar)?$"));

		assertEquals("foo(bar)?", PatternConverter.rewrite("^(foo(bar)?)$"));
	}

	@Test
	public void testBound() throws REException {
		assertEquals("@foo@", PatternConverter.rewrite("foo"));

		assertEquals("@foo", PatternConverter.rewrite("foo$"));
		assertEquals("foo@", PatternConverter.rewrite("^foo"));
		assertEquals("foo", PatternConverter.rewrite("^foo$"));

		// Make sure that a n escaped dollar sign is not mistaken
		// for the end o the string in binding the regex.
		assertEquals("@foo$@", PatternConverter.rewrite("foo\\\\$")); // Waaaah
	}

	@Test
	public void testMultiBound() throws REException {
		assertEquals("foo|bar", PatternConverter.rewrite("^foo$|^bar$"));
		assertEquals("(foo|bar)@", PatternConverter.rewrite("^foo|^bar"));
		assertEquals("@foo|bar@", PatternConverter.rewrite("foo$|^bar"));
		assertEquals("foo|@bar@", PatternConverter.rewrite("^foo$|bar"));
		assertEquals("foo|bar", PatternConverter.rewrite("^(foo|bar)$"));
		assertEquals("@(foo|bar)@", PatternConverter.rewrite("foo|bar"));

	}

	@Test
	public void testPreserveEscapings() throws REException { // Must
		// preserve "\.".
		assertEquals("\\.", PatternConverter.rewrite("^\\\\.$"));

		// Must preserve "\+".
		assertEquals("\\+", PatternConverter.rewrite("^\\\\+$"));

		// Must preserve "\*".
		assertEquals("\\*", PatternConverter.rewrite("^\\\\*$"));

		// Must preserve "\\".
		assertEquals("\\\\", PatternConverter.rewrite("^\\\\\\\\$"));
	}

	@Test
	public void testEscapeTabs() throws REException {
		assertEquals("\t", PatternConverter.rewrite("^\t$"));
		assertEquals("\n", PatternConverter.rewrite("^\n$"));
	}

	@Test
	public void testEscapeAtSymbol() throws REException {
		assertEquals("foo\\@bar", PatternConverter.rewrite("^foo@bar$"));
		assertEquals("@foo\\@bar@", PatternConverter.rewrite("foo@bar"));
		assertEquals("[a-z]\\@[0-9]", PatternConverter.rewrite("^[a-z]@[0-9]$"));
	}

	@Test
	public void testDigits() throws REException {
		assertEquals("[0-9]", PatternConverter.rewrite("^\\\\d$"));
		assertEquals("\\\\d", PatternConverter.rewrite("^\\\\\\\\d$"));
		assertEquals("[a0-9]", PatternConverter.rewrite("^[a\\\\d]$"));
		assertEquals("foo[0-9]bar", PatternConverter.rewrite("^foo[\\\\d]bar$"));
		assertEquals("foo[abc\\]def0-9]bar", PatternConverter.rewrite("^foo[abc\\\\]def\\\\d]bar$"));
	}

	@Test
	public void testCharClasses() throws REException {
		assertEquals("[0-9]", PatternConverter.rewrite("^[0-9]$"));
		assertEquals("[0-9]+", PatternConverter.rewrite("^[0-9]+$"));
		assertEquals("[0-9]+\\+[0-9]", PatternConverter.rewrite("^[0-9]+\\\\+[0-9]$"));
		assertEquals("[.+\\-]", PatternConverter.rewrite("^[.+-]$"));

		assertEquals("[.+\\-]", PatternConverter.rewrite("^[\\\\.+-]$"));
		assertEquals("[\\\\.]", PatternConverter.rewrite("^[\\\\\\\\.]$"));
	}

	@Test
	public void testGroupNoncapturing() throws REException {
		assertEquals("foo(bar)?", PatternConverter.rewrite("^foo(?:bar)?$"));
	}

	@Test
	public void testParentheses() throws REException {
		assertEquals("abcdefghi", PatternConverter.rewrite("^abc(def)ghi$"));
		assertEquals("abc(def)?ghi", PatternConverter.rewrite("^abc(def)?ghi$"));
		assertEquals("abc[0-9]?ghi", PatternConverter.rewrite("^abc(\\\\d)?ghi$"));
		assertEquals("abc\\(def\\)ghi", PatternConverter.rewrite("^abc\\\\(def\\\\)ghi$"));
		assertEquals("abc[()]def", PatternConverter.rewrite("^abc[()]def$"));
	}

	@Test
	public void testMultiplicity() throws REException {
		assertEquals("a{3}", PatternConverter.rewrite("^a{3}$"));
		assertEquals("a{2,5}", PatternConverter.rewrite("^a{2,5}$"));
	}

	@Test
	public void testAnyWordCharacter() throws REException {
		assertEquals("[0-9A-Za-z_]", PatternConverter.rewrite("^\\\\w$"));
		assertEquals("[0-9A-Za-z_]", PatternConverter.rewrite("^[\\\\w]$"));
	}

	@Test
	public void testBackslashInRange() throws REException {
		assertEquals("[a\\\\]", PatternConverter.rewrite("^[a\\\\\\\\]$"));
		assertEquals("[\\\\a]", PatternConverter.rewrite("^[\\\\\\\\a]$"));
	}

	@Test
	public void testDoubleQuotes() throws REException {
		assertEquals("\\\"", PatternConverter.rewrite("^\\\"$"));
	}

	@Test
	public void testDoubleQuotes_schema48425() throws REException {
		assertEquals("[0-9]{1,3}'[0-9]{1,2}\\\"", PatternConverter.rewrite("^[0-9]{1,3}'[0-9]{1,2}\\\"$"));
		assertEquals("[0-9]{1,3}'[0-9]{2}\\\"", PatternConverter.rewrite("^[0-9]{1,3}'[0-9]{2}\\\"$"));
		assertEquals("[0-9]{1,3}'[0-9]{2}\\\"", PatternConverter.rewrite("^[0-9]{1,3}'[0-9]{2}\\\"$"));
	}

	@Test
	public void testDoubleQuotes_inCharClasses() throws REException {
		assertEquals("[\\\"']", PatternConverter.rewrite("^[\\\"']$"));
		assertEquals("[\\\"-%]", PatternConverter.rewrite("^[\\\"-%]$"));
		assertEquals("[!-\\\"]", PatternConverter.rewrite("^[!-\\\"]$"));
	}

	// TODO
	// Pattern p =
	// Pattern.createFromRegexp("^(\\/?((\\.{2})|([a-z0-9\\-]*))($|\\/))*$")
	// this is not rewritten correctly

	// TODO - escape |
}
