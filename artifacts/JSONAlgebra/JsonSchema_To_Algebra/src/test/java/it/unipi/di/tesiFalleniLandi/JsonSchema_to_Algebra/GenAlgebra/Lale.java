package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Lale {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir") +
            "/witnessGenTestfiles/LALE/");

    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("weka_j48.json");
        assertEquals(0, genVal.validateWitness("weka_j48.json",witness));
    }


}
