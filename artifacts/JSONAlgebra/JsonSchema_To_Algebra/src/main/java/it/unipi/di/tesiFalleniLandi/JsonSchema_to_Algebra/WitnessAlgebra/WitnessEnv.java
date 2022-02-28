package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Defs_Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.Exceptions.WitnessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WitnessEnv implements WitnessAssertion {
    private static Logger logger = LogManager.getLogger(WitnessEnv.class);

    private BiMap<WitnessVar, WitnessAssertion> varList; //map of definitions <variable, value> (included root and not_root)
    private BiMap<WitnessVar, WitnessAssertion> sortedVarList; //map of definitions <variable, value> (included root and not_root)
    private BiMap<WitnessVar, WitnessVar> coVar; //maintain the relation between the variables and the complement variables
    private WitnessVar rootVar; //indicate the root variable
    protected WitnessBDD bdd;
    protected WitnessVarManager variableNamingSystem;
    protected WitnessPattReqManager pattReqManager;
    private LinkedList<WitnessVar> varToBeNotEliminated; // contains the variables that must be processed by notElimination + buildOBDD

    public int getSize(){
        return varList.size();
    }
    public Map<WitnessVar, WitnessAssertion> getVarList() {
        return varList;
    }

    public BiMap<WitnessVar, WitnessVar> getCoVar() {
        return coVar;
    }

    public WitnessVar getRootVar() {
        return rootVar;
    }

    private WitnessEnv(){
        varList = HashBiMap.create();
        sortedVarList = HashBiMap.create();
        coVar = HashBiMap.create();
        varToBeNotEliminated = new LinkedList<>();
    }

    public WitnessEnv(WitnessVarManager manager, WitnessPattReqManager pattReqManager){
        varList = HashBiMap.create();
        sortedVarList = HashBiMap.create();
        coVar = HashBiMap.create(); //init BiMap
        varToBeNotEliminated = new LinkedList<>();
        variableNamingSystem = manager;
        bdd = new WitnessBDD(variableNamingSystem);
        varList.put(bdd.getFalseVar(), new WitnessBoolean(false));
        varList.put(bdd.getTrueVar(), new WitnessBoolean(true));
        coVar.put(bdd.getFalseVar(), bdd.getTrueVar());
        this.pattReqManager = pattReqManager;

        logger.trace("Creating an empty WitnessEnv");
    }

    /*public Map.Entry<WitnessVar, WitnessVar> addWithComplement(WitnessAssertion value) {
        if(value.getClass() == WitnessAnd.class && ((WitnessAnd) value).getIfUnitaryAnd() != null)
            value = ((WitnessAnd) value).getIfUnitaryAnd();

        logger.trace("Adding with complement {}", value);

        WitnessVar var = variableNamingSystem.buildVar(variableNamingSystem.getName(value));
        add(var, value);

        buildOBDD_notElimination(); //complete the variable and build obdd

        logger.trace("Added {} and its complement {}", var, getCoVar(var));

        return new AbstractMap.SimpleEntry<>(var, getCoVar(var));
    }*/

    /**
     *
     * @param key
     * @param value
     * @return if add a key-value newX:S pair to "this", unless S=oldY, in which case "newX" is renamed everywhere to oldY
     *         moreover, if oldY:S is already present, then in this case it is "oldY" that is rename to "newX"
     *         finally, if no renaming is performed, then newX is added to variablesToBeElaborated
     */
    public WitnessVar addOrUnifyAndAddToBeNotEliminated(WitnessVar key, WitnessAssertion value){
//        String s = String.format("Adding to env %1s", key);
        logger.trace("Adding to env <{}, {}>", key, value);
//        System.out.println(s);

        //unitary and
        if(value instanceof WitnessAnd && ((WitnessAnd)value).getIfUnitaryAnd() != null) {
            return addOrUnifyAndAddToBeNotEliminated(key, ((WitnessAnd) value).getIfUnitaryAnd());
        }
        else if (value instanceof WitnessOr && ((WitnessOr)value).getIfUnitaryOr() != null ){
            return addOrUnifyAndAddToBeNotEliminated(key,((WitnessOr)value).getIfUnitaryOr());
        }
        //variable composed by only a variable (newVar=ref("var"))
        else if(value instanceof WitnessVar){
            variableNamingSystem.rename(key, (WitnessVar) value);
            return (WitnessVar) value;
        }
        else {
            WitnessVar keyPresent = varList.inverse().get(value);
            if(keyPresent != null){

                if(keyPresent.equals(key)) return key;
                //we have the same variable "body", but with 2 different names, we keep the one already present in the varList

                varList.remove(key);
                //coVar.remove(key);
                //coVar.inverse().remove(key);
                // substitute all instances of "key" with "keyPresent", hence, in this case
                // the old name survives
                variableNamingSystem.rename(key, keyPresent);
                return keyPresent;
            } else {
                varList.put(key, value);
                varToBeNotEliminated.add(key); // this add is not needed in the "then" branch, since it
                // recursively calls add, and at the next call we arrive here
                //check WitnessVarManager count
                String[] splittedName = key.getName().split("_");
                try {
                    int count = Integer.parseInt(splittedName[splittedName.length - 1]);
                    variableNamingSystem.setCountMin(count + 1);
                } catch (NumberFormatException e) { }

                return key;
            }
        }

    }

    public WitnessVar addUnify(WitnessVar key, WitnessAssertion value) {
        WitnessVar keyPresent = varList.inverse().get(value);
        if (keyPresent != null) {
            if (keyPresent.equals(key))
                return key;
            //we have the same variable "body", but with 2 different names, we keep the one already present in the varList
            varList.remove(key);
            variableNamingSystem.rename(key, keyPresent);
            return keyPresent;
        } else {
            varList.put(key, value);
            return key;
        }
    }
    public void buildOBDD_notElimination(){

        try {
            notElimination_dumb();
        } catch (WitnessException | REException e) {
            e.printStackTrace();
        }
        buildOBDD();
    }

    /**
     * Associate to each variable one obdd (also trivial).
     * There are two cases of variable:
     *      - variables that are not boolean expression of variables.
     *          In this case we create a trivial obbd
     *
     *      - variables that are boolean espression
     *          in this case we put all those variables in a list.
     *          Then we sort the list (asc, order by the number of variable in the body without obdd) and then create the associated obbd.
     *          if the obbd cannot be created due to a variable that has not yet been associated to an obbd,
     *          then we move the element in the tail of the list and proceed with the rest.
     */

    //TODO: to remove iterazioniSenzaDiminuzione we can count the var without repetitions and run checkForloopRef()???
    private void buildOBDD() {
        logger.trace("Building obdd");

         //for each variable whose body is not a boolean expression define a trivial BDD
        for (Map.Entry<WitnessVar, WitnessAssertion> varDef : varList.entrySet()) {
           WitnessVar var = varDef.getKey();
            if (!bdd.contains(var) && !varList.get(var).isBooleanExp()) {
                     bdd.createVar(var);
                }
        }

        //for each variable whose body is a boolean expression compute a degree such that
        //when x depends on y then degree(x) > degree(y)
        LinkedList<Map.Entry<WitnessVar, Float>> booleanExpressions = new LinkedList<>();
        for (Map.Entry<WitnessVar, WitnessAssertion> varDef : varList.entrySet()) {
             WitnessVar var = varDef.getKey();
            if (!bdd.contains(var) && varList.get(var).isBooleanExp()) {
                Float varWithoutBDD = varList.get(var).countVarWithoutBDD(this, new LinkedList<>());
                booleanExpressions.add(new AbstractMap.SimpleEntry<>(var, varWithoutBDD));
            }
        }

        //booleanExpressions list gets sorted so that if x depends on y then x will be visited before y
        Collections.sort(booleanExpressions, (witnessVarIntegerEntry, t1) -> (int) (witnessVarIntegerEntry.getValue() - t1.getValue()));

        //to remove easily the element from booleanExpressions list we copy only the variable names in a tmp list
        LinkedList<WitnessVar> booleanExpressionsTemp = new LinkedList<>();
        for(Map.Entry<WitnessVar, Float> entry : booleanExpressions)
            booleanExpressionsTemp.add(entry.getKey());

        int iterazioniSenzaDiminuzione = 0;

        while (booleanExpressionsTemp.size() != 0) {
            logger.trace("booleanExpression.size = {}; itWithoutDec= {}", booleanExpressions.size(), iterazioniSenzaDiminuzione);

            //no BDD can be created if the current variable is not forced
            //it should happen when we have an un-guarded loop between variable
            // def "x" = ref("y"),
            // def "y" = ref("x")
            if (iterazioniSenzaDiminuzione == booleanExpressions.size()) { //TODO: raise an Exception
                // (forcing the current??)
                Map.Entry<WitnessVar, Float> first = booleanExpressions.removeFirst();
                first.getKey().forceOBDD(this, variableNamingSystem);
                booleanExpressions.addLast(first);
                iterazioniSenzaDiminuzione = 0;
                logger.warn("Forcing new obdd {}, that should not have happened", first.getKey());
                continue;
            }

            WitnessVar first = booleanExpressionsTemp.removeFirst();

            try {
                WitnessAssertion tmp = varList.get(first);
                WitnessVar obddName = tmp.buildOBDD(this, variableNamingSystem);

                //the variable already exists -- > it can be renamed
                if(varList.containsKey(obddName)){
                    //varToBeElaborated.remove(first.getKey());
                    logger.trace("Renaming variable (already exists with the same semantics) from {} to {}", first, obddName);

                    varList.remove(first);

                    WitnessVar complName = null;
                    if(coVar.containsKey(first) )
                        complName = coVar.remove(first);
                    else
                        complName = coVar.inverse().remove(first);

                    bdd.rename(first, obddName);
                    variableNamingSystem.rename(first, obddName);

                    //same for the complement if is not used by other variables
                    varList.remove(complName);
                    bdd.rename(complName, getCoVar(obddName));
                    booleanExpressionsTemp.remove(complName);
                    booleanExpressionsTemp.remove(getCoVar(obddName));
                    variableNamingSystem.rename(complName, getCoVar(obddName));


                }else {
                    bdd.rename(obddName, first);
                    logger.trace("Renaming the string associated to obdd with the variable name. from {} to {}", obddName, first);
                }

                iterazioniSenzaDiminuzione = 0;
            } catch (WitnessException ex) {
                //trying to create obbd of a boolean expression that contains variable with no obbd associated
                logger.catching(ex);
                booleanExpressionsTemp.addLast(first);
                logger.trace("ref to a var without obdd in {}", first);
                iterazioniSenzaDiminuzione++;

                throw new RuntimeException("mutual recursion");
            }
        }
    }


    public WitnessAssertion getDefinition(WitnessVar var){
        if(!varList.containsKey(var))
            throw new RuntimeException("Schema with definition-reference not completed: "+var.getName()+". that should not have happened");
        return varList.get(var);
    }

    public WitnessVar getCoVar(WitnessVar name){
        if(!varToBeNotEliminated.isEmpty()){
            buildOBDD_notElimination();
        }
        if(coVar.containsKey(name)){
            return coVar.get(name);
        }else if(coVar.inverse().containsKey(name)){
            return coVar.inverse().get(name);
        }else {
            throw new RuntimeException("var not found in coVar: " + name);
        }
    }

    public void setRootVar(String key, WitnessAssertion value) {
        WitnessVar var = variableNamingSystem.buildVar(key);
        addOrUnifyAndAddToBeNotEliminated(var, value);
        rootVar = var;
    }

    public boolean containsVar(WitnessVar var){
        return varList.containsKey(var);
    }

    @Override
    public String toString() {
        return "Env{" +
                "var=" + varList +
                ", rootVar=" + rootVar +
                '}';
    }

    @Override
    public void checkLoopRef(WitnessEnv env, Collection<WitnessVar> varList) throws RuntimeException {
        for(Map.Entry<WitnessVar, WitnessAssertion> entry : this.varList.entrySet()) {
            varList = new HashSet<>();
            varList.add(entry.getKey());
            entry.getValue().checkLoopRef(this, varList);
        }
    }

    @Override
    public void reachableRefs(Set<WitnessVar> collectedVar, WitnessEnv env) throws RuntimeException {
        Set<WitnessVar> visitedVar = new HashSet<>();
        for(Map.Entry<WitnessVar, WitnessAssertion> var : varList.entrySet())
            if(!visitedVar.contains(var.getKey())) //We skip all the variable that we already visit
                var.getValue().reachableRefs(visitedVar, this);
    }

    //for every var in varToBeElaborated we first put <varName, coVarName> into covar
    //then we compute the body of the new value
    //We need to do in this way becasue .not() method on a variable use the coVar to compute the complement name
    //in this way we can manage definitions like
    // def x = items[y]
    // def y = items[x]
    private void notElimination_dumb() throws WitnessException, REException {
        if(varToBeNotEliminated.isEmpty()) return; //Nothing to do

        LinkedList<Map.Entry<WitnessVar, WitnessVar>> toBeCompleted = new LinkedList<>(); //positiveName, negativeName (the one we have to compute the negation)

        for(WitnessVar var : varToBeNotEliminated){
            if(coVar.containsKey(var) || coVar.containsValue(var))//TODO: evito di aggiungere entry duplicate nella coVar
                continue;

            String complementName = AlgebraStrings.NOT_DEFS + var.getName();

            if (var.getName().startsWith(AlgebraStrings.NOT_DEFS))
                complementName = var.getName().substring(AlgebraStrings.NOT_DEFS.length());

            WitnessVar complementVarName = variableNamingSystem.buildVar(complementName);

            if(!varList.containsKey(complementVarName)) { //if complementVarName is a new variable
                toBeCompleted.add(new AbstractMap.SimpleEntry<>(var, complementVarName));
            }else if(coVar.containsKey(complementVarName) || coVar.containsValue(complementVarName)){
                //complementVarName is not a new var and is in the coVar

                WitnessVar positiveVar = coVar.get(complementVarName); //i can't use getCoVar (stackOverflow)
                if(positiveVar == null) positiveVar = coVar.inverse().get(complementVarName);
                varList.remove(var);
                variableNamingSystem.rename(var, positiveVar);
            }

            coVar.put(var, complementVarName);


        }

        varToBeNotEliminated = new LinkedList<>();

        for(Map.Entry<WitnessVar, WitnessVar> entry : toBeCompleted){
            WitnessVar positiveVar = entry.getKey();
            WitnessVar negativeVar = entry.getValue();

            WitnessAssertion negativeVarValue = varList.get(coVar.inverse().get(negativeVar)).not(this);

            WitnessVar eventualNegativeVarDuplicateName = varList.inverse().get(negativeVarValue); //CL: this name is terrible, i know...

            if(eventualNegativeVarDuplicateName != null){ // the negation is already present, this value and his "positive" twin can be substituted
                varList.remove(positiveVar);
                coVar.remove(positiveVar);
                WitnessVar eventualPositiveVarDuplicateName = getCoVar(eventualNegativeVarDuplicateName);
                variableNamingSystem.rename(positiveVar, eventualPositiveVarDuplicateName);
                variableNamingSystem.rename(negativeVar, eventualNegativeVarDuplicateName);

                continue;
            }

            varList.put(negativeVar, negativeVarValue);
        }
    }

    /**
     * Complete each variable with his negation and update the coVar map
     * @throws WitnessException
     * @throws REException
     */
    /*private void notElimination() {
        if(varToBeElaborated.isEmpty()) return; //Nothing to do

        logger.trace("Performing notElimination");

        HashSet<WitnessVar> completed = new HashSet<>(); //set of completed variable
        LinkedList<Map.Entry<WitnessVar, WitnessAssertion>> newVar = new LinkedList<>(); //to avoid concurrentModificationException
        LinkedList<WitnessVar> tempVariables = new LinkedList<>();

        int iterazioniSenzaDiminuzione = 0;
        while (varToBeElaborated.size() != 0){
            logger.trace("notElimination: list.size = {}; itWithoutDec= {}", varToBeElaborated.size(), iterazioniSenzaDiminuzione);

             \\* example:    ****
             \\* def "x" = props["a": ref("y");],
             \\* def "y" = ref("x")
             \\*
             \\* create a "temp" var x <--> not_x without value.
             \\* when this loop ends, we compute the body for those var (listed in tempVariables)

            if(iterazioniSenzaDiminuzione == varToBeElaborated.size()){
                WitnessVar first = varToBeElaborated.removeFirst();

                assert(first.isRecursive(this, new LinkedList<>()));

                String complementName = AlgebraStrings.NOT_DEFS + first.getName();

                if (first.getName().startsWith(AlgebraStrings.NOT_DEFS))
                    complementName = first.getName().substring(AlgebraStrings.NOT_DEFS.length());

                WitnessVar complementVarName = variableNamingSystem.buildVar(complementName);

                if(coVar.containsValue(complementVarName)){ //type[all].not() == false --> so type[all] == true, i can remove it
                    varList.remove(first);
                    variableNamingSystem.rename(first, coVar.inverse().get(complementVarName));
                    continue;
                }
                coVar.put(first, complementVarName);
                tempVariables.add(complementVarName);

                logger.debug("notElimination: Forcing new var {}", complementVarName);

                varToBeElaborated.addLast(first);
                iterazioniSenzaDiminuzione = 0;
                continue;
            }

            WitnessVar key = varToBeElaborated.removeFirst();
            WitnessAssertion value = varList.get(key);

            try {

                if (completed.contains(key) || coVar.containsKey(key) || coVar.containsValue(key)){ // if name is already completed --> skip
                    iterazioniSenzaDiminuzione = 0;
                    continue;
                }

                completed.add(key);

                //compute the name of the complement
                String complementName = AlgebraStrings.NOT_DEFS + key.getName();

                if (key.getName().startsWith(AlgebraStrings.NOT_DEFS))
                    complementName = key.getName().substring(AlgebraStrings.NOT_DEFS.length());
                WitnessVar complementVarName = variableNamingSystem.buildVar(complementName);

                //does the complement already exist?
                WitnessAssertion not = value.not(this);
                WitnessVar obddName = null;

                if(not.isBooleanExp())
                    obddName = not.buildOBDD(this, variableNamingSystem);
                else
                    obddName = bdd.createVar(); //create a variable that represents it

                if (varList.containsKey(obddName)) {
                    if(coVar.containsValue(obddName)) {
                        varList.remove(key);
                        variableNamingSystem.rename(key, coVar.inverse().get(obddName));
                    }else
                        coVar.put(key, obddName);
                    completed.add(obddName);
                    variableNamingSystem.rename(complementVarName.getName(), obddName.getName());
                    iterazioniSenzaDiminuzione = 0;
                    continue;
                }

                if (varList.containsKey(complementVarName)) {
                    completed.add(complementVarName);
                    coVar.put(key, complementVarName);
                } else {
                    coVar.put(key, complementVarName);
                    newVar.add(new AbstractMap.SimpleEntry(complementVarName, not));
                }

            }catch (WitnessException | REException ex){
                logger.catching(ex);
                completed.remove(key);
                varToBeElaborated.addLast(key);
                iterazioniSenzaDiminuzione++;
                continue;
            }
        }

        //add the new var
        for(Map.Entry<WitnessVar, WitnessAssertion> entry : newVar)
            varList.put(entry.getKey(), entry.getValue());

        //compute a body for the "temp" var created by ****
        for(WitnessVar var : tempVariables) {
            try {
                varList.put(var, varList.get(getCoVar(var)).not(this));
            } catch (REException e) {
                logger.catching(e);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }*/



    @Override
    public WitnessEnv mergeWith(WitnessAssertion a, WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        throw new UnsupportedOperationException();
    }

    @Override
    public WitnessAssertion merge(WitnessVarManager varManager, WitnessPattReqManager pattReqManager) throws REException {
        WitnessEnv newEnv = cloneNoWitnessVar();

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : varList.entrySet())
            if(entry.getKey().equals(rootVar))
                newEnv.setRootVar(entry.getKey().getName(), entry.getValue().merge(varManager, pattReqManager));
            else
                newEnv.addOrUnifyAndAddToBeNotEliminated(entry.getKey(), entry.getValue().merge(varManager, pattReqManager));

        newEnv.varToBeNotEliminated = new LinkedList<>(varToBeNotEliminated);

        return newEnv;
    }

    @Override
    public WitnessType getGroupType() {
        return null;
    }

    @Override
    public Assertion getFullAlgebra() {
        Defs_Assertion defs = new Defs_Assertion();

        if(rootVar != null) defs.setRootDef(rootVar.getName(), varList.get(rootVar).getFullAlgebra());

        Set<Map.Entry<WitnessVar, WitnessAssertion>> entrySet = varList.entrySet();
        for(Map.Entry<WitnessVar, WitnessAssertion> entry : entrySet) {
            if(!entry.getKey().equals(rootVar))
                defs.add(entry.getKey().getName(), entry.getValue().getFullAlgebra());
        }

        return defs;
    }

    @Override
    public WitnessEnv clone() {
        logger.debug("Cloning WitnessEnv");

        WitnessEnv clone = cloneNoWitnessVar();

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : varList.entrySet())
            clone.varList.put(entry.getKey(), entry.getValue().clone());

        return clone;
    }

    private WitnessEnv cloneNoWitnessVar(){
        WitnessEnv clone = new WitnessEnv();

        clone.rootVar = rootVar;
        clone.coVar = coVar;
        clone.bdd = bdd;
        clone.varToBeNotEliminated = new LinkedList<>(varToBeNotEliminated);
        clone.variableNamingSystem = variableNamingSystem;
        clone.pattReqManager = pattReqManager;

        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitnessEnv that = (WitnessEnv) o;

        if(! Objects.equals(that.rootVar, this.rootVar))
            return false;

        if(that.varList.size() != this.varList.size())
            return false;

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : that.varList.entrySet()) {
            if (!this.varList.containsKey(entry.getKey())) return false;
            if(!this.varList.get(entry.getKey()).equals(entry.getValue()))
                return false;
        }

        return true;
    }

    //auto generated code
    @Override
    public int hashCode() {
        int result = varList != null ? varList.hashCode() : 0;
        result = 31 * result + (rootVar != null ? rootVar.hashCode() : 0);
        return result;
    }

    @Override
    public WitnessAssertion not(WitnessEnv env) throws REException {
        buildOBDD_notElimination();

        WitnessEnv clone = this.clone();
        clone.rootVar = coVar.get(rootVar);

        return clone;
    }

    @Override
    public WitnessEnv groupize() throws WitnessException, REException {
        WitnessEnv env = cloneNoWitnessVar();

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : this.varList.entrySet()) {
            if(entry.getValue() instanceof WitnessAnd || entry.getValue() instanceof WitnessBoolean)
                //env.varList.put(entry.getKey(), entry.getValue().groupize());
                env.addOrUnifyAndAddToBeNotEliminated(entry.getKey(), entry.getValue().groupize());
            else {
                WitnessAnd and = new WitnessAnd();
                and.add(entry.getValue());
                //env.varList.put(entry.getKey(), and.groupize());
                env.addOrUnifyAndAddToBeNotEliminated(entry.getKey(), and.groupize());
            }
        }

        return env;
    }

    @Override
    public Float countVarWithoutBDD(WitnessEnv env, List<WitnessVar> visitedVar) {
        Float count = 0f;

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : this.varList.entrySet())
            count += entry.getValue().countVarWithoutBDD(this, new LinkedList<>());

        return count;
    }

    @Override
    public int countVarToBeExp(WitnessEnv env) {
        return 0;
    }

    @Override
    public List<Map.Entry<WitnessVar, WitnessAssertion>> varNormalization_separation(WitnessEnv env, WitnessVarManager varManager) throws WitnessException, REException {
        List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();
        List<String> checkList = new LinkedList<>();

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : this.varList.entrySet())
            newDefinitions.addAll(entry.getValue().varNormalization_separation(this, this.variableNamingSystem));

        //update the environment with the new definitions
        for(Map.Entry<WitnessVar, WitnessAssertion> entry : newDefinitions){
            this.addOrUnifyAndAddToBeNotEliminated(entry.getKey(), entry.getValue());
            checkList.add(entry.getKey().getName());
        }

        buildOBDD_notElimination();

        for(int i = newDefinitions.size() -1; i >= 0; i--){
            if(checkList.contains(newDefinitions.get(i).getKey().getName())) {
                WitnessVar covar = getCoVar(newDefinitions.get(i).getKey());
                //varList.put(covar, getDefinition(covar).groupize()); //We do not use the add method because the variables are already elaborated
                this.addOrUnifyAndAddToBeNotEliminated(covar, getDefinition(covar).groupize());
            }else
                newDefinitions.remove(i);
        }

        return newDefinitions;
    }

    @Override
    public WitnessEnv varNormalization_expansion(WitnessEnv env) throws WitnessException {
        WitnessEnv newEnv = cloneNoWitnessVar();
        LinkedList<Map.Entry<WitnessVar, Integer>> orderedList = new LinkedList<>(); //orderList: pair list <varName, #varToBeExpanded>

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : varList.entrySet()) {
            int nVarToBeExp = entry.getKey().countVarToBeExp(this);
            orderedList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), nVarToBeExp));
        }

        Collections.sort(orderedList, Comparator.comparingInt(Map.Entry::getValue));

        //due to sorting, each variable is only expanded with the previous
        for(Map.Entry<WitnessVar, Integer> entry : orderedList) {
            if(entry.getValue() == 0)
                newEnv.addOrUnifyAndAddToBeNotEliminated(entry.getKey(), varList.get(entry.getKey()));
            else
                newEnv.addOrUnifyAndAddToBeNotEliminated(entry.getKey(), varList.get(entry.getKey()).varNormalization_expansion(newEnv));
        }

        newEnv.varToBeNotEliminated = new LinkedList<>(varToBeNotEliminated); //TODO: da fare meglio

        return newEnv;
    }


    /**
     * recursive descent into variables of listVars
     * assign to each var its height, when not already visited
     * leaves unreachable nodes with a null height
     * @param listvars
     * @param currentH
     */
