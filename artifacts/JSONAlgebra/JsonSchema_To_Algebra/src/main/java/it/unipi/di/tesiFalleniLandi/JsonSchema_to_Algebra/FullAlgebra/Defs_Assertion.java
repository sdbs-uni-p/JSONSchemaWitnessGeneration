package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessPattReqManager;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVarManager;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Defs_Assertion implements Assertion{
	private HashMap<String, Assertion> defs; // all the definitions
	private String rootDef; // name of the main definition
	//private Defs_Assertion env = null; //used by pattOfS //TODO: Attenzione!!!!!!!!!!!
	private WitnessVarManager varManager;
	private WitnessPattReqManager pattReqManager;

	private static Logger logger = LogManager.getLogger(Defs_Assertion.class);

	public Defs_Assertion() {
		logger.trace("Creating an empty Defs_Assertion");
		//env = this;
		defs = new HashMap<>();
		varManager = new WitnessVarManager();
		pattReqManager = new WitnessPattReqManager();
	}
	
	public void add(String key, Assertion value) throws ParseCancellationException {
		if(defs.containsKey(key)) {
			throw new ParseCancellationException("Detected 2 defs with the same name :" + key);
		}
		defs.put(key, value);
	}
	
	public void setRootDef(String rootDefName, Assertion rootDef) {
		logger.trace("Setting {}={} as rootdef", rootDefName);
		add(rootDefName, rootDef);
		this.rootDef = rootDefName;
	}

	public void addAll(Defs_Assertion defs){
		logger.trace("Adding {} as defs", defs);
		this.defs.putAll(defs.defs);
	}

	@Override
	public JsonElement toJSONSchema(WitnessVarManager rootVar) {

		JsonElement obj = new JsonObject();
		if(rootDef != null)
			obj = defs.get(rootDef).toJSONSchema(varManager);

		JsonObject jsonDefs = new JsonObject();
		for(Entry<String, Assertion> a : defs.entrySet())
			if(!a.getKey().equals(rootDef))
				jsonDefs.add(a.getKey(), a.getValue().toJSONSchema(varManager));

		if(obj instanceof JsonObject)
			((JsonObject)obj).add("$defs", jsonDefs);

		return obj;
	}

	@Override
	public Assertion not() {
		Defs_Assertion not = this.notElimination();
		not.rootDef = AlgebraStrings.NOT_DEFS + not.rootDef;

		return not;
	}

	@Override
	public Defs_Assertion notElimination() {
		Defs_Assertion returnDef = new Defs_Assertion();

		//Complete the not
		for(Entry<String, Assertion> entry : defs.entrySet()) {
			returnDef.defs.put(entry.getKey(), entry.getValue().notElimination());

			//case negation of not_x --> x
			if(entry.getKey().startsWith(AlgebraStrings.NOT_DEFS))
				continue;

			if (!defs.containsKey(AlgebraStrings.NOT_DEFS+entry.getKey()))
				returnDef.defs.put(AlgebraStrings.NOT_DEFS+entry.getKey(), entry.getValue().notElimination().not());
		}

		returnDef.rootDef = rootDef;

		return returnDef;
	}

	@Override
	public String toGrammarString() {
		StringBuilder def = new StringBuilder();

		if(defs != null) {
			Set<Entry<String, Assertion>> entrySet = defs.entrySet();

			def.append("\""+rootDef+"\""+" defs [\r\n");

			def.append("\"" + rootDef + "\"" + " : ").append(defs.get(rootDef).toGrammarString());
			for (Entry<String, Assertion> entry : entrySet) {
				String tmp = entry.getValue().toGrammarString();
				if (tmp.isEmpty()) continue;  //
				if (!entry.getKey().equals(rootDef)) {
					def.append(AlgebraStrings.COMMA + "\"" + entry.getKey() + "\"" + " : " + tmp);
				}
			}
		}

		if(def.length() == 0) return "";
		def.substring(AlgebraStrings.COMMA.length());
		def.append("\r\n]");
		return def.toString();
	}

	@Override
	public WitnessEnv toWitnessAlgebra(WitnessVarManager varManager, Defs_Assertion env, WitnessPattReqManager pattReqManager) throws REException {
		WitnessEnv newEnv = new WitnessEnv(this.varManager, this.pattReqManager);

		for(Entry<String, Assertion> entry : defs.entrySet())
			if(entry.getKey().equals(rootDef))
				newEnv.setRootVar(entry.getKey(), entry.getValue().toWitnessAlgebra(this.varManager, this, this.pattReqManager));
			else
				newEnv.addOrUnifyAndAddToBeNotEliminated(this.varManager.buildVar(entry.getKey()), entry.getValue().toWitnessAlgebra(this.varManager, this, this.pattReqManager));


		newEnv.checkLoopRef(null, null);
		newEnv.buildOBDD_notElimination();
		newEnv.reachableRefs(null, null);

		return newEnv;
	}

	public Assertion getDef(String ref){
		return defs.get(ref);
	}

	public String getRootName(){
		return rootDef;
	}
}
