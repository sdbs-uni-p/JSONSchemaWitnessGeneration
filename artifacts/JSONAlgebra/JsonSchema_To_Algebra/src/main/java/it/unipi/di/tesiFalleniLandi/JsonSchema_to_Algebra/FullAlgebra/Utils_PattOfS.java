package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.Pattern;
import de.uni_passau.sds.patterns.REException;

import java.util.List;

public class Utils_PattOfS {
    private static ComplexPattern truePattern;
    private static ComplexPattern falsePattern;

    private static Logger logger = LogManager.getLogger(Utils_PattOfS.class);


    static {
        try {
            truePattern = ComplexPattern.createFromRegexp(".*");
        } catch (REException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        falsePattern = truePattern.complement();
    }

    /*
     *  Although this method is invoked after FullAlgebraLevel not-elimination
     *  it may still meet cases of "Enum" or "Const"
     * it should never meet a oneOf
     * This should really be a method of all classes, but is so rarely used
     * that it is not worth spending time on this issue
     */

    public static ComplexPattern pattOfS(Assertion a, Defs_Assertion env){
        // The default case is truePattern.clone()
        // hence we ignore all typed operotors of a type that is not "string"
        // and the boolean "true"

        ComplexPattern returnPattern = truePattern.clone();

        if(a instanceof Type_Assertion)
            returnPattern = pattOfS((Type_Assertion) a);

        if(a instanceof Const_Assertion)
            returnPattern = pattOfS((Const_Assertion) a);

        if(a instanceof Enum_Assertion) {
            returnPattern = pattOfS((Enum_Assertion) a);
        }

        if(a instanceof AllOf_Assertion)
            returnPattern = pattOfS((AllOf_Assertion) a, env);

        if(a instanceof AnyOf_Assertion)
            returnPattern = pattOfS((AnyOf_Assertion) a, env);

        if(a instanceof OneOf_Assertion) {
            System.out.println("Unexpected OneOf referenced by propertyNames");
            System.exit(-1);
        }

        if(a instanceof Boolean_Assertion) {
            returnPattern = pattOfS((Boolean_Assertion) a, env);
        }

        if(a instanceof Not_Assertion)
            returnPattern = pattOfS((Not_Assertion) a, env);

        if(a instanceof Pattern_Assertion)
            returnPattern = pattOfS((Pattern_Assertion) a);

        if(a instanceof Len_Assertion)
            returnPattern = pattOfS((Len_Assertion) a);

        if(a instanceof NotPattern_Assertion)
            returnPattern = pattOfS((NotPattern_Assertion) a);

        if(a instanceof Ref_Assertion)
            returnPattern = pattOfS((Ref_Assertion) a, env);

        if(a instanceof IfThenElse_Assertion) {
            System.out.println("Unexpected IfThenElse referenced by propertyNames");
            System.exit(-1);
        }

        logger.trace("PattOfS({}) returning {}", a, returnPattern);

        return returnPattern;
    }

    public static ComplexPattern pattOfS(Type_Assertion a){
        if(a.contains(AlgebraStrings.TYPE_STRING)) return truePattern.clone();

        return falsePattern.clone();
    }

    public static ComplexPattern pattOfS(Const_Assertion a){
        JsonElement JsonEl = a.getJsonElementValue();
        if(JsonEl.getAsJsonPrimitive().isString())
            return ComplexPattern.createFromName(JsonEl.getAsString());
        else
            return falsePattern.clone();
    }

    public static ComplexPattern pattOfS(Enum_Assertion a){
        ComplexPattern falseClone = falsePattern.clone();
        ComplexPattern result = falseClone;
         for(JsonElement element : a.getEnumList()) {
            if(element.getAsJsonPrimitive().isString()) {
                if (result == falseClone) {
                    result = ComplexPattern.createFromName(element.getAsString());
                } else {
                    result = result.union(ComplexPattern.createFromName(element.getAsString()));
                }
            }
        }
        return result;

    }

    public static ComplexPattern pattOfS(AllOf_Assertion a, Defs_Assertion env){
        List<Assertion> and = a.getAndList();  // should add an assertion and.size() > 0
        ComplexPattern p = pattOfS(and.get(0), env);
        for(int i = 1; i < and.size(); i++)
            p = p.intersect(pattOfS(and.get(i), env));

        return p;
    }

    public static ComplexPattern pattOfS(AnyOf_Assertion a, Defs_Assertion env){
        List<Assertion> or = a.getOrList(); // should add an assertion or.size() > 0
        ComplexPattern p = pattOfS(or.get(0), env);
        for(int i = 1; i < or.size(); i++)
            p = p.union(pattOfS(or.get(i), env));

        return p;
    }

    public static ComplexPattern pattOfS(Not_Assertion a, Defs_Assertion env){
        return pattOfS(a.getValue(), env).complement();
    }

    public static ComplexPattern pattOfS(Boolean_Assertion a, Defs_Assertion env){
        if (a.getValue())
            return truePattern.clone();
        else
            return falsePattern.clone();
    }

    public static ComplexPattern pattOfS(Pattern_Assertion a){
        return a.getValue();
    }

    //TODO: CL: tocheck with Prof. Ghelli
    public static ComplexPattern pattOfS(NotPattern_Assertion a){ return a.getValue().complement();}

    public static ComplexPattern pattOfS(Ref_Assertion a, Defs_Assertion env){
        return pattOfS(env.getDef(a.getRef()), env);
    }

    public static ComplexPattern pattOfS(Len_Assertion a){
        return a.toPattern();
    }

}
