package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferencesTest {

	private GenAndValidate genVal = 
			new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/references/");

	@Test
	public void test1() throws Exception {
		String witness = genVal.genWitness("1.json");
		System.out.println(witness);
		assertEquals(0,genVal.validateWitness("1.json",witness));
	}
	
	@Test
	// TODO Cannot invoke "java.lang.Integer.intValue()" because "this.tupleLength" is null
	public void test2() throws Exception {
		String witness = genVal.genWitness("2.json");
		System.out.println(witness);
		assertEquals(0,genVal.validateWitness("2.json",witness));
	}
}
