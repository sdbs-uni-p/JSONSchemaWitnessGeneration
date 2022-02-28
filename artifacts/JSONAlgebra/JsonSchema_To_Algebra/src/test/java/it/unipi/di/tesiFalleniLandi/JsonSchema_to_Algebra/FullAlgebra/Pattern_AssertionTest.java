package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

public class Pattern_AssertionTest {


    //Out of memory !!
    @Test
    public void testCreatePattern1() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^$|(^(?:\\S+\\s+){0,99}\\S+$)"));
        //Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^$|(^(?:\\S+\\s+){0,49}\\S+$)"));
        //Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^$|(^(?:\\S+\\s+){0,199}\\S+$)"));
        //pattern.toWitnessAlgebra();
    }

    @Test
    public void testCreatePattern2() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^(?:\\S+\\s+){0,99}\\S+$"));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern3() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("\"^urn\\\\:[ \\\\S]{1,64}\\\\:device\\\\:[ \\\\S]{1,64}\\\\:[0-9]{1,8}$\""));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern4() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$"));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern5() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^[\\\\S]{1,128}$"));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern6() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^image/[a-zA-Z0-9\\\\+\\\\.]{1,12}$"));
        pattern.toWitnessAlgebra(null,null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern7() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("\"^$|^\\\\d{1,15}(?:\\\\.\\\\d{1,5})?$\""));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern8() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^/(([a-zA-Z0-9._~!$&'()*+,;=:@-]|%[0-9a-fA-F]{2})+(/([a-zA-Z0-9._~!$&'()*+,;=:@-]|%[0-9a-fA-F]{2})*)*)?$"));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern9() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^[a-f0-9]{8}-[a-f0-9]{4}-[1-5][a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}$"));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern10() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("\"^[A-Fa-f\\\\d]{24}$\""));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern11() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^[0-9a-zA-Z_-]{1,255}$"));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }

    @Test
    public void testCreatePattern12() throws REException {
        Pattern_Assertion pattern = new Pattern_Assertion(ComplexPattern.createFromRegexp("^(0|([1-9]\\\\d*))\\\\.(0|([1-9]\\\\d*))\\\\.(0|([1-9]\\\\d*))$"));
        pattern.toWitnessAlgebra(null, null, null);
        pattern.not().toWitnessAlgebra(null, null, null);
    }
}