package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Ignore;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class VarSeparationTest {

    @Test
    @Ignore
    public void varSepTest1() throws IOException, REException, WitnessException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/varSeparation/input_1.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/varSeparation/output_1.algebra");
        WitnessEnv input = (WitnessEnv) in.toWitnessAlgebra(null,null, null);
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        input.buildOBDD_notElimination();
        input = input.groupize();
        input = input.DNF();
        input.varNormalization_separation(null, null);

        //System.out.println(Utils.beauty(input.getFullAlgebra().toGrammarString()) +"\n\n\n");
        //System.out.println(Utils.beauty(output.getFullAlgebra().toGrammarString()));

        assertEquals(input, output);
    }
}
