package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GenTypedAssertion implements GenAssertion{
    private static Logger logger = LogManager.getLogger(GenTypedAssertion.class);

    @Override
    public String toString() {
        return "GenTypedAssertion{" +
                "witness=" + witness +
                ", typedAssertionMap=" + typedAssertionMap +
                '}';
    }

    private JsonElement witness;
//    private List<GenAssertion> typedAssertion;
    //the external map corresponds to disjunction
    //the internal list corresponds to conjunction (typedGroup)
    private HashMap<String,List<GenAssertion>> typedAssertionMap; //organize assertions by their type
    private Class[] orderedTypes = {GenBool.class, GenVarFalse.class, GenVarTrue.class, GenNum.class, GenString.class, GenArray.class, GenObject.class, GenNull.class};
//    private Class[] orderedTypes = { GenArray.class, GenObject.class,GenNum.class, GenString.class,GenBool.class, GenVarFalse.class, GenVarTrue.class,GenNull.class};
    private String[] orderedTypeNames = Arrays.stream(orderedTypes).map(t->t.getSimpleName()).toArray(String[]::new);




    @Override
    public JsonElement getWitness() {
        return witness;
    }


    /**
     * handy function
     * @return
     */
    public Integer size(){
        return typedAssertionMap.values().size();
    }
    /**
     * handy function
     * @return
     */
    public HashMap<String,Integer> getLength(){
        HashMap<String,Integer> res = new HashMap<>();
        typedAssertionMap.forEach((t,d)->res.put(t,d.size())); //type,disjunction
        return res;
    }



    /**
     * handy function
     * @return
     */
    public List<GenAssertion> getTypedAssertion() {
//        return new ArrayList<>(typedAssertionMap.values());
        return typedAssertionMap.values().stream()
                .flatMap(l->l.stream()).collect(Collectors.toList());
    }

    //dummy typed assertion
    public GenTypedAssertion(){

    }
    public GenTypedAssertion(List<GenAssertion> typedAssertion) {
        typedAssertionMap = new HashMap<>();
//        typedAssertion.stream().collect(Collectors.groupingBy(t->t.getClass().getSimpleName()));
        typedAssertionMap.putAll(typedAssertion.stream().collect(Collectors.groupingBy(t->t.getClass().getSimpleName())));
    }

    public boolean containsBaseType() {
        return typedAssertionMap.containsKey(GenNull.class.getSimpleName())||
                typedAssertionMap.containsKey(GenNum.class.getSimpleName())||
                typedAssertionMap.containsKey(GenString.class.getSimpleName());
    }


    /**
     * Predefined order given by orderedTypeNames
     * @return
     */
    public statuses generateWithFixedOrderOfTypes() {
        statuses status;
        for(String t :orderedTypeNames)
        {
            if(typedAssertionMap.containsKey(t))
            {
                GenAssertion g = typedAssertionMap.get(t).get(0); //pick the first branch of the union, if any
                status = g.generate();
                if(status==statuses.Populated){
                    witness = g.getWitness();
                    return status;
                }
            }
       }
        return statuses.Empty;
    }


    /**
     * examines  branches until reaching the end or generating a witness
     * @return
     */
    public statuses generateExhaustiveWithFixedOrderOfTypes() {
//        logger.debug("");
        statuses currentStatus;
        List<statuses> statusList = new ArrayList<>();
        for(String t :orderedTypeNames)
        {
            if(typedAssertionMap.containsKey(t))
            {
                //union of assertions with potentially n branches
                List<GenAssertion> genAssertions= typedAssertionMap.get(t);
                GenAssertion genAssertion;
                for(int i = 0; i< genAssertions.size() ; i++){
                    logger.debug("Exploring branch #{}", i);
                    genAssertion = genAssertions.get(i);
                    currentStatus = genAssertion.generate();
                    logger.debug("status {}", currentStatus);
                    statusList.add(currentStatus);
                    if(currentStatus==statuses.Populated) {
                        witness = genAssertion.getWitness();
                        logger.debug("witness {}", witness);
                        return currentStatus;
                    }
                }
            }
        }
        if(statusList.contains(statuses.Open))
            return statuses.Open;
        else
            return statuses.Empty;
    }


    /**
     * Pre-condition: no object or array group contains an open variable
     * Post-condition: returns at least one witness
     * Pick a random List of GenAssertion and pick a random branch of the union, if any
     * @return
     */
    public statuses generateWithRandomOrder() {
        JsonElement result = null;
        statuses status;
        //retrieves a type randomly
        int size = typedAssertionMap.size();
        Random r = new Random();
        int typeIndex = r.nextInt(size);
        List<GenAssertion> lg =  (List<GenAssertion>) typedAssertionMap.values().toArray()[typeIndex];
        int branchIndex = r.nextInt(lg.size());
        GenAssertion g = lg.get(branchIndex);
        status = g.generate();
        if(status==statuses.Populated)
            witness = g.getWitness();
        return status;
    }

    @Override
    public statuses generate() {
//        return generateWithRandomOrder();
//        return generateWithFixedOrderOfTypes();
        return generateExhaustiveWithFixedOrderOfTypes();
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
        return typedAssertionMap.values().stream()
                .flatMap(genAssertions->genAssertions.stream())
                .flatMap(genAssertion -> genAssertion.usedVars().stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
