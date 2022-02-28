package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class InconsistentWitnessGen {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir") +
            "/witnessGenTestfiles/inconsistentWitnessGen/");

    //TODO: Pushing the generate Witness Button returns different values!
    @Test
    public void test1() throws IOException, REException, WitnessException {
        String witness = genVal.genWitness("1.json");
        String flag = witness;

        for(int i = 0; i <= 50; i++) {
            witness = genVal.genWitness("1.json");
            assertEquals(flag, witness);
        }
    }
}
