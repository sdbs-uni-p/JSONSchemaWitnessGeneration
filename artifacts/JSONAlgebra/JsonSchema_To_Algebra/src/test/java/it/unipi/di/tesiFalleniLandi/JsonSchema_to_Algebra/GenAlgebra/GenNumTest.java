package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class GenNumTest {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";

    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/oneTypeSchemas/number/");


    // type num
    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(0,genVal.validateWitness("1.json",witness));
    }


    // type num / mof: 2
    @Test
    public void test2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2.json");
        assertEquals(0,genVal.validateWitness("2.json",witness));
    }


    // type num / notMof: 2
    @Test
    public void test3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("3.json");
        assertEquals(0,genVal.validateWitness("3.json",witness));
    }



    // type num / allOf: mof[2,5] / bet(1,10)
    @Test
    public void test4() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("4.json");
        assertEquals(0,genVal.validateWitness("4.json",witness));
    }



    // type num / mof: 2 / notMof: 4 / bet(4,10)
    @Test
    public void test5() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("5.json");
        assertEquals(0,genVal.validateWitness("5.json",witness));
    }



    // type num / allOf: mof[2,5] / notMof: 4
    @Test
    public void test6() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("6.json");
        assertEquals(0,genVal.validateWitness("6.json",witness));
    }



    // type num / mof: 4 / notMof: 2
    @Test
    public void test7() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("7.json");
        assertEquals(noWitness,witness);
    }



    // type num / mof: 4 / xMin: 4 / Max: 8
    @Test
    public void test8() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("8.json");
        assertEquals(0,genVal.validateWitness("8.json",witness));
    }




    // type num / mof: 5 / bet(11,15)
    @Test
    public void test9() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("9.json");
        assertEquals(0,genVal.validateWitness("9.json",witness));
    }




    // type num / mof: 5 / bet(10,15) / xbet(10,16)
    @Test
    public void test10() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("10.json");
        assertEquals(0,genVal.validateWitness("10.json",witness));
    }




    // type num / mof: 5 / xbet(10,15)
    @Test
    public void test11() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("11.json");
        assertEquals(noWitness,witness);
    }



    // type num / allOf: mof[2,3] && notMof[4,9] / bet(10,50)

    // StackOverflowError: when we put >=2 notMof with >=1 mof
    @Test
    public void test12() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("12.json");
        assertEquals(0,genVal.validateWitness("12.json",witness));
    }





    // type num / allOf: notMof[2,3,4,9] / bet(200,400)
    @Test
    public void test13() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("13.json");
        assertEquals(0,genVal.validateWitness("13.json",witness));
    }





    // type num / allOf: mof[2,3,4,5,6,7,8,9] / bet(1000,3000)
    @Test
    public void test14() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("14.json");
        assertEquals(0,genVal.validateWitness("14.json",witness));
    }




    // type num / allOf: mof[2,3,4,5,6,7,8,9] / Min: 5000
    @Test
    public void test15() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("15.json");
        assertEquals(0,genVal.validateWitness("15.json",witness));
    }






    // type num / notMof: [2,3,4,5,6,7,8,9]
    // bet(x,y): OK
    // xBet(x,y): OK
    // Only Min: not OK
    //      Max: not OK
    //      xMin: OK
    //      xMax: not OK
    // Min and xMax: not OK
    // Max and xMin: OK

    // not OK --> infinite loop

    @Test
    public void test16() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("16.json");
        assertEquals(0,genVal.validateWitness("16.json",witness));
    }





    // type num / anyOf: [mof(2), notMof(4)] || [mof(3), notMof(9)] / xMin= 10
    @Test
    public void test17() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("17.json");
        assertEquals(0,genVal.validateWitness("17.json",witness));
    }



    // type num / oneOf: [mof(4), mof(5)] / bet(6,10)
    @Test
    public void test18() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("18.json");
        assertEquals(0,genVal.validateWitness("18.json",witness));
    }



    // type num / oneOf: [mof(2), notMof(4)] || [mof(3), notMof(9)] / bet(1,10)

    // StackOverflowError
    @Test
    public void test19() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("19.json");
        assertEquals(0,genVal.validateWitness("19.json",witness));
    }

    @Test
    public void test20() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("20.json");
        assertEquals(noWitness,witness);
    }



    @Test
    public void test21() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("21.json");
        assertEquals(0,genVal.validateWitness("21.json",witness));
    }

    @Test
    public void test22() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("22.json");
        assertEquals(0,genVal.validateWitness("22.json",witness));
    }


    @Test
    public void test23() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("23.json");
        assertEquals(0,genVal.validateWitness("23.json",witness));
    }

    @Test
    public void test24() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("24.json");
        assertEquals(0,genVal.validateWitness("24.json",witness));
    }


    @Test
    public void test25() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("25.json");
        assertEquals(0,genVal.validateWitness("25.json",witness));
    }



    @Test
    public void test26() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("26.json");
        assertEquals(0,genVal.validateWitness("26.json",witness));
    }



    @Test
    public void test27() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("27.json");
        assertEquals(0,genVal.validateWitness("27.json",witness));
    }




    @Test
    public void test28() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("28.json");
        assertEquals(0,genVal.validateWitness("28.json",witness));
    }

    @Test
    public void test29() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("29.json");
        assertEquals(0,genVal.validateWitness("29.json",witness));
    }

    @Test
    public void test30() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("30.json");
        assertEquals(0,genVal.validateWitness("30.json",witness));
    }


    @Test
    public void test31() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("31.json");
        assertEquals(0,genVal.validateWitness("31.json",witness));
    }



    @Test
    public void test32() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("32.json");
        assertEquals(0,genVal.validateWitness("32.json",witness));
    }


    @Test
    //TODO: algebra should be false for this one xBet(0.01,0.01)
    public void test33() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("33.json");
        assertEquals(noWitness,witness);
    }

    @Test
    public void test34() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("34.json");
        assertEquals(noWitness,witness);
    }
}
