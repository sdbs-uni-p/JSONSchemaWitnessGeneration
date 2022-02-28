package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import de.uni_passau.sds.patterns.REException;

/**
 * Schemas from the tutorial https://json-schema.org/learn/getting-started-step-by-step.html.
 */
public class GenJsonSchemaOrg {

    private GenAndValidate genVal = 
    		new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/jsonSchemaOrg/");


    @Test
    // https://json-schema.org/learn/getting-started-step-by-step.html
    public void example1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("example1.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("example1.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/getting-started-step-by-step.html
    // TODO: Integer vs. Float
    public void example2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("example2.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("example2.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/getting-started-step-by-step.html
    // TODO: Integer vs. Float
    public void example3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("example3.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("example3.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/getting-started-step-by-step.html
    // TODO: Exception: Type assertion must contain ONE type
    public void example4() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("example4.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("example4.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/getting-started-step-by-step.html
    // TODO: Exception: Type assertion must contain ONE type
    public void example5() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("example5.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("example5.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/getting-started-step-by-step.html
    // TODO: Exception: Type assertion must contain ONE type
    public void example6() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("example6.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("example6.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/getting-started-step-by-step.html
    // TODO: Exception: Type assertion must contain ONE type
    public void example7() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("example7.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("example7.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/examples/address.schema.json
    public void addressSchema() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("address_schema.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("address_schema.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/examples/calendar.schema.json
    public void calendarSchema() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("calendar_schema.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("calendar_schema.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/examples/card.schema.json
    // TODO: Exception: Type assertion must contain ONE type
    public void cardSchema() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("card_schema.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("card_schema.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/miscellaneous-examples.html
    public void misc1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("misc1.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("misc1.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/miscellaneous-examples.html
    public void misc2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("misc2.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("misc2.json",witness));
    }
    
    @Test
    // https://json-schema.org/learn/miscellaneous-examples.html
    // TODO: Exception: Type assertion must contain ONE type
    public void misc3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("misc3.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("misc3.json",witness));
    }
    
    @Test
    // https://json-schema.org/understanding-json-schema/reference/regular_expressions.html#example
    public void regex() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("regex.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("regex.json",witness));
    }
}
