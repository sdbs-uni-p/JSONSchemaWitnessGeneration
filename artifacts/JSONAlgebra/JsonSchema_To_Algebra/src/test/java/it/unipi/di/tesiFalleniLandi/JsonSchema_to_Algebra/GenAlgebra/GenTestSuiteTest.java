package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;


import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class GenTestSuiteTest {

    // TODO problems with test 197: Witness "\u0003" does not match pattern "^\cc$"

	private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/jsonSchemaTestSuiteDraft6/");
	
    @Test
    public void test0() throws Exception {
        String witness = genVal.genWitness("0.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("0.json",witness));
    }
	
    @Test
    public void test1() throws Exception {
        String witness = genVal.genWitness("1.json");
        assertEquals(0,genVal.validateWitness("1.json",witness));
    }
	
    @Test
    public void test3() throws Exception {
        String witness = genVal.genWitness("3.json");
        assertEquals(0,genVal.validateWitness("3.json",witness));
    }
    
    @Test
    public void test4() throws Exception {
        String witness = genVal.genWitness("4.json");
        assertEquals(0,genVal.validateWitness("4.json",witness));
    }
    
    @Test
    public void test5() throws Exception {
        String witness = genVal.genWitness("5.json");
        assertEquals(0,genVal.validateWitness("5.json",witness));
    }
    
    @Test
    public void test6() throws Exception {
        String witness = genVal.genWitness("6.json");
        assertEquals(0,genVal.validateWitness("6.json",witness));
    }
    
    @Test
    public void test7() throws Exception {
        String witness = genVal.genWitness("7.json");
        assertEquals(0,genVal.validateWitness("7.json",witness));
    }
    
    @Test
    public void test8() throws Exception {
        String witness = genVal.genWitness("8.json");
        assertEquals(0,genVal.validateWitness("8.json",witness));
    }
    
    @Test
    public void test9() throws Exception {
        String witness = genVal.genWitness("9.json");
        assertEquals(0,genVal.validateWitness("9.json",witness));
    }
    
    @Test
    public void test10() throws Exception {
        String witness = genVal.genWitness("10.json");
        assertEquals(0,genVal.validateWitness("10.json",witness));
    }
    
    @Test
    public void test11() throws Exception {
        String witness = genVal.genWitness("11.json");
        assertEquals(0,genVal.validateWitness("11.json",witness));
    }
    
    @Test
    public void test12() throws Exception {
        String witness = genVal.genWitness("12.json");
        assertEquals(0,genVal.validateWitness("12.json",witness));
    }
    
    @Test
    public void test13() throws Exception {
        String witness = genVal.genWitness("13.json");
        assertEquals(0,genVal.validateWitness("13.json",witness));
    }
    
    @Test
    public void test14() throws Exception {
        String witness = genVal.genWitness("14.json");
        assertEquals(0,genVal.validateWitness("14.json",witness));
    }
    
    @Test
    public void test15() throws Exception {
        String witness = genVal.genWitness("10.json");
        assertEquals(0,genVal.validateWitness("10.json",witness));
    }
    
    @Test
    public void test16() throws Exception {
        String witness = genVal.genWitness("16.json");
        assertEquals(0,genVal.validateWitness("16.json",witness));
    }
    
    @Test
    // TODO
    public void test17() throws Exception {
        String witness = genVal.genWitness("17.json");
        assertEquals(0,genVal.validateWitness("17.json",witness));
    }
    
    @Test
    // TODO
    public void test18() throws Exception {
        String witness = genVal.genWitness("18.json");
        assertEquals(0,genVal.validateWitness("18.json",witness));
    }
    
    @Test
    public void test19() throws Exception {
        String witness = genVal.genWitness("19.json");
        assertEquals(0,genVal.validateWitness("19.json",witness));
    }
    
    @Test
    public void test20() throws Exception {
        String witness = genVal.genWitness("20.json");
        assertEquals(0,genVal.validateWitness("20.json",witness));
    }
    
    @Test
    public void test21() throws Exception {
        String witness = genVal.genWitness("21.json");
        assertEquals(0,genVal.validateWitness("21.json",witness));
    }
    
    @Test
    public void test22() throws Exception {
        String witness = genVal.genWitness("22.json");
        assertEquals(0,genVal.validateWitness("22.json",witness));
    }
    
    @Test
    public void test23() throws Exception {
        String witness = genVal.genWitness("23.json");
        assertEquals(0,genVal.validateWitness("23.json",witness));
    }
    
    @Test
    public void test24() throws Exception {
        String witness = genVal.genWitness("24.json");
        assertEquals(0,genVal.validateWitness("24.json",witness));
    }
    
    @Test
    public void test25() throws Exception {
        String witness = genVal.genWitness("25.json");
        assertEquals(0,genVal.validateWitness("25.json",witness));
    }
    
    @Test
    public void test26() throws Exception {
        String witness = genVal.genWitness("26.json");
        assertEquals(0,genVal.validateWitness("26.json",witness));
    }
    
    @Test
    public void test27() throws Exception {
        String witness = genVal.genWitness("27.json");
        assertEquals(0,genVal.validateWitness("27.json",witness));
    }
    
    @Test
    public void test28() throws Exception {
        String witness = genVal.genWitness("28.json");
        assertEquals(0,genVal.validateWitness("28.json",witness));
    }
    
    @Test
    // TODO
    public void test29() throws Exception {
        String witness = genVal.genWitness("29.json");
        assertEquals(0,genVal.validateWitness("29.json",witness));
    }
    
    @Test
    public void test30() throws Exception {
        String witness = genVal.genWitness("30.json");
        assertEquals(0,genVal.validateWitness("30.json",witness));
    }

    @Test
    public void generatedTest0() throws Exception {
        String witness = genVal.genWitness("0.json");
        assertEquals(0,genVal.validateWitness("0.json",witness));
    }

    @Test
    public void generatedTest1() throws Exception {
        String witness = genVal.genWitness("1.json");
        assertEquals(0,genVal.validateWitness("1.json",witness));
    }

    @Test
    public void generatedTest2() throws Exception {
        String witness = genVal.genWitness("2.json");
        assertEquals(0,genVal.validateWitness("2.json",witness));
    }

    @Test
    public void generatedTest3() throws Exception {
        String witness = genVal.genWitness("3.json");
        assertEquals(0,genVal.validateWitness("3.json",witness));
    }

    @Test
    public void generatedTest4() throws Exception {
        String witness = genVal.genWitness("4.json");
        assertEquals(0,genVal.validateWitness("4.json",witness));
    }

    @Test
    public void generatedTest5() throws Exception {
        String witness = genVal.genWitness("5.json");
        assertEquals(0,genVal.validateWitness("5.json",witness));
    }

    @Test
    public void generatedTest6() throws Exception {
        String witness = genVal.genWitness("6.json");
        assertEquals(0,genVal.validateWitness("6.json",witness));
    }

    @Test
    public void generatedTest7() throws Exception {
        String witness = genVal.genWitness("7.json");
        assertEquals(0,genVal.validateWitness("7.json",witness));
    }

    @Test
    public void generatedTest8() throws Exception {
        String witness = genVal.genWitness("8.json");
        assertEquals(0,genVal.validateWitness("8.json",witness));
    }

    @Test
    public void generatedTest9() throws Exception {
        String witness = genVal.genWitness("9.json");
        assertEquals(0,genVal.validateWitness("9.json",witness));
    }

    @Test
    public void generatedTest10() throws Exception {
        String witness = genVal.genWitness("10.json");
        assertEquals(0,genVal.validateWitness("10.json",witness));
    }

    @Test
    public void generatedTest11() throws Exception {
        String witness = genVal.genWitness("11.json");
        assertEquals(0,genVal.validateWitness("11.json",witness));
    }

    @Test
    public void generatedTest12() throws Exception {
        String witness = genVal.genWitness("12.json");
        assertEquals(0,genVal.validateWitness("12.json",witness));
    }

    @Test
    public void generatedTest13() throws Exception {
        String witness = genVal.genWitness("13.json");
        assertEquals(0,genVal.validateWitness("13.json",witness));
    }

    @Test
    public void generatedTest14() throws Exception {
        String witness = genVal.genWitness("14.json");
        assertEquals(0,genVal.validateWitness("14.json",witness));
    }

    @Test
    public void generatedTest15() throws Exception {
        String witness = genVal.genWitness("15.json");
        assertEquals(0,genVal.validateWitness("15.json",witness));
    }

    @Test
    public void generatedTest16() throws Exception {
        String witness = genVal.genWitness("16.json");
        assertEquals(0,genVal.validateWitness("16.json",witness));
    }

    @Test
    public void generatedTest17() throws Exception {
        String witness = genVal.genWitness("17.json");
        assertEquals(0,genVal.validateWitness("17.json",witness));
    }

    @Test
    public void generatedTest18() throws Exception {
        String witness = genVal.genWitness("18.json");
        assertEquals(0,genVal.validateWitness("18.json",witness));
    }

    @Test
    public void generatedTest19() throws Exception {
        String witness = genVal.genWitness("19.json");
        assertEquals(0,genVal.validateWitness("19.json",witness));
    }

    @Test
    public void generatedTest20() throws Exception {
        String witness = genVal.genWitness("20.json");
        assertEquals(0,genVal.validateWitness("20.json",witness));
    }

    @Test
    public void generatedTest21() throws Exception {
        String witness = genVal.genWitness("21.json");
        assertEquals(0,genVal.validateWitness("21.json",witness));
    }

    @Test
    public void generatedTest22() throws Exception {
        String witness = genVal.genWitness("22.json");
        assertEquals(0,genVal.validateWitness("22.json",witness));
    }

    @Test
    public void generatedTest23() throws Exception {
        String witness = genVal.genWitness("23.json");
        assertEquals(0,genVal.validateWitness("23.json",witness));
    }

    @Test
    public void generatedTest24() throws Exception {
        String witness = genVal.genWitness("24.json");
        assertEquals(0,genVal.validateWitness("24.json",witness));
    }

    @Test
    public void generatedTest25() throws Exception {
        String witness = genVal.genWitness("25.json");
        assertEquals(0,genVal.validateWitness("25.json",witness));
    }

    @Test
    public void generatedTest26() throws Exception {
        String witness = genVal.genWitness("26.json");
        assertEquals(0,genVal.validateWitness("26.json",witness));
    }

    @Test
    public void generatedTest27() throws Exception {
        String witness = genVal.genWitness("27.json");
        assertEquals(0,genVal.validateWitness("27.json",witness));
    }

    @Test
    public void generatedTest28() throws Exception {
        String witness = genVal.genWitness("28.json");
        assertEquals(0,genVal.validateWitness("28.json",witness));
    }

    @Test
    public void generatedTest29() throws Exception {
        String witness = genVal.genWitness("29.json");
        assertEquals(0,genVal.validateWitness("29.json",witness));
    }

    @Test
    public void generatedTest30() throws Exception {
        String witness = genVal.genWitness("30.json");
        assertEquals(0,genVal.validateWitness("30.json",witness));
    }

    @Test
    public void generatedTest31() throws Exception {
        String witness = genVal.genWitness("31.json");
        assertEquals(0,genVal.validateWitness("31.json",witness));
    }

    @Test
    public void generatedTest32() throws Exception {
        String witness = genVal.genWitness("32.json");
        assertEquals(0,genVal.validateWitness("32.json",witness));
    }

    @Test
    public void generatedTest33() throws Exception {
        String witness = genVal.genWitness("33.json");
        assertEquals(0,genVal.validateWitness("33.json",witness));
    }

    @Test
    public void generatedTest34() throws Exception {
        String witness = genVal.genWitness("34.json");
        assertEquals(0,genVal.validateWitness("34.json",witness));
    }

    @Test
    public void generatedTest35() throws Exception {
        String witness = genVal.genWitness("35.json");
        assertEquals(0,genVal.validateWitness("35.json",witness));
    }

    @Test
    public void generatedTest36() throws Exception {
        String witness = genVal.genWitness("36.json");
        assertEquals(0,genVal.validateWitness("36.json",witness));
    }

    @Test
    public void generatedTest37() throws Exception {
        String witness = genVal.genWitness("37.json");
        assertEquals(0,genVal.validateWitness("37.json",witness));
    }

    @Test
    public void generatedTest38() throws Exception {
        String witness = genVal.genWitness("38.json");
        assertEquals(0,genVal.validateWitness("38.json",witness));
    }

    @Test
    public void generatedTest39() throws Exception {
        String witness = genVal.genWitness("39.json");
        assertEquals(0,genVal.validateWitness("39.json",witness));
    }

    @Test
    public void generatedTest40() throws Exception {
        String witness = genVal.genWitness("40.json");
        assertEquals(0,genVal.validateWitness("40.json",witness));
    }

    @Test
    public void generatedTest41() throws Exception {
        String witness = genVal.genWitness("41.json");
        assertEquals(0,genVal.validateWitness("41.json",witness));
    }

    @Test
    public void generatedTest42() throws Exception {
        String witness = genVal.genWitness("42.json");
        assertEquals(0,genVal.validateWitness("42.json",witness));
    }

    @Test
    public void generatedTest43() throws Exception {
        String witness = genVal.genWitness("43.json");
        assertEquals(0,genVal.validateWitness("43.json",witness));
    }

    @Test
    public void generatedTest44() throws Exception {
        String witness = genVal.genWitness("44.json");
        assertEquals(0,genVal.validateWitness("44.json",witness));
    }

    @Test
    public void generatedTest45() throws Exception {
        String witness = genVal.genWitness("45.json");
        assertEquals(0,genVal.validateWitness("45.json",witness));
    }

    @Test
    public void generatedTest46() throws Exception {
        String witness = genVal.genWitness("46.json");
        assertEquals(0,genVal.validateWitness("46.json",witness));
    }

    @Test
    public void generatedTest47() throws Exception {
        String witness = genVal.genWitness("47.json");
        assertEquals(0,genVal.validateWitness("47.json",witness));
    }

    @Test
    public void generatedTest48() throws Exception {
        String witness = genVal.genWitness("48.json");
        assertEquals(0,genVal.validateWitness("48.json",witness));
    }

    @Ignore
    @Test
    //TODO: Termination problem
    public void generatedTest49() throws Exception {
        String witness = genVal.genWitness("49.json");
        assertEquals(0,genVal.validateWitness("49.json",witness));
    }

    @Test
    public void generatedTest50() throws Exception {
        String witness = genVal.genWitness("50.json");
        assertEquals(0,genVal.validateWitness("50.json",witness));
    }

    @Test
    public void generatedTest51() throws Exception {
        String witness = genVal.genWitness("51.json");
        assertEquals(0,genVal.validateWitness("51.json",witness));
    }

    @Test
    public void generatedTest52() throws Exception {
        String witness = genVal.genWitness("52.json");
        assertEquals(0,genVal.validateWitness("52.json",witness));
    }

    @Test
    public void generatedTest53() throws Exception {
        String witness = genVal.genWitness("53.json");
        assertEquals(0,genVal.validateWitness("53.json",witness));
    }

    @Test
    public void generatedTest54() throws Exception {
        String witness = genVal.genWitness("54.json");
        assertEquals(0,genVal.validateWitness("54.json",witness));
    }

    @Test
    public void generatedTest55() throws Exception {
        String witness = genVal.genWitness("55.json");
        assertEquals(0,genVal.validateWitness("55.json",witness));
    }

    @Test
    public void generatedTest56() throws Exception {
        String witness = genVal.genWitness("56.json");
        assertEquals(0,genVal.validateWitness("56.json",witness));
    }

    @Test
    public void generatedTest57() throws Exception {
        String witness = genVal.genWitness("57.json");
        assertEquals(0,genVal.validateWitness("57.json",witness));
    }

    @Test
    public void generatedTest58() throws Exception {
        String witness = genVal.genWitness("58.json");
        assertEquals(0,genVal.validateWitness("58.json",witness));
    }

    @Test
    public void generatedTest59() throws Exception {
        String witness = genVal.genWitness("59.json");
        assertEquals(0,genVal.validateWitness("59.json",witness));
    }

    @Test
    public void generatedTest60() throws Exception {
        String witness = genVal.genWitness("60.json");
        assertEquals(0,genVal.validateWitness("60.json",witness));
    }

    @Test
    public void generatedTest61() throws Exception {
        String witness = genVal.genWitness("61.json");
        assertEquals(0,genVal.validateWitness("61.json",witness));
    }

    @Test
    public void generatedTest62() throws Exception {
        String witness = genVal.genWitness("62.json");
        assertEquals(0,genVal.validateWitness("62.json",witness));
    }

    @Test
    public void generatedTest63() throws Exception {
        String witness = genVal.genWitness("63.json");
        assertEquals(0,genVal.validateWitness("63.json",witness));
    }

    @Test
    public void generatedTest64() throws Exception {
        String witness = genVal.genWitness("64.json");
        assertEquals(0,genVal.validateWitness("64.json",witness));
    }

    @Test
    public void generatedTest65() throws Exception {
        String witness = genVal.genWitness("65.json");
        assertEquals(0,genVal.validateWitness("65.json",witness));
    }

    @Test
    public void generatedTest66() throws Exception {
        String witness = genVal.genWitness("66.json");
        assertEquals(0,genVal.validateWitness("66.json",witness));
    }

    @Test
    public void generatedTest67() throws Exception {
        String witness = genVal.genWitness("67.json");
        assertEquals(0,genVal.validateWitness("67.json",witness));
    }

    @Test
    public void generatedTest68() throws Exception {
        String witness = genVal.genWitness("68.json");
        assertEquals(0,genVal.validateWitness("68.json",witness));
    }

    @Test
    public void generatedTest69() throws Exception {
        String witness = genVal.genWitness("69.json");
        assertEquals(0,genVal.validateWitness("69.json",witness));
    }

    @Test
    public void generatedTest70() throws Exception {
        String witness = genVal.genWitness("70.json");
        assertEquals(0,genVal.validateWitness("70.json",witness));
    }

    @Test
    public void generatedTest71() throws Exception {
        String witness = genVal.genWitness("71.json");
        assertEquals(0,genVal.validateWitness("71.json",witness));
    }

    @Test
    public void generatedTest72() throws Exception {
        String witness = genVal.genWitness("72.json");
        assertEquals(0,genVal.validateWitness("72.json",witness));
    }

    @Test
    public void generatedTest73() throws Exception {
        String witness = genVal.genWitness("73.json");
        assertEquals(0,genVal.validateWitness("73.json",witness));
    }

    @Test
    public void generatedTest74() throws Exception {
        String witness = genVal.genWitness("74.json");
        assertEquals(0,genVal.validateWitness("74.json",witness));
    }

    @Test
    public void generatedTest75() throws Exception {
        String witness = genVal.genWitness("75.json");
        assertEquals(0,genVal.validateWitness("75.json",witness));
    }

    @Test
    public void generatedTest76() throws Exception {
        String witness = genVal.genWitness("76.json");
        assertEquals(0,genVal.validateWitness("76.json",witness));
    }

    @Test
    public void generatedTest77() throws Exception {
        String witness = genVal.genWitness("77.json");
        assertEquals(0,genVal.validateWitness("77.json",witness));
    }

    @Test
    public void generatedTest78() throws Exception {
        String witness = genVal.genWitness("78.json");
        assertEquals(0,genVal.validateWitness("78.json",witness));
    }

    @Test
    public void generatedTest79() throws Exception {
        String witness = genVal.genWitness("79.json");
        assertEquals(0,genVal.validateWitness("79.json",witness));
    }

    @Test
    public void generatedTest80() throws Exception {
        String witness = genVal.genWitness("80.json");
        assertEquals(0,genVal.validateWitness("80.json",witness));
    }

    @Test
    public void generatedTest81() throws Exception {
        String witness = genVal.genWitness("81.json");
        assertEquals(0,genVal.validateWitness("81.json",witness));
    }

    @Test
    public void generatedTest82() throws Exception {
        String witness = genVal.genWitness("82.json");
        assertEquals(0,genVal.validateWitness("82.json",witness));
    }

    @Test
    public void generatedTest83() throws Exception {
        String witness = genVal.genWitness("83.json");
        assertEquals(0,genVal.validateWitness("83.json",witness));
    }

    @Test
    public void generatedTest84() throws Exception {
        String witness = genVal.genWitness("84.json");
        assertEquals(0,genVal.validateWitness("84.json",witness));
    }

    @Test
    public void generatedTest85() throws Exception {
        String witness = genVal.genWitness("85.json");
        assertEquals(0,genVal.validateWitness("85.json",witness));
    }

    @Test
    public void generatedTest86() throws Exception {
        String witness = genVal.genWitness("86.json");
        assertEquals(0,genVal.validateWitness("86.json",witness));
    }

    @Test
    public void generatedTest87() throws Exception {
        String witness = genVal.genWitness("87.json");
        assertEquals(0,genVal.validateWitness("87.json",witness));
    }

    @Test
    public void generatedTest88() throws Exception {
        String witness = genVal.genWitness("88.json");
        assertEquals(0,genVal.validateWitness("88.json",witness));
    }

    @Test
    public void generatedTest89() throws Exception {
        String witness = genVal.genWitness("89.json");
        assertEquals(0,genVal.validateWitness("89.json",witness));
    }

    @Test
    public void generatedTest90() throws Exception {
        String witness = genVal.genWitness("90.json");
        assertEquals(0,genVal.validateWitness("90.json",witness));
    }

    @Test
    public void generatedTest91() throws Exception {
        String witness = genVal.genWitness("91.json");
        assertEquals(0,genVal.validateWitness("91.json",witness));
    }

    @Test
    public void generatedTest92() throws Exception {
        String witness = genVal.genWitness("92.json");
        assertEquals(0,genVal.validateWitness("92.json",witness));
    }

    @Test
    public void generatedTest93() throws Exception {
        String witness = genVal.genWitness("93.json");
        assertEquals(0,genVal.validateWitness("93.json",witness));
    }

    @Test
    public void generatedTest94() throws Exception {
        String witness = genVal.genWitness("94.json");
        assertEquals(0,genVal.validateWitness("94.json",witness));
    }

    @Test
    public void generatedTest95() throws Exception {
        String witness = genVal.genWitness("95.json");
        assertEquals(0,genVal.validateWitness("95.json",witness));
    }

    @Test
    public void generatedTest96() throws Exception {
        String witness = genVal.genWitness("96.json");
        assertEquals(0,genVal.validateWitness("96.json",witness));
    }

    @Test
    public void generatedTest97() throws Exception {
        String witness = genVal.genWitness("97.json");
        assertEquals(0,genVal.validateWitness("97.json",witness));
    }

    @Test
    public void generatedTest98() throws Exception {
        String witness = genVal.genWitness("98.json");
        assertEquals(0,genVal.validateWitness("98.json",witness));
    }

    @Test
    public void generatedTest99() throws Exception {
        String witness = genVal.genWitness("99.json");
        assertEquals(0,genVal.validateWitness("99.json",witness));
    }

    @Test
    public void generatedTest100() throws Exception {
        String witness = genVal.genWitness("100.json");
        assertEquals(0,genVal.validateWitness("100.json",witness));
    }

    @Test
    public void generatedTest101() throws Exception {
        String witness = genVal.genWitness("101.json");
        assertEquals(0,genVal.validateWitness("101.json",witness));
    }

    @Test
    public void generatedTest102() throws Exception {
        String witness = genVal.genWitness("102.json");
        assertEquals(0,genVal.validateWitness("102.json",witness));
    }

    @Test
    public void generatedTest103() throws Exception {
        String witness = genVal.genWitness("103.json");
        assertEquals(0,genVal.validateWitness("103.json",witness));
    }

    @Test
    public void generatedTest104() throws Exception {
        String witness = genVal.genWitness("104.json");
        assertEquals(0,genVal.validateWitness("104.json",witness));
    }

    @Test
    public void generatedTest105() throws Exception {
        String witness = genVal.genWitness("105.json");
        assertEquals(0,genVal.validateWitness("105.json",witness));
    }

    @Test
    public void generatedTest106() throws Exception {
        String witness = genVal.genWitness("106.json");
        assertEquals(0,genVal.validateWitness("106.json",witness));
    }

    @Test
    public void generatedTest107() throws Exception {
        String witness = genVal.genWitness("107.json");
        assertEquals(0,genVal.validateWitness("107.json",witness));
    }

    @Test
    public void generatedTest108() throws Exception {
        String witness = genVal.genWitness("108.json");
        assertEquals(0,genVal.validateWitness("108.json",witness));
    }

    @Test
    public void generatedTest109() throws Exception {
        String witness = genVal.genWitness("109.json");
        assertEquals(0,genVal.validateWitness("109.json",witness));
    }

    @Test
    public void generatedTest110() throws Exception {
        String witness = genVal.genWitness("110.json");
        assertEquals(0,genVal.validateWitness("110.json",witness));
    }

    @Test
    public void generatedTest111() throws Exception {
        String witness = genVal.genWitness("111.json");
        assertEquals(0,genVal.validateWitness("111.json",witness));
    }

    @Test
    public void generatedTest112() throws Exception {
        String witness = genVal.genWitness("112.json");
        assertEquals(0,genVal.validateWitness("112.json",witness));
    }

    @Test
    public void generatedTest113() throws Exception {
        String witness = genVal.genWitness("113.json");
        assertEquals(0,genVal.validateWitness("113.json",witness));
    }

    @Test
    public void generatedTest114() throws Exception {
        String witness = genVal.genWitness("114.json");
        assertEquals(0,genVal.validateWitness("114.json",witness));
    }

    @Test
    public void generatedTest115() throws Exception {
        String witness = genVal.genWitness("115.json");
        assertEquals(0,genVal.validateWitness("115.json",witness));
    }

    @Test
    public void generatedTest116() throws Exception {
        String witness = genVal.genWitness("116.json");
        assertEquals(0,genVal.validateWitness("116.json",witness));
    }

    @Test
    public void generatedTest117() throws Exception {
        String witness = genVal.genWitness("117.json");
        assertEquals(0,genVal.validateWitness("117.json",witness));
    }

    @Test
    public void generatedTest118() throws Exception {
        String witness = genVal.genWitness("118.json");
        assertEquals(0,genVal.validateWitness("118.json",witness));
    }

    @Test
    public void generatedTest119() throws Exception {
        String witness = genVal.genWitness("119.json");
        assertEquals(0,genVal.validateWitness("119.json",witness));
    }

    @Test
    public void generatedTest120() throws Exception {
        String witness = genVal.genWitness("120.json");
        assertEquals(0,genVal.validateWitness("120.json",witness));
    }

    @Test
    public void generatedTest121() throws Exception {
        String witness = genVal.genWitness("121.json");
        assertEquals(0,genVal.validateWitness("121.json",witness));
    }

    @Test
    public void generatedTest122() throws Exception {
        String witness = genVal.genWitness("122.json");
        assertEquals(0,genVal.validateWitness("122.json",witness));
    }

    @Test
    public void generatedTest123() throws Exception {
        String witness = genVal.genWitness("123.json");
        assertEquals(0,genVal.validateWitness("123.json",witness));
    }

    @Test
    public void generatedTest124() throws Exception {
        String witness = genVal.genWitness("124.json");
        assertEquals(0,genVal.validateWitness("124.json",witness));
    }

    @Test
    public void generatedTest125() throws Exception {
        String witness = genVal.genWitness("125.json");
        assertEquals(0,genVal.validateWitness("125.json",witness));
    }

    @Test
    public void generatedTest126() throws Exception {
        String witness = genVal.genWitness("126.json");
        assertEquals(0,genVal.validateWitness("126.json",witness));
    }

    @Test
    public void generatedTest127() throws Exception {
        String witness = genVal.genWitness("127.json");
        assertEquals(0,genVal.validateWitness("127.json",witness));
    }

    @Test
    public void generatedTest128() throws Exception {
        String witness = genVal.genWitness("128.json");
        assertEquals(0,genVal.validateWitness("128.json",witness));
    }

    @Test
    public void generatedTest129() throws Exception {
        String witness = genVal.genWitness("129.json");
        assertEquals(0,genVal.validateWitness("129.json",witness));
    }

    @Test
    public void generatedTest130() throws Exception {
        String witness = genVal.genWitness("130.json");
        assertEquals(0,genVal.validateWitness("130.json",witness));
    }

    @Test
    public void generatedTest131() throws Exception {
        String witness = genVal.genWitness("131.json");
        assertEquals(0,genVal.validateWitness("131.json",witness));
    }

    @Test
    public void generatedTest132() throws Exception {
        String witness = genVal.genWitness("132.json");
        assertEquals(0,genVal.validateWitness("132.json",witness));
    }

    @Test
    public void generatedTest133() throws Exception {
        String witness = genVal.genWitness("133.json");
        assertEquals(0,genVal.validateWitness("133.json",witness));
    }

    @Test
    public void generatedTest134() throws Exception {
        String witness = genVal.genWitness("134.json");
        assertEquals(0,genVal.validateWitness("134.json",witness));
    }

    @Test
    public void generatedTest135() throws Exception {
        String witness = genVal.genWitness("135.json");
        assertEquals(0,genVal.validateWitness("135.json",witness));
    }

    @Test
    public void generatedTest136() throws Exception {
        String witness = genVal.genWitness("136.json");
        assertEquals(0,genVal.validateWitness("136.json",witness));
    }

    @Test
    public void generatedTest137() throws Exception {
        String witness = genVal.genWitness("137.json");
        assertEquals(0,genVal.validateWitness("137.json",witness));
    }

    @Test
    public void generatedTest138() throws Exception {
        String witness = genVal.genWitness("138.json");
        assertEquals(0,genVal.validateWitness("138.json",witness));
    }

    @Test
    public void generatedTest139() throws Exception {
        String witness = genVal.genWitness("139.json");
        assertEquals(0,genVal.validateWitness("139.json",witness));
    }

    @Test
    public void generatedTest140() throws Exception {
        String witness = genVal.genWitness("140.json");
        assertEquals(0,genVal.validateWitness("140.json",witness));
    }

    @Test
    public void generatedTest141() throws Exception {
        String witness = genVal.genWitness("141.json");
        assertEquals(0,genVal.validateWitness("141.json",witness));
    }

    @Test
    public void generatedTest142() throws Exception {
        String witness = genVal.genWitness("142.json");
        assertEquals(0,genVal.validateWitness("142.json",witness));
    }

    @Test
    public void generatedTest143() throws Exception {
        String witness = genVal.genWitness("143.json");
        assertEquals(0,genVal.validateWitness("143.json",witness));
    }

    @Test
    public void generatedTest144() throws Exception {
        String witness = genVal.genWitness("144.json");
        assertEquals(0,genVal.validateWitness("144.json",witness));
    }

    @Test
    public void generatedTest145() throws Exception {
        String witness = genVal.genWitness("145.json");
        assertEquals(0,genVal.validateWitness("145.json",witness));
    }

    @Test
    public void generatedTest146() throws Exception {
        String witness = genVal.genWitness("146.json");
        assertEquals(0,genVal.validateWitness("146.json",witness));
    }

    @Test
    public void generatedTest147() throws Exception {
        String witness = genVal.genWitness("147.json");
        assertEquals(0,genVal.validateWitness("147.json",witness));
    }

    @Test
    public void generatedTest148() throws Exception {
        String witness = genVal.genWitness("148.json");
        assertEquals(0,genVal.validateWitness("148.json",witness));
    }

    @Test
    public void generatedTest149() throws Exception {
        String witness = genVal.genWitness("149.json");
        assertEquals(0,genVal.validateWitness("149.json",witness));
    }

    @Test
    public void generatedTest150() throws Exception {
        String witness = genVal.genWitness("150.json");
        assertEquals(0,genVal.validateWitness("150.json",witness));
    }

    @Test
    public void generatedTest151() throws Exception {
        String witness = genVal.genWitness("151.json");
        assertEquals(0,genVal.validateWitness("151.json",witness));
    }

    @Test
    public void generatedTest152() throws Exception {
        String witness = genVal.genWitness("152.json");
        assertEquals(0,genVal.validateWitness("152.json",witness));
    }

    @Test
    public void generatedTest153() throws Exception {
        String witness = genVal.genWitness("153.json");
        assertEquals(0,genVal.validateWitness("153.json",witness));
    }

    @Test
    public void generatedTest154() throws Exception {
        String witness = genVal.genWitness("154.json");
        assertEquals(0,genVal.validateWitness("154.json",witness));
    }

    @Test
    public void generatedTest155() throws Exception {
        String witness = genVal.genWitness("155.json");
        assertEquals(0,genVal.validateWitness("155.json",witness));
    }

    @Test
    public void generatedTest156() throws Exception {
        String witness = genVal.genWitness("156.json");
        assertEquals(0,genVal.validateWitness("156.json",witness));
    }

    @Test
    public void generatedTest157() throws Exception {
        String witness = genVal.genWitness("157.json");
        assertEquals(0,genVal.validateWitness("157.json",witness));
    }

    @Test
    public void generatedTest158() throws Exception {
        String witness = genVal.genWitness("158.json");
        assertEquals(0,genVal.validateWitness("158.json",witness));
    }

    @Test
    public void generatedTest159() throws Exception {
        String witness = genVal.genWitness("159.json");
        assertEquals(0,genVal.validateWitness("159.json",witness));
    }

    @Test
    public void generatedTest160() throws Exception {
        String witness = genVal.genWitness("160.json");
        assertEquals(0,genVal.validateWitness("160.json",witness));
    }

    @Test
    public void generatedTest161() throws Exception {
        String witness = genVal.genWitness("161.json");
        assertEquals(0,genVal.validateWitness("161.json",witness));
    }

    @Test
    public void generatedTest162() throws Exception {
        String witness = genVal.genWitness("162.json");
        assertEquals(0,genVal.validateWitness("162.json",witness));
    }

    @Test
    public void generatedTest163() throws Exception {
        String witness = genVal.genWitness("163.json");
        assertEquals(0,genVal.validateWitness("163.json",witness));
    }

    @Test
    public void generatedTest164() throws Exception {
        String witness = genVal.genWitness("164.json");
        assertEquals(0,genVal.validateWitness("164.json",witness));
    }

    @Test
    public void generatedTest165() throws Exception {
        String witness = genVal.genWitness("165.json");
        assertEquals(0,genVal.validateWitness("165.json",witness));
    }

    @Test
    public void generatedTest166() throws Exception {
        String witness = genVal.genWitness("166.json");
        assertEquals(0,genVal.validateWitness("166.json",witness));
    }

    @Test
    public void generatedTest167() throws Exception {
        String witness = genVal.genWitness("167.json");
        assertEquals(0,genVal.validateWitness("167.json",witness));
    }

    @Test
    public void generatedTest168() throws Exception {
        String witness = genVal.genWitness("168.json");
        assertEquals(0,genVal.validateWitness("168.json",witness));
    }

    @Test
    public void generatedTest169() throws Exception {
        String witness = genVal.genWitness("169.json");
        assertEquals(0,genVal.validateWitness("169.json",witness));
    }

    @Test
    public void generatedTest170() throws Exception {
        String witness = genVal.genWitness("170.json");
        assertEquals(0,genVal.validateWitness("170.json",witness));
    }

    @Test
    public void generatedTest171() throws Exception {
        String witness = genVal.genWitness("171.json");
        assertEquals(0,genVal.validateWitness("171.json",witness));
    }

    @Test
    public void generatedTest172() throws Exception {
        String witness = genVal.genWitness("172.json");
        assertEquals(0,genVal.validateWitness("172.json",witness));
    }

    @Test
    public void generatedTest173() throws Exception {
        String witness = genVal.genWitness("173.json");
        assertEquals(0,genVal.validateWitness("173.json",witness));
    }

    @Test
    public void generatedTest174() throws Exception {
        String witness = genVal.genWitness("174.json");
        assertEquals(0,genVal.validateWitness("174.json",witness));
    }

    @Test
    public void generatedTest175() throws Exception {
        String witness = genVal.genWitness("175.json");
        assertEquals(0,genVal.validateWitness("175.json",witness));
    }

    @Test
    public void generatedTest176() throws Exception {
        String witness = genVal.genWitness("176.json");
        assertEquals(0,genVal.validateWitness("176.json",witness));
    }

    @Test
    public void generatedTest177() throws Exception {
        String witness = genVal.genWitness("177.json");
        assertEquals(0,genVal.validateWitness("177.json",witness));
    }

    @Test
    public void generatedTest178() throws Exception {
        String witness = genVal.genWitness("178.json");
        assertEquals(0,genVal.validateWitness("178.json",witness));
    }

    @Test
    public void generatedTest179() throws Exception {
        String witness = genVal.genWitness("179.json");
        assertEquals(0,genVal.validateWitness("179.json",witness));
    }

    @Test
    public void generatedTest180() throws Exception {
        String witness = genVal.genWitness("180.json");
        assertEquals(0,genVal.validateWitness("180.json",witness));
    }

    @Test
    public void generatedTest181() throws Exception {
        String witness = genVal.genWitness("181.json");
        assertEquals(0,genVal.validateWitness("181.json",witness));
    }

    @Test
    public void generatedTest182() throws Exception {
        String witness = genVal.genWitness("182.json");
        assertEquals(0,genVal.validateWitness("182.json",witness));
    }

    @Test
    public void generatedTest183() throws Exception {
        String witness = genVal.genWitness("183.json");
        assertEquals(0,genVal.validateWitness("183.json",witness));
    }

    @Test
    public void generatedTest184() throws Exception {
        String witness = genVal.genWitness("184.json");
        assertEquals(0,genVal.validateWitness("184.json",witness));
    }

    @Test
    public void generatedTest185() throws Exception {
        String witness = genVal.genWitness("185.json");
        assertEquals(0,genVal.validateWitness("185.json",witness));
    }

    @Test
    public void generatedTest186() throws Exception {
        String witness = genVal.genWitness("186.json");
        assertEquals(0,genVal.validateWitness("186.json",witness));
    }

    @Test
    public void generatedTest187() throws Exception {
        String witness = genVal.genWitness("187.json");
        assertEquals(0,genVal.validateWitness("187.json",witness));
    }

    @Test
    public void generatedTest188() throws Exception {
        String witness = genVal.genWitness("188.json");
        assertEquals(0,genVal.validateWitness("188.json",witness));
    }

    @Test
    public void generatedTest189() throws Exception {
        String witness = genVal.genWitness("189.json");
        assertEquals(0,genVal.validateWitness("189.json",witness));
    }

    @Test
    public void generatedTest190() throws Exception {
        String witness = genVal.genWitness("190.json");
        assertEquals(0,genVal.validateWitness("190.json",witness));
    }

    @Test
    public void generatedTest191() throws Exception {
        String witness = genVal.genWitness("191.json");
        assertEquals(0,genVal.validateWitness("191.json",witness));
    }

    @Test
    public void generatedTest192() throws Exception {
        String witness = genVal.genWitness("192.json");
        assertEquals(0,genVal.validateWitness("192.json",witness));
    }

    @Test
    public void generatedTest193() throws Exception {
        String witness = genVal.genWitness("193.json");
        assertEquals(0,genVal.validateWitness("193.json",witness));
    }

    @Test
    public void generatedTest194() throws Exception {
        String witness = genVal.genWitness("194.json");
        assertEquals(0,genVal.validateWitness("194.json",witness));
    }

    @Test
    public void generatedTest195() throws Exception {
        String witness = genVal.genWitness("195.json");
        assertEquals(0,genVal.validateWitness("195.json",witness));
    }

    @Test
    public void generatedTest196() throws Exception {
        String witness = genVal.genWitness("196.json");
        assertEquals(0,genVal.validateWitness("196.json",witness));
    }

    @Test
    public void generatedTest197() throws Exception {
        String witness = genVal.genWitness("197.json");
        assertEquals(0,genVal.validateWitness("197.json",witness));
    }

    @Test
    public void generatedTest198() throws Exception {
        String witness = genVal.genWitness("198.json");
        assertEquals(0,genVal.validateWitness("198.json",witness));
    }

    @Test
    public void generatedTest199() throws Exception {
        String witness = genVal.genWitness("199.json");
        assertEquals(0,genVal.validateWitness("199.json",witness));
    }

    @Test
    public void generatedTest200() throws Exception {
        String witness = genVal.genWitness("200.json");
        assertEquals(0,genVal.validateWitness("200.json",witness));
    }

    @Test
    public void generatedTest201() throws Exception {
        String witness = genVal.genWitness("201.json");
        assertEquals(0,genVal.validateWitness("201.json",witness));
    }

    @Test
    public void generatedTest202() throws Exception {
        String witness = genVal.genWitness("202.json");
        assertEquals(0,genVal.validateWitness("202.json",witness));
    }

    @Test
    public void generatedTest203() throws Exception {
        String witness = genVal.genWitness("203.json");
        assertEquals(0,genVal.validateWitness("203.json",witness));
    }

    @Test
    public void generatedTest204() throws Exception {
        String witness = genVal.genWitness("204.json");
        assertEquals(0,genVal.validateWitness("204.json",witness));
    }

    @Test
    public void generatedTest205() throws Exception {
        String witness = genVal.genWitness("205.json");
        assertEquals(0,genVal.validateWitness("205.json",witness));
    }

    @Test
    public void generatedTest206() throws Exception {
        String witness = genVal.genWitness("206.json");
        assertEquals(0,genVal.validateWitness("206.json",witness));
    }

    @Test
    public void generatedTest207() throws Exception {
        String witness = genVal.genWitness("207.json");
        assertEquals(0,genVal.validateWitness("207.json",witness));
    }

    @Test
    public void generatedTest208() throws Exception {
        String witness = genVal.genWitness("208.json");
        assertEquals(0,genVal.validateWitness("208.json",witness));
    }

    @Test
    public void generatedTest209() throws Exception {
        String witness = genVal.genWitness("209.json");
        assertEquals(0,genVal.validateWitness("209.json",witness));
    }

    @Test
    public void generatedTest210() throws Exception {
        String witness = genVal.genWitness("210.json");
        assertEquals(0,genVal.validateWitness("210.json",witness));
    }

    @Test
    public void generatedTest211() throws Exception {
        String witness = genVal.genWitness("211.json");
        assertEquals(0,genVal.validateWitness("211.json",witness));
    }

    @Test
    public void generatedTest212() throws Exception {
        String witness = genVal.genWitness("212.json");
        assertEquals(0,genVal.validateWitness("212.json",witness));
    }

    @Test
    public void generatedTest213() throws Exception {
        String witness = genVal.genWitness("213.json");
        assertEquals(0,genVal.validateWitness("213.json",witness));
    }

    @Test
    public void generatedTest214() throws Exception {
        String witness = genVal.genWitness("214.json");
        assertEquals(0,genVal.validateWitness("214.json",witness));
    }

    @Test
    public void generatedTest215() throws Exception {
        String witness = genVal.genWitness("215.json");
        assertEquals(0,genVal.validateWitness("215.json",witness));
    }
}
