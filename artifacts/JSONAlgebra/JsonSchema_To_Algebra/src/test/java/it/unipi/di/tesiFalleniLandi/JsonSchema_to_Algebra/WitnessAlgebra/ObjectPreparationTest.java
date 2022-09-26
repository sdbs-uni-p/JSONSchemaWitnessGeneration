package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.junit.Ignore;
import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ObjectPreparationTest {

    @Test
    @Ignore //it fails because type[a, b, c] != anyOf[type(a), type(b), type(c)]
    public void testObjPrep1() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/input_1.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/output_1.algebra");
        WitnessEnv input = (WitnessEnv) in.toWitnessAlgebra(null,null, null);
        WitnessAssertion output = out.toWitnessAlgebra(null,null, null);

        input.buildOBDD_notElimination();
        input = input.groupize();
        input = input.DNF();
        input.varNormalization_separation(null, null);
        input = input.varNormalization_expansion(null);
        //input.toOrPattReq();
        input.objectPrepare();

        assertEquals(input, output);
    }

    @Test
    @Ignore
    public void testObjPrep2() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/input_2.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/output_2.algebra");
        WitnessEnv input = Utils_FullAlgebra.getWitnessAlgebra(in);
        WitnessEnv output = Utils_FullAlgebra.getWitnessAlgebra(out);

        input.buildOBDD_notElimination();
        input = (WitnessEnv) input.merge(null, null);
        input = input.groupize();
        input = input.DNF();
        input.varNormalization_separation(null, null);
        input = input.varNormalization_expansion(null);
        //input.toOrPattReq();
        input.objectPrepare();

        assertEquals(input, output);
    }

    @Test
    public void testObjPrep2bis() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/input_2.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/output_2.algebra");
        WitnessEnv input = Utils_FullAlgebra.getWitnessAlgebra(in);
        WitnessEnv output = Utils_FullAlgebra.getWitnessAlgebra(out);

        input.buildOBDD_notElimination();
        input.varNormalization_separation(null, null);
        input = (WitnessEnv) input.merge(null, null);
        input = input.groupize();
        input = input.varNormalization_expansion(null);
        input = input.DNF();
        //input.toOrPattReq();
        input.objectPrepare();

        assertEquals(input, output);
   }

    @Test
    @Ignore
    public void testObjPrep3() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/input_3.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/output_3.algebra");
        WitnessEnv input = Utils_FullAlgebra.getWitnessAlgebra(in);
        WitnessEnv output = Utils_FullAlgebra.getWitnessAlgebra(out);

        input.checkLoopRef(null, null);
        input.buildOBDD_notElimination();
        input = (WitnessEnv) input.merge(null, null);
        input = input.groupize();
        input = input.DNF();
        input.varNormalization_separation(null, null);
        input = input.varNormalization_expansion(null);
        //input.toOrPattReq();
        input.objectPrepare();

        assertEquals(input, output);
    }

    @Test
    @Ignore
    public void testObjPrep4() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/input_4.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/output_4.algebra");
        WitnessEnv input = Utils_FullAlgebra.getWitnessAlgebra(in);
        WitnessEnv output = Utils_FullAlgebra.getWitnessAlgebra(out);

        input.checkLoopRef(null, null);
        input.buildOBDD_notElimination();
        input = (WitnessEnv) input.merge(null, null);
        input = input.groupize();
        input = input.DNF();
        input.varNormalization_separation(null, null);
        input = input.varNormalization_expansion(null);
        //input.toOrPattReq();
        input.objectPrepare();

        assertEquals(input, output);
    }

    @Test
    @Ignore
    public void testObjPrep5() throws WitnessException, IOException, REException {
        Assertion in = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/input_5.algebra");
        Assertion out = Utils_FullAlgebra.parseFile("unit-test/objectPrepare/output_5.algebra");
        WitnessEnv input = Utils_FullAlgebra.getWitnessAlgebra(in);
        WitnessEnv output = Utils_FullAlgebra.getWitnessAlgebra(out);

        input.checkLoopRef(null, null);
        input.buildOBDD_notElimination();
        input = (WitnessEnv) input.merge(null, null);
        input = input.groupize();
        input = input.DNF();
        input.varNormalization_separation(null, null);
        input = input.varNormalization_expansion(null);
        //input.toOrPattReq();
        input.objectPrepare();

        System.out.println(Utils.beauty(input.getFullAlgebra().toGrammarString()));
        System.out.println(Utils.beauty(output.getFullAlgebra().toGrammarString()));

        assertEquals(input, output);
    }
}
