package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FailedTests {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";

    private GenAndValidate genAlgebraicVal =
            new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/failedTests/",true);


    private GenAndValidate genVal =
            new GenAndValidate(System.getProperty("user.dir")+"//witnessGenTestfiles/failedTests/");




    // Algebra
    @Test
    public void test1Algebra() throws WitnessException, IOException, REException {
        String witness = genAlgebraicVal.genWitness("1.algebra");
        assertEquals(noWitness,witness);
    }


    @Test
    public void test1BisAlgebra() throws WitnessException, IOException, REException {
        String witness = genAlgebraicVal.genWitness("1bis.algebra");
        assertEquals(noWitness,witness);
    }


    @Test
    public void test1TerAlgebra() throws WitnessException, IOException, REException {
        String witness = genAlgebraicVal.genWitness("1ter.algebra");
        assertEquals(noWitness,witness);
    }


    // Works: previous algebra ---> toJSON ---> generate
    @Test
    public void test1JSON() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(noWitness,witness);
    }
}
