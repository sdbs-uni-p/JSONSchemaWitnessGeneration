package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JsonSchemaTestSuiteDraft6WitnessGen;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import org.junit.Ignore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestTrickySolved {

    private GenAndValidate genJSchemaVal =
            new GenAndValidate(System.getProperty("user.dir")+"/expDataset/handwritten/failures/solved/",false);

    void testValid(String filename) throws WitnessException, IOException, REException {
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }

    void testEmpty(String filename) throws WitnessException, IOException, REException {
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals("{\"Problem\":\"Empty witness\"}",witness);
    }


    @Test
    public void fail3() throws WitnessException, IOException, REException {
        testValid("fail3.json");
    }

    @Test
    public void failedpattreq2() throws WitnessException, IOException, REException {
        testValid("failedpattreq2.json");
    }

    @Ignore
    public void failshouldbeempty() throws WitnessException, IOException, REException {
        testEmpty("failshouldbeempty.json");
    }

    @Test
    public void notloopbuttooslow() throws WitnessException, IOException, REException {
        testValid("notloopbuttooslow.json");
    }

    @Test
    public void failpattreq() throws WitnessException, IOException, REException {
        testValid("failpattreq.json");
    }

    @Ignore
    public void failMany() throws WitnessException, IOException, REException {
        LinkedList<String> list = new LinkedList();
        list.add("fail3.json");
        list.add("failedpattreq2.json");
        list.add("notloopbuttooslow.json");
        list.add("solved.failpattreq.json");
        for (String filename : list) {
            String witness = genJSchemaVal.genWitness(filename);
            assertEquals(0, genJSchemaVal.validateWitness(filename, witness));
        }
    }



}
