package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.AlgebraParserElement;

/**
 * Interface to represented json primitive types. Needed by the parsing operation
 */
public interface AntlrValue extends AlgebraParserElement{
	
	public Object getValue();
	
}
