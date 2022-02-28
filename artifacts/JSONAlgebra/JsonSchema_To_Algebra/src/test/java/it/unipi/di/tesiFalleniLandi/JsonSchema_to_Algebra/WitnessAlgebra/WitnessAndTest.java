package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessFalseAssertionException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import static org.junit.Assert.*;

public class WitnessAndTest {


    //Test and appiattito
    @Test
    public void testAddAnd() throws REException, WitnessFalseAssertionException {
        WitnessAnd a1 = new WitnessAnd();
        a1.add(new WitnessBoolean(true));
        a1.add(new WitnessMof(3.0));

        WitnessAnd a2 = new WitnessAnd();
        a2.add(new WitnessBoolean(true));
        a2.add(new WitnessMof(5.0));

        WitnessAnd output = new WitnessAnd();
        output.add(new WitnessBoolean(true));
        output.add(new WitnessMof(3.0));
        output.add(new WitnessMof(5.0));

        a1.add(a2);

        assertEquals(a1, output);
    }

    @Test
    public void testEquals() throws WitnessFalseAssertionException {
        WitnessAnd a1 = new WitnessAnd();
        a1.add(new WitnessMof(3.0));
        a1.add(new WitnessMof(4.0));

        WitnessAnd a2 = new WitnessAnd();
        a2.add(new WitnessMof(4.0));
        a2.add(new WitnessMof(3.0));

        assertEquals(a1, a2);

    }

}