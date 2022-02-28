package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class RecursionTest {

	 private GenAndValidate genVal = 
	    		new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/recursion/");
	
    @Test
    public void test0() throws Exception {
        String witness = genVal.genWitness("0.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("0.json",witness));
    }
}
