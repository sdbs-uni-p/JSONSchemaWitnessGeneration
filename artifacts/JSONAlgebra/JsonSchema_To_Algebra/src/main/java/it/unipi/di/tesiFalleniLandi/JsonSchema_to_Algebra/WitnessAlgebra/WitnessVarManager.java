package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages variable renaming and creation of unused names
 */

public class WitnessVarManager {
    private static Logger logger = LogManager.getLogger(WitnessVarManager.class);

    private HashMap<String, String> renamed; //map oldName --> newName
    private HashMap<String, LinkedList<WeakReference<WitnessVar>>> instancesOfName;
    //map of each varname to all WitnessVar's with that name, left or right habd side of a definition
    //is used to rename the variable
    //We use WeakReference to avoid memory leaks. An object stored in WeakReference do not increment the numbers of pointer
    //that refer to that object, so if the object is pointed only by a WeakReference, the garbage collector can delete it

    private long count; //used to generate new names different from the old ones

    public WitnessVarManager() {
        renamed = new HashMap<>();
        instancesOfName = new HashMap<>();
        count = 0l;
    }

    /**
     * Creates a WitnessVar with name "name", adds the WitnessVar to instancesOfName(name)
     * @param name : name of the new WitnessVar
     * @return the WitnessVar created
     * an old WitnessVar with the same name may already exist, or not
     */

    public WitnessVar buildVar(String name){
        // The next line should always leave "name==newName" - we may raise an error if it is
        // not like this
        String newName = resolveName(name);
        WitnessVar var = new WitnessVar(newName);

        if(instancesOfName.containsKey(newName))
            instancesOfName.get(newName).add(new WeakReference<>(var));
        else {
            LinkedList<WeakReference<WitnessVar>> tmp = new LinkedList<>();
            tmp.add(new WeakReference<>(var));
            instancesOfName.put(newName, tmp);
        }

        logger.trace("Created a new WitnessVar with name=({}=resolveName({}))", newName, name);

        return var;
    }

    public void rename(WitnessVar oldName, WitnessVar newName) {
        rename(oldName.getName(), newName.getName());
    }

    //rename all the instances of ref(oldName) into ref(newName)
    public void rename(String oldName, String newName){
        if(oldName.equals(newName)) return;

        logger.trace("Renaming {} (old) to {} (new)", oldName, newName);

        newName = resolveName(newName); //get the "real" name to be renamed.
        renamed.put(oldName, newName); //save the rename operation.

        //rename instances
        renameInstances(newName, oldName);
    }


    /**
     * example
     * if b=a --> add(a, body_a);        add(b, body_a) do not create b,
     *                                   rewrite all instances of ref(b) as ref(a),
     *                                   remember that if someone want to create ref(b), we must instead return ref(a)
     * c=b
     *
     * resolve(c) --> resolve(b) --> a
     */
    protected String resolveName(String oldName){
        if(renamed.containsKey(oldName))    return resolveName(renamed.get(oldName));

        return oldName;
    }

    /**
     * Rename all the instances of oldName with newName
     * @param newName
     * @param oldName
     */
    private void renameInstances(String newName, String oldName){
        List<WeakReference<WitnessVar>> instancesToBeRenamed = instancesOfName.remove(oldName);
        List<WeakReference<WitnessVar>> newNameInstances = instancesOfName.get(newName);

        if(instancesToBeRenamed == null) return;

        for(WeakReference<WitnessVar> weakReference : instancesToBeRenamed) {
            if(weakReference.get() == null) continue; //garbage collector

            weakReference.get().setName(newName);
            newNameInstances.add(weakReference);
        }
    }

    protected void setCountMin(int n){
        if(n > count) count = n;
    }

    /**
     * Returns a fresh name that depends on the class of the assertion
     * @param assertion
     * @return
     * Invariant: the method add of WitnessEnv ensures that "count" is always grater or equal to the maximal
     * Integer "n" such that a variable with name ending with "_n" is in the environment
     */
    public String getName(WitnessAssertion assertion){
        return assertion.getClass().getSimpleName()+ "_" + count++;
    }
}
