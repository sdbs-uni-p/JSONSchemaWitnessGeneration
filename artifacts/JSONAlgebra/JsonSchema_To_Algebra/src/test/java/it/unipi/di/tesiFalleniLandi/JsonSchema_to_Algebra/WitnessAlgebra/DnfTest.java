package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DnfTest {

    @Test
    public void testDNF1() throws IOException, REException, WitnessException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/dnf/input_1.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/dnf/output_1.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null).groupize();
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.DNF(), output);

        System.out.println();
    }

    @Test
    public void testDNF2() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/dnf/input_2.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/dnf/output_2.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null).groupize();
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.DNF(), output);
    }

    @Test
    public void testDNF3() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/dnf/input_3.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/dnf/output_3.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null).merge(null, null).groupize();
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.DNF(), output);
    }

    @Test
    public void testDNF4() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/dnf/input_4.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/dnf/output_4.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null).merge(null, null).groupize();
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.DNF(), output);
    }

    @Test
    public void testDNF5() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/dnf/input_5.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/dnf/output_5.algebra");
        WitnessAssertion input = in.toWitnessAlgebra(null,null, null).groupize();
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        assertEquals(input.DNF(), output);
    }
}
