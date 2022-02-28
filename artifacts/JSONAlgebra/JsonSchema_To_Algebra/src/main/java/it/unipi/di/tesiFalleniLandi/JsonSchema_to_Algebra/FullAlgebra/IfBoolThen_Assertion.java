package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessIfBoolThen;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

public class IfBoolThen_Assertion implements Assertion {
    private boolean value;

    private static Logger logger = LogManager.getLogger(IfBoolThen_Assertion.class);

    public  IfBoolThen_Assertion(boolean value){
        this.value = value;
        logger.trace("Created a new IfBoolThen_Assertion with value {}", value);
    }

    @Override
    public JsonElement toJSONSchema(WitnessVarManager rootVar) {
        Type_Assertion t = new Type_Assertion();
        t.add("bool");
        return new IfThenElse_Assertion(t, new Const_Assertion(new JsonPrimitive(value)), null).toJSONSchema(rootVar);
    }

    @Override
    public Assertion not() {
        return new IfBoolThen_Assertion(!value);
    }

    @Override
    public Assertion notElimination() {
        return new IfBoolThen_Assertion(value);
    }

    @Override
    public String toGrammarString() {
        return AlgebraStrings.IFBOOLTHEN(value+"");
    }

    @Override
    public WitnessIfBoolThen toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
        return new WitnessIfBoolThen(value);
    }
}
