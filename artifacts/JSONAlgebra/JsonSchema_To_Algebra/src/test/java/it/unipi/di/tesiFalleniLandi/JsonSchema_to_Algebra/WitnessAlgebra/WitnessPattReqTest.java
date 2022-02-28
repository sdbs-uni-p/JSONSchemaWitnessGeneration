package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import org.junit.Ignore;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import static org.junit.Assert.*;

public class WitnessPattReqTest {

    @Test
    public void testPattReqMerge1() throws REException {
        WitnessPattReqManager pattReqManager = new WitnessPattReqManager();
        WitnessPattReq p1 = pattReqManager.build(ComplexPattern.createFromRegexp(".*"), new WitnessBoolean(true));
        WitnessPattReq p2 = pattReqManager.build(ComplexPattern.createFromName("aaa"), new WitnessBoolean(true));

        WitnessPattReq output = pattReqManager.build(ComplexPattern.createFromName("aaa"), new WitnessBoolean(true));

        assertEquals(p1.mergeElement(p2), output);
    }

    @Test
    @Ignore
    public void testPattReqMerge2() throws REException {
        WitnessPattReqManager pattReqManager = new WitnessPattReqManager();
        WitnessPattReq p1 = pattReqManager.build(ComplexPattern.createFromRegexp(".*"), new WitnessBoolean(true));
        WitnessPattReq p2 = pattReqManager.build(ComplexPattern.createFromRegexp("#"), new WitnessBoolean(true));

        assertEquals(p1.mergeElement(p2), null);
    }
}