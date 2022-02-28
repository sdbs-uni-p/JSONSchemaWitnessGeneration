package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GenBinaryTest {

    private GenAndValidate genJSchemaVal =
            new GenAndValidate(System.getProperty("user.dir")+"/testDeepTreeFiles/Binary/",false);

    private GenAndValidate genAlgebraicVal =
            new GenAndValidate(System.getProperty("user.dir")+"/testDeepTreeFiles/Binary/",true);


    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genJSchemaVal.genWitness("binary.json");
        assertEquals(0,genJSchemaVal.validateWitness("binary.json",witness));
    }

    @Test
    public void test2() throws WitnessException, IOException, REException {
        String witness = genAlgebraicVal.genWitness("binaryshort.algebra");
        assertEquals(0,genAlgebraicVal.validateWitness("binaryshort.algebra",witness));
    }

    @Test
    public void testbinaryalgebra() throws WitnessException, IOException, REException {
        String witness = genAlgebraicVal.genWitness("binarycompleteat4.algebra");
        assertEquals(0,genAlgebraicVal.validateWitness("binarycompleteat4.algebra",witness));
    }


    @Test
    public void testbinaryalgebra01() throws WitnessException, IOException, REException {
        /* the produced witness
        {"0":{"0":{"0":{"0":{},"1":{}},"1":{"0":{},"1":{}}},"1":{"0":{"0":{},"1":{}},"1":{"0":{},"1":{}}}},
        "1":{"0":{"0":{"0":{},"1":{}},"1":{"0":{},"1":{}}},"1":{"0":{"0":{},"1":{}},"1":{"0":{},"1":{}}}}}
          is ok, but the validator is not able to validate it
         */
        String witness = genAlgebraicVal.genWitness("binary01.algebra");
        assertEquals(0,genAlgebraicVal.validateWitness("binarycompleteat4.algebra",witness));
    }

}
