package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GenArrayTest {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+
                                                       "/witnessGenTestfiles/oneTypeSchemas/array/");


    // type array
    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(0,genVal.validateWitness("1.json",witness));
    }




    // ******************* Testing different lengths *******************


    // type array / minItems=0
    @Test
    public void test2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2.json");
        assertEquals(0,genVal.validateWitness("2.json",witness));
    }



    // type array / minItems=1
    @Test
    public void test3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("3.json");
        assertEquals(0,genVal.validateWitness("3.json",witness));
    }





    // type array / minItems=10
    @Test
    public void test4() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("4.json");
        assertEquals(0,genVal.validateWitness("4.json",witness));
    }






    // type array / maxItems=0
    @Test
    public void test5() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("5.json");
        assertEquals(0,genVal.validateWitness("5.json",witness));
    }



    // type array / maxItems=1
    @Test
    public void test6() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("6.json");
        assertEquals(0,genVal.validateWitness("6.json",witness));
    }





    // type array / maxItems=10
    @Test
    public void test7() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("7.json");
        assertEquals(0,genVal.validateWitness("7.json",witness));
    }






    // type array / minItems=0 / maxItems=1
    @Test
    public void test8() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("8.json");
        assertEquals(0,genVal.validateWitness("8.json",witness));
    }



    // type array / minItems=2 / maxItems=4
    @Test
    public void test9() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("9.json");
        assertEquals(0,genVal.validateWitness("9.json",witness));
    }





    // type array / minItems=5 / maxItems=10
    @Test
    public void test10() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("10.json");
        assertEquals(0,genVal.validateWitness("10.json",witness));
    }







    // ******************* Testing arrays with one type for all items *******************


    // type array / minItems=2 / items: type null
    @Test
    public void test11() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("11.json");
        assertEquals(0,genVal.validateWitness("11.json",witness));
    }




    // type array / minItems=2 / items: type bool
    @Test
    public void test12() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("12.json");
        assertEquals(0,genVal.validateWitness("12.json",witness));
    }




    // type array / minItems=2 / items: type num
    @Test
    public void test13() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("13.json");
        assertEquals(0,genVal.validateWitness("13.json",witness));
    }




    // type array / minItems=2 / items: type str
    @Test
    public void test14() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("14.json");
        assertEquals(0,genVal.validateWitness("14.json",witness));
    }




    // type array / minItems=2 / items: type array
    @Test
    public void test15() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("15.json");
        assertEquals(0,genVal.validateWitness("15.json",witness));
    }





    // type array / minItems=2 / items: type object
    @Test
    public void test16() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("16.json");
        assertEquals(0,genVal.validateWitness("16.json",witness));
    }





    // ******************* Testing the "contains" constraint *******************



    // type array / minItems=2 / contains: type null
    @Test
    public void test17() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("17.json");
        assertEquals(0,genVal.validateWitness("17.json",witness));
    }




    // type array / minItems=2 / contains: type bool
    @Test
    public void test18() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("18.json");
        assertEquals(0,genVal.validateWitness("18.json",witness));
    }




    // type array / minItems=2 / contains: type num
    @Test
    public void test19() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("19.json");
        assertEquals(0,genVal.validateWitness("19.json",witness));
    }




    // type array / minItems=2 / contains: type num   bet(1,10)   allOf:[mof(2), mof(3)]
    @Test
    public void test20() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("20.json");
        assertEquals(0,genVal.validateWitness("20.json",witness));
    }




    // type array / minItems=2 / contains: type str
    @Test
    public void test21() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("21.json");
        assertEquals(0,genVal.validateWitness("21.json",witness));
    }




    // type array / minItems=2 / contains: type array
    @Test
    public void test22() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("22.json");
        assertEquals(0,genVal.validateWitness("22.json",witness));
    }


    // type array / minContains=2 / contains: type array
    @Test
    public void test22b() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("22b.json");
        assertEquals(0,genVal.validateWitness("22b.json",witness));
    }




    // type array / minItems=2 / contains: type object
    @Test
    public void test23() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("23.json");
        assertEquals(0,genVal.validateWitness("23.json",witness));
    }


    // type array / minContains=2 / contains: type object
    @Test
    public void test23b() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("23b.json");
        assertEquals(0,genVal.validateWitness("23b.json",witness));
    }



    // type array / minItems=2 / contains: type object   required: numValue
    // properties[numValue:type num / anyOf: [mof(2), notMof(4)] || [mof(3), notMof(9)] / xMin= 10 ]]
    @Test
    public void test24() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("24.json");
        assertEquals(0,genVal.validateWitness("24.json",witness));
    }


    // type array / minContains=2 / contains: type object   required: numValue
    // properties[numValue:type num / anyOf: [mof(2), notMof(4)] || [mof(3), notMof(9)] / xMin= 10 ]]
    @Test
    public void test24b() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("24b.json");
        assertEquals(0,genVal.validateWitness("24b.json",witness));
    }






    // ******************* Testing tuples: items with different schemas *******************




    // type array / items: [null, bool, num, str, array, object]
    @Test
    public void test25() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("25.json");
        assertEquals(0,genVal.validateWitness("25.json",witness));
    }




    // type array / items: [null, bool, num, str, array, object] / minItems=10
    @Test
    public void test26() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("26.json");
        assertEquals(0,genVal.validateWitness("26.json",witness));
    }





    // type array / items: [null, bool, num, str, array, object] / minItems=2 / maxItems=4
    @Test
    public void test27() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("27.json");
        assertEquals(0,genVal.validateWitness("27.json",witness));
    }





    // type array / items: [null, bool, num, str, array, object] / minItems=10
    // additionalItems: type num / xMin=2 / mof(2)
    @Test
    public void test28() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("28.json");
        assertEquals(0,genVal.validateWitness("28.json",witness));
    }





    // type array / items: [null, bool, num, str, array, object] / minItems=10
    // additionalItems: type num / uniqueItems: true
    @Test
    public void test29() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("29.json");
        assertEquals(0,genVal.validateWitness("29.json",witness));
    }





    // type array / items: [null,bool,num,str,array,object] / maxItems: 0
    @Test
    public void test30() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("30.json");
        assertEquals(0,genVal.validateWitness("30.json",witness));
    }


    // ******************* Testing empty schemas *******************





    // type array / items: type null / minItems=2 / uniqueItems: true
    @Test
    public void test31() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("31.json");
        assertEquals(noWitness,witness);
    }





    // type array / minItems=1 / maxItems=0
    @Test
    public void test32() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("32.json");
        assertEquals(noWitness,witness);
    }



    @Test
    // TODO expected witness [0] or ["ok"]
    public void test36() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("36.json");
        assertEquals(0,genVal.validateWitness("36.json",witness));
    }

    @Test
    // TODO expected witness [0] or ["ok"]
    public void test38() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("38.json");
        assertEquals(0,genVal.validateWitness("38.json",witness));
    }

    @Test
    // TODO expected witness [0] or ["ok"]
    public void test39() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("39.json");
        assertEquals(0,genVal.validateWitness("39.json",witness));
    }

    @Test
    // TODO expected witness [0] or ["ok"]
    public void test40() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("40.json");
        assertEquals(0,genVal.validateWitness("40.json",witness));
    }
















}
