package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import static org.junit.Assert.*;

public class WitnessBetTest {


    //bet(10, +inf), bet(12, +inf)
    @Test
    public void mergeBet1() throws REException {
        WitnessBet b1 = new WitnessBet(10.0, null);
        WitnessBet b2 = new WitnessBet(12.0, null);
        WitnessBet output = new WitnessBet(12.0, null);

        assertEquals(b1.mergeElement(b2, null), output);
    }

    //bet(10, +inf), xbet(10, +inf)
    @Test
    public void mergeXBet1() throws REException {
        WitnessBet b1 = new WitnessBet(10.0, null);
        WitnessXBet b2 = new WitnessXBet(10.0, null);
        WitnessXBet output = new WitnessXBet(10.0, null);

        assertEquals(b1.mergeElement(b2, null), output);
    }

    //bet(-inf, 10), bet(-inf, 12)
    @Test
    public void mergeBet2() throws REException {
        WitnessBet b1 = new WitnessBet(null, 10.0);
        WitnessBet b2 = new WitnessBet(null, 12.0);
        WitnessBet output = new WitnessBet(null, 10.0);

        assertEquals(b1.mergeElement(b2, null), output);
    }

    //bet(-inf, 10), xbet(-inf, 10)
    @Test
    public void mergeXBet2() throws REException {
        WitnessBet b1 = new WitnessBet(null, 10.0);
        WitnessXBet b2 = new WitnessXBet(null, 10.0);
        WitnessXBet output = new WitnessXBet(null, 10.0);

        assertEquals(b1.mergeElement(b2, null), output);
    }

    //bet(20, 50), bet(21, 49)
    @Test
    public void mergeBet3() throws REException {
        WitnessBet b1 = new WitnessBet(20.0, 50.0);
        WitnessBet b2 = new WitnessBet(21.0, 49.0);
        WitnessBet output = new WitnessBet(21.0, 49.0);

        assertEquals(b1.mergeElement(b2, null), output);
    }

    //bet(-inf, 3), bet(6, +inf)
    @Test
    public void mergeBetFalse1() throws REException {
        WitnessBet b1 = new WitnessBet(null, 3.0);
        WitnessBet b2 = new WitnessBet(6.0, null);
        WitnessType t = new WitnessType("num");

        assertEquals(b1.mergeElement(b2, null), t.not(null));
    }

    //bet(-inf, 3), xbet(3, +inf)
    @Test
    public void mergeBetFalse2() throws REException {
        WitnessBet b1 = new WitnessBet(null, 3.0);
        WitnessXBet b2 = new WitnessXBet(3.0, null);
        WitnessType t = new WitnessType("num");

        assertEquals(b1.mergeElement(b2, null), t.not(null));
    }
}