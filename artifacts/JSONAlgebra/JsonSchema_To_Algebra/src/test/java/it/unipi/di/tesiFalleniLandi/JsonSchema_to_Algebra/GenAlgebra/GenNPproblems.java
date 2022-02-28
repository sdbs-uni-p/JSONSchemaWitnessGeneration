package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import de.uni_passau.sds.patterns.REException;

public class GenNPproblems {

	 private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/NPproblems/");

	    @Test
	    // TODO java.lang.Exception: No type construct in a typed group
	    public void testSetCover() throws WitnessException, IOException, REException {
	        String witness = genVal.genWitness("setcover.json");
	        assertEquals(0,genVal.validateWitness("setcover.json",witness));
	    }
	    
	    @Test
	    // TODO the return value of "GenObject$GPattReq.getOrpList()" is null
	    public void testHittingSet() throws WitnessException, IOException, REException {
	        String witness = genVal.genWitness("hittingset.json");
	        assertEquals(0,genVal.validateWitness("hittingset.json",witness));
	    }
}
