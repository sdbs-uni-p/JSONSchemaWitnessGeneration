package de.uni_passau.sds.patterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import dk.brics.automaton.SpecialOperations;

public class Pattern {

	/**
	 * The internal Bricks automaton.
	 * Do not access directly, but via {@link #getAutomaton()} method.
	 */
	private Automaton automaton = null;

	/**
	 * Lazily instantiates automaton.
	 */
	private Automaton getAutomaton() {
		
		if (this.automaton == null) {
			// Brics flags: '#' is the empty language. Disable this.
			RegExp regExp = new RegExp(this.originalBricksPattern, RegExp.ALL & ~RegExp.EMPTY);
			this.automaton = regExp.toAutomaton();
		}
		
		return automaton;		
	}
	
	/**
	 * Records the underlying regular expression in Bricks (!) syntax.
	 **/
	private String originalBricksPattern = null;

	/**
	 * A string representation of the underlying pattern.
	 */
	private String printablePattern;

	/**
	 * @param patternName String constant that is not a regular expression
	 */
	public static Pattern createFromName(String patternName) {
		return new Pattern(BasicAutomata.makeString(patternName),patternName);
	}

	/**
	 * @param ecmaRegex String in ECMAScript syntax (not anchored by default)
	 * @exception IllegalArgumentException if regex is invalid
	 * @exception REException              if ecmaRegex contains syntax error
	 */
	public static Pattern createFromRegexp(String ecmaRegex) throws REException {
		// Pre-processing of regular expressions with modifiers.
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^/(.*)/[gmiyu]?$");
		Matcher matcher = pattern.matcher(ecmaRegex);
		if (matcher.matches()) {
			ecmaRegex = matcher.group(1);
		}
		String brics = PatternConverter.rewrite(ecmaRegex);
		return new Pattern(brics);
	}

	private Pattern(Automaton automaton, String printablePattern) {
		this.printablePattern = printablePattern;
		this.automaton = automaton;
	}

	private Pattern(Automaton automaton) {
		this.automaton = automaton;
	}

	/**
	 * Assumes that regex is already in the Bricks syntax.
	 * Automaton will be constructed lazily.
	 */
	protected Pattern(String bricksRegex) {
	
		// Will construct automaton lazily.
		this.printablePattern = bricksRegex;
		this.originalBricksPattern = bricksRegex;		

		//System.out.println(this.originalBricksPattern);
	}

	public boolean isEmpty() {
		return BasicOperations.isEmpty(this.getAutomaton());
	}

	public boolean match(String str) {
		// The less performant way to match a string.
		// return BasicOperations.run(this.automaton, str);

		// For full performance, we use the RunAutomaton class.
		RunAutomaton ra = new RunAutomaton(this.getAutomaton());
		return ra.run(str);
	}

	public Pattern intersect(Pattern p) {
		Automaton a = BasicOperations.intersection(this.getAutomaton(), p.getAutomaton());
		Pattern pi = new Pattern(a);

		pi.printablePattern = "allOf[" + this + ", " + p + "]";
		return pi;
	}

	public Pattern minus(Pattern p) {
		Pattern m = new Pattern(BasicOperations.minus(this.getAutomaton(), p.getAutomaton()));

		m.printablePattern = "allOf[" + this + ", not(" + p + ")]";
		return m;
	}

	public Pattern union(Pattern p) {
		Pattern u = new Pattern(BasicOperations.union(this.getAutomaton(), p.getAutomaton()));

		u.printablePattern = "anyOf[" + this + ", " + p + "]";
		return u;
	}

	public Pattern complement() {
		Pattern complement = new Pattern(BasicOperations.complement(this.getAutomaton()));
		complement.printablePattern = "not(" + this + ")";

		return complement;
	}

	/**
	 * Returns true if this pattern declares a language that is a subset of the
	 * language declared by pattern p.
	 */
	public boolean isSubsetOf(Pattern p) {
		return BasicOperations.subsetOf(this.getAutomaton(), p.getAutomaton());
	}

