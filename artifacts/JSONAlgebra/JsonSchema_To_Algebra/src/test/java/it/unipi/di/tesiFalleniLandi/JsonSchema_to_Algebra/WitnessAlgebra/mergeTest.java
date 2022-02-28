package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class mergeTest {

    @Test
    public void mergeFalse() throws REException {
        WitnessAnd a1 = new WitnessAnd();
        a1.add(new WitnessMof(3.0));
        a1.add(new WitnessBoolean(false));

        WitnessAnd a2 = new WitnessAnd();
        a2.add(new WitnessBoolean(true));

        assertEquals(((WitnessAnd)a1.mergeWith(a2, null, null)).getIfUnitaryAnd(), new WitnessBoolean(false));
    }

    @Test
    public void testMerge1() throws REException, IOException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/merge/input_1.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/merge/output_1.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null);
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.merge(null, null), output.merge(null, null));
    }

    @Test
    public void testMerge2() throws REException, IOException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/merge/input_2.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/merge/output_2.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null);
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.merge(null, null), output.merge(null, null));
    }

    @Test
    public void testMerge3() throws REException, IOException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/merge/input_3.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/merge/output_3.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null);
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.merge(null, null), output);
    }

    @Test
    public void testMerge4() throws REException, IOException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/merge/input_4.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/merge/output_4.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null);
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.merge(null, null), output);
    }
}
