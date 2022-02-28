package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GenNullTest {

    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/oneTypeSchemas/null/");


    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(0,genVal.validateWitness("1.json",witness));
    }

}
