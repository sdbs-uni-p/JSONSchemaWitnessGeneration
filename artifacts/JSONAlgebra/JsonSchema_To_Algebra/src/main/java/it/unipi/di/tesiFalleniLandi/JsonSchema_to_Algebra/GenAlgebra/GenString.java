package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattern;
import de.uni_passau.sds.patterns.REException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GenString implements GenAssertion{
    private ComplexPattern pattern;
    private JsonElement witness;
    private Collection<String> oldWitnesses = new LinkedList<>();


    @Override
    public String toString() {
        return "GenString{" +
                "pattern=" + pattern +_sep +
                '}';
    }

    public GenString() {
//        this.pattern=new ComplexPattern(new Pattern(".*"));
        try {
            this.pattern = ComplexPattern.createFromRegexp(".*");
        } catch (REException e) {
            e.printStackTrace();
        }
    }

    public JsonElement getWitness() {
        return witness;
    }


    public void setPattern(ComplexPattern pattern) {
        this.pattern = pattern;
    }

    public GenString(WitnessPattern wp){
        pattern = wp.getPattern();
    }

    /**
     *
     * @return
     */
    @Override
    public statuses generate() {
        //Collection<String> words = pattern.generateWords();
        String word = null;
        try {
            word = pattern.generateNewWord(oldWitnesses);
            oldWitnesses.add(word);
        }catch (REException e){
            e.printStackTrace();
        }

        if(word!=null){
            witness = new JsonPrimitive(word); //pick first string of the regular expression
            return statuses.Populated;
        }
        else
            return statuses.Empty;
    }

    @Override
    public JsonElement generateNext() {
        return null;
    }

    @Override
    public WitnessAssertion toWitnessAlgebra() {
        return null;
    }

    @Override
    public List<GenVar> usedVars() {
        return new LinkedList<>();
    }


}
