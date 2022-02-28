package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * The tests in this class check whether the **first** generated Witness is valid or not.
 */
public class GenWitnessTest {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private final String PATH = System.getProperty("user.dir") +
            "/witnessGenTestfiles/genWitness/";

    private GenAndValidate genVal = new GenAndValidate(PATH);

    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(noWitness, witness);
    }

    @Test
    public void test2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2.json");
        assertEquals(0, genVal.validateWitness("2.json", witness));
    }

    //Test generate Witness for rewritten and normalized schema
    @Test
    public void test3()throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("3.json");
//        assertEquals(0, genVal.validateWitness("3.json", witness));
        assertEquals(noWitness, witness);

    }

    @Test
    public void test4() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("4.json");
        assertEquals(0, genVal.validateWitness("4.json", witness));
    }
}
