package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrPattReq_Assertion implements Assertion{
    List<Map.Entry<ComplexPattern, Assertion>> reqList;

    private static Logger logger = LogManager.getLogger(OrPattReq_Assertion.class);

    public OrPattReq_Assertion() {
        reqList = new LinkedList<>();
        logger.trace("Creating an empty OrPAttReq_Assertion");
    }

    public void add(ComplexPattern key, Assertion value) {
        logger.trace("Adding <{}, {}> to {}", key, value, this);

        /*if(ORP.containsKey(key)) {
            logger.trace("Same ORP contains {} --> generating value as AnyOf", key);
            AnyOf_Assertion or = new AnyOf_Assertion(); //TODO: what if ORP already contains an entry with the same key?
            or.add(ORP.get(key));
            or.add(value);
            ORP.put(key, or);
            return;
        }*/

        reqList.add(new AbstractMap.SimpleEntry(key, value));
    }

    @Override
    public JsonElement toJSONSchema(WitnessVarManager rootVar) {
        throw new UnsupportedOperationException("OrPattReq.toJSONSchema");
    }

    @Override
    public Assertion not() {
        AllOf_Assertion and = new AllOf_Assertion();
        for(Map.Entry<ComplexPattern, Assertion> entry : this.reqList) {
            PatternRequired_Assertion tmp = new PatternRequired_Assertion(entry.getKey(), entry.getValue().notElimination());
            and.add(tmp.not());
        }
        return and;
        //throw new UnsupportedOperationException("OrPattReq.not");
    }

    @Override
    public Assertion notElimination() {
        OrPattReq_Assertion returnValue = new OrPattReq_Assertion();
        for(Map.Entry<ComplexPattern, Assertion> entry : this.reqList)
            returnValue.add(entry.getKey(), entry.getValue().notElimination());
        return returnValue; //TODO: ask Ghelli
    }

    @Override
    public String toGrammarString() {
        StringBuilder str = new StringBuilder();

        if(reqList != null) {
            for(Map.Entry<ComplexPattern, Assertion> entry : reqList) {
                String returnedValue = entry.getValue().toGrammarString();
                if(!returnedValue.isEmpty())
                    str.append(AlgebraStrings.COMMA)
                            .append(entry.getKey().getAlgebraString())
                            .append(":")
                            .append(returnedValue);
            }
        }
        // I am not sure at all about the following line!
        if(str.length() == 0) str.append("   ");
        return AlgebraStrings.ORPATTREQ(str.substring(AlgebraStrings.COMMA.length()));
    }

    @Override
    public WitnessAssertion toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
        WitnessOrPattReq witnessOrPattReq = new WitnessOrPattReq();
        for (Map.Entry<ComplexPattern, Assertion> entry : this.reqList)
            witnessOrPattReq.add(pattReqManager.build(entry.getKey(), entry.getValue().toWitnessAlgebra(varManager,env,pattReqManager)));

        return witnessOrPattReq;
    }
}