	public boolean isEquivalent(Pattern p) {
		return this.getAutomaton().equals(p.getAutomaton());
	}

	/**
	 * If domain(a) is finite, return |a|. Otherwise, return null.
	 */
	public Integer domainSize() {
		Set<String> domain = SpecialOperations.getFiniteStrings(this.getAutomaton(), 1000);
		return domain == null ? null : domain.size();
	}

	/**
	 * @param n number of witness strings
	 * @return true iff the pattern has more than n witness strings
	 */
	public boolean hasMoreThanNStrings(int n) {
		Integer domainSize = domainSize();
		if (domainSize == null) // Language is infinite.
			return true;

		// Language is finite.
		return domainSize > n;
	}
	
	/**
	 * Returns the set of accepted strings, assuming this automaton has a finite
	 * language. If the language is not finite, this returns one word that matches.
	 * If the language is empty, this returns null.
	 * 
	 * Caution: If language is finite but large, this may not terminate in time.
	 */
	@Deprecated
	public Collection<String> generateWords() {	
		Set<String> words = SpecialOperations.getFiniteStrings(this.getAutomaton());

		if (words != null)
			return words;

		if (!this.getAutomaton().isEmpty())
			return Collections.singleton(BasicOperations.getShortestExample(this.getAutomaton(), true));

		return null;
	}
	
	/**
	 * Returns an accepted string.
	 * If the language is empty, this returns null.
	 * @throws REException
	 */
	public String generateNewWord() throws REException {
		return generateNewWord(new ArrayList<String>());
	}
	
	/**
	 * Returns an accepted string that is not one of the old witnesses provided.
	 * If the language is empty, this returns null.
	 * @throws REException
	 */
	public String generateNewWord(Collection<String> oldWitnesses) throws REException {
		
		if (oldWitnesses.isEmpty())
			return BasicOperations.getShortestExample(this.getAutomaton(), true);
		
		// Turn old witnesses into regexp.
		String joined = "^(" + String.join("|", oldWitnesses) + ")$";
		
		Pattern joinedPattern = Pattern.createFromRegexp(joined);
		Pattern newPattern = this.minus(joinedPattern);
		
		return BasicOperations.getShortestExample(newPattern.getAutomaton(), true);
	}

	public Pattern clone() {
		Pattern clone = new Pattern(this.getAutomaton().clone());
		clone.originalBricksPattern = this.originalBricksPattern;
		clone.printablePattern = this.printablePattern;
		return clone;
	}

	/**
	 * Given {A1, .., An}, returns pattern for not(A1|...|An)
	 */
	public static Pattern listComplement(Collection<Pattern> patterns) {
		Pattern u = null;

		for (Pattern p : patterns) {
			if (u == null)
				u = p;
			else
				u = u.union(p);
		}

		return u.complement();
	}

	/**
	 * Returns true iff the language declared by the two de.uni_passau.sds.patterns overlaps, i.e.
	 * their intersection is not empty.
	 */
	public static boolean overlaps(Pattern left, Pattern right) {
		return !left.getAutomaton().overlap(right.getAutomaton()).isEmpty();
	}

	/**
	 * Returns true iff there are two de.uni_passau.sds.patterns that overlap.
	 */
	public static boolean overlaps(Collection<Pattern> patterns) {
		Pattern[] array = patterns.toArray(new Pattern[0]);

		for (int i = 0; i < array.length; i++) {
			Pattern first = array[i];

			if (i == array.length - 1)
				break;

			for (int j = i + 1; j < array.length; j++) {
				Pattern second = array[j];

				if (Pattern.overlaps(first, second))
					return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {

		if (this.originalBricksPattern != null)
			return "base(" + this.originalBricksPattern + ")";

		// assert (this.printablePattern != null) : "should not have happened";
		return this.printablePattern;
	}

	/**
	 * For debugging purposes.
	 * 
	 * @return Serialization of the automaton states and transitions.
	 */
	public String toAutomatonString() {
		return this.getAutomaton().toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Pattern pattern = (Pattern) o;
		return this.isEquivalent(pattern);
	}

}
