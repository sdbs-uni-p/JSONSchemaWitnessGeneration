package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GenObjTest {


    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private final String maxIter = "{\"Problem\":\"Max iterations reached\"}";

    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/oneTypeSchemas/object/");




    // type obj
    @Test
    public void test1() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("1.json");

        assertEquals(0,genVal.validateWitness("1.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);

    }



    // ******************* Testing different sizes *******************

    // type obj / minPro: 0
    @Test
    public void test2() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("2.json");

        assertEquals(0,genVal.validateWitness("2.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }


    // type obj / minPro: 1
    @Test
    public void test3() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("3.json");

        assertEquals(0,genVal.validateWitness("3.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / minPro: 5
    @Test
    public void test4() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("4.json");

        assertEquals(0,genVal.validateWitness("4.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / maxPro: 0
    @Test
    public void test5() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("5.json");

        assertEquals(0,genVal.validateWitness("5.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / maxPro: 1
    @Test
    public void test6() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("6.json");

        assertEquals(0,genVal.validateWitness("6.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / maxPro: 5
    @Test
    public void test7() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("7.json");

        assertEquals(0,genVal.validateWitness("7.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / minPro: 1 / maxPro: 0
    @Test
    public void test8() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("8.json");
        assertEquals(noWitness,witness);
    }




    // type obj / minPro: 1 / maxPro: 2
    @Test
    public void test9() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("9.json");

        assertEquals(0,genVal.validateWitness("9.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }




    // type obj / minPro: 2 / maxPro: 4
    @Test
    public void test10() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("10.json");

        assertEquals(0,genVal.validateWitness("10.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }




    // ******************* Testing properties values with different types *******************


    // type obj / 1 required property of type string
    @Test
    public void test11() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("11.json");

        assertEquals(0,genVal.validateWitness("11.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / 1 required property of type null
    @Test
    public void test12() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("12.json");

        assertEquals(0,genVal.validateWitness("12.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }




    // type obj / 1 required property of type bool
    @Test
    public void test13() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("13.json");

        assertEquals(0,genVal.validateWitness("13.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }





    // type obj / 1 required property of type num
    @Test
    public void test14() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("14.json");

        assertEquals(0,genVal.validateWitness("14.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }






    // type obj / 1 required property of type object
    @Test
    public void test15() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("15.json");

        assertEquals(0,genVal.validateWitness("15.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }






    // ******************* Testing properties names with a pattern *******************




    // type obj / pattern: ^a$
    @Test
    public void test16() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("16.json");

        assertEquals(0,genVal.validateWitness("16.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }




    // type obj / pattern: ^a$ / minPro: 1
    @Test
    public void test17() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("17.json");

        assertEquals(0,genVal.validateWitness("17.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }




    // type obj / pattern: ^a.*$ / minPro: 1 / required: abc
    @Test
    public void test18() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("18.json");

        assertEquals(0,genVal.validateWitness("18.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / pattern: ^a.*$ / minPro: 1 / required: b
    @Test
    public void test19() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("19.json");
        assertEquals(noWitness, witness);
    }







    // ******************* Testing property dependencies *******************


    // type obj / properties: "a","b","c" / minPro: 1 / required: a / dependencies: if there is "c" then we must have "b"

    @Test
    public void test20() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("20.json");

        assertEquals(0,genVal.validateWitness("20.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }




    // type obj / properties: "a","b","c" / minPro: 1 / required: a,c / dependencies: if there is "c" then we must have "b"
    @Test
    public void test21() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("21.json");

        assertEquals(0,genVal.validateWitness("21.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }






    // ******************* Testing pattern properties *******************



    // type obj / minPro: 1 / patternProps: ["^a.*"--> num, .*z$ --> str]

    @Test
    public void test22() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("22.json");

        assertEquals(0,genVal.validateWitness("22.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / minPro: 1 / patternProps: ["^a.*"--> num, .*z$ --> str] / additionalProperties: bool

    @Test
    public void test23() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("23.json");

        assertEquals(0,genVal.validateWitness("23.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }




    // type obj / properties: ["obj1": type obj,minPro=2 && "obj2": type obj,minPro=2]
    // "patternProperties": ["^a.*": type obj,minPro=2 && "^b.*":type obj,minPro=]
    // req: "obj1", "obj2"
    // minPro=2

    @Test
    public void test24() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("24.json");

        assertEquals(0,genVal.validateWitness("24.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }





    // type obj / properties: ["obj1": type obj,minPro=2 && "obj2": type obj,minPro=2]
    // "patternProperties": ["^a.*": type obj,minPro=2 && "^b.*":type obj,minPro=]
    // req: "obj1", "obj2"
    // minPro=8

    @Test
    public void test25() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("25.json");

        assertEquals(0,genVal.validateWitness("25.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }



    // type obj / properties: ["obj1": type obj,minPro=2 && "obj2": type obj,minPro=2]
    // "patternProperties": ["^a.*": type obj,minPro=2 && "^.+b$*":type obj,minPro=]
    // req: "obj1", "obj2"
    // minPro=3

    @Test
    public void test26() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("26.json");

        assertEquals(0,genVal.validateWitness("26.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }


    @Test
    // TODO: check the algebra (problem in the root)
    public void test27() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("27.json");

        assertEquals(0,genVal.validateWitness("27.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }




    @Test
    public void test28() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("28.json");

        assertEquals(0,genVal.validateWitness("28.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }


    @Test
    public void test29() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("29.json");

        assertEquals(0,genVal.validateWitness("29.json",witness));
        assertNotEquals(noWitness,witness);
        assertNotEquals(maxIter,witness);
    }


    @Test
    public void test30() throws WitnessException, IOException, REException {

        String witness = genVal.genWitness("30.json");
        assertEquals(noWitness,witness);
    }





















}
