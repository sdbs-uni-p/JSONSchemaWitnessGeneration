package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GenMixedSchemasTest {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private final String maxIter = "{\"Problem\":\"Max iterations reached\"}";

    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/mixedTypesSchemas/");




    // Min:10 / Max:20 / pattern:^a.* / minLength:4

    // returns randomly a different value of a different type at each execution
    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(0,genVal.validateWitness("1.json",witness));
    }




    // Min:10 / Max:20 / notMof: 2 / pattern:^a.* / minLength:4

    // adding the notMof constraint makes it return always a number
    @Test
    public void test2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2.json");
        assertEquals(0,genVal.validateWitness("2.json",witness));
    }





    // allOf: [Min:10, Max:20, pattern:^a.* ,minLength:4]
    @Test
    public void test3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("3.json");
        assertEquals(0,genVal.validateWitness("3.json",witness));
    }





    // allOf: [Min:10, Max:20, notMof: 2, pattern:^a.* ,notPattern: .*a$ ,minLength:4]
    @Test
    public void test4() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("4.json");
        assertEquals(noWitness,witness);
    }





    // allOf: [Min:10, Max:20, notMof: 2, pattern:^a.* ,minLength:4, not type: num]
    @Test
    public void test5() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("5.json");
        assertEquals(noWitness,witness);
    }

    // anyOf:   {allOf:[Min:10, Max:20, notMof: 2, pattern:^a.* ,minLength:4]} || {notPattern: .*a$}
    @Test
    public void test6() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("6.json");
        assertEquals(0,genVal.validateWitness("6.json",witness));
    }

    @Test
    // oneOf subschemas are equivalent
    // TODO Typed group must be either And or Type or ref, but found Boolean{value=false}
    public void test11() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("11.json");
        assertEquals(noWitness,witness);
    }
    
    @Test
    // expect a witness such as {"year": "2021"}
    // TODO Typed group must be either And or Type or ref, but found Boolean{value=false}
    public void test12() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("12.json");
        assertEquals(0,genVal.validateWitness("12.json",witness));
    }

    
    @Test
    // expect a witness such as {  "address": {"street": "", "number": 1} }
    // TODO Cannot invoke "java.util.List.contains(Object)"
    public void test13() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("13.json");
        assertEquals(0,genVal.validateWitness("13.json",witness));
    }

    @Test
    // TODO java.lang.IndexOutOfBoundsException: Index 4 out of bounds for length 4
    public void test14() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("14.json");
        assertEquals(0,genVal.validateWitness("14.json",witness));
    }

    
//
//    @Test
//    public void test7() throws WitnessException, IOException, REException {
//        assertEquals("{\"Problem\":\"Empty witness\"}",GenAndValidate.genWitness(path+"7.json"));
//    }
//
//
//
//
//    @Test
//    public void test8() throws WitnessException, IOException, REException {
//        assertEquals(0,GenAndValidate.validateWitness(path+"8.json"));
//    }
//
//
//
//
//
//    @Test
//    public void test9() throws WitnessException, IOException, REException {
//        assertEquals(0,GenAndValidate.validateWitness(path+"9.json"));
//    }
//
//
//
//
//
//    @Test
//    public void test10() throws WitnessException, IOException, REException {
//        assertEquals(0,GenAndValidate.validateWitness(path+"10.json"));
//    }
}
