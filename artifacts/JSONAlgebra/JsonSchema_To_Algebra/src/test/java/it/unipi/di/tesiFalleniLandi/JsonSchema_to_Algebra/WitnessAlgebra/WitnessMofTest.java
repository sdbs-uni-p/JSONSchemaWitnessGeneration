package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import static org.junit.Assert.*;

public class WitnessMofTest {

    @Test
    public void mergeMof1() throws REException {
        WitnessMof m1 = new WitnessMof(3.0);
        WitnessMof m2 = new WitnessMof(7.0);
        WitnessMof output = new WitnessMof(21.0);

        assertEquals(m1.mergeElement(m2), output);
    }

    @Test
    public void mergeMof2() throws REException {
        WitnessMof m1 = new WitnessMof(6.0);
        WitnessNotMof m2 = new WitnessNotMof(3.0);
        WitnessType t = new WitnessType("num");

        assertEquals(m1.mergeElement(m2), t.not(null));
    }

    @Test
    public void mergeMof3() throws REException {
        WitnessMof m1 = new WitnessMof(7.0);
        WitnessNotMof m2 = new WitnessNotMof(3.0);

        assertEquals(m1.mergeElement(m2), null); //non li posso unire
    }

}