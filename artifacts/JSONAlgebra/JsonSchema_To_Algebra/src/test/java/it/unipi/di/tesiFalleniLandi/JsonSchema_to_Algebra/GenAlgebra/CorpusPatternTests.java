package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * The following tests are from https://github.com/sdbs-uni-p/json-schema-corpus/tree/main/json_schema_corpus
 * Last commit: 9c0e7963559c6c632694d5851c081662178ba70b
 *
 * To find the json file follow the link and change #schema to the corresponding file you are searching for:
 * https://github.com/sdbs-uni-p/json-schema-corpus/blob/main/json_schema_corpus/#schema
 *
 * @author Luca Escher
 */
public class CorpusPatternTests {

    @Test
    public void test() throws REException, WitnessException, IOException {
        GenAndValidate genVal = new GenAndValidate("C:\\Users\\chris\\IdeaProjects\\json-schema-corpus\\json_schema_corpus\\");
        int file = 17571000;
        long start = System.currentTimeMillis();
        String witness = genVal.genWitness("pp_" + file + ".json");
        long end = System.currentTimeMillis();
        System.out.println("Generation time: " + (end - start));
        int valid = genVal.validateWitness("pp_" + file + ".json", witness);
        System.out.println("Valid: " + valid);
        assertEquals(0, valid);
    }

    @Test
    public void test0() {
        GenAndValidate genVal = new GenAndValidate("C:\\Users\\chris\\IdeaProjects\\json-schema-corpus\\json_schema_corpus\\");
        int[] files = {9790, 9803, 9896, 9967, 17571, 21053, 26958, 35154, 36516, 48761, 76551};
        long[] genTimes = new long[files.length];
        int[] valid = new int[files.length];
        int i = 0;
        for (int file : files) {
            try {
                long start = System.currentTimeMillis();
                String witness = genVal.genWitness("pp_" + file + ".json");
                long end = System.currentTimeMillis();
                genTimes[i] = end - start;
                try {
                    valid[i] = genVal.validateWitness("pp_" + file + ".json", witness);
                } catch (Throwable e) {
                    valid[i] = -1;
                }
            } catch (Throwable e) {
                genTimes[i] = -1;
            }
            i++;
        }
        System.out.println("Generation times: " + Arrays.toString(genTimes));
        System.out.println("Valid: " + Arrays.toString(valid));
        for (int j : valid) {
            assertEquals(0, j);
        }
    }

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private GenAndValidate genVal = new GenAndValidate(System.getProperty("user.dir") +
            "/testsCorpus/patternTests/");

    //The following tests from 1 to 56 are tests for patterns.
    //Note that Patterns with Look ahead will be ignored!