//    public void setVarHeights(List<WitnessVar> listvars, List<WitnessVar> visited, Integer currentH){
//        for(WitnessVar var:listvars){
////            System.out.println("var = "+var.getName());
//            if(!visited.contains(var))
//            {
//                visited.add(var);
//                var.setHeight(currentH);
//                WitnessAssertion wassert = varList.get(var);
//                List<WitnessVar>  l = wassert.uses();
//                setVarHeights(l,visited,currentH+1);
//            }
//        }
//    }


//    public void sortVarListByHeight(){
//        List<WitnessVar> init = new LinkedList<WitnessVar>();
//        init.add(rootVar);
//        setVarHeights(init,new LinkedList<WitnessVar>(),0);
//        List<Map.Entry<WitnessVar, Integer>> sortedList = new LinkedList<>();
//
//        Integer order; //
//        for(WitnessVar var : varList.keySet()) {
//            order = var.getHeight();
//            if(order==null)
//                order= Integer.MAX_VALUE;
//            sortedList.add(new AbstractMap.SimpleEntry<>(var, order));
//        }
//        WitnessVar v;
//        for(Map.Entry<WitnessVar, Integer> entry : sortedList) {
//            v = entry.getKey();
//            sortedVarList.put(v,varList.get(v));
//        }
//        varList = sortedVarList;
//        sortedVarList = null;
//        sortedByHight = true;
//    }

    /**
     * used for testing
     * TODO remove once debugging is finished
     * @return
     * @throws WitnessException
     */
