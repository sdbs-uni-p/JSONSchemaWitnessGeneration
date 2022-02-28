package de.uni_passau.sds.patterns;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RealWorldTest {

	@Test
	public void testOr() throws REException {
		// js_0.json
		Pattern p = Pattern.createFromRegexp("^dev|alpha|beta|rc|RC|stable$");

		assertTrue(p.match("dev"));
		assertTrue(p.match("developer")); // Note that only the first word is bounded.
		assertTrue(p.match("alpha"));
		assertTrue(p.match("foo-alpha-bar"));
		assertFalse(p.match("stables and horses"));
	}

	@Test
	public void testBackslash() throws REException {
		Pattern p = Pattern.createFromRegexp("^[\\\\S]+$");

		// matches any non-whitespace characters
		assertTrue("S", p.match("S"));
		assertTrue("foo", p.match("foo"));
		assertFalse("a b", p.match("a b"));
		assertFalse("a\nb", p.match("a\nb"));
		assertFalse("\t", p.match("\t"));
	}

	@Test
	public void testAlphaNumRange() throws REException {
		Pattern p = Pattern.createFromRegexp("^[a-z0-9-\\\\.]+$");

		assertTrue("<alpha>", p.match("a"));
		assertTrue("...", p.match("..."));
		assertTrue("a1-79", p.match("a1-z9"));
		assertFalse("a\\b\\c", p.match("a\\b\\c"));
	}

	@Test
	public void testItext() throws REException {
		Pattern p = Pattern.createFromRegexp("^I[a-z0-f]{4}$");

		assertTrue(p.match("Iabcd"));
		assertTrue(p.match("I1234"));
		assertTrue(p.match("I[AQ7"));
	}

	@Test
	public void testEmail() throws REException {
		// js_1.json, property author_email
		Pattern p = Pattern.createFromRegexp("^[a-z\\\\d_\\\\.\\+-]+@([a-z\\\\d\\\\.-]+\\.)+[a-z]+$");

		assertTrue("correct email", p.match("bill.gates@microsoft.com"));
		assertFalse("incorrect email", p.match("bill.gates.microsoft.com"));
		assertTrue("correct email with numbers", p.match("bill2@web.com"));
	}

	@Test
	public void testBackslashD() throws REException {
		// js_100.json, policy
		Pattern p = Pattern.createFromRegexp("^((\\\\d+\\\\.)?(\\\\d+\\\\.)?(\\\\*|\\\\d+))|builtin$");

		assertTrue("builtin", p.match("builtin"));
		assertFalse("d", p.match("d"));
		assertTrue("1.3.4", p.match("1.3.4")); // From the JSON Schema document itself.
		assertTrue("0.1", p.match("0.1")); // From the JSON Schema document itself.

		assertTrue("*", p.match("*"));
	}

	@Test
	public void testSwaggerHost() throws REException {
		// js_10009.json.
		Pattern p = Pattern.createFromRegexp("^[^{}/ :\\\\\\\\]+(?::\\\\d+)?$");

		assertTrue(p.match("swagger.io"));
		assertTrue(p.match("petstore.swagger.wordnik.com"));
		assertTrue(p.match("example.com:8089"));
	}

	@Test
	public void testSwaggerBasePathFixed() throws REException {
		// js_10009.json, with fix.
		assertTrue(Pattern.createFromRegexp("^/").match("/api"));
	}

	@Test
	public void testUniqueIdentifier() throws REException {
		// js_10015.json
		Pattern p = Pattern.createFromRegexp("^[-_.A-Za-z0-9]+$");

		assertTrue(p.match("foo-bar_baz.09"));
	}

	@Test
	public void testCountryCode() throws REException {
		// js_10015.json
		// ISO 3166-1 alpha-2 country code in upper case.
		Pattern p = Pattern.createFromRegexp("^[A-Z]{2}$");

		assertTrue(p.match("DE"));
	}

	@Test
	public void testAge() throws REException {
		// js_10015.json
		// RFC3339 date (or prefix).
		Pattern p = Pattern.createFromRegexp("^\\\\d\\\\d\\\\d\\\\d(-\\\\d\\\\d(-\\\\d\\\\d)?)?$");

		assertTrue(p.match("1985-04-12"));
		assertFalse(p.match("1985-04-12T23:20:50.52Z"));
	}

	@Test
	public void testNameOfDirectory() throws REException {
		// js_10019.json
		assertTrue(Pattern.createFromRegexp("^[a-zA-Z0-9_.]*$").match("shot01"));
	}

	@Test
	public void testDateTime() throws REException {
		// js_1002.json
		Pattern p = Pattern.createFromRegexp(
				"^(\\\\d{4})(-)?(\\\\d\\\\d)(-)?(\\\\d\\\\d)(T)?(\\\\d\\\\d)(:)?(\\\\d\\\\d)(:)?(\\\\d\\\\d)(\\.\\\\d+)?(Z|([+-])(\\\\d\\\\d)(:)?(\\\\d\\\\d))");

		assertTrue(p.match("1985-04-12T23:20:50.52Z"));
		assertTrue(p.match("2009-04-16T12:07:25.123+01:00"));
	}

	@Test
	public void testNameOf() throws REException {
		// js_10021.json
		Pattern p = Pattern.createFromRegexp("^\\\\w*$");

		assertTrue(p.match("Hulk"));
		assertTrue(p.match("maya2016"));
	}

	@Test
	public void testAddressToMongoDB() throws REException {
		// js_10021.json
		Pattern p = Pattern.createFromRegexp("^mongodb://[\\\\w/@:.]*$");

		assertTrue(p.match("mongodb://localhost:27017"));
	}

	@Test
	public void testAddressToSentryEtc() throws REException {
		// js_10021.json
		Pattern p = Pattern.createFromRegexp("^http[\\\\w/@:.]*$");

		assertTrue(p.match(
				"https://5b872b280de742919b115bdc8da076a5:8d278266fe764361b8fa6024af004a9c@logs.mindbender.com/2"));
		assertTrue(p.match("http://192.168.99.101"));
	}

	@Test
	public void testContainerID() throws REException {
		// js_10021.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w.]*$");

		assertTrue(p.match("avalon.container"));
	}

	@Test
	public void testFilter() throws REException {
		// js_10036.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w\\\\-@]+\\\\.[\\\\w\\\\-\\\\.#\\\\*\\\\[\\\\]\\\\?]+$");

		assertTrue(p.match("ab@.ab-c#?*[]")); // I made this string up.
	}

	@Test
	public void testFilter2() throws REException {
		// js_10036.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w\\\\-\\\\*\\\\[\\\\]\\\\?]+@[\\\\w\\\\-]+\\\\.[\\\\w\\\\-\\\\*\\\\[\\\\]\\\\?]+$");

		assertTrue(p.match("ab*?@abc.com?")); // I made this string up.
	}

	@Test
	public void testExpires() throws REException {
		// js_1004.json
		Pattern p = Pattern.createFromRegexp(
				"^(\\\\d{4})(-)?(\\\\d\\\\d)(-)?(\\\\d\\\\d)(T)?(\\\\d\\\\d)(:)?(\\\\d\\\\d)(:)?(\\\\d\\\\d)(\\\\.\\\\d+)?(Z|([+-])(\\\\d\\\\d)(:)?(\\\\d\\\\d))?");

		assertTrue(p.match("1985-04-12T23:20:50.52Z"));
		assertTrue(p.match("2009-04-16T12:07:25.123+01:00"));
	}

	@Test
	public void testResourceID() throws REException {
		// js_10040.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w\\\\-]+(\\\\.[\\\\w\\\\-^#]+)+$");

		assertTrue(p.match("foo-.bar#.baz^")); // I made this up.
	}

	@Test
	public void testResource() throws REException {
		// js_10040.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w\\\\-]+\\\\..+$");

		assertTrue(p.match("foo-.bar#.baz^")); // I made this up.
	}

	@Test
	public void testProto() throws REException {
		// js_10041.json
		Pattern p = Pattern.createFromRegexp("^tcp$|^udp$|^\\\\\\\\*$");

		assertTrue(p.match("tcp"));
		assertTrue(p.match("udp"));
		assertTrue(p.match("\\\\"));
		assertFalse(p.match("tcudp"));
	}

	@Test
	public void testEndpoint() throws REException {
		// js_10041.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w\\\\-\\\\*\\\\[\\\\]\\\\?]+$");

		assertTrue(p.match("-*[]?"));
	}

	@Test
	public void testPathType() throws REException {
		// js_10376.json
		Pattern p = Pattern.createFromRegexp("^(/?((\\\\.{2})|([a-z0-9\\\\-]*))($|/))*$");

		assertTrue(p.match("/foo/bar/../baz/"));
	}

	@Test
	public void testRegistrationType() throws REException {
		// js_13403.json
		Pattern p = Pattern.createFromRegexp("^$|^List$");

		assertTrue(p.match(""));
		assertTrue(p.match("List"));
		assertFalse(p.match("Listing"));
	}

	@Test
	public void testClientSideExtension() throws REException {
		// js_13403.json
		Pattern p = Pattern.createFromRegexp("^ClientSideExtension\\\\\\\\..*$");

		assertTrue(p.match("ClientSideExtension\\1"));
		assertTrue(p.match("ClientSideExtension\\\\abc"));
	}

	@Test
	public void testPublicationIdentifier() throws REException {
		// js_116.jso
		Pattern p = Pattern.createFromRegexp("^PMID:[0-9]+$|^doi:10\\\\.[0-9]{4}[\\\\d\\\\S:\\\\.]+");

		assertTrue(p.match("PMID:12345678"));
		assertTrue(p.match("doi:10.1234/abc123"));
	}

	@Test
	public void testObjectName() throws REException {
		// js_10042.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w\\\\-\\\\.]+$");

		assertTrue(p.match("a-b-c...-d"));
	}

	@Test
	public void testImage() throws REException {
		// js_10042.json
		Pattern p = Pattern.createFromRegexp("^native:|docker://.+|http://.+|file://.+$");

		assertTrue(p.match("native:"));
		assertTrue(p.match("docker://foo"));
		assertTrue(p.match("http://bar"));
		assertTrue(p.match("file://baz"));
	}

	@Test
	public void testArray() throws REException {
		// js_10042.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w\\\\-]+$");

		assertTrue(p.match("a-b-c"));
	}

	@Test
	public void testExpression() throws REException {
		// js_10043.json
		Pattern p = Pattern.createFromRegexp("^[\\\\w\\\\s\\\\-\\\\*/,]+$");

		assertTrue(p.match("a1 -*/,"));
	}

	@Test
	public void testPathNotNullChar() throws REException {
		// pp_27348.json from 07/2020
		// "pattern": "^[^\\0]+$"
		Pattern p = Pattern.createFromRegexp("^[^\\0]+$"); // anything but the null-chars

		assertTrue(p.match("000"));
	}

	@Test
	public void testServiceBenefits() throws REException {
		// pp_13386.json from 07/2020
		// "pattern": "^(?:\\S+\\s+){0,9}\\S+$"
		Pattern p = Pattern.createFromRegexp("^(?:\\\\S+\\\\s+){0,9}\\\\S+$");

		assertTrue(p.match("Integrated with CDN"));
	}

	@Test
	public void testBureauCode() throws REException {
		// pp_12123.json from 07/2020
		Pattern p = Pattern.createFromRegexp("[0-9]{3}:[0-9]{2}");

		assertTrue(p.match("123:45"));
	}

	@Test
	public void testDescribeByType() throws REException {
		// pp_12123.json from 07/2020
		// Actually, there is a typo in this regexp, the first '/' ought to be escaped.
		Pattern p = Pattern.createFromRegexp("^[-\\\\w]+/[-\\\\w]+(\\\\.[-\\\\w]+)*([+][-\\\\w]+)?$");

		assertTrue(p.match("application/pdf"));
	}

	@Test
	public void testUnicodeStringRanges() throws REException {
		// pp_2121.json from 07/2020
		Pattern p = Pattern.createFromRegexp("^[\u0151-\u0171\u0142-\u0161]*$");

		assertFalse(p.match("abc"));
	}

	@Test
	public void testProof() throws REException {
		// pp_34846.json from 07/2020
		Pattern p = Pattern.createFromRegexp("^[\\x00-\\xFF]{32}$");

		assertTrue(p.match("6723B06A22F1A2F6009F4C6B12345678"));
	}

	@Test
	public void testBezeichner() throws REException {
		// pp_13158.json from 07/2020
		Pattern p = Pattern.createFromRegexp("^[a-z0-9_-]{2,}$");

		assertTrue(p.match("de-mv-rostock-polizeidienststellen"));
	}

	@Test
	public void testServiceFeatures() throws REException {
		// pp_13392.json from 07/2020
		Pattern p = Pattern.createFromRegexp("^(?:\\\\S+\\\\s+){0,9}\\\\S+$");

		assertTrue(p.match("Online diary allows users to share immediate up-to-date information"));
	}

	@Test(expected = REException.class)
	public void testSortString() throws REException {
		// pp_56129.json from 07/2020

		// Syntax error in prefix "(?i)"
		Pattern.createFromRegexp("(?i)^[\\\\w\\\\s]+(?:,\\\\s(DESC|DESCENDING|ASC|ASCENDING|VERSION|VER)*)*$");
	}

	@Test
	public void testBCP47Tag() throws REException {
		// pp_38338.json from 07/2020

		// Error, because subpattern name "privateUse" is not unique.
		Pattern.createFromRegexp("^((?<grandfathered>(en-GB-oed|i-ami|i-bnn|i-default|i-enochian|i-hak|i-klingon"
				+ "|i-lux|i-mingo|i-navajo|i-pwn|i-tao|i-tay|i-tsu|sgn-BE-FR|sgn-BE-NL|sgn-CH-DE)|"
				+ "(art-lojban|cel-gaulish|no-bok|no-nyn|zh-guoyu|zh-hakka|zh-min|zh-min-nan|zh-xiang))"
				+ "|((?<language>([A-Za-z]{2,3}(-(?<extlang>[A-Za-z]{3}(-[A-Za-z]{3}){0,2}))?)"
				+ "|[A-Za-z]{4}|[A-Za-z]{5,8})(-(?<script>[A-Za-z]{4}))?"
				+ "(-(?<region>[A-Za-z]{2}|[0-9]{3}))?(-(?<variant>[A-Za-z0-9]{5,8}|[0-9][A-Za-z0-9]{3}))*"
				+ "(-(?<extension>[0-9A-WY-Za-wy-z](-[A-Za-z0-9]{2,8})+))*"
				+ "(-(?<privateUse>x(-[A-Za-z0-9]{1,8})+))?)|(?<privateUse>x(-[A-Za-z0-9]{1,8})+))$");
	}

	// Positive lookaheads not yet supported.
	// This is rather rare (ca. 100 real-world instances).
	@Test(expected = REException.class)
	public void testAdminPassword() throws REException {
		// pp_1623.json from 07/2020
		Pattern p = Pattern.createFromRegexp(
				"^(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[!@#$%\\^&\\*\\(\\)])[a-zA-Z\\d!@#$%\\^&\\*\\(\\)]{12,123}$");

		assertTrue(p.match("someSecret!Password"));
		assertFalse(p.match("somesecret!password")); // Must contain at least one capitalized letter.
	}

	// Negative lookahead not yet supported.
	// There are about 260 real-world instances in our data from July 2020.
	@Test(expected = REException.class)
	public void testHost() throws REException {
		// pp_16745.json from 07/2020
		Pattern p = Pattern.createFromRegexp("^((?!\\://).)*$");

		assertTrue(p.match("www.google.com"));
		assertFalse(p.match("http://www.google.com"));
	}

	// Negative lookbehind not yet supported.
	// There are about 10 real-world instances in our data from July 2020.
	@Test(expected = REException.class)
	public void testRequestCriteria() throws REException {
		// pp_56128.json from 07/2020
		Pattern p = Pattern.createFromRegexp("(?<!\\\\\\\\)=");

		assertTrue(p.match("P=NP"));
		assertFalse(p.match("P\\=NP"));
	}

	@Test
	public void testAmazonStandardId() throws REException {
		// pp_21828.json from 07/2020
		Pattern p = Pattern.createFromRegexp("/^[A-Z0-9]{10}$/");

		assertTrue(p.match("B000J8VLEC"));
		assertFalse(p.match("0123456789A"));
	}

	@Test
	public void testAccessControlList() throws REException {
		// pp_45260.json from 07/2020
		Pattern p = Pattern.createFromRegexp("/(\\\\*|GET|POST|PUT|PATCH|DELETE)(:/.*)?/g");

		assertTrue(p.match("PUT:/foo.com"));
	}
	
	@Test
	public void testLongString() throws REException {
		// pp_6212.json
		
		Pattern p = Pattern.createFromRegexp("^[0-9a-zA-Z_-]{1,255}$");

		assertTrue(p.match("dev"));
		assertTrue(p.match("123"));
		assertFalse(p.match("stables and horses"));
		
		// String longer than 255 characters.
		assertFalse(p.match("ZMMj7ToSrCveOQWVGh5IOk7Uf2FVvWOP877v1QWKbZqorW2RV0szac1MNSu8B4WpYzdn4rMnk5RVeQPorHQPGUa1lc6ULMKWNT9T2kbzWbiaFkYmzjkVz2cteQlJYafD3plBA8rwBmkkBgcYdxWe3MUkjFV234oee2URXxpoZWef0JtR7cbH5oXwHBjXK7dHY2vmgLH6DpiTEezWVibHR4q3XdI1j1WioattWbTJ6DboL92K892qqYAL3jeUHqmJkwQH"));
	}

}
