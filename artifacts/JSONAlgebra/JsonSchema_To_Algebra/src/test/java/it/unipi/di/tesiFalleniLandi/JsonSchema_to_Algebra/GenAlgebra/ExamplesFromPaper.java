package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import de.uni_passau.sds.patterns.REException;

public class ExamplesFromPaper {

	private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/demopaper/");

    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(0,genVal.validateWitness("1.json",witness));
    }
    
    @Test
    // TODO Index 4 out of bounds for length 4
    public void test1Another() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1Another.json");
        assertEquals(0,genVal.validateWitness("1Another.json",witness));
    }
    
    @Test
    public void test2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2.json");
        assertEquals(0,genVal.validateWitness("2.json",witness));
    }
    
    @Test
    // Generate a witness different from the one in test2().
    public void test2Another() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2Another.json");
        assertEquals(0,genVal.validateWitness("2Another.json",witness));
    }

    @Test
    // Generate a witness different from the one in test2() and test2Another().
    public void test2Another2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2Another2.json");
        assertEquals(0,genVal.validateWitness("2Another2.json",witness));
        System.out.println(witness);
    }
    
    @Test
    // Generate a witness different from the previous ones, based on test2().
    // TODO java.lang.Exception: No type construct in a typed group
    public void test2Another3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2Another3.json");
        assertEquals(0,genVal.validateWitness("2Another3.json",witness));
        System.out.println(witness);
    }
    
    @Test
    // TODO Index 4 out of bounds for length 4
    public void testCompare1and2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("containment_1_and_2.json");
        assertEquals(0,genVal.validateWitness("containment_1_and_2.json",witness));
        System.out.println(witness);
    }
    
    @Test
    public void testCompare2and1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("containment_2_and_1.json");
        assertEquals(0,genVal.validateWitness("containment_2_and_1.json",witness));
        System.out.println(witness);
    }
}
