package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import de.uni_passau.sds.patterns.REException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JsonSchemaTestSuiteDraft6WitnessGen;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import org.junit.Ignore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestTrickyNewProps {

    private GenAndValidate genJSchemaVal =
            new GenAndValidate(System.getProperty("user.dir")+"/expDataset/handwritten/testtrickynew/",false);

    void testValid(String filename) throws WitnessException, IOException, REException {
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }

    void testEmpty(String filename) throws WitnessException, IOException, REException {
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals("{\"Problem\":\"Empty witness\"}",witness);
    }


    @Test
    public void emptynotnames3() throws WitnessException, IOException, REException {
        testEmpty("emptynotnames3.json");
    }
    
    @Test
    public void emptynotnames8() throws WitnessException, IOException, REException {
        testEmpty("emptynotnames8.json");
    }
    
    @Test
    public void emptyoneof10() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof10.json");
    }
    
    @Test
    public void emptyoneof11() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof11.json");
    }
    
    @Test
    public void emptyoneof12() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof12.json");
    }
    
    @Test
    public void emptyoneof13() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof13.json");
    }
    
    @Test
    public void emptyoneof14() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof14.json");
    }
    
    @Test
    public void emptyoneof15() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof15.json");
    }
    
    @Test
    public void emptyoneof16() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof16.json");
    }
    
    @Test
    public void emptyoneof17() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof17.json");
    }
    
    @Test
    public void emptyoneof18() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof18.json");
    }
    
    @Test
    public void emptyoneof7() throws WitnessException, IOException, REException {
        testEmpty("emptyoneof7.json");
    }
    
    @Test
    public void emptyrealworld12() throws WitnessException, IOException, REException {
        testEmpty("emptyrealworld12.json");
    }
    
    @Test
    public void emptyrealworld13() throws WitnessException, IOException, REException {
        testEmpty("emptyrealworld13.json");
    }
    
    @Test
    public void emptyrealworld14() throws WitnessException, IOException, REException {
        testEmpty("emptyrealworld14.json");
    }
    
    @Test
    public void emptyrealworld23() throws WitnessException, IOException, REException {
        testEmpty("emptyrealworld23.json");
    }
    
    @Test
    public void emptyrealworld24() throws WitnessException, IOException, REException {
        testEmpty("emptyrealworld24.json");
    }
    
    @Test
    public void emptyrealworld34() throws WitnessException, IOException, REException {
        testEmpty("emptyrealworld34.json");
    }
    
    @Test
    public void notnames1() throws WitnessException, IOException, REException {
        testValid("notnames1.json");
    }
    
    @Ignore
    //takes one minute
    public void notnames10() throws WitnessException, IOException, REException {
        testValid("notnames10.json");
    }
    
    @Test
    public void notnames2() throws WitnessException, IOException, REException {
        testValid("notnames2.json");
    }
    
    @Test
    public void notnames4() throws WitnessException, IOException, REException {
        testValid("notnames4.json");
    }
    
    @Test
    public void notnames5() throws WitnessException, IOException, REException {
        testValid("notnames5.json");
    }
    
    @Test
    public void notnames6() throws WitnessException, IOException, REException {
        testValid("notnames6.json");
    }
    
    @Test
    public void notnames7() throws WitnessException, IOException, REException {
        testValid("notnames7.json");
    }
    
    @Test
    public void notnames9() throws WitnessException, IOException, REException {
        testValid("notnames9.json");
    }
    
    @Test
    public void object1() throws WitnessException, IOException, REException {
        testValid("object1.json");
    }
    
    @Test
    public void object10() throws WitnessException, IOException, REException {
        testValid("object10.json");
    }
    
    @Test
    public void object2() throws WitnessException, IOException, REException {
        testValid("object2.json");
    }
    
    @Test
    public void object3() throws WitnessException, IOException, REException {
        testValid("object3.json");
    }
    
    @Test
    public void object4() throws WitnessException, IOException, REException {
        testValid("object4.json");
    }
    
    @Test
    public void object5() throws WitnessException, IOException, REException {
        testValid("object5.json");
    }
    
    @Test
    public void object6() throws WitnessException, IOException, REException {
        testValid("object6.json");
    }
    
    @Test
    public void object7() throws WitnessException, IOException, REException {
        testValid("object7.json");
    }
    
    @Test
    public void object8() throws WitnessException, IOException, REException {
        testValid("object8.json");
    }
    
    @Ignore
    //takes VERY long time
    public void object9() throws WitnessException, IOException, REException {
        testValid("object9.json");
    }
    
    @Test
    public void oneof10() throws WitnessException, IOException, REException {
        testValid("oneof10.json");
    }
    
    @Test
    public void oneof11() throws WitnessException, IOException, REException {
        testValid("oneof11.json");
    }
    
    @Test
    public void oneof12() throws WitnessException, IOException, REException {
        testValid("oneof12.json");
    }
    
    @Test
    public void oneof13() throws WitnessException, IOException, REException {
        testValid("oneof13.json");
    }
    
    @Test
    public void oneof5() throws WitnessException, IOException, REException {
        testValid("oneof5.json");
    }
    
    @Test
    public void oneof6() throws WitnessException, IOException, REException {
        testValid("oneof6.json");
    }
    
    @Test
    public void oneof8() throws WitnessException, IOException, REException {
        testValid("oneof8.json");
    }
    
    @Test
    public void oneof9() throws WitnessException, IOException, REException {
        testValid("oneof9.json");
    }
    
    @Test
    public void oneofpr1() throws WitnessException, IOException, REException {
        testValid("oneofpr1.json");
    }
    
    @Test
    public void oneofpr2() throws WitnessException, IOException, REException {
        testValid("oneofpr2.json");
    }
    
    @Test
    public void oneofpr3() throws WitnessException, IOException, REException {
        testValid("oneofpr3.json");
    }
    
    @Test
    public void oneofpr4() throws WitnessException, IOException, REException {
        testValid("oneofpr4.json");
    }
    
    @Test
    public void oneofpr5() throws WitnessException, IOException, REException {
        testValid("oneofpr5.json");
    }
    
    @Test
    public void oneofpr6() throws WitnessException, IOException, REException {
        testValid("oneofpr6.json");
    }
    
    @Test
    public void oneofpr7() throws WitnessException, IOException, REException {
        testValid("oneofpr7.json");
    }
    
    @Test
    public void oneofpr8() throws WitnessException, IOException, REException {
        testValid("oneofpr8.json");
    }
    
    @Ignore
    //slow
    public void testwp9() throws WitnessException, IOException, REException {
        testValid("testwp9.json");
    }
    
    @Ignore
    //very long
    public void testwp98() throws WitnessException, IOException, REException {
        testValid("testwp98.json");
    }
    
}