    // #schema --> pp_77414.json
    @Test
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(0, genVal.validateWitness("1.json", witness));
    }

    // #schema --> pp_21828.json
    @Test
    public void test2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2.json");
        assertEquals(0, genVal.validateWitness("2.json", witness));
    }

    // #schema --> pp_20376.json
    @Test
    public void test3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("3.json");
        assertEquals(0, genVal.validateWitness("3.json", witness));
    }

    // #schema --> pp_41049.json
    @Test
    public void test4() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("4.json");
        assertEquals(0, genVal.validateWitness("4.json", witness));
    }

    // #schema --> pp_64546.json
    @Test
    public void test5() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("5.json");
        assertEquals(0, genVal.validateWitness("5.json", witness));
    }

    // #schema --> pp_51224.json
    @Test
    public void test6() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("6.json");
        assertEquals(0, genVal.validateWitness("6.json", witness));
    }

    // #schema --> pp_40230.json
    @Test
    public void test7() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("7.json");
        assertEquals(0, genVal.validateWitness("7.json", witness));
    }

    // #schema --> pp_70376.json
    @Test
    public void test8() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("8.json");
        assertEquals(0, genVal.validateWitness("8.json", witness));
    }

    // #schema --> pp_69997.json
    @Test
    public void test9() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("9.json");
        assertEquals(0, genVal.validateWitness("9.json", witness));
    }

    // #schema --> pp_41033.json
    @Test
    public void test10() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("10.json");
        assertEquals(0, genVal.validateWitness("10.json", witness));
    }

    // #schema --> pp_81526.json
    @Test
    public void test11() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("11.json");
        assertEquals(0, genVal.validateWitness("11.json", witness));
    }

    // #schema --> pp_81530.json
    @Test
    public void test12() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("12.json");
        assertEquals(0, genVal.validateWitness("12.json", witness));
    }

    // #schema --> pp_81532.json
    @Test
    public void test13() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("13.json");
        assertEquals(0, genVal.validateWitness("13.json", witness));
    }

    // #schema --> pp_43291.json
    @Test
    public void test14() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("14.json");
        assertEquals(0, genVal.validateWitness("14.json", witness));
    }

    // #schema --> pp_81533.json
    // NullPointer
    @Test
    public void test15() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("15.json");
        assertEquals(0, genVal.validateWitness("15.json", witness));
    }

    // #schema --> pp_81531.json
    @Test
    public void test16() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("16.json");
        assertEquals(0, genVal.validateWitness("16.json", witness));
    }

    // #schema --> pp_28226.json
    @Test
    public void test17() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("17.json");
        assertEquals(0, genVal.validateWitness("17.json", witness));
    }

    // #schema --> pp_78060.json
    @Test
    public void test18() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("18.json");
        assertEquals(0, genVal.validateWitness("18.json", witness));
    }

    // #schema --> pp_21149.json
    @Test
    public void test19() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("19.json");
        assertEquals(0, genVal.validateWitness("19.json", witness));
    }

    // #schema --> pp_21848.json
    @Test
    public void test20() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("20.json");
        assertEquals(0, genVal.validateWitness("20.json", witness));
    }

    // #schema --> pp_90328.json
    @Test
    public void test21() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("21.json");
        assertEquals(0, genVal.validateWitness("21.json", witness));
    }

    // #schema --> pp_50680.json
    @Test
    public void test22() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("22.json");
        assertEquals(0, genVal.validateWitness("22.json", witness));
    }

    // #schema --> pp_63937.json
    @Test
    public void test23() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("23.json");
        assertEquals(0, genVal.validateWitness("23.json", witness));
    }

    // #schema --> pp_63934.json
    @Test
    public void test24() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("24.json");
        assertEquals(0, genVal.validateWitness("24.json", witness));
    }

    // #schema --> pp_21852.json
    @Test
    public void test25() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("25.json");
        assertEquals(0, genVal.validateWitness("25.json", witness));
    }

    // #schema --> pp_83394.json
    @Test
    public void test26() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("26.json");
        assertEquals(0, genVal.validateWitness("26.json", witness));
    }

    // #schema --> pp_21850.json
    @Test
    public void test27() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("27.json");
        assertEquals(0, genVal.validateWitness("27.json", witness));
    }

    // #schema --> pp_83092.json
    @Test
    public void test28() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("28.json");
        assertEquals(0, genVal.validateWitness("28.json", witness));
    }

    // #schema --> pp_1529.json
    @Test
    public void test29() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("29.json");
        assertEquals(0, genVal.validateWitness("29.json", witness));
    }

    // #schema --> pp_73816.json
    @Test
    public void test30() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("30.json");
        assertEquals(0, genVal.validateWitness("30.json", witness));
    }

    // #schema --> pp_82696.json
    @Test
    public void test31() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("31.json");
        assertEquals(0, genVal.validateWitness("31.json", witness));
    }

    // #schema --> pp_14405.json
    @Test
    public void test32() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("32.json");
        assertEquals(0, genVal.validateWitness("32.json", witness));
    }

    // #schema --> pp_369.json
    @Test
    public void test33() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("33.json");
        assertEquals(0, genVal.validateWitness("33.json", witness));
    }

    // #schema --> pp_59664.json
    @Test
    public void test34() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("34.json");
        assertEquals(0, genVal.validateWitness("34.json", witness));
    }

    // #schema --> pp_3906.json
    @Test
    public void test35() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("35.json");
        assertEquals(0, genVal.validateWitness("35.json", witness));
    }

    // #schema --> pp_82694.json
    @Test
    public void test36() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("36.json");
        assertEquals(0, genVal.validateWitness("36.json", witness));
    }

    // #schema --> pp_13655.json
    @Test
    public void test37() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("37.json");
        assertEquals(0, genVal.validateWitness("37.json", witness));
    }

    // #schema --> pp_81600.json
    @Test
    public void test38() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("38.json");
        assertEquals(0, genVal.validateWitness("38.json", witness));
    }

    // #schema --> pp_58776.json
    @Test
    public void test39() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("39.json");
        assertEquals(0, genVal.validateWitness("39.json", witness));
    }

    // #schema --> pp_11975.json
    @Test
    public void test40() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("40.json");
        assertEquals(0, genVal.validateWitness("40.json", witness));
    }

    // #schema --> pp_82710.json
    @Test
    public void test41() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("41.json");
        assertEquals(0, genVal.validateWitness("41.json", witness));
    }

    // #schema --> pp_21115.json
    @Test
    public void test42() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("42.json");
        assertEquals(0, genVal.validateWitness("42.json", witness));
    }

    // #schema --> pp_90904.json
    @Test
    public void test43() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("43.json");
        assertEquals(0, genVal.validateWitness("43.json", witness));
    }

    // #schema --> pp_32807.json
    @Test
    public void test44() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("44.json");
        assertEquals(0, genVal.validateWitness("44.json", witness));
    }

    // #schema --> pp_50681.json
    @Test
    public void test45() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("45.json");
        assertEquals(0, genVal.validateWitness("45.json", witness));
    }

    // #schema --> pp_8494.json
    @Test
    public void test46() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("46.json");
        assertEquals(0, genVal.validateWitness("46.json", witness));
    }

    // #schema --> pp_55244.json
    @Test
    public void test47() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("47.json");
        assertEquals(0, genVal.validateWitness("47.json", witness));
    }

    // #schema --> pp_71453.json
    @Test
    public void test48() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("48.json");
        assertEquals(0, genVal.validateWitness("48.json", witness));
    }

    // #schema --> pp_82864.json
    @Test
    public void test49() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("49.json");
        assertEquals(0, genVal.validateWitness("49.json", witness));
    }

    // #schema --> pp_66693.json
    //JsonMetaSchema - Error --> a lot of errors here
    @Test
    public void test50() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("50.json");
        assertEquals(0, genVal.validateWitness("50.json", witness));
    }

    // #schema --> pp_47658.json
    @Test
    public void test51() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("51.json");
        assertEquals(0, genVal.validateWitness("51.json", witness));
    }

    // #schema --> pp_61026.json
    @Test
    public void test52() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("52.json");
        assertEquals(0, genVal.validateWitness("52.json", witness));
    }

    // #schema --> pp_59215.json
    @Test
    @Ignore
    public void test53() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("53.json");
        assertEquals(0, genVal.validateWitness("53.json", witness));
    }

    // #schema --> pp_79409.json
    //StackOverflowError
    @Test
    @Ignore
    public void test54() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("54.json");
        assertEquals(0, genVal.validateWitness("54.json", witness));
    }

    // #schema --> pp_43344.json
    @Test
    @Ignore
    public void test55() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("55.json");
        assertEquals(0, genVal.validateWitness("55.json", witness));
    }

    // #schema --> pp_80324.json
    @Test
    @Ignore
    public void test56() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("56.json");
        assertEquals(0, genVal.validateWitness("56.json", witness));
    }
}