//    public WitnessEnv optimizedDNF() throws WitnessException {
//        WitnessEnv newEnv = cloneNoWitnessVar();
//
//        List<WitnessVar> init = new LinkedList<WitnessVar>();
//        init.add(rootVar);
//        setVarHeights(init, new LinkedList<WitnessVar>(),0);
//        //traverse varList starting from the root downward
//
//        List<Map.Entry<WitnessVar, Integer>> sortedVarList = new LinkedList<>();
//
//        Integer order; //
//        for(WitnessVar var : varList.keySet()) {
//            order = var.getHeight();
//            if(order==null)
//                order= Integer.MAX_VALUE;
//            sortedVarList.add(new AbstractMap.SimpleEntry<>(var, order));
//        }
//
//        Collections.sort(sortedVarList, Comparator.comparingInt(Map.Entry::getValue));
////        Collections.reverse(sortedVarList);
//
//        for(Map.Entry<WitnessVar, Integer> entry : sortedVarList) {
//            newEnv.addOrUnifyAndAddToBeNotEliminated(entry.getKey(),varList.get(entry.getKey()).DNF());
//            System.out.println(String.format("processing %1s ", entry.getKey()));
//        }
//
//        return newEnv;
//    }

    public WitnessEnv DNF() throws WitnessException {
        WitnessEnv newEnv = cloneNoWitnessVar();

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : varList.entrySet()){
//            System.out.println(String.format("processing %1s ", entry.getKey()));
            newEnv.addOrUnifyAndAddToBeNotEliminated(entry.getKey(), (entry.getValue()).DNF());
        }

        return newEnv;
    }

    @Override
    public WitnessAssertion toOrPattReq() {
        for (WitnessVar key : new HashSet<>(varList.keySet()))
            addOrUnifyAndAddToBeNotEliminated(key, varList.get(key).toOrPattReq());

        return this;
    }

    @Override
    public boolean isBooleanExp() {
        throw new UnsupportedOperationException("isBooleanExp on WitnessEnv");
    }

    @Override
    public boolean isRecursive(WitnessEnv env, LinkedList<WitnessVar> visitedVar) {
        throw new UnsupportedOperationException("isRecursive on WitnessEnv");
    }

    @Override
    public WitnessVar buildOBDD(WitnessEnv env, WitnessVarManager varManager) {
        throw new UnsupportedOperationException("buildOBDD on WitnessEnv");
    }

    @Override
    public void getReport(ReportResults reportResults) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<WitnessVar> uses() {
        return new ArrayList<>();
    }



    public List<Map.Entry<WitnessVar, ReportResults>> getReport(){
        List<Map.Entry<WitnessVar, ReportResults>> returnList = new LinkedList<>();

        for(Map.Entry<WitnessVar, WitnessAssertion> entry : varList.entrySet()){
            ReportResults rr = new ReportResults();
            entry.getValue().getReport(rr);
            returnList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), rr));
        }

        return returnList;
    }


    /**
     * TODO invoke specific preparation based on x in type[x]
     * unified preparation for objects and arrays in a top-down fashion
     * @throws REException
     * @throws WitnessException
     */
    public void preparation() throws REException, WitnessException {

        toOrPattReq();
        Queue<WitnessVar> queue = new LinkedList<WitnessVar>();
        List<WitnessVar> visited = new LinkedList<WitnessVar>();
        queue.add(rootVar);

        WitnessVar currentVar;
        WitnessAssertion currentAssertion;


        while(!queue.isEmpty()){
            currentVar = queue.remove();visited.add(currentVar);
//            System.out.println(String.format("currentVar %s ",currentVar));
            currentAssertion = varList.get(currentVar);
            // 1- dnf
            currentAssertion = currentAssertion.DNF();
            currentAssertion = currentAssertion.merge(null,null);
//            this.addOrUnifyAndAddToBeNotEliminated(currentVar,currentAssertion);
//            this.addUnify(currentVar, currentAssertion);
            // 2- invoke both preparations on the body and collects the new variables
            List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();
            // preparation is only defined for WitnessAnd and WitnessOr
            //Hence in the following lines we transform every WitnessAssertion into an Or or an And
            //Actually, we should only meet either WitnessAnd or WitnessOr
            if (currentAssertion.getClass() == WitnessAnd.class){
                newDefinitions.addAll(((WitnessAnd) currentAssertion).arrayPreparation(this));
                newDefinitions.addAll(((WitnessAnd) currentAssertion).objectPrepare(this));
            }

            else if (currentAssertion.getClass() == WitnessOr.class){
                newDefinitions.addAll(((WitnessOr) currentAssertion).arrayPreparation(this));
                newDefinitions.addAll(((WitnessOr) currentAssertion).objectPrepare(this));
            }



            //else if(entry.getValue().getClass() == WitnessType.class && entry.getValue().equals(new WitnessType("obj"))){ //only type object
            else {
                // in case of definitions likes:
                // def "x" = type[arr] or type[obj]
                // we have to prepare that definition but the assertion type[arr] is not contained in boolean operator (AND, OR)
                WitnessAnd tmp = new WitnessAnd();
                tmp.add(currentAssertion);
                newDefinitions.addAll(tmp.arrayPreparation(this)); // if the element is not a type[obj], the method call tmp.objectPrepare(this) have no effect
                newDefinitions.addAll(tmp.objectPrepare(this));
            }

            //TODO check
            WitnessVar v = this.addUnify(currentVar, currentAssertion);
//            System.out.println(String.format("currentVar %s v %s",currentVar,v));

            //3- add the new definitions to the environment

            List<Map.Entry<String, Integer>> varCountList = new LinkedList<>();

            for (Map.Entry<WitnessVar, WitnessAssertion> newDef : newDefinitions) {
                this.addOrUnifyAndAddToBeNotEliminated(newDef.getKey(), newDef.getValue());
                varCountList.add(new AbstractMap.SimpleEntry<>(new String(newDef.getKey().getName()),
                        newDef.getValue().countVarToBeExp(this)));
                /* the following hack does not work - a cyclic dependency may be in newDefinitions already
                WitnessVar toBeAdded = newDef.getKey();
                WitnessVar addedOrUnified = this.addOrUnifyAndAddToBeNotEliminated(toBeAdded, newDef.getValue());
                if (addedOrUnified == toBeAdded) {
                    varCountList.add(new AbstractMap.SimpleEntry<>(new String(addedOrUnified.getName()),
                            newDef.getValue().countVarToBeExp(this)));
                } */
            }

            Collections.sort(varCountList, Comparator.comparingInt(Map.Entry::getValue));
            Collections.reverse(varCountList);

            //4- re-apply not elimination on the entire environment
            this.buildOBDD_notElimination();

            //mi ricosctruisco il set su cui scorrere, ci aggiungo anche le variabili negate????
            newDefinitions = new LinkedList<>();
            for (Map.Entry<String, Integer> newDef : varCountList) {
                String name = newDef.getKey();
                WitnessVar varName = variableNamingSystem.buildVar(name);
                if (name.equals(varName.getName())) {//se NON è stata rinominata è una variabile nuova
                    newDefinitions.add(new AbstractMap.SimpleEntry<>(varName, getDefinition(varName)));

                    // since I did notElimination, every variable has its complement
                    WitnessVar coName = getCoVar(varName);
                    newDefinitions.add(new AbstractMap.SimpleEntry<>(coName, varList.get(coName)));
                }
            }

            logger.debug("Expanding {} variables", newDefinitions.size());

            // for every new variable, normalize the body, put <var,normalized> in the
            // environment and in the newDefinitions list
            // The list is sorted so that the last element does not depend on preceding
            // elements; we go backwards since Java does not like forward loops that
            // modify elements, since we decided to overwrite "newDefinitions"
            for (int i = newDefinitions.size() - 1; i >= 0; i--) {
                Map.Entry<WitnessVar, WitnessAssertion> newDef = newDefinitions.get(i);
                WitnessAssertion normalizedValue = newDef.getValue();

//                System.out.println("== newDef " + newDef);

                normalizedValue = normalizedValue.varNormalization_expansion(this);

                normalizedValue = normalizedValue.merge(variableNamingSystem, pattReqManager);
                normalizedValue = normalizedValue.groupize();
                normalizedValue = normalizedValue.DNF();
                normalizedValue = normalizedValue.merge(variableNamingSystem, pattReqManager);

                addOrUnifyAndAddToBeNotEliminated(newDef.getKey(), normalizedValue);
                newDefinitions.set(i, new AbstractMap.SimpleEntry<>(newDef.getKey(), normalizedValue));
            }

            List<WitnessVar> nextVars = currentAssertion.uses();
            //last- add the dependencies of current
            for(WitnessVar dependency: nextVars)
                if(dependency != currentVar && !visited.contains(dependency))
                    queue.add(dependency);

        }


    }


    /**
     * TODO complete
     * unified preparation for objects and arrays in a top-down fashion
     * @throws REException
     * @throws WitnessException
     */
