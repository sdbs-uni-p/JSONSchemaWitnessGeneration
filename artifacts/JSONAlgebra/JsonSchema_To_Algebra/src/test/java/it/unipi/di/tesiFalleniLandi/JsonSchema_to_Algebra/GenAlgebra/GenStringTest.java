package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GenStringTest {


    private final String noWitness = "{\"Problem\":\"Empty witness\"}";

    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/oneTypeSchemas/string/");



    // type str
    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(0,genVal.validateWitness("1.json",witness));
    }



    // type str / pattern: ^b
    @Test
    public void test2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2.json");
        assertEquals(0,genVal.validateWitness("2.json",witness));
    }




    // type str / pattern: a$
    @Test
    public void test3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("3.json");
        assertEquals(0,genVal.validateWitness("3.json",witness));
    }





    // type str / anyOf: ^b || a$
    @Test
    public void test4() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("4.json");
        assertEquals(0,genVal.validateWitness("4.json",witness));
    }




    // type str / allOf: ^b && a$
    @Test
    public void test5() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("5.json");
        assertEquals(0,genVal.validateWitness("5.json",witness));
    }





    // type str / ^abc
    @Test
    public void test6() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("6.json");
        assertEquals(0,genVal.validateWitness("6.json",witness));
    }





    // type str / xyz$
    @Test
    public void test7() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("7.json");
        assertEquals(0,genVal.validateWitness("7.json",witness));
    }





    // type str / allOf: ^[1-9]* && ^[1-9]+
    @Test
    public void test8() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("8.json");
        assertEquals(0,genVal.validateWitness("8.json",witness));
    }





    // type str / allOf: ^[1-9]? && [a-z]?$
    @Test
    public void test9() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("9.json");
        assertEquals(0,genVal.validateWitness("9.json",witness));
    }






    // type str / allOf: [1-9]{2} && [a-z]{2}$
    @Test
    public void test10() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("10.json");
        assertEquals(0,genVal.validateWitness("10.json",witness));
    }




    // type str / allOf: ^[1-9]{3,7}- && [a-z]{4,5}$
    @Test
    public void test11() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("11.json");
        assertEquals(0,genVal.validateWitness("11.json",witness));
    }







    // type str / anyof: ^[1-9]{3,7}- || [a-z]{4,5}$
    @Test
    public void test12() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("12.json");
        assertEquals(0,genVal.validateWitness("12.json",witness));
    }







    // type str / allOf: ^a && (not: a$)
    @Test
    public void test13() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("13.json");
        assertEquals(0,genVal.validateWitness("13.json",witness));
    }






    // type str / allOf: ^a. && (not: a$) && [a-z]{8}$
    @Test
    public void test14() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("14.json");
        assertEquals(0,genVal.validateWitness("14.json",witness));
    }




    // type str / allOf: ^a && (not: a)
    @Test
    public void test15() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("15.json");
        assertEquals(noWitness,witness);
    }



    // type str / allOf: a$ && (not: a)
    @Test
    public void test16() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("16.json");
        assertEquals(noWitness,witness);
    }




    // type str / minLength: 1
    @Test
    public void test17() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("17.json");
        assertEquals(0,genVal.validateWitness("17.json",witness));
    }



    // type str / maxLength: 1
    @Test
    public void test18() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("18.json");
        assertEquals(0,genVal.validateWitness("18.json",witness));
    }




    // type str / minLength: 2
    @Test
    public void test19() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("19.json");
        assertEquals(0,genVal.validateWitness("19.json",witness));
    }





    // type str / maxLength: 2
    @Test
    public void test20() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("20.json");
        assertEquals(0,genVal.validateWitness("20.json",witness));
    }



    // type str / minLength: 9 / ^([a-z]{4}-[1-9]{4})+$
    @Test
    public void test21() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("21.json");
        assertEquals(0,genVal.validateWitness("21.json",witness));
    }




    // type str / minLength: 10 / ^([a-z]{4}-[1-9]{4})+$
    @Test
    public void test22() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("22.json");
        assertEquals(0,genVal.validateWitness("22.json",witness));
    }




    // type str / minLength: 5 / maxLength: 10
    @Test
    public void test23() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("23.json");
        assertEquals(0,genVal.validateWitness("23.json",witness));
    }



    // type str / minLength: 10 / maxLength: 15 / ^([a-z]{4}-[1-9]{4})+$
    // length of strings recognized by the pattern: 9n (n>0)
    @Test
    public void test24() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("24.json");
        assertEquals(noWitness,witness);
    }



    // type str / maxLength:4 / ^a.*$
    @Test
    public void test25() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("25.json");
        assertEquals(0,genVal.validateWitness("25.json",witness));
    }





    // type str / minLength: 2 / maxLength:4 / ^a.*a$
    @Test
    public void test26() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("26.json");
        assertEquals(0,genVal.validateWitness("26.json",witness));
    }



}
