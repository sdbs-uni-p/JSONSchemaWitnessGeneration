package de.uni_passau.sds.patterns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PatternToStringTest {

	@Test
	public void testFoo() throws REException {
		Pattern pattern = Pattern.createFromRegexp("^[abc]$");

		assertEquals("base([abc])", pattern.toString());
	}

	@Test
	public void testIntersect() throws REException {
		Pattern abc = Pattern.createFromRegexp("^[abc]$");
		Pattern cde = Pattern.createFromRegexp("^[cde]$");

		Pattern c = abc.intersect(cde);

		assertEquals("allOf[base([abc]), base([cde])]", c.toString());
	}

	@Test
	public void testNot() throws REException {
		Pattern foo = Pattern.createFromRegexp("^foo$");
		Pattern notFoo = foo.complement();

		assertEquals("not(base(foo))", notFoo.toString());
	}

	@Test
	public void testClone() throws REException {
		Pattern foo = Pattern.createFromRegexp("^foo$");
		Pattern clone = foo.clone();

		assertEquals(foo.toString(), clone.toString());
	}

	@Test
	public void testMinus() throws REException {
		Pattern fooQ = Pattern.createFromRegexp("^foo?$");
		Pattern foo = Pattern.createFromRegexp("^foo$");
		Pattern minus = fooQ.minus(foo);

		assertEquals("allOf[base(foo?), not(base(foo))]", minus.toString());
	}

	@Test
	public void testUnion() throws REException {
		Pattern foo = Pattern.createFromRegexp("^foo$");
		Pattern bar = Pattern.createFromRegexp("^bar$");
		Pattern union = foo.union(bar);

		assertEquals("anyOf[base(foo), base(bar)]", union.toString());
	}

}