//    public void oldPreparation() throws REException, WitnessException {
//        if(!sortedByHight)
//            this.sortVarListByHeight();
//        toOrPattReq();
//        Collection<Map.Entry<WitnessVar, WitnessAssertion>> toBePrepared = new HashMap<>(varList).entrySet();
//
//        while (!toBePrepared.isEmpty()) {
//            List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();
//
//
//            // new TODO invoke specific preparation based on x in type[x]
//            // 1- dnf
//            // 2- invoke both preparations on every body of the environment and collects the new variables
//            for (Map.Entry<WitnessVar, WitnessAssertion> entry : toBePrepared) {
//                //1-substitute the old value - observe that o.DNF() damages o in very strange ways
//                //hence we rebuild entry.getValue() using entry.setValue()
//                entry.setValue(entry.getValue().DNF());
//                varList.put(entry.getKey(),entry.getValue());
//                //WitnessAssertion DNFResult = entry.getValue().DNF();
//                //varList.put(entry.getKey(),DNFResult);
//                //2-
//                // preparation is only defined for WitnessAnd amd WitnessOr
//                //Hence in the following lines we transform every WitnessAssertion into an Or or an And
//                //Actually, we should only meet either WitnessAnd or WitnessOr
//                if (entry.getValue().getClass() == WitnessAnd.class){
//                    newDefinitions.addAll(((WitnessAnd) entry.getValue()).arrayPreparation(this));
//                    newDefinitions.addAll(((WitnessAnd) entry.getValue()).objectPrepare(this));
//                }
//
//                else if (entry.getValue().getClass() == WitnessOr.class){
//                    newDefinitions.addAll(((WitnessOr) entry.getValue()).arrayPreparation(this));
//                    newDefinitions.addAll(((WitnessOr) entry.getValue()).objectPrepare(this));
//                }
//
//                    //else if(entry.getValue().getClass() == WitnessType.class && entry.getValue().equals(new WitnessType("obj"))){ //only type object
//                else {
//                    // in case of definitions likes:
//                    // def "x" = type[arr] or type[obj]
//                    // we have to prepare that definition but the assertion type[arr] is not contained in boolean operator (AND, OR)
//                    WitnessAnd tmp = new WitnessAnd();
//                    tmp.add(entry.getValue());
//                    newDefinitions.addAll(tmp.arrayPreparation(this)); // if the element is not a type[obj], the method call tmp.objectPrepare(this) have no effect
//                    newDefinitions.addAll(tmp.objectPrepare(this));
//                }
//            }
//
//            List<Map.Entry<String, Integer>> varCountList = new LinkedList<>();
//
//            for (Map.Entry<WitnessVar, WitnessAssertion> newDef : newDefinitions) {
//                this.addOrUnifyAndAddToBeNotEliminated(newDef.getKey(), newDef.getValue());
//                varCountList.add(new AbstractMap.SimpleEntry<>(new String(newDef.getKey().getName()),
//                        newDef.getValue().countVarToBeExp(this)));
//            }
//
//            Collections.sort(varCountList, Comparator.comparingInt(Map.Entry::getValue));
//            Collections.reverse(varCountList);
//
//            this.buildOBDD_notElimination();
//
//            //mi ricosctruisco il set su cui scorrere, ci aggiungo anche le variabili negate????
//            newDefinitions = new LinkedList<>();
//            for (Map.Entry<String, Integer> newDef : varCountList) {
//                String name = newDef.getKey();
//                WitnessVar varName = variableNamingSystem.buildVar(name);
//                if (name.equals(varName.getName())) {//se NON è stata rinominata è una variabile nuova
//                    newDefinitions.add(new AbstractMap.SimpleEntry<>(varName, getDefinition(varName)));
//
//                    // since I did notElimination, every variable has its complement
//                    WitnessVar coName = getCoVar(varName);
//                    newDefinitions.add(new AbstractMap.SimpleEntry<>(coName, varList.get(coName)));
//                }
//            }
//
//            logger.debug("Expanding {} variables", newDefinitions.size());
//
//            // for every new variable, normalize the body, put <var,normalized> in the
//            // environment and in the newDefinitions list
//            // The list is sorted so that the last element does not depend on preceding
//            // elements; we go backwards since Java does not like forward loops that
//            // modify elements, since we decided to overwrite "newDefinitions"
//            for (int i = newDefinitions.size() - 1; i >= 0; i--) {
//                Map.Entry<WitnessVar, WitnessAssertion> newDef = newDefinitions.get(i);
//                WitnessAssertion normalizedValue = newDef.getValue();
//
////                System.out.println("== newDef " + newDef);
//
//                normalizedValue = normalizedValue.varNormalization_expansion(this);
//
//                normalizedValue = normalizedValue.merge(variableNamingSystem, pattReqManager);
//                normalizedValue = normalizedValue.groupize();
//                normalizedValue = normalizedValue.DNF();
//                normalizedValue = normalizedValue.merge(variableNamingSystem, pattReqManager);
//
//                addOrUnifyAndAddToBeNotEliminated(newDef.getKey(), normalizedValue);
//                newDefinitions.set(i, new AbstractMap.SimpleEntry<>(newDef.getKey(), normalizedValue));
//            }
//
//
//            toBePrepared = newDefinitions;
//
//
//        }
//
//
//    }

    public void objectPrepare() throws REException, WitnessException {
        toOrPattReq();

        Collection<Map.Entry<WitnessVar, WitnessAssertion>> toBePrepared = new HashMap<>(varList).entrySet();

        while (!toBePrepared.isEmpty()) {
            List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();


            for (Map.Entry<WitnessVar, WitnessAssertion> entry : toBePrepared) {

                if (entry.getValue().getClass() == WitnessAnd.class)
                    newDefinitions.addAll(((WitnessAnd) entry.getValue()).objectPrepare(this));
                else if (entry.getValue().getClass() == WitnessOr.class)
                    newDefinitions.addAll(((WitnessOr) entry.getValue()).objectPrepare(this));
                    //else if(entry.getValue().getClass() == WitnessType.class && entry.getValue().equals(new WitnessType("obj"))){ //only type object
                else {
                    // in case of definitions like:
                    // def "x" = type[obj]
                    // we have to prepare that definition but the assertion type[obj] is not contained in boolean operator (AND, OR)
                    WitnessAnd tmp = new WitnessAnd();
                    tmp.add(entry.getValue());
                    newDefinitions.addAll(tmp.objectPrepare(this)); // if the element is not a type[obj], the method call tmp.objectPrepare(this) have no effect
                    /*if (tmp.getIfUnitaryAnd() != null)
                        varList.put(entry.getKey(), tmp.getIfUnitaryAnd());
                    else
                        varList.put(entry.getKey(), tmp);*/
                }
            }
            List<Map.Entry<String, Integer>> tmp = new LinkedList<>();

            for(Map.Entry<WitnessVar, WitnessAssertion> newDef : newDefinitions) {
                addOrUnifyAndAddToBeNotEliminated(newDef.getKey(), newDef.getValue());
                tmp.add(new AbstractMap.SimpleEntry<>(new String(newDef.getKey().getName()), newDef.getValue().countVarToBeExp(this)));
            }

            Collections.sort(tmp, Comparator.comparingInt(Map.Entry::getValue));
            Collections.reverse(tmp);

            buildOBDD_notElimination();

            //mi ricosctruisco il set su cui scorrere, ci aggiungo anche le variabili negate????
            newDefinitions = new LinkedList<>();
            for(Map.Entry<String, Integer> newDef : tmp) {
                String name = newDef.getKey();
                WitnessVar varName = variableNamingSystem.buildVar(name);
                if (name.equals(varName.getName())) {//se NON è stata rinominata è una variabile nuova
                    newDefinitions.add(new AbstractMap.SimpleEntry<>(varName, getDefinition(varName)));

                    // il suo complemento
                    WitnessVar coName = getCoVar(varName);
                    newDefinitions.add(new AbstractMap.SimpleEntry<>(coName, varList.get(coName)));
                }
            }

            logger.debug("Expanding {} variables", newDefinitions.size());

            for(int i = newDefinitions.size() -1; i >= 0; i--){
                Map.Entry<WitnessVar, WitnessAssertion> newDef = newDefinitions.get(i);
                WitnessAssertion normalizedValue = newDef.getValue();

                normalizedValue = normalizedValue.varNormalization_expansion(this);
                normalizedValue = normalizedValue.merge(variableNamingSystem, pattReqManager);
                normalizedValue = normalizedValue.groupize();
                normalizedValue = normalizedValue.DNF();
                normalizedValue = normalizedValue.merge(variableNamingSystem, pattReqManager);

                addOrUnifyAndAddToBeNotEliminated(newDef.getKey(), normalizedValue);
                newDefinitions.set(i, new AbstractMap.SimpleEntry<>(newDef.getKey(), normalizedValue));
            }

            toBePrepared = newDefinitions;
        }
    }



    public boolean requiresArrayPreparation(){

        return true;
    }

    /**
     * original method written by the students
     * @throws REException
     * @throws WitnessException
     */
    public void arrayPreparation() throws REException, WitnessException {

        Collection<Map.Entry<WitnessVar, WitnessAssertion>> toBePrepared = new HashMap<>(varList).entrySet();
        //At the first loop, every var in VarList is in toBePrepared, from the loop i+1 they are those created in loop i
        while (!toBePrepared.isEmpty()) {
            List<Map.Entry<WitnessVar, WitnessAssertion>> newDefinitions = new LinkedList<>();


            // Invokes arrayPreparation on every body of the environment and collects the new variables
            for (Map.Entry<WitnessVar, WitnessAssertion> entry : toBePrepared) {
                //arrayPreparation is only defined for WitnessAnd amd WitnessOr
                //Hence in the following lines we transform every WitnessAssertion into an Or or an And
                //Actually, we should only meet either WitnessAnd or WitnessOr
                if (entry.getValue().getClass() == WitnessAnd.class)
                    newDefinitions.addAll(((WitnessAnd) entry.getValue()).arrayPreparation(this));

                else if (entry.getValue().getClass() == WitnessOr.class)
                    newDefinitions.addAll(((WitnessOr) entry.getValue()).arrayPreparation(this));

                    //else if(entry.getValue().getClass() == WitnessType.class && entry.getValue().equals(new WitnessType("obj"))){ //only type object
                else {
                    // in case of definitions likes: // def "x" = type[arr]
                    // we have to prepare that definition but the assertion type[arr] is not contained in boolean operator (AND, OR)
                    WitnessAnd tmp = new WitnessAnd();
                    tmp.add(entry.getValue());
                    newDefinitions.addAll(tmp.arrayPreparation(this)); // if the element is not a type[obj], the method call tmp.objectPrepare(this) have no effect
                }
            }

            Map<String, Integer> varCountList = new HashMap<>();

            //For each variable in newDefinitions add it to both this.varList and this.toBeElaborated
            //If two variables with the same body are found in newDefinitions, they will only be added once
            for (Map.Entry<WitnessVar, WitnessAssertion> newDef : newDefinitions) {
                //this.addOrUnify only adds the variable if neither the name nor the value of the variable
                //is already present; if the body is already present, the old variable is deleted and
                //renamed - if no renaming, the variable is added to varToBeElaborated
                WitnessVar addedOrUnified = this.addOrUnifyAndAddToBeNotEliminated(newDef.getKey(), newDef.getValue());
                if (!varCountList.containsKey(addedOrUnified.getName())) {
                    varCountList.put(addedOrUnified.getName(), varList.get(addedOrUnified).countVarToBeExp(this));
                }
            }

            List<Map.Entry<String, Integer>> pairList = new ArrayList<>();
            for (Map.Entry<String, Integer> stringIntegerEntry : varCountList.entrySet()) {
                pairList.add(stringIntegerEntry);
            }
            //pairList.sort((Comparator<Map.Entry<String, Integer>>) Map.Entry.comparingByValue());
            pairList.sort(Map.Entry.comparingByValue());
            Collections.reverse(pairList);

            // the following call will rename the variables inside VarCountList
            this.buildOBDD_notElimination();

            //This loop should re-create a newDefinitions list built starting from varCountList
            //where the variables that have been unified only appear once but every variable
            //also has its covariable
            newDefinitions = new LinkedList<>();
            for (Map.Entry<String, Integer> newDef : pairList) {
                String name = newDef.getKey();
                WitnessVar varName = variableNamingSystem.buildVar(name);
                if (name.equals(varName.getName())) {//se NON è stata rinominata è una variabile nuova
                    newDefinitions.add(new AbstractMap.SimpleEntry<>(varName, getDefinition(varName)));

                    // since I did notElimination, every variable has its complement
                    WitnessVar coName = getCoVar(varName);
                    newDefinitions.add(new AbstractMap.SimpleEntry<>(coName, varList.get(coName)));
                }
            }

            logger.debug("Expanding {} variables", newDefinitions.size());

            // for every new variable, normalize the body, put <var,normalized> in the
            // environment and in the newDefinitions list
            // The list is sorted so that the last element does not depend on preceding
            // elements; we go backwards since Java does not like forward loops that
            // modify elements, since we decided to overwrite "newDefinitions"
            for (int i = newDefinitions.size() - 1; i >= 0; i--) {
                Map.Entry<WitnessVar, WitnessAssertion> newDef = newDefinitions.get(i);
                WitnessAssertion normalizedValue = newDef.getValue();

//                System.out.println("== newDef " + newDef);

                normalizedValue = normalizedValue.varNormalization_expansion(this);

                normalizedValue = normalizedValue.merge(variableNamingSystem, pattReqManager);
                normalizedValue = normalizedValue.groupize();
                normalizedValue = normalizedValue.DNF();
                normalizedValue = normalizedValue.merge(variableNamingSystem, pattReqManager);

                addOrUnifyAndAddToBeNotEliminated(newDef.getKey(), normalizedValue);
                newDefinitions.set(i, new AbstractMap.SimpleEntry<>(newDef.getKey(), normalizedValue));
            }


            toBePrepared = newDefinitions;


        }
    }
    /**
     * chain may be cyclic
     * @param root
     * @return
     */
    public List<WitnessVar> reachableFrom(WitnessVar root, List<WitnessVar> met){
        List<WitnessVar> toExplore = this.getDefinition(root).uses(),
                metextended;
        if(toExplore.isEmpty())
            return Stream.of(root).collect(Collectors.toList());
        else {
            toExplore.removeAll(met);
            metextended = met.stream().collect(Collectors.toList());
            metextended.add(root);
            metextended.addAll(toExplore.stream().flatMap(e-> reachableFrom(e, metextended).stream())
//                    .distinct()
                    .collect(Collectors.toList()));
            return metextended.stream().distinct().collect(Collectors.toList());
        }
    }

    public void keepOnly(List<WitnessVar> toKeep){
        List<WitnessVar> toRemove = varList.keySet().stream().filter(v->!toKeep.contains(v)).collect(Collectors.toList());
        for(WitnessVar witnessVar: toRemove)
                varList.remove(witnessVar);
    }
}
