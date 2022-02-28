package de.uni_passau.sds.patterns;

/**
 * Dedicated to tests handling word boundary problems, caused by the fact that
 * Brics automata cannot match '^' and '$'.
 *
 */
public class StringBoundaryTest {

	// TODO
	// \\b
	// =>
	// "(^\\w|\\w$|\\W\\w|\\w\\W)"

	// p_15620.json: "pattern": "^(?!$|^[A-Fa-f0-9]{64})[ -~]{1,255}$"

	// pp_21833.json: "pattern": "/^[0-9]{4}-[0-9]{2}-[0-9]{2}$/",

	// pp_24220.json: "pattern": "/[1,-1]{1}/"
}
