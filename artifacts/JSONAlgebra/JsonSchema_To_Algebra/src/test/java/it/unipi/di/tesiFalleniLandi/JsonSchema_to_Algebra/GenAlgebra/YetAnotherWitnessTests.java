package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.lib.IJsonSchemaLib;
import de.uni_passau.sds.lib.JsonSchemaLibFactory;
import de.uni_passau.sds.lib.schemaComparism.JsonSchemaComparison;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Helpers.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class YetAnotherWitnessTests {

    private final String noWitness = "{\"Problem\":\"Empty witness\"}";
    private static final String PATH = System.getProperty("user.dir") + "/witnessGenTestfiles/yetAnotherWitness/";

    private final IJsonSchemaLib libFactory = JsonSchemaLibFactory.getJsonSchemaLib();
    private JsonSchemaComparison data = new JsonSchemaComparison();

    //first witness = 0
    //Invalid Witness -10 is generated for second witness
    @Test
    public void reflexive_multipleOf_id4() throws Exception {
        String schema = FileUtils.fileToSchema("reflexive_multipleOf_id4.json", PATH);
        Collection<String> prevWits = new ArrayList<>();

        Optional<String> witness = libFactory.generateWitness(schema);
        prevWits.add(witness.get());

        Optional<String> scondWit = libFactory.generateWitness(schema, prevWits);

        //0.123456789 * 1 000 000 000 = 123 456 789 --> next Witness
        String expected = "123456789";

        Assert.assertEquals(expected, scondWit.get());
    }
}
