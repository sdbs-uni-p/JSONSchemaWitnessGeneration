package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.ANTLR4;

public class AntlrBoolean implements AntlrValue{
	private boolean value;
	
	public AntlrBoolean(boolean value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "AntlrBoolean [" + value + "]";
	}

	@Override
	public Boolean getValue() {
		return value;
	}

}
