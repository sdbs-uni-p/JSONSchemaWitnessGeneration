package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import de.uni_passau.sds.patterns.REException;

import java.util.Collection;

public class TestPatterns {
    public static void main(String[] args) throws REException {
        String pattern = "^.$" ;//"^.{0,2}$" ; // "^.{0,2}$";  //"^stable|not";
        ComplexPattern p = ComplexPattern.createFromRegexp(pattern);
        Collection<String> string = p.generateWords();
        System.out.println("size=" + string.size() + " domain size=" + p.domainSize());
        string.forEach(s->System.out.println(s));
//        Iterator<String> it = string.iterator();
//
//        for(int i = 0; i < 10 && it.hasNext(); i++)
//            System.out.println(it.next());
    }
}
