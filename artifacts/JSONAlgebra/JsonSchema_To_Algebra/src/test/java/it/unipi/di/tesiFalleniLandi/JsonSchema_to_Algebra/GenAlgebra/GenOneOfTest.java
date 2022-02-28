package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GenOneOfTest {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";

    private GenAndValidate genAlgebraicVal =
            new GenAndValidate(System.getProperty("user.dir")+"/testOneOfFiles/",true);

    @Test
    public void testfail() throws WitnessException, IOException, REException {
        String filename = "linear/fail.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        assertEquals(0,genAlgebraicVal.validateWitness(filename,witness));
    }

    @Test
    public void fail105() throws WitnessException, IOException, REException {
        String filename = "linear/fail105.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        assertEquals(noWitness,witness);
    }

    @Test
    public void testencoding() throws WitnessException, IOException, REException {
        String filename = "linear/encoding.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        assertEquals(0,genAlgebraicVal.validateWitness(filename,witness));
    }

    @Test
    public void equivalencemofmof() throws WitnessException, IOException, REException {
        String filename = "linear/equivalencemofmof.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        assertEquals(noWitness,witness);
    }

    @Test
    public void encoding34() throws WitnessException, IOException, REException {
        String filename = "quadraticNotPropsItems/encoding34.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        assertEquals(noWitness,witness);
    }

    @Test
    public void not7PropsEncodedTakesTooMuchTime() throws WitnessException, IOException, REException {
        String filename = "quadraticNotPropsItems/not7PropsEncodedTakesTooMuchTime.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        assertEquals(noWitness,witness);
    }



}
