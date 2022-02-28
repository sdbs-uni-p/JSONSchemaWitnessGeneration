package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.lib.IJsonSchemaLib;
import de.uni_passau.sds.lib.JsonSchemaLibFactory;
import de.uni_passau.sds.lib.exceptions.InvalidWitnessException;
import de.uni_passau.sds.lib.exceptions.WitnessGenerationException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Helpers.FileUtils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import de.uni_passau.sds.patterns.REException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * The purpose of this class is to verify whether the **second** Witness is correct.
 * Therefore, all tests generate a second/another witness.
 */
public class GenAnotherWitness {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private final String PATH = System.getProperty("user.dir") +
            "/witnessGenTestfiles/anotherWitness/";

    private GenAndValidate genVal = new GenAndValidate(PATH);
    private final IJsonSchemaLib jsonSchemaLibFactory = JsonSchemaLibFactory.getJsonSchemaLib();

    @Test
    @Tag("deployment")
    public void test1() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("1.json");
        assertEquals(noWitness, witness);
    }

    @Test
    @Tag("deployment")
    // Witnesses for {}
    public void test2() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("2.json");
        assertEquals(0, genVal.validateWitness("2.json", witness));
    }

    @Test
    // Witnesses for {}
    public void test3() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("3.json");
        assertEquals(0, genVal.validateWitness("3.json", witness));
    }

    @Test
    // Witnesses for {}
    public void test4() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("4.json");
        assertEquals(0, genVal.validateWitness("4.json", witness));
    }

    @Test
    @Tag("deployment")
    // Witnesses for {}
    public void test5() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("5.json");
        assertEquals(0, genVal.validateWitness("5.json", witness));
    }

    @Test
    @Tag("deployment")
    // Witness for {"allOf":[{},{"not": {"const":123}},{"not": {"const":null}},{"not": {"const":{}}},{"not": {"const":{"0":123}}},{"not": {"const":{"0":123,"1":123}}},{"not": {"const":{"0":123,"1":123,"2":123}}}]}

    // throws index out of Bounds Exception in GenObject.java
    // --> Method: private List<List<GPattReq>> hittingSet(List<GPattReq> requests, Integer from, List<GOrPattReq> orpList)
    // the result is null --> throws IllegalArgumentException
    public void test6() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("6.json");
        assertEquals(0, genVal.validateWitness("6.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test7() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("7.json");
        assertEquals(0, genVal.validateWitness("7.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test8() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("8.json");
        assertEquals(0, genVal.validateWitness("8.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test9() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("9.json");
        assertEquals(0, genVal.validateWitness("9.json", witness));
    }

    @Test
    //@Tag("deployment")
    public void test10() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("10.json");
        assertEquals(0, genVal.validateWitness("10.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test11() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("11.json");
        assertEquals(0, genVal.validateWitness("11.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test12() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("12.json");
        assertEquals(0, genVal.validateWitness("12.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test13() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("13.json");
        assertEquals(0, genVal.validateWitness("13.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test14() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("14.json");
        assertEquals(0, genVal.validateWitness("14.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test15() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("15.json");
        assertEquals(0, genVal.validateWitness("15.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test16() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("16.json");
        assertEquals(0, genVal.validateWitness("16.json", witness));
    }

    @Test
    //@Tag("deployment") --> unknown keyword $id / const
    public void test17() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("17.json");
        assertEquals(0, genVal.validateWitness("17.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test18() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("18.json");
        assertEquals(0, genVal.validateWitness("18.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test19() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("19.json");
        assertEquals(0, genVal.validateWitness("19.json", witness));
    }

    @Test
    public void test20() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("20.json");
        assertEquals(0, genVal.validateWitness("20.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test21() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("21.json");
        assertEquals(0, genVal.validateWitness("21.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test22() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("22.json");
        assertEquals(0, genVal.validateWitness("22.json", witness));
    }

    @Test
    //@Tag("deployment")
    public void test23() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("23.json");
        assertEquals(0, genVal.validateWitness("23.json", witness));
    }

    @Test
    //@Tag("deployment")
    public void test24() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("24.json");
        assertEquals(0, genVal.validateWitness("24.json", witness));
    }

    //TODO: Flanky Test
    @Test
    //@Tag("deployment")
    public void test25() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("25.json");
        assertEquals(0, genVal.validateWitness("25.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test26() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("26.json");
        assertEquals(0, genVal.validateWitness("26.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test27() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("27.json");
        assertEquals(0, genVal.validateWitness("27.json", witness));
    }

    @Test
    //@Tag("deployment") --> does not work in mvn install
    // Extension of test 27.
    public void test27b() throws WitnessException, IOException, REException {
        for (int i = 0; i < 10; i++) {
            String witness = genVal.genWitness("27b.json");
            assertEquals(0, genVal.validateWitness("27b.json", witness));
        }
    }

    @Test
    @Tag("deployment")
    public void test28() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("28.json");
        assertEquals(0, genVal.validateWitness("28.json", witness));
    }

    @Test
    //  TODO Witness not valid. Should be integer but is double.
    public void test29() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("29.json");
        assertEquals(0, genVal.validateWitness("29.json", witness));
    }


    @Test
    //  Variation of test29.
    public void test29b() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("29b.json");
        assertEquals(0, genVal.validateWitness("29b.json", witness));
    }

    @Test
    @Tag("deployment")
    // from: JSON-Schema-Test-Suite/tests/draft2020-12/items.json
    public void test30() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("30.json");
        assertEquals(0, genVal.validateWitness("30.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test31() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("31.json");
        assertEquals(0, genVal.validateWitness("31.json", witness));
    }

    @Test
    //  TODO required attributes are not in the generated witness but should be
    public void test32() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("32.json");
        assertEquals(0, genVal.validateWitness("32.json", witness));
    }

    @Test
    //  TODO returns empty witness 
    public void test33() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("33.json");
        assertEquals(0, genVal.validateWitness("33.json", witness));
    }

    @Test
    //@Tag("deployment") --> does not work in mvn install
    public void test34() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("34.json");
        assertEquals(0, genVal.validateWitness("34.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test35() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("35.json");
        assertEquals(0, genVal.validateWitness("35.json", witness));
    }

    @Test
    public void test36() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("36.json");
        assertEquals(0, genVal.validateWitness("36.json", witness));
    }

    @Test
    // TODO Problem with spaces in String
    public void test37() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("37.json");
        assertEquals(0, genVal.validateWitness("37.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test38() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("38.json");
        assertEquals(0, genVal.validateWitness("38.json", witness));
    }

    //TODO generates empty witnesses but is not, due to AssertionError
    @Test
    public void test39() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("39.json");
        assertEquals(0, genVal.validateWitness("39.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test40() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("40.json");
        assertEquals(0, genVal.validateWitness("40.json", witness));
    }

    @Test
    //@Tag("deployment")
    public void test41() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("41.json");
        assertEquals(0, genVal.validateWitness("41.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test42() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("42.json");
        assertEquals(0, genVal.validateWitness("42.json", witness));
    }

    //TODO: AssertionError
    @Test
    public void test43() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("43.json");
        assertEquals(0, genVal.validateWitness("43.json", witness));
    }

    //TODO: AssertionError
    @Test
    public void test44() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("44.json");
        assertEquals(0, genVal.validateWitness("44.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test45() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("45.json");
        assertEquals(0, genVal.validateWitness("45.json", witness));
    }

    //Test if second witness is empty witness
    @Test
    public void test45b() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        Collection<String> prevWits = new ArrayList<>();
        String schema = FileUtils.fileToSchema(PATH, "45.json");
        String witness = genVal.genWitness("45.json");

        prevWits.add(witness);

        Optional<String> secondWitness = jsonSchemaLibFactory.generateWitness(schema, prevWits);
        assertEquals(true, secondWitness.isEmpty());
    }

    @Test
    @Tag("deployment")
    public void test46() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("46.json");
        assertEquals(noWitness, witness);
    }

    //Unsatisfiable
    @Test
    @Tag("deployment")
    public void test47() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("47.json");
        assertEquals(noWitness, witness);
    }

    //Unsatisfiable
    @Test
    @Tag("deployment")
    public void test48() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("48.json");
        assertEquals(noWitness, witness);
    }

    @Test
    @Tag("deployment")
    public void test49() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("49.json");
        assertEquals(0, genVal.validateWitness("49.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test50() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("50.json");
        assertEquals(0, genVal.validateWitness("50.json", witness));
    }

    @Test
    @Tag("deployment")
    public void test51() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("51.json");
        assertEquals(0, genVal.validateWitness("51.json", witness));
    }

    @Test
    public void LALE_Schemas52() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        String witness = genVal.genWitness("52.json");
        assertEquals(0, genVal.validateWitness("52.json", witness));
    }

    @Test
    public void LALE_Schemas53() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        String witness = genVal.genWitness("53.json");
        assertEquals(0, genVal.validateWitness("53.json", witness));
    }

    //TODO: regression problems
    @Test
    @Tag("deployment")
    public void valid_propertyNames_id1() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        String witness = genVal.genWitness("54.json");
        assertEquals(noWitness, witness);
    }

    //TODO: regression problems
    @Test
    @Tag("deployment")
    public void valid_propertyNames_id7() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        String witness = genVal.genWitness("55.json");
        assertEquals(noWitness, witness);
    }

    @Test
    public void test56() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        String witness = genVal.genWitness("56.json");
        assertEquals(0, genVal.validateWitness("56.json", witness));
    }

    @Test
    public void test57() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        String witness = genVal.genWitness("57.json");
        assertEquals(0, genVal.validateWitness("57.json", witness));
    }

    @Test
    public void test58() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        String witness = genVal.genWitness("58.json");
        assertEquals(0, genVal.validateWitness("58.json", witness));
    }

    @Test
    public void test59() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("59.json");
        assertEquals(0, genVal.validateWitness("59.json", witness));
    }

   @Test
    public void test60() throws WitnessException, IOException, REException {
        String witness = genVal.genWitness("60.json");
        assertEquals(0, genVal.validateWitness("60.json", witness));
    }

    @Test
    public void test61() throws WitnessException, IOException, REException, InvalidWitnessException, WitnessGenerationException {
        String schema = FileUtils.fileToSchema(PATH, "61.json");
        Collection<String> prevWits = new ArrayList<>();

        Optional<String> witness = jsonSchemaLibFactory.generateWitness(schema);
        prevWits.add(witness.get());

        System.out.println(witness.get());
        for (int i = 0; i < 10; i++) {

            var newWitness = jsonSchemaLibFactory.generateWitness(schema, prevWits);
            if (newWitness.isEmpty()){
            } else {


                System.out.println(newWitness);

                prevWits.add(newWitness.get());
            }
        }
    }
}