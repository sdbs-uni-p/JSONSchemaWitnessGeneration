package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Selected schemas from Schemastore: https://www.schemastore.org/json/
 * @author Stefanie 
 *
 */
public class GenSchemaStoreTests {

	private GenAndValidate genVal = 
			new GenAndValidate(System.getProperty("user.dir")+"/witnessGenTestfiles/schemastore/");
	
    @Test
    public void testTSD() throws Exception {
        String witness = genVal.genWitness("tsd.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("tsd.json",witness));
    }
    
    @Test
    public void testBizTalkServerApplicationSchema() throws Exception {
        String witness = genVal.genWitness("BizTalkServerApplicationSchema.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("BizTalkServerApplicationSchema.json",witness));
    }

    @Test
    public void testAnsibleCollectionGalaxy() throws Exception {
        String witness = genVal.genWitness("ansible-collection-galaxy.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("ansible-collection-galaxy.json",witness));
    }
    
    @Test
    public void testAnsibleInventory() throws Exception {
        String witness = genVal.genWitness("ansible-inventory.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("ansible-inventory.json",witness));	
    }
    
    @Test
    // TODO - long runtime?
    public void testJscsrc() throws Exception {
        String witness = genVal.genWitness("jscsrc.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("jscsrc.json",witness));	
    }
    
    @Test
    // TODO - long runtime?
    public void testSarif() throws Exception {
        String witness = genVal.genWitness("sarif.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("sarif.json",witness));	
    }
    
    @Test
    // TODO - long runtime?
    public void testExpo() throws Exception {
        String witness = genVal.genWitness("expo-40.0.0.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("expo-40.0.0.json",witness));	
    }


    @Test
    // TODO: check items notElim
    public void testSiteAssociation() throws Exception {
        String witness = genVal.genWitness("apple-app-site-association.json");
        System.out.println(witness);
        assertEquals(0,genVal.validateWitness("apple-app-site-association.json",witness));
    }
}
