package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.LinkedList;
import java.util.List;

public class Utils_WitnessAlgebra {
    private static Logger logger = LogManager.getLogger(Utils_WitnessAlgebra.class);


    // Pipeline used until now
    public static WitnessEnv getWitnessEnv1(Assertion schema) throws  WitnessException, REException {
        WitnessEnv witnessEnv = Utils_FullAlgebra.getWitnessAlgebra(schema);
        witnessEnv.buildOBDD_notElimination();

        witnessEnv = (WitnessEnv) witnessEnv.merge(null, null);
        witnessEnv = witnessEnv.varNormalization_expansion(null);
        witnessEnv = witnessEnv.groupize();
        witnessEnv = witnessEnv.DNF();
        witnessEnv.varNormalization_separation(null, null);
        witnessEnv = witnessEnv.DNF();
        witnessEnv = witnessEnv.varNormalization_expansion(null);
        witnessEnv = (WitnessEnv) witnessEnv.merge(null, null);
        witnessEnv.toOrPattReq();
        witnessEnv.objectPrepare();
        witnessEnv.arrayPreparation();

        return  witnessEnv;

    }



    public static String getFullAlgebra(WitnessEnv env ){
        return Utils.beauty(env.getFullAlgebra().toGrammarString());

    }

    public static  WitnessEnv getWitnessEnv2(Assertion schema) throws WitnessException, REException {
        WitnessEnv witnessEnv = Utils_FullAlgebra.getWitnessAlgebra(schema);
        witnessEnv.buildOBDD_notElimination();
        System.out.println("\n buildOBDD_notElimination\n");
        System.out.println(Utils.beauty(witnessEnv.getFullAlgebra().toGrammarString())+"\n");

        witnessEnv = (WitnessEnv) witnessEnv.merge(null, null);
        System.out.println("\n merge\n");
        System.out.println(Utils.beauty(witnessEnv.getFullAlgebra().toGrammarString())+"\n");

        witnessEnv = witnessEnv.groupize();
        System.out.println("\n groupize\n");
        System.out.println(Utils.beauty(witnessEnv.getFullAlgebra().toGrammarString())+"\n");

        int i = 1, prePrepEnvSize = 0;
        boolean fixpoint = false;

        do
        {
            witnessEnv.varNormalization_separation(null, null);
            witnessEnv = witnessEnv.varNormalization_expansion(null);
//            witnessEnv = (WitnessEnv) witnessEnv.merge(null, null);
            fixpoint = (prePrepEnvSize == witnessEnv.getSize());

            if (!fixpoint)
                System.out.println("before iteration "+i+", prevEnvSize "+prePrepEnvSize+", currEnvSize " + witnessEnv.getSize() +"\n");
            else
                System.out.println("after last iteration "+(i-1)+", currEnvSize " + witnessEnv.getSize() +"\n");

            System.out.println(Utils.beauty(witnessEnv.getFullAlgebra().toGrammarString())+"\n");

            if (fixpoint) break;

            prePrepEnvSize = witnessEnv.getSize();

            witnessEnv.preparation();
            System.out.println("\n preparation\n");
            System.out.println(Utils.beauty(witnessEnv.getFullAlgebra().toGrammarString())+"\n");

            witnessEnv = (WitnessEnv) witnessEnv.merge(null, null);
            fixpoint = (prePrepEnvSize == witnessEnv.getSize());


        }while (true);

        return witnessEnv;
    }



    // New pipeline
    // Giorgio says: the Assertion "schema" has already been normalized as in
    //      JsonObject object = gson.fromJson(reader, JsonObject.class);
    //      JSONSchema root = new JSONSchema(object);
    //      Assertion jsonSchema = Utils_JSONSchema.normalize(root).toGrammar();
    public static  WitnessEnv oldgetWitnessEnv2(Assertion schema) throws WitnessException, REException {
        String currentEnv;
        WitnessEnv witnessEnv = Utils_FullAlgebra.getWitnessAlgebra(schema);
        currentEnv = getFullAlgebra(witnessEnv);
        logger.trace("algebra from translation {}",currentEnv);

        witnessEnv.buildOBDD_notElimination();

        //prune the environment by removing unreachable variables TODO check
        logger.trace("size before pruning {} ", witnessEnv.getSize());
//        List<WitnessVar> toKeep = witnessEnv.reachableFrom(witnessEnv.getRootVar(),new LinkedList<>());
//        witnessEnv.keepOnly(toKeep);
//        logger.trace("size after pruning {} ", witnessEnv.getSize());

        currentEnv = getFullAlgebra(witnessEnv);
        logger.trace("algebra from buildOBDD_notElimination {}",currentEnv);

        witnessEnv = (WitnessEnv) witnessEnv.merge(null, null);
        currentEnv = getFullAlgebra(witnessEnv);
        logger.trace("algebra after merge {}", currentEnv);
        witnessEnv = witnessEnv.groupize();
        currentEnv = getFullAlgebra(witnessEnv);
        logger.trace("algebra after groupize {}", currentEnv);

        int i = 1, prePrepEnvSize = 0;
        boolean fixpoint = false;

        do
        {
            witnessEnv.varNormalization_separation(null, null);
            witnessEnv = witnessEnv.varNormalization_expansion(null);
            currentEnv = getFullAlgebra(witnessEnv);
            logger.trace("algebra after separation-normalization {}", currentEnv);
            //prune the environment by removing unreachable variables TODO check
//            logger.trace("size before pruning {} ", witnessEnv.getSize());
//            witnessEnv.keepOnly(witnessEnv.reachableFrom(witnessEnv.getRootVar(),new LinkedList<>()));
//            logger.trace("size after pruning {} ", witnessEnv.getSize());
//            witnessEnv = witnessEnv.DNF();
            witnessEnv = witnessEnv.DNF();
            currentEnv = getFullAlgebra(witnessEnv);
            logger.trace("algebra after DNF {}", currentEnv);
            witnessEnv = (WitnessEnv) witnessEnv.merge(null, null);
            currentEnv = getFullAlgebra(witnessEnv);
            logger.trace("algebra after merge {}", currentEnv);

            if (!fixpoint)
                System.out.println("before iteration "+i+", prevEnvSize "+prePrepEnvSize+", currEnvSize " + witnessEnv.getSize() +"\n");
            else
                System.out.println("after last iteration "+(i-1)+", currEnvSize " + witnessEnv.getSize() +"\n");

            System.out.println(Utils.beauty(witnessEnv.getFullAlgebra().toGrammarString())+"\n");

            if (fixpoint) break;

            prePrepEnvSize = witnessEnv.getSize();

            witnessEnv.objectPrepare();
            currentEnv = getFullAlgebra(witnessEnv);
            logger.trace("algebra after objectPrepare {}", currentEnv);
            witnessEnv.arrayPreparation();
            currentEnv = getFullAlgebra(witnessEnv);
            logger.trace("algebra after arrayPreparation {}", currentEnv);
            witnessEnv = (WitnessEnv) witnessEnv.merge(null, null);
            currentEnv = getFullAlgebra(witnessEnv);
            logger.trace("algebra after merge {}", currentEnv);

            fixpoint = (prePrepEnvSize == witnessEnv.getSize());

//            logger.trace("iteration {} of preparation", i ++);


        } while (true);

        return witnessEnv;
    }
}
