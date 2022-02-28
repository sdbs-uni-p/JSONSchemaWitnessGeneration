package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GenChooseReqTest {


        private GenAndValidate genJSchemaVal =
                new GenAndValidate(System.getProperty("user.dir")+"/testChooseReq/",false);

        private GenAndValidate genAlgebraicVal =
                new GenAndValidate(System.getProperty("user.dir")+"/testChooseReq/",true);


    @Test
    public void choose() throws WitnessException, IOException, REException {
        String filename = "choose.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        //assertEquals(0,genAlgebraicVal.validateWitness(filename,witness));
    }

    @Test
    public void choose3() throws WitnessException, IOException, REException {
        String filename = "choose3.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        //assertEquals(0,genAlgebraicVal.validateWitness(filename,witness));
    }

    @Test
    public void chooseFail() throws WitnessException, IOException, REException {
        String filename = "chooseFail.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        //assertEquals(0,genAlgebraicVal.validateWitness(filename,witness));
    }

    @Test
    public void choose3Json() throws WitnessException, IOException, REException {
        String filename = "choose3.json";
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }

    @Test
    public void chooseFailJson() throws WitnessException, IOException, REException {
        String filename = "chooseFail.json";
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }


}
