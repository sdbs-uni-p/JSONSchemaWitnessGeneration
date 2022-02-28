package de.uni_passau.sds.patterns;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;

import dk.brics.automaton.RegExp;
import org.junit.Test;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RunAutomaton;
// import jdk.nashorn.internal.runtime.regexp.RegExp;

/**
 * Functionality tests directly using the Bricks automaton library.
 */
public class AutomatonTest {

	@Test
	public void testNull() {
		Automaton a = (new RegExp("[^\0]")).toAutomaton();

		RunAutomaton ra = new RunAutomaton(a);
		assertTrue(ra.run("0"));
	}

	@Test
	public void testHex() {
		Automaton a = (new RegExp("[\\x00-\\xFF]")).toAutomaton();

		// System.out.println(a.toString()); // TODO

		RunAutomaton ra = new RunAutomaton(a);
		assertTrue(ra.run("0"));
		// assertTrue(ra.run("!"));
	}

	@Test
	public void testPattern() {
		// From schema pp_48427.json
		String pattern = "Normal|Fighting|Flying|Poison|Ground|Rock|Bug|Ghost|Steel|Fire|Water|Grass|Electric|Psychic|Ice|Dragon|Dark|Fairy";

		Automaton a = (new RegExp(pattern)).toAutomaton();
		RunAutomaton ra = new RunAutomaton(a);
		assertTrue(ra.run("Ice"));
	}
	
	@Test
	//TODO - too expensive!
	@Ignore
	public void testLongPattern() {
		String pattern = "(@Red@|@Blue@|@Yellow@|@Gold@|@Silver@|@Crystal@|@Ruby@|@Sapphire@|@Emerald@|@FireRed@|@LeafGreen@|@Diamond@|@Pearl@|@Platinum@|@HeartGold@|@SoulSilver@|@Black@|@White@|@Black 2@|@White 2@|@X@|@Y@|@Omega Ruby@|@Alpha Sapphire@|@Sun@|@Moon@|@Ultra Sun@|@Ultra Moon@|@Let's Go Pikachu@|@Let's Go Eevee@)";
		
		Automaton a = (new RegExp(pattern)).toAutomaton();
		RunAutomaton ra = new RunAutomaton(a);
		assertTrue(ra.run("simplyRed"));
	}
}
