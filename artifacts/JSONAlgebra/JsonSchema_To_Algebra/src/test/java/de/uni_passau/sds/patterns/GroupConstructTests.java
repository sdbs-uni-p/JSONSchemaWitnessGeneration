package de.uni_passau.sds.patterns;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests devoted to group constructs.
 */
public class GroupConstructTests {

	@Test
	public void testGroupMatch() throws REException {
		Pattern p = Pattern.createFromRegexp("^(?:abc){3}$");

		assertTrue(p.match("abcabcabc"));
	}

	@Test
	public void testNamedCapturingGroup() throws REException {
		Pattern p = Pattern.createFromRegexp("(?<foopattern>)^foo.*$");

		assertTrue(p.match("foobar"));
	}

	@Test(expected = REException.class)
	public void testPosLookahead() throws REException {
		Pattern p = Pattern.createFromRegexp("X(?=Y)");

		assertTrue(p.match("XYZ"));
		assertFalse(p.match("XX"));
	}

	@Test(expected = REException.class)
	public void testNegLookahead() throws REException {
		Pattern p = Pattern.createFromRegexp("X(?!Y)");

		assertFalse(p.match("XYZ"));
		assertTrue(p.match("XX"));
	}

	@Test(expected = REException.class)
	public void testNegLookbehind() throws REException {
		Pattern p = Pattern.createFromRegexp("(?<=a)b");

		assertFalse(p.match("bbb"));
		assertTrue(p.match("abc"));
	}

	@Test(expected = REException.class)
	public void testPosLookbehind() throws REException {
		Pattern p = Pattern.createFromRegexp("(?<!a)b");

		assertTrue(p.match("bbb"));
		assertFalse(p.match("abc"));
	}
}
