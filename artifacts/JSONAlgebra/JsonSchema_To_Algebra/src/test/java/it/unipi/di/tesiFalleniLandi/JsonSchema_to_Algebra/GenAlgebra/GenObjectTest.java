package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GenObjectTest {

    private GenAndValidate genJSchemaVal =
            new GenAndValidate(System.getProperty("user.dir")+"/testObjectGen/",false);

    private GenAndValidate genAlgebraicVal =
            new GenAndValidate(System.getProperty("user.dir")+"/testObjectGen/",true);

    @Test
    public void bigObjGen() throws WitnessException, IOException, REException {
        String filename = "bigObjGen.json";
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }

    @Test
    public void fourFields() throws WitnessException, IOException, REException {
        String filename = "fourFields.json";
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }

    @Test
    public void patternTwo() throws WitnessException, IOException, REException {
        String filename = "patternTwo.json";
        String witness = genJSchemaVal.genWitness(filename);
        //this one is empty!!!! Hence the number 3 below
        assertEquals(3,genJSchemaVal.validateWitness(filename,witness));
    }

    @Test
    public void patternOne() throws WitnessException, IOException, REException {
        String filename = "patternOne.json";
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }

    @Test
    public void threePatterns() throws WitnessException, IOException, REException {
        String filename = "threePatterns.json";
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }

    @Test
    public void test_generateNames_emptyString() throws REException {
        ComplexPattern pattern1 = ComplexPattern.createFromRegexp("^.{0,0}$");
        ComplexPattern pattern2 = ComplexPattern.createFromRegexp("^$");
        List<String> names = GenObject.generateNames(pattern1, 1.0);
        assertEquals( "\"^.{0,0}$\", maxSize 1","", names.get(0));
        names = GenObject.generateNames(pattern1, 2.0);
        assertEquals( "\"^.{0,0}$\", maxSize 2",0, names.size());
        names = GenObject.generateNames(pattern2, 1.0);
        assertEquals( "\"^$\", maxSize 1","", names.get(0));
        names = GenObject.generateNames(pattern2, 2.0);
        assertEquals( "\"^$\", maxSize 2",0, names.size());
    }

    @Test
    public void test_generateNames_letterDigitFirst() throws REException {
        ComplexPattern pattern = ComplexPattern.createFromRegexp("^.{1,1}$");
        List<String> names = GenObject.generateNames(pattern, 62.0);
        for (String name : names) {
            assertTrue("First 62 names should consist only letters or digits.", name.matches("[A-Za-z0-9]+"));
        }
        names = GenObject.generateNames(pattern, 63.0);
        assertFalse("63rd name should not be letter or digit.", names.get(62).matches("[A-Za-z0-9]+"));
    }

    @Test
    public void test_generateNames_noLetterDigit() throws REException {
        ComplexPattern pattern = ComplexPattern.createFromRegexp("^[^A-Za-z0-9]$");
        assertNotEquals("Generate names for pattern without letter/digit.", 0, GenObject.generateNames(pattern, 10.0).size());
    }

    @Test
    public void test_generateNames_noLetter() throws REException {
        ComplexPattern pattern = ComplexPattern.createFromRegexp("^[^A-Za-z]$");
        List<String> names = GenObject.generateNames(pattern, 11.0);
        for (int i = 0; i < 10; i++) {
            assertEquals("Generate digit " + i, i, Integer.parseInt(names.get(i)));
        }
        assertFalse("Generate first non-digit", names.get(10).matches("[A-Za-z0-9]"));
    }

    @Test
    public void test_generateNames_noLetter_withExclusions() throws REException {
        ComplexPattern pattern = ComplexPattern.createFromRegexp("^[^A-Za-z]$");
        List<String> names = GenObject.generateNames(pattern, 3.0, Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8"));
        assertEquals("Generate digit 9", "9", names.get(0));
        assertFalse("Generate non-digit", names.get(1).matches("[A-Za-z0-9]"));
        assertFalse("Generate non-digit", names.get(2).matches("[A-Za-z0-9]"));
    }

}
