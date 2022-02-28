package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

        import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
        import org.junit.Test;
        import de.uni_passau.sds.patterns.REException;

        import java.io.IOException;

        import static org.junit.Assert.assertEquals;

public class GenAutomataTest {

    private GenAndValidate genJSchemaVal =
            new GenAndValidate(System.getProperty("user.dir")+"/testAutomataFiles/moduloFiles/",false);

    private GenAndValidate genAlgebraicVal =
            new GenAndValidate(System.getProperty("user.dir")+"/testAutomataFiles/moduloFiles/",true);

    @Test
    public void testmodulo30slow() throws WitnessException, IOException, REException {
        String witness = genAlgebraicVal.genWitness("modulo30.slow.algebra");
        assertEquals(0,genAlgebraicVal.validateWitness("modulo30.slow.algebra",witness));
    }

    @Test
    public void testmodulo30() throws WitnessException, IOException, REException {
        String witness = genAlgebraicVal.genWitness("modulo30.algebra");
        assertEquals(0,genAlgebraicVal.validateWitness("modulo30.algebra",witness));
    }

    @Test
    public void testmodulo6bin() throws WitnessException, IOException, REException {
        String filename = "modulo6bin.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        assertEquals(0,genAlgebraicVal.validateWitness(filename,witness));
    }

    @Test
    public void testmodulo30bis() throws WitnessException, IOException, REException {
        String filename = "modulo30bis.algebra";
        String witness = genAlgebraicVal.genWitness(filename);
        assertEquals(0,genAlgebraicVal.validateWitness(filename,witness));
    }


}
