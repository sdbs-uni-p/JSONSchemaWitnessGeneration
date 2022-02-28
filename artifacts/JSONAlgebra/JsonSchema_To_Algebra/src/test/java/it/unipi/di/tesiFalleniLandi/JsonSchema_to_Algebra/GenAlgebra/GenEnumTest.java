/**
 * 
 */
package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import de.uni_passau.sds.patterns.REException;


public class GenEnumTest {

	   private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/oneTypeSchemas/enum/");


	    @Test
	    public void test1() throws WitnessException, IOException, REException {
	        String witness = genVal.genWitness("1.json");
	        assertEquals(0,genVal.validateWitness("1.json",witness));
	    }
	    
	    @Test
	    // TODO java.lang.Exception: No type construct in a typed group
	    // Expected: {  "fruit": "banana"}
	    public void test2() throws WitnessException, IOException, REException {
	        String witness = genVal.genWitness("2.json");
	        assertEquals(0,genVal.validateWitness("2.json",witness));
	    }
	    
	    @Test
	    // TODO: Yields empty witness, but "kiwi" or "orange" would be a witness.
	    public void test3() throws WitnessException, IOException, REException {
	        String witness = genVal.genWitness("3.json");
	        assertEquals(0,genVal.validateWitness("3.json",witness));
	    }
	    
	    @Test
	    public void test4() throws WitnessException, IOException, REException {
	        String witness = genVal.genWitness("4.json");
	        assertEquals(0,genVal.validateWitness("4.json",witness));
	    }
	    
	    @Test
	    // Another witness than test4
	    public void test5() throws WitnessException, IOException, REException {
	        String witness = genVal.genWitness("5.json");
	        assertEquals(0,genVal.validateWitness("5.json",witness));
	    }

	    @Test
	    // Another witness than test4
	    public void test6() throws WitnessException, IOException, REException {
	        String witness = genVal.genWitness("6.json");
	        assertEquals(0,genVal.validateWitness("6.json",witness));
	    }
}
