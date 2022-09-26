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

public class TestLogicalErrorsG {

    //private GenAndValidate genJSchemaVal =
      //      new GenAndValidate(System.getProperty("user.dir")+"/expDataset/handwritten/testtrickynew/",false);

    void testValid(String filename) throws WitnessException, IOException, REException {
        testValid("/handwritten/testtrickynew/",filename);
    }

    void testValid(String directory, String filename) throws WitnessException, IOException, REException {
        GenAndValidate validator = new GenAndValidate(System.getProperty("user.dir")+"/expDataset"+directory,false);
        String witness = validator.genWitness(filename+".json");
        assertEquals(0,validator.validateWitness(filename+".json",witness));
    }

    void testEmpty(String filename) throws WitnessException, IOException, REException {
        testEmpty("/handwritten/testtrickynew/",filename);
    }

    void testEmpty(String directory, String filename) throws WitnessException, IOException, REException {
        GenAndValidate validator = new GenAndValidate(System.getProperty("user.dir")+"/expDataset"+directory,false);
        String witness = validator.genWitness(filename+".json");
        assertEquals("{\"Problem\":\"Empty witness\"}",witness);
    }

    @Test
    public void draft2019_09_valid_const_id24_subschema1_not_2() throws WitnessException, IOException, REException {
        testValid("/subSchema/sat/originalWithConst/","draft2019_09_valid_const_id24_subschema1_not_2");
    }

    @Test
    public void draft2019_09_valid_const_id21_subschema1_not_2() throws WitnessException, IOException, REException {
        testValid("/subSchema/sat/originalWithConst/","draft2019_09_valid_const_id21_subschema1_not_2");
    }

    @Test
    public void draft2019_09_valid_oneOf_id8_subschema1_not_2() throws WitnessException, IOException, REException {
        testValid("/subSchema/sat/originalWithConst/","draft2019_09_valid_oneOf_id8_subschema1_not_2");
    }

    @Test
    public void draft2019_09_valid_oneOf_id10_subschema1_not_2() throws WitnessException, IOException, REException {
        testValid("/subSchema/sat/originalWithConst/","draft2019_09_valid_oneOf_id10_subschema1_not_2");
    }

    @Test
    public void draft2019_09_valid_const_id41_subschema1_not_2() throws WitnessException, IOException, REException {
        testValid("/subSchema/sat/originalWithConst/","draft2019_09_valid_const_id41_subschema1_not_2");
    }

    @Test
    public void o89147() throws WitnessException, IOException, REException {
        testValid("/realWorldSchemas/processedFiles/","o89147");
    }

    @Test
    public void o81746() throws WitnessException, IOException, REException {
        testValid("/realWorldSchemas/processedFiles/","o81746");
    }


}
