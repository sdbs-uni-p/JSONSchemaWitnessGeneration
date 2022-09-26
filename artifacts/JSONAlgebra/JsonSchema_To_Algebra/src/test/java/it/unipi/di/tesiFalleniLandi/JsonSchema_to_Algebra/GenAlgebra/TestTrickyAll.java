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

public class TestTrickyAll {

    private GenAndValidate genJSchemaVal =
            new GenAndValidate(System.getProperty("user.dir")+"/expDataset/handwritten/testtricky/",false);

    void testValid(String filename) throws WitnessException, IOException, REException {
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals(0,genJSchemaVal.validateWitness(filename,witness));
    }

    void testEmpty(String filename) throws WitnessException, IOException, REException {
        String witness = genJSchemaVal.genWitness(filename);
        assertEquals("{\"Problem\":\"Empty witness\"}",witness);
    }


    @Test
    public void allIt1() throws WitnessException, IOException, REException {
        testValid("allIt1.json");
    }

    @Test
    public void allIt2() throws WitnessException, IOException, REException {
        testValid("allIt2.json");
    }
    
    @Test
    public void allIt3() throws WitnessException, IOException, REException {
        testValid("allIt3.json");
    }
    
    @Test
    public void allIt4() throws WitnessException, IOException, REException {
        testValid("allIt4.json");
    }
    
    @Test
    public void allIt5() throws WitnessException, IOException, REException {
        testValid("allIt5.json");
    }
    
    @Test
    public void allIt6() throws WitnessException, IOException, REException {
        testValid("allIt6.json");
    }
    
    @Test
    public void allIt7() throws WitnessException, IOException, REException {
        testValid("allIt7.json");
    }
    
    @Test
    public void allIt8() throws WitnessException, IOException, REException {
        testValid("allIt8.json");
    }
    
    @Test
    public void allOfPProp1() throws WitnessException, IOException, REException {
        testValid("allOfPProp1.json");
    }
    
    @Test
    public void allOfPProp10() throws WitnessException, IOException, REException {
        testValid("allOfPProp10.json");
    }
    
    @Test
    public void allOfPProp2() throws WitnessException, IOException, REException {
        testValid("allOfPProp2.json");
    }
    
    @Test
    public void allOfPProp3() throws WitnessException, IOException, REException {
        testValid("allOfPProp3.json");
    }
    
    @Test
    public void allOfPProp4() throws WitnessException, IOException, REException {
        testValid("allOfPProp4.json");
    }
    
    @Test
    public void allOfPProp5() throws WitnessException, IOException, REException {
        testValid("allOfPProp5.json");
    }
    
    @Test
    public void allOfPProp6() throws WitnessException, IOException, REException {
        testValid("allOfPProp6.json");
    }
    
    @Test
    public void allOfPProp7() throws WitnessException, IOException, REException {
        testValid("allOfPProp7.json");
    }
    
    @Test
    public void allOfPProp8() throws WitnessException, IOException, REException {
        testValid("allOfPProp8.json");
    }
    
    @Test
    public void allOfPProp9() throws WitnessException, IOException, REException {
        testValid("allOfPProp9.json");
    }
    
    @Test
    public void dep1() throws WitnessException, IOException, REException {
        testValid("dep1.json");
    }
    
    @Test
    public void dep10() throws WitnessException, IOException, REException {
        testValid("dep10.json");
    }
    
    @Test
    public void dep2() throws WitnessException, IOException, REException {
        testValid("dep2.json");
    }
    
    @Test
    public void dep3() throws WitnessException, IOException, REException {
        testValid("dep3.json");
    }
    
    @Test
    public void dep4() throws WitnessException, IOException, REException {
        testValid("dep4.json");
    }
    
    @Test
    public void dep5() throws WitnessException, IOException, REException {
        testValid("dep5.json");
    }
    
    @Test
    public void dep6() throws WitnessException, IOException, REException {
        testValid("dep6.json");
    }
    
    @Test
    public void dep7() throws WitnessException, IOException, REException {
        testValid("dep7.json");
    }
    
    @Test
    public void dep8() throws WitnessException, IOException, REException {
        testValid("dep8.json");
    }
    
    @Test
    public void dep9() throws WitnessException, IOException, REException {
        testValid("dep9.json");
    }
    
    @Test
    public void mod30() throws WitnessException, IOException, REException {
        testValid("mod30.json");
    }
    
    @Test
    public void mod301() throws WitnessException, IOException, REException {
        testValid("mod301.json");
    }
    
    @Test
    public void mod302() throws WitnessException, IOException, REException {
        testValid("mod302.json");
    }
    
    @Test
    public void mod303() throws WitnessException, IOException, REException {
        testValid("mod303.json");
    }
    
    @Test
    public void mod304() throws WitnessException, IOException, REException {
        testValid("mod304.json");
    }
    
    @Test
    public void mod305() throws WitnessException, IOException, REException {
        testValid("mod305.json");
    }
    
    @Test
    public void mod306() throws WitnessException, IOException, REException {
        testValid("mod306.json");
    }
    
    @Test
    public void mod309() throws WitnessException, IOException, REException {
        testValid("mod309.json");
    }
    
    @Test
    public void num4() throws WitnessException, IOException, REException {
        testValid("num4.json");
    }
    
    @Test
    public void num5() throws WitnessException, IOException, REException {
        testValid("num5.json");
    }
    
    @Test
    public void oneOf1() throws WitnessException, IOException, REException {
        testValid("oneOf1.json");
    }
    
    @Test
    public void oneOf2() throws WitnessException, IOException, REException {
        testValid("oneOf2.json");
    }
    
    @Test
    public void oneOf3() throws WitnessException, IOException, REException {
        testValid("oneOf3.json");
    }
    
    @Test
    public void oneOf4() throws WitnessException, IOException, REException {
        testValid("oneOf4.json");
    }
    
    @Test
    public void pnmp1() throws WitnessException, IOException, REException {
        testValid("pnmp1.json");
    }
    
    @Test
    public void pnmp10() throws WitnessException, IOException, REException {
        testValid("pnmp10.json");
    }
    
    @Test
    public void pnmp2() throws WitnessException, IOException, REException {
        testValid("pnmp2.json");
    }
    
    @Test
    public void pnmp3() throws WitnessException, IOException, REException {
        testValid("pnmp3.json");
    }
    
    @Test
    public void pnmp4() throws WitnessException, IOException, REException {
        testValid("pnmp4.json");
    }
    
    @Test
    public void pnmp5() throws WitnessException, IOException, REException {
        testValid("pnmp5.json");
    }
    
    @Test
    public void pnmp6() throws WitnessException, IOException, REException {
        testValid("pnmp6.json");
    }
    
    @Test
    public void pnmp7() throws WitnessException, IOException, REException {
        testValid("pnmp7.json");
    }
    
    @Test
    public void pnmp8() throws WitnessException, IOException, REException {
        testValid("pnmp8.json");
    }
    
    @Test
    public void pnmp9() throws WitnessException, IOException, REException {
        testValid("pnmp9.json");
    }
    
    @Test
    public void pp1() throws WitnessException, IOException, REException {
        testValid("pp1.json");
    }
    
    @Test
    public void pp2() throws WitnessException, IOException, REException {
        testValid("pp2.json");
    }
    
    @Test
    public void pp3() throws WitnessException, IOException, REException {
        testValid("pp3.json");
    }
    
    @Test
    public void pp4() throws WitnessException, IOException, REException {
        testValid("pp4.json");
    }
    
    @Test
    public void test_NoType() throws WitnessException, IOException, REException {
        testValid("test.NoType.json");
    }

    
}
