package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4.AlgebraParser;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4.ErrorListener;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4.GrammaticaLexer;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4.GrammaticaParser;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Utils_FullAlgebra {
    private static Logger logger = LogManager.getLogger(Utils_FullAlgebra.class);

    /**
     * Legge il file (sintassi full-algebra) indicato da path e ne restituisce la rappresentazione come insieme di definizioni
     * @param path path del file da parsare
     * @return ritorna un'istanza di Defs_Assertion
     * @throws IOException errori nell'accesso al file
     */
    public static Defs_Assertion parseFile(String path) throws IOException {
        logger.trace("Parsing file {}", path);
        if(new File(path).length() <= 2) throw new ParseCancellationException("Empty File");

        Reader reader = new FileReader(path);
        GrammaticaLexer lexer = new GrammaticaLexer(CharStreams.fromReader(reader));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammaticaParser parser = new GrammaticaParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener());

        ParseTree tree = parser.assertion();
        AlgebraParser p = new AlgebraParser();
        Assertion schema = (Assertion) p.visit(tree);
        if (schema.getClass() != Defs_Assertion.class) {
            Defs_Assertion tmp = new Defs_Assertion();
            tmp.setRootDef(AlgebraStrings.ROOTDEF_DEFAULTNAME, schema);
            schema = tmp;
        }

        return (Defs_Assertion) schema;
    }

    /**
     * Legge la Stringa (sintassi full-algebra) e ne restituisce la rappresentazione come insieme di definizioni
     * @param toParse Stringa da parsare
     * * @return ritorna un'istanza di Defs_Assertion
     */
    public static Defs_Assertion parseString(String toParse) {
        logger.trace("Parsing string");
        if(toParse.length() <= 2) throw new ParseCancellationException("Empty String");
        GrammaticaLexer lexer = new GrammaticaLexer(CharStreams.fromString(toParse));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GrammaticaParser parser = new GrammaticaParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener());

        ParseTree tree = parser.assertion();
        AlgebraParser p = new AlgebraParser();
        Assertion schema = (Assertion) p.visit(tree);
        if (schema.getClass() != Defs_Assertion.class) {
            Defs_Assertion tmp = new Defs_Assertion();
            tmp.setRootDef(AlgebraStrings.ROOTDEF_DEFAULTNAME, schema);
            schema = tmp;
        }

        return (Defs_Assertion) schema;
    }

    /**
     * Dato un oggetto istanza di Assertion (Full algebra), ne restituisce la rappresentazione come WitnessEnv
     * WitnessEnv è un oggetto che contiene le coppie <variabile (def name), valore>.
     * Se root è Defs_Assertion ne restituisce la versione convertita, altrimenti crea un WitnessEnv
     * con una variaile di nome
     * GrammarStringDefinitions.ROOTDEF_DEFAULTNAME e valore root.toWitnessAlgebra().
     * root viene in ogni cso notEliminato/completato
     * @param root Rappresentazione di un documento in oggetti di tipo Assertion (Full algebra)
     * @return ritorna WitnessEnv costruito come indicato sopra
     */
    public static WitnessEnv getWitnessAlgebra(Assertion root) throws REException {
        root = root.notElimination();
        WitnessAssertion returnedValue = root.toWitnessAlgebra(null,null, null);
        if(returnedValue.getClass() != WitnessEnv.class){
            WitnessEnv env = new WitnessEnv(new WitnessVarManager(), new WitnessPattReqManager());
            env.setRootVar(AlgebraStrings.ROOTDEF_DEFAULTNAME, returnedValue);
            return env;
        }
        return (WitnessEnv) returnedValue;
    }

}
