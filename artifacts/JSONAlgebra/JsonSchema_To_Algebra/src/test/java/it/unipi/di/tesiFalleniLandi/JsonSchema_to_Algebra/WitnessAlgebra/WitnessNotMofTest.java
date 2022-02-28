package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import static org.junit.Assert.*;

public class WitnessNotMofTest {

    @Test
    public void mergeNotMof1() throws REException {
        WitnessNotMof m1 = new WitnessNotMof(6.0);
        WitnessNotMof m2 = new WitnessNotMof(3.0);

        WitnessNotMof output = new WitnessNotMof(3.0);

        assertEquals(m1.mergeElement(m2), output);
    }

    @Test
    public void mergeNotMof2() throws REException {
        WitnessNotMof m1 = new WitnessNotMof(7.0);
        WitnessNotMof m2 = new WitnessNotMof(3.0);

        assertEquals(m1.mergeElement(m2), null);
    }
}