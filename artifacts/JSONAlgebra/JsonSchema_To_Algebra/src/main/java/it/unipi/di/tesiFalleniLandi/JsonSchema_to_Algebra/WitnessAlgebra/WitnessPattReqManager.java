package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class WitnessPattReqManager {
    private static Logger logger = LogManager.getLogger(WitnessPattReqManager.class);

    private HashMap<Object, WeakReference<WitnessPattReq>> instances;

    public WitnessPattReqManager(){
        instances = new HashMap<>();
    }

    public WitnessPattReq build(ComplexPattern key, WitnessAssertion assertion){
        if(instances == null) instances = new HashMap<>();

        WitnessPattReq tmp = new WitnessPattReq(key, assertion);

        if(instances.containsKey(tmp.toString())) {
            if (instances.get(tmp.toString()).get() != null) {
                logger.trace("PattReq returning an OLD instance: ", instances.get(tmp.toString()).get());

                return instances.get(tmp.toString()).get();
            } else
                instances.remove(tmp.toString());
        }

        instances.put(tmp.toString(), new WeakReference<>(tmp));
        logger.trace("PattReq returning a NEW instance: ", instances.get(tmp.toString()));
        return tmp;
    }
}
