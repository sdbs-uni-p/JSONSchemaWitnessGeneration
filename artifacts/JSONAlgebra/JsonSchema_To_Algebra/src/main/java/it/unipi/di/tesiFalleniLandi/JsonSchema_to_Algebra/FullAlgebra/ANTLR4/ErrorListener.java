package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.BitSet;

public class ErrorListener implements ANTLRErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) throws ParseCancellationException {
        System.out.println("ERRORE RILEVATO syntax: ");
        System.out.println("\triga: "+ i +"\r\n\tcolonna:"+  i1 + "\r\n\t\t"+ s);
        throw new ParseCancellationException("line " + i + ":" + i1 + " " + s);
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {
        //System.out.println("ERRORE RILEVATO ambiguo");
        //System.out.println("\triga: "+ i +"\r\n\tcolonna:"+  i1 + "\r\n\t\t"+ atnConfigSet.getAlts().toString());
    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {
        /*System.out.println("ERRORE RILEVATO full context");
        System.out.println("\triga: "+ i +"\r\n\tcolonna:"+  i1 + "\r\n\t\t"+ bitSet);*/
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {
        /*System.out.println("ERRORE RILEVATO context sensitivity");
        System.out.println("\triga: "+ i +"\r\n\tcolonna:"+  i1 + "\r\n\t\t"+ atnConfigSet);*/
    }
}