package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern;

import java.util.LinkedList;
import java.util.List;

public class pAllOf implements ComplexPatternElement {

    private List<ComplexPatternElement> pAllOf;

    public pAllOf() {
        this.pAllOf = new LinkedList<>();
    }

    public void add(ComplexPatternElement toAdd) {
        if (toAdd.getClass() == this.getClass())
            for(ComplexPatternElement el : ((pAllOf) toAdd).pAllOf)
                add(el);
        else
            if(!contains(toAdd))
                pAllOf.add(toAdd);
    }

    private boolean contains(ComplexPatternElement el){
        for(ComplexPatternElement a : pAllOf)
            if(el.toString().equals(a.toString())) return true;

        return false;
    }

    @Override
    public ComplexPatternElement clone() {
        pAllOf clone = new pAllOf();
        clone.pAllOf = new LinkedList<>(this.pAllOf);
        return clone;
    }

    @Override
    public String toString() {
        return "pAllOf" + pAllOf.toString();
    }
}
