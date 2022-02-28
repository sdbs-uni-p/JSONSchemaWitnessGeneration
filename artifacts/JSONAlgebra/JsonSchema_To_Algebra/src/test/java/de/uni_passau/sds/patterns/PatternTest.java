package de.uni_passau.sds.patterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class PatternTest {

	// What we cannot match yet.
	// TODO \k<name>
	// TODO \n (for nth subpattern)

	@Test
	public void testNullRange() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^[^\\0]$");

		assertTrue(pattern.match("a"));
		assertTrue(pattern.match("0")); // 0 != \0
	}

	@Test
	@Ignore // isEmpty fails for some reason
	public void testEmptyString() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^()$");

		assertTrue(pattern.match(""));
		assertTrue(pattern.isEmpty()); // fails
	}

	@Test
	public void testNonWhiteSpace() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^[\\\\S]+$");

		assertTrue("foo-bar", pattern.match("foo-bar"));
		assertFalse("<space>", pattern.match(" "));
	}

	@Test
	public void testWhiteSpaceChar() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^[\\\\s]+$");

		assertTrue("<ws>", pattern.match(" "));
		assertTrue("<newline>", pattern.match("\n"));
	}

	@Test
	public void testNonWhiteSpaceOutsideRange() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^\\\\S$");

		assertTrue("x", pattern.match("x"));
		assertFalse("<newline>", pattern.match("\n"));
	}

	@Test(expected = REException.class) // TODO Negative class in negated char class
	public void testNonWhiteSpaceNegated() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^[^0-9\\\\S]$"); // it is wrong to split this into [^0-9]|[^\S]

		assertTrue("<blank>", pattern.match(" "));
		assertTrue("<newline>", pattern.match("\n"));
		assertFalse("<digit>", pattern.match("1"));
		assertFalse("x", pattern.match("x"));
	}

	@Test // Works
	public void testNonWhiteSpacePositive() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^[0-9\\\\S]$");

		assertTrue("<character>", pattern.match("a"));
		assertFalse("<newline>", pattern.match("\n"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCreateFromName() {
		Pattern pattern = Pattern.createFromName("foo");
		assertEquals(1, (int) pattern.domainSize());
		assertTrue(pattern.generateWords().contains("foo"));
	}

	@Test
	public void testCreateFromRegexp() throws REException {
		// Regexps are not anchored by default.
		assertTrue(Pattern.createFromRegexp("foo").match("some-foo-thing"));
		assertTrue(Pattern.createFromRegexp("foo").match("foo"));

		assertFalse(Pattern.createFromRegexp("^foo$").match("some-foo-thing"));
		assertTrue(Pattern.createFromRegexp("^foo$").match("foo"));
	}

	@Test
	public void testIsEmptyFalse() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^a?$");
		assertFalse(pattern.isEmpty());
	}

	@Test
	public void testIsEmptyTrue() throws REException {
		Pattern a = Pattern.createFromRegexp("^a$");
		Pattern b = Pattern.createFromRegexp("^b$");
		Pattern c = a.intersect(b);
		assertNotNull(c);
		assertTrue(c.isEmpty());
	}

	@Test
	public void testIsIntersectEmptyFalse() throws REException {
		Pattern a = Pattern.createFromRegexp("^a*$");
		Pattern b = Pattern.createFromRegexp("^b*$");
		// (a*) & (b*) includes the empty word
		assertFalse(a.intersect(b).isEmpty());
	}

	@Test
	@Deprecated
	public void testGenerateWords() throws REException {
		assertTrue(Pattern.createFromRegexp("^a?$").generateWords().contains("a"));
	}
	
	@Test
	@Deprecated
	@Ignore // Does not terminate because solution space is too big.
	public void testGenerateWordsConstraints() throws REException {
		// TODO - works for a{0,2} but not for .{0,2}
		Pattern p = Pattern.createFromRegexp("^.{0,2}$");
		System.out.println(p.toAutomatonString());
		
		//long start = System.nanoTime();
	
		System.out.println("words " + p.generateNewWord());
		// TODO
		
		//long diff = System.nanoTime() - start;

		//System.out.println("time: " + diff);
	}

	@Test
	@Deprecated
	public void testGenerate2Words() throws REException {
		// If language is finite, we generate a list of all words.
		Collection<String> words = Pattern.createFromRegexp("^(a|b)$").generateWords();
		assertTrue(words.contains("a"));
		assertTrue(words.contains("b"));
		assertFalse(words.contains("c"));
	}

	
	@Test
	@Deprecated
	public void testGenerateWordsInf() throws REException {
		// If language is infinite, we generate just one word.
		Collection<String> words = Pattern.createFromRegexp("^a+$").generateWords();
		assertEquals(1, words.size());
		assertTrue(words.contains("a"));
	}

	@Test
	public void testGenerateNextWordEmptyBaseline() throws REException {
		Pattern p = Pattern.createFromRegexp("^(a|b|c)$");
		Collection<String> oldWords = new ArrayList<>();
		
		String word1 = p.generateNewWord(oldWords);
		
		assertNotNull(word1);
	} 
	
	@Test
	public void testGenerateNextWord() throws REException {
		// Generate a word not yet seen.
		Collection<String> oldWords = new ArrayList<>();
		
		Pattern p = Pattern.createFromRegexp("^foo*$");
		String word1 = p.generateNewWord(oldWords);
		oldWords.add(word1);
		
		String word2 = p.generateNewWord(oldWords);		
		assertFalse(oldWords.contains(word2));
		oldWords.add(word2);
		
		String word3 = p.generateNewWord(oldWords);
		assertFalse(oldWords.contains(word3));
		oldWords.add(word3);

		String word4 = p.generateNewWord(oldWords);
		assertFalse(oldWords.contains(word4));
		oldWords.add(word4);
	}


	@Test
	public void testGenerateNextWordRobustness() throws REException {
		// Make sure generation of next words is robust.				
		Pattern p = Pattern.createFromRegexp("^(a|b|c)$");
		@SuppressWarnings("deprecation")
		Collection<String> oldWords = p.generateWords();
		assertEquals(3, oldWords.size());
		
		String newWord = p.generateNewWord(oldWords);
		assertNull(newWord);
	}

	@Test
	public void testGenerateNewWordScalability() throws REException {
		// The following regexp has over 65K possible witnesses.
		Pattern p = Pattern.createFromRegexp("^.{1,3}$");
		String word1= p.generateNewWord();
		assertNotNull(word1);
		
		List<String> oldWords = new ArrayList<>();
		oldWords.add(word1);
		String word2 = p.generateNewWord(oldWords);
		assertNotNull(word2);
	}
	
	@Test
	public void testGenerateNextWordEmptiness() throws REException {
		// Make sure generation of next words is robust.
		
		// Empty language.
		Pattern a = Pattern.createFromRegexp("^a$");
		Pattern b = Pattern.createFromRegexp("^b$");
		Pattern p = a.intersect(b);
		
		@SuppressWarnings("deprecation")
		Collection<String> oldWords = p.generateWords(); // TODO
		assertEquals(0, oldWords.size());
		
		String newWord = p.generateNewWord(oldWords);
		assertNull(newWord);
	}

	
	@Test
	public void testDomainSize() throws REException {
		assertEquals(Pattern.createFromRegexp("^(a|b|c)$").domainSize(), Integer.valueOf(3));
	}

	@Test
	public void testDomainSizeInf() throws REException {
		assertNull(Pattern.createFromRegexp("^a*$").domainSize());
	}
	
	@Test
	public void testHasMoreThanNStrings() throws REException {
		assertTrue(Pattern.createFromRegexp("^(a|b|c)$").hasMoreThanNStrings(1));
		assertFalse(Pattern.createFromRegexp("^(a|b|c)$").hasMoreThanNStrings(3));
		assertFalse(Pattern.createFromRegexp("^(a|b|c)$").hasMoreThanNStrings(4));
	}
	
	@Test
	public void testHasMoreThanNStringsInf() throws REException {
		Pattern regexp = Pattern.createFromRegexp("^.*$");
		assertTrue(regexp.hasMoreThanNStrings(1));
		assertTrue(regexp.hasMoreThanNStrings(100));
	}
	
	@Test(timeout = 5000)
	public void testHasMoreThanNStringsStressTest() throws REException {
		// The pattern is a stress test for witness generation.
		assertTrue(Pattern.createFromRegexp("^[0-9a-zA-Z_-]{1,255}$").hasMoreThanNStrings(42));
	}
	
	@Test
	public void testMinus() throws REException {
		Pattern p1 = Pattern.createFromRegexp("^aa?$");
		Pattern p2 = Pattern.createFromRegexp("^a$");

		assertFalse(p1.minus(p2).isEmpty());
		assertTrue(p2.minus(p1).isEmpty());
	}

	@Test
	public void testMatchTrue() throws REException {
		assertTrue(Pattern.createFromRegexp("aa*").match("aa"));
	}

	@Test
	public void testMatchFalse() throws REException {
		assertFalse(Pattern.createFromRegexp("^aa*$").match("abc"));
	}

	@Test
	public void testClone() throws REException {
		Pattern p1 = Pattern.createFromRegexp("a*");
		Pattern p2 = p1.clone();

		assertNotSame(p1, p2);
		assertTrue(p1.isEquivalent(p2));
	}

	@Test
	public void testSubsetOf() throws REException {
		Pattern p1 = Pattern.createFromRegexp("^a*$");
		Pattern p2 = Pattern.createFromRegexp("^a+$");

		assertFalse(p1.isSubsetOf(p2));
		assertTrue(p2.isSubsetOf(p1));
	}

	@Test
	public void testIsEquivalent() throws REException {
		Pattern p1 = Pattern.createFromRegexp("^aa*$");
		Pattern p2 = Pattern.createFromRegexp("^a+$");

		assertTrue(p1.isEquivalent(p1));
		assertTrue(p1.isEquivalent(p2));
		assertTrue(p2.isEquivalent(p1));
	}

	@Test
	public void testComplement() throws REException {
		Pattern p = Pattern.createFromRegexp("^a$");
		assertTrue(p.isEquivalent(p.complement().complement()));
	}

	@Test
	public void testUnion() throws REException {
		Pattern p1 = Pattern.createFromRegexp("^a?$");
		Pattern p2 = Pattern.createFromRegexp("^b?$");
		Pattern u = p1.union(p2);

		assertTrue(u.match(""));
		assertTrue(u.match("a"));
		assertTrue(u.match("b"));
	}

	@Test
	public void testListComplement() throws REException {
		Pattern p1 = Pattern.createFromRegexp("^a$");
		Pattern p2 = Pattern.createFromRegexp("^b$");

		Collection<Pattern> patterns = new HashSet<>();
		patterns.add(p1);
		patterns.add(p2);

		Pattern c = Pattern.listComplement(patterns);
		assertFalse(c.match("a"));
		assertFalse(c.match("b"));
		assertTrue(c.match("c"));
	}

	@Test
	public void testOverlaps() throws REException {
		Pattern p1 = Pattern.createFromRegexp("^a?b?$");
		Pattern p2 = Pattern.createFromRegexp("^b?c?$");
		Pattern p3 = Pattern.createFromName("xyz");

		assertTrue(Pattern.overlaps(p1, p2));
		assertFalse(Pattern.overlaps(p1, p3));
		assertFalse(Pattern.overlaps(p2, p3));
	}

	@Test
	public void testOverlapsCollection() throws REException {
		Pattern p1 = Pattern.createFromRegexp("^a?b?$");
		Pattern p2 = Pattern.createFromRegexp("^b?c?$");
		Pattern p3 = Pattern.createFromName("xyz");

		Collection<Pattern> collection = new HashSet<>();
		collection.add(p1);
		collection.add(p2);
		collection.add(p3);

		assertTrue(Pattern.overlaps(collection));
	}

	@Test
	public void testOverlapsCollectionNoMatch() throws REException {
		Pattern p1 = Pattern.createFromRegexp("^foo$");
		Pattern p2 = Pattern.createFromRegexp("^bar$");
		Pattern p3 = Pattern.createFromRegexp("^baz$");

		Collection<Pattern> collection = new HashSet<>();
		collection.add(p1);
		collection.add(p2);
		collection.add(p3);

		assertFalse(Pattern.overlaps(collection));
	}

	@Test
	public void testInvalidPattern() throws REException {
		Pattern p = Pattern.createFromRegexp("^a{1,2$"); // invalid syntax, so matches literally
		assertTrue("a{1,2", p.match("a{1,2"));
	}

	@Test
	public void testAt() throws REException {
		Pattern p = Pattern.createFromRegexp("^@$");

		assertTrue("@", p.match("@"));
		assertFalse("a", p.match("a"));
	}

	@Test
	public void testBracket() throws REException {
		Pattern p = Pattern.createFromRegexp("^(\\\\[|\\\\])$");

		assertTrue("[", p.match("["));
	}

	@Test
	public void testBracketRange() throws REException {
		// The original pattern inspiring this test: "^[\\w\\-\\*\\[\\]\\?]+$"
		Pattern p = Pattern.createFromRegexp("^[\\\\[\\\\]]$");

		assertTrue("[", p.match("["));
		assertTrue("]", p.match("]"));
	}

	@Test
	public void testBackslashStar() throws REException {
		Pattern p = Pattern.createFromRegexp("^\\\\\\\\*$");

		assertTrue("\\", p.match("\\"));
		assertTrue("\\\\", p.match("\\\\"));
	}

	// In Bricks automaton, '#' stands for the empty language.
	@Test
	public void testHash() throws REException {
		Pattern p = Pattern.createFromRegexp("^[a#d]$");

		assertTrue(p.match("#"));
	}

	@Test
	public void testDigits() throws REException {
		Pattern p = Pattern.createFromRegexp("^[0-9]$");

		assertTrue(p.match("1"));
		assertFalse(p.match("a"));
	}

	@Test
	public void testDigits2() throws REException {
		Pattern p = Pattern.createFromRegexp("^[a-z0-9]$");

		assertTrue(p.match("1"));
		assertFalse(p.match("42"));
	}

	@Test
	public void testNonDigit() throws REException {
		Pattern p = Pattern.createFromRegexp("^\\\\D$");

		assertTrue(p.match("a"));
		assertFalse(p.match("3"));
	}

	@Test
	public void testWord() throws REException {
		Pattern p = Pattern.createFromRegexp("^\\\\w+$"); // [a-zA-Z0-9_]

		assertTrue(p.match("abcABC123_"));
		assertFalse(p.match("!!!"));
	}

	@Test
	public void testNonWord() throws REException {
		Pattern p = Pattern.createFromRegexp("^\\\\W+$"); // [^a-zA-Z0-9_]

		assertFalse(p.match("abcABC123_"));
		assertTrue(p.match("!!!"));
	}

	@Test
	public void testPosRange() throws REException {
		Pattern p = Pattern.createFromRegexp("^[a-z]$");
		assertTrue(p.match("a"));
		assertFalse(p.match("1"));
	}

	@Test
	public void testNegPosRange() throws REException {
		Pattern p = Pattern.createFromRegexp("^[^a-z]$");
		assertFalse(p.match("a"));
		assertTrue(p.match("1"));
	}

	@Test
	public void testGnuRegexpWhitespace() throws REException {
		// Tests the whitespace symbols supported by gnu.regexp.
		Pattern p = Pattern.createFromRegexp("^[ \\n\\t\\r]+$");

		assertTrue("<newline>", p.match("\n"));
		assertTrue("<tab>", p.match("\t"));
		assertTrue("<returnescape>", p.match("\r"));

	}

	@Test
	public void testWhitespace() throws REException {
		Pattern p = Pattern.createFromRegexp("^[ \\n\\t\\r\\f\\\\v]+$");

		assertTrue("<space>", p.match(" "));
		assertTrue("<newline>", p.match("\n"));
		assertTrue("<tab>", p.match("\t\t\t"));
		assertTrue("<formfeed>", p.match("\f\t\n"));
		assertTrue("<vtab>", p.match("\u000b")); // '\v', not known to Java

		assertFalse(p.match("ntr"));
	}

	@Test
	public void testFormFeedOutsideRange() throws REException {
		// "\f" is not originally supported by gnu.regexp.
		// This had to be added, so we need to test this explicitly.
		Pattern p = Pattern.createFromRegexp("^\\f+$");

		assertTrue("<formfeed>", p.match("\f"));
		assertFalse("escaped f", p.match("\\f\\f\\f"));
	}

	@Test
	public void testVerticalTabOutsideRange() throws REException {
		// "\v" is not originally supported by gnu.regexp.
		// This had to be added, so we need to test this explicitly.
		Pattern p = Pattern.createFromRegexp("^\\\\v+$");

		assertTrue("<vtab>", p.match("\u000b"));
		assertFalse("escaped v", p.match("\\v\\v"));
	}

	@Test
	public void testNoBlankOrNewline() throws REException {
		Pattern p = Pattern.createFromRegexp("^[^ \\n]+$");

		assertFalse("<space>", p.match(" "));
		assertFalse("<newline>", p.match("\n"));

		assertTrue("\\n", p.match("\\n"));
	}

	@Test
	public void testRepetition() throws REException {
		Pattern p = Pattern.createFromRegexp("^a{3}$");

		assertTrue("aaa", p.match("aaa"));
		assertFalse("a", p.match("a"));
	}

	@Test
	public void testOr() throws REException {
		Pattern p = Pattern.createFromRegexp("^foo$|^bar$");

		assertTrue("foo", p.match("foo"));
		assertTrue("bar", p.match("bar"));
		assertFalse("fobar", p.match("fobar"));
	}

	@Test
	public void testAtInRange() throws REException {
		Pattern p = Pattern.createFromRegexp("^[abc@]+$");

		// assertTrue("aaa", p.match("aaa"));
		assertTrue("@@@", p.match("@@@"));
		assertFalse("def", p.match("def"));
	}

	@Test
	public void testAtInRange2() throws REException {
		Pattern p = Pattern.createFromRegexp("^[@-C]+$");

		assertTrue("AAA", p.match("AAA"));
		assertTrue("@BC", p.match("@BC"));
		assertFalse("def", p.match("def"));
	}

	@Test
	public void testDash() throws REException {
		assertTrue(Pattern.createFromRegexp("^\\-$").match("-"));
		assertTrue(Pattern.createFromRegexp("^-$").match("-"));
		assertTrue(Pattern.createFromRegexp("^[\\-]$").match("-"));
		assertTrue(Pattern.createFromRegexp("^[-]$").match("-"));
	}

	@Test
	public void testParExpr() throws REException {
		assertTrue(Pattern.createFromRegexp("^abc[()]def$").match("abc(def"));
		assertTrue(Pattern.createFromRegexp("^abc[\\\\(\\\\)]def$").match("abc(def"));
		assertTrue(Pattern.createFromRegexp("^abc\\\\(\\\\)def$").match("abc()def"));
		assertTrue(Pattern.createFromRegexp("^abc()def$").match("abcdef"));
	}

	@Test
	public void testDots() throws REException {
		assertTrue("<match any>", Pattern.createFromRegexp("^.$").match("x"));
		assertTrue("<match dot>", Pattern.createFromRegexp("^\\.$").match("."));

		assertFalse("<match any, range>", Pattern.createFromRegexp("^[.]$").match("x"));

		assertFalse("<match dot, range>", Pattern.createFromRegexp("^[\\.]$").match("x"));
		assertTrue("<match dot, range>", Pattern.createFromRegexp("^[.]$").match("."));
	}

	@Test
	public void testPlusMinus() throws REException {
		Pattern p = Pattern.createFromRegexp("^([\\.]|\\\\+|-)$");

		assertTrue(p.match("."));
		assertTrue(p.match("+"));
		assertTrue(p.match("-"));
		assertFalse(p.match("x"));
	}

	@Test
	public void testFoo() throws REException {
		Pattern p = Pattern.createFromRegexp("^a+@(b+\\.)+c+$");

		assertTrue(p.match("aaa@bbb.ccc"));
	}

	@Test
	public void testUnicode() throws REException {
		Pattern p = Pattern.createFromRegexp("^\u0214$");

		assertTrue(p.match("Ȕ"));
	}

	@Test
	public void testUnicodeRange() throws REException {
		Pattern p = Pattern.createFromRegexp("^[\u0214-\u0216]$");

		assertTrue(p.match("Ȕ"));
		assertEquals(p.domainSize().intValue(), 3);
	}

	@Test
	public void testHexConstant() throws REException {
		Pattern p = Pattern.createFromRegexp("^\\\\x41*$");

		assertTrue(p.match("AAA"));
		assertFalse(p.match("x41111"));
	}

	@Test
	public void testHexConstantInRange() throws REException {
		Pattern p = Pattern.createFromRegexp("^[\\x41-\\x43]$");

		assertTrue(p.match("A"));
		assertTrue(p.match("B"));
		assertTrue(p.match("C"));
	}

	// TODO - word boundaries not yet supported
	@Test(expected = REException.class)
	public void testWordBoundary() throws REException {
		Pattern p = Pattern.createFromRegexp("\\\\bis\\\\b");

		assertTrue(p.match("This island is beautiful"));
		assertFalse(p.match("This island isn't beautiful"));
	}

	// TODO - non-word boundary \\B
	@Test(expected = REException.class)
	public void testNonWordBoundary() throws REException {
		Pattern p = Pattern.createFromRegexp("\\\\Bis");

		assertTrue(p.match("This island is beautiful"));
		assertTrue(p.match("Is the island beautiful?"));
	}

	@Test
	public void testControlCharacter() throws REException {
		Pattern p = Pattern.createFromRegexp("\\\\ca"); // CTRL+a

		assertTrue(p.match("\u0001"));
		assertFalse(p.match("ca"));
	}

	@Test
	public void testOctalConstant() throws REException {
		Pattern p = Pattern.createFromRegexp("^\\101[0-5]$"); // A0, A1, ...

		// System.out.println(p.toAutomatonString());
		assertTrue(p.match("A0"));
	}

	@Test
	public void testOctalConstantRange() throws REException {
		Pattern p = Pattern.createFromRegexp("^[\\101-\\103]$");

		assertTrue(p.match("A"));
		assertTrue(p.match("B"));
	}

	@Test
	public void testDoubleQuotes() throws REException {
		Pattern p = Pattern.createFromRegexp("^\\\"$");

		assertTrue(p.match("\""));
		assertFalse(p.match("\\\""));
	}

	@Test
	public void testDoubleQuotes_schema48425() throws REException {
		Pattern p = Pattern.createFromRegexp("^[0-9]{1,3}'[0-9]{1,2}\\\"$");

		assertTrue(p.match("012'34\""));
		assertFalse(p.match("567'89\\\""));
	}

	@Test
	public void testDoubleQuotes_inCharClasses() throws REException {
		Pattern p = Pattern.createFromRegexp("^[\\\"-%]$");

		assertTrue(p.match("\""));
		assertTrue(p.match("#"));
		assertTrue(p.match("$"));
		assertTrue(p.match("%"));
		assertFalse(p.match("&"));
		assertFalse(p.match("\\\""));
	}

}
