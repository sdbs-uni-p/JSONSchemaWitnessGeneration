package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern;

public class pNot implements ComplexPatternElement {
    ComplexPatternElement not;

    public static ComplexPatternElement createPNot(ComplexPatternElement not){
        ComplexPatternElement returnValue = new pNot(not);

        if(not.getClass() == pNot.class)
            return ((pNot) not).not;

        return returnValue;
    }

    private pNot(ComplexPatternElement not) {
        this.not = not;
    }

    @Override
    public ComplexPatternElement clone() {
        return new pNot(not);
    }

    @Override
    public String toString() {
        return "pNot(" + not.toString()+")";
    }
}
