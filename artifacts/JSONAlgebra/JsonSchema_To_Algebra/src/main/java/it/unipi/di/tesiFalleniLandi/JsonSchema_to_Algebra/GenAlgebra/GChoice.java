package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessProperty;

import java.util.Iterator;
import java.util.List;

/*WitnessChoice counterpart*/
/*Actually, we only use the subList, the reqList and the reqFullSet may only be
  useful to check invariants
 */
public class GChoice {
    GenObject.GProperty constraint; // useless
    List<GenObject.GPattReq> reqList;  //useless
    List<GenObject.GPattReq> subList;  // this is a sublist of reqList
    List<GenObject.GPattReq> reqFullSet;  // useless this is a superlist of reqList, the pattern of the choice depends on the universe
    ComplexPattern pattern;
    GenVar schema;

    public GenObject.GProperty getConstraint() {
        return constraint;
    }

    public List<GenObject.GPattReq> getReqList() {
        return reqList;
    }

    public List<GenObject.GPattReq> getSubList() {
        return subList;
    }

    public List<GenObject.GPattReq> getReqFullSet() {
        return reqFullSet;
    }

    public ComplexPattern getPattern() {
        return pattern;
    }

    public GenVar getSchema() {
        return schema;
    }

    public GenVar usedVar() {
        return schema;
    }

    private String _sep = "\r\n";

    /*public GChoice(WitnessProperty constraint, List<GenObject.GPattReq> gReqList, List<GenObject.GPattReq> gSubList,
                   List<GenObject.GPattReq> gReqFullSet, ComplexPattern pattern) {
    }*/

    //public List<GenVar> usedVars() {
    //    return reqList.stream().map(p -> p.usedVar()).collect(Collectors.toList());
    //}

    @Override
    public String toString() {
        return "GChoice{" +
                "constraint=" + constraint + _sep +
                "reqList=" + reqList + _sep +
                "subList=" + subList + _sep +
                "reqFullSet=" + reqFullSet + _sep +
                "pattern=" + pattern + _sep +
                "schema=" + schema + _sep +
                '}' + _sep;
    }

    /*
    public String toGrammarString() {
        StringBuilder str = new StringBuilder();

        Iterator<Assertion> it = subList.iterator();

        while(it.hasNext()) {
            String returnedValue = it.next().toGrammarString();
            if(returnedValue.isEmpty())
                continue;
            str.append(AlgebraStrings.COMMA)
                    .append(returnedValue);
        }

        if (choices!=null) {
            str.append(AlgebraStrings.SEMICOLON);
            Iterator<Assertion> itc = choices.iterator();
            while(itc.hasNext()) {
                String returnedValue = itc.next().toGrammarString();
                if(returnedValue.isEmpty())
                    continue;
                str.append(AlgebraStrings.COMMA)
                        .append(returnedValue);
            }
        } else {
            str.append(AlgebraStrings.SEMICOLON);
            str.append("no choice");
        }

        if(str.length() == 0) return "";
        if(andList.size() == 1) return str.delete(0, AlgebraStrings.COMMA.length()).toString();
        if(!duplicates) {
            str = str.delete(0, AlgebraStrings.COMMA.length()); //TODO: CHECK
            return str.append("\r\n}").insert(0, "{\r\n").toString();
        }

        return AlgebraStrings.ALLOF(str.substring(AlgebraStrings.COMMA.length()));
    }
     */
    /**
     *
     */
    public GChoice(GenObject.GProperty constraint,
                   List<GenObject.GPattReq> reqList,
                   List<GenObject.GPattReq> subList,
                   List<GenObject.GPattReq> reqFullSet,
                   ComplexPattern pattern) {
        this.constraint = constraint;
        this.reqList = reqList;
        this.subList = subList;
        this.reqFullSet = reqFullSet;
        this.pattern = pattern;
    }

    public void setSchema(WitnessAssertion assertion, GenEnv env) {
        schema = env.retrieveVar(assertion);
    }

    public void setSchema(GenVar schema) {
        this.schema = schema;
    }

    /**
     * returns true when all variables are empty or when the request list is empty
     *
     * @return
     */
        /*public boolean allVarsEmpty() {
            Optional<Boolean> res = null;
            res = this.reqList.stream().map(req -> req.usedVar().isEmpty())
                    .reduce((a, b) -> a && b);
            if (res.isPresent())
                return res.get();
            else
                return true; //empty list is considered to be Empty
        }*/

}

