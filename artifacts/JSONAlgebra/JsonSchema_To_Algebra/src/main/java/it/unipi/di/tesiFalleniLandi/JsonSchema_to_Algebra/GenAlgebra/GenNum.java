package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class GenNum implements GenAssertion {
    private static Logger logger = LogManager.getLogger(GenNum.class);
    private JsonElement witness;


    private Double min, max;
    boolean isInteger;
    private Double mof;
    private List<Double> notMofs; //keep sorted
    private Double _nongen = -999d;
    private Double epsilon = 0.000001d;

    @Override
    public String toString() {
        return "GenNum{" +
                "min=" + min +
                ", max=" + max +
                ", mof=" + mof +
                ", notMofs=" + notMofs +
                '}';
    }

    @Override
    public JsonElement getWitness() {
        return witness;
    }

    public GenNum(boolean isInteger) {
        logger.debug("Creation");
        notMofs = new LinkedList<>();
        mof = null;
        min = Double.NEGATIVE_INFINITY;
        max = Double.POSITIVE_INFINITY;
        this.isInteger = isInteger;
    }

    /**
     *
     * @param WitnessBet
     */
    public void addMinMax(WitnessBet WitnessBet){
        min = Double.max(min, WitnessBet.getMin());
        max = Double.min(max, WitnessBet.getMax());
        logger.debug("Set min to {} max to {}", min, max);
    }

    public void addMinMax(WitnessXBet witnessXBet){
        min = Double.max(min,witnessXBet.getMin()+epsilon);
        max = Double.min(max, witnessXBet.getMax()-epsilon);
        logger.debug("Set Xmin to {} Xmax to {}", min, max);
    }

    /**
     * checked at two positions: while creating Mof and notMofs constraints since no precedence is enforced
     * @throws Exception
     */
    private void invariant1() throws Exception{
        List<Double> notMofsSmaller = notMofs.stream().filter(n->n <= mof)
                                                      .collect(Collectors.toList()); //TODO check with negative numbers
        notMofsSmaller.sort(Comparator.naturalOrder());

        if(notMofsSmaller.size()>0){
            Double mofTest = containsDivisor(notMofsSmaller,this.mof);
            if(mofTest>0)
            {
                logger.trace("The number {} in the NotMof List is divisor of {}", mofTest, mof);
                throw new Exception("NotMof List contains a divisor of Mof");
            }
        }
    }

    /**
     *
     * @param mof
     * @throws Exception
     */
    public void setMof(WitnessMof mof) throws Exception {
        this.mof = mof.getValue();
        //check invariant1
              invariant1();
        logger.debug("Set mof to {}", this.mof);
    }

    public void setNotMofs(List<WitnessNotMof> notMofs) throws Exception{
        this.notMofs=notMofs.stream()
                .map(e->e.getValue()).collect(Collectors.toList());
        this.notMofs.sort(Comparator.naturalOrder());
        //check invariants
        if(this.mof!=null)
                invariant1();
        //invariant2
       if(containsPairMultiple(this.notMofs))
            throw new Exception("NotMof List contains a pair of multiples");
        logger.debug("Set NotMofs to {}", this.notMofs);

    }



    /** Auxiliary functions */

    /**
     *
     * @param factor number which msy be a factor (divisor) of multiple
     * @param multiple
     * @return returns true iff divisor divides the dividend, i.e. whether divisor is a factor of dividend
     */
    private Boolean isMutipleOf(Double multiple, Double factor){
        double remainder = multiple%factor;
        // return (remainder==0);
        if (remainder==0) return true;
        else if (factor%1==0) return false;
        else {
            double epsilon = 0.0000000000001d;
            return (Math.abs(remainder) < epsilon
                    || Math.abs(factor)-Math.abs(remainder) < epsilon );
        }
    }

    /**
     *
     * @param list must be sorted - every element of list is expected to be smaller than num
     * @param num
     * @return 0d in the normal case when list contains no divisor for num - if list contains any divisor for
     *         num, then one such divisor is returned
     */
    private Double containsDivisor(List<Double> list, Double num){
        Double res = 0d;
        Double remainder = 0d;
        for(Double el:list) {
            if (isMutipleOf(num,el)) {
                res = el;
                break;
            }
        }
        return res;
    }

    /**
     *
     * @param list must be sorted
     * @return
     */
    private boolean containsPairMultiple(List<Double> list) {
    //expect list size >1
        Double[] arr = new Double[list.size()];
        arr = list.toArray(arr);
        for (int i=0; i<arr.length;i++)
            for(int j=i+1; j<arr.length; j++)
                if(isMutipleOf(arr[j],arr[i]))
                    return true;
        return false;
    }

    /**
     * whether n is a multiple of a some element in notmof
     * @param n
     * @param notmof
     * @return
     */
    private boolean isMultipleOfANotMof(Double n, List<Double> notmof){
        boolean res = false;
        if(notmof.size()>0){
            for(Double nm:notmof)
                if(isMutipleOf(n,nm)){
    // old verions                if(((n.floatValue()/nm.floatValue())%1)==0){
                        res = true;
                    break;
                }
        }
        return res;
    }


    /**
     *
     * @param number
     * @param mof
     * @return the biggest mof such that mof <= number
     */
    private Double nextMof(Double number, Double mof){
        int v = (int) (number/mof);
        return v * mof;
    }

    /**
     * use simple trick to output doubles with 0 in the decimal parts as integers no matter the type
     * @param generated
     * @return
     */
        private void setWitness(Double generated) {
            int intPart = (int) generated.intValue();
            double rest = generated-intPart;
            if(rest==0 || isInteger)
                witness =  new JsonPrimitive(generated.intValue());
            else
                witness = new JsonPrimitive(generated);
    }


    private Double getStep() {
        Double step;
        //TODO: clean this
        if(mof==null)

            if(notMofs.size()>0) {
                Double minNotMof = Collections.min(notMofs);
                if(min!=Double.NEGATIVE_INFINITY && max!=Double.POSITIVE_INFINITY)
                    step = Math.min(max-min,minNotMof)/(notMofs.size()+2); // following the method of Giorgio (email 8/3/20)
                else
                    step = Math.random() * minNotMof; // get a number between 0 and minNotMof

            }
            else
                if(min!=Double.NEGATIVE_INFINITY && max!=Double.POSITIVE_INFINITY)
                    step = min + Math.random() * (max - min);
                else
                    step = Math.random(); // change it to generate a bigger number ? Since here  we don't have any constraint
                                          // no mof, no notMofList, infintie min and max
        else
            step = mof;

        return step;

    }

    @Override
    public statuses generate() {

            // to check why test24 in Gennum was not working
//        System.out.println("1.05768/0.00452  = "+1.05768d/0.00452d);
//        System.out.println("1.05768%0.00452  = "+1.05768d%0.00452d);
//        System.out.println("(1.05768/0.00452)%1  = "+(1.05768d/0.00452)%1);

        Double result = min,
                 step = getStep();

//        double epsilon = 0.000001d;

        if (mof!=null && mof==0) {
            result = 0.0;
            if (result < min) return statuses.Empty;
            else if (result > max) return statuses.Empty;
            else if (isMultipleOfANotMof(result,notMofs)) return statuses.Empty;
            else {
                setWitness(result);
                return statuses.Populated;
            }
        }

        if(min>max)
//        if (min > max+epsilon) //GG: this is a patch!!!
            return statuses.Empty;

        else
            if(min.doubleValue()==max.doubleValue())
//            if (Math.abs(min - max) < epsilon) //this is a patch!
            {
              if((mof!=null && (!isMutipleOf(result,mof))) || isMultipleOfANotMof(result,notMofs))
                  return statuses.Empty;
              else
              {
                  //TODO redundant check
//                  if(isInteger)
//                      witness =  new JsonPrimitive(result.intValue());
//                  else
//                    witness = new JsonPrimitive(result);
                  setWitness(result);
                  return statuses.Populated;
              }
            }
                else
                    //min<max
                {
                    if(min.isInfinite())
                        if(max.isInfinite())
                        {
                            //start from a random position
                            result = 0d;

                            //TODO result already % 0 redundancy elimination needed
                            while((mof!=null && !isMutipleOf(result,mof))
                                    ||  isMultipleOfANotMof(result,notMofs)){
                                result += step;
                            }
//                            if(isInteger)
//                                witness =  new JsonPrimitive(result.intValue());
//                            else
//                                witness = new JsonPrimitive(result);
                            setWitness(result);
                            return statuses.Populated;
                        }
                        else
                        {
                            //start from Max downward

                            result = nextMof(max, step);
                            while((mof!=null && !isMutipleOf(result,mof)) ||  isMultipleOfANotMof(result,notMofs) || result>max){
                                result -= step;
                            }
//                            if(isInteger)
//                                witness =  new JsonPrimitive(result.intValue());
//                            else
//                                witness = new JsonPrimitive(result);
                            setWitness(result);
                            return statuses.Populated;

                        }
                    else  //min is finite and different from max
                    {
                        //increment by n
                        result = nextMof(min, step);
                        logger.debug(" result {} step {}",  result , step);

                        // exit condition 1: result > max
                        // exit cond 2: either mof null or result is mof AND isnot multiple of a not mof
                        // and result >= min. Is multiple of: res/mof is an integere, i.e. (res/mof) mod 1 = 0
                        while(result<=max && ((mof!=null && !isMutipleOf(result,mof) )
                                               || isMultipleOfANotMof(result,notMofs)
                                               || result<min) ){
                            result += step;
                        }

                        if(result<=max){
//                            if(isInteger)
//                                witness =  new JsonPrimitive(result.intValue());
//                            else
//                                witness = new JsonPrimitive(result);
                            setWitness(result);
                            return statuses.Populated;
                        }
                        else {
                            //TODO check whether we really have no witness
                            return statuses.Empty;
                        }
                    }

//                    while( (result<=max) && (isMultipleOfANotMof(result,notMofs) || result % mof!=0 ) )
//                    {
//                        result++;
//                    }
//                    if(result<=max){
//                        witness = new JsonPrimitive(result);
//                        return statuses.Populated;
//                    }
//                    else
//                        return statuses.Empty;
                }

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
        return new LinkedList<>();
    }


}
/**
 //     * constructor with optional arguments
 //     * TODO Remove
 //     * @param minMaxOptional
 //     * @param XminMaxOptional
 //     * @param mofOptional
 //     * @param notMofsOptional
 //     */
//    public GenNum(Optional<WitnessBet> minMaxOptional,
//                  Optional<WitnessXBet> XminMaxOptional,
//                  Optional<WitnessMof> mofOptional,
//                  Optional<List<WitnessNotMof>> notMofsOptional) throws Exception {
//
//        if(minMaxOptional.isPresent()){
//            min=minMaxOptional.get().getMin();
//            max=minMaxOptional.get().getMax();
//        }
//
//        if(XminMaxOptional.isPresent()){
//            min=XminMaxOptional.get().getMin();
//            max=XminMaxOptional.get().getMax();
//            minExclusive=maxExclusive=true;
//        }
//
//        if(mofOptional.isPresent()){
//            mof= mofOptional.get().getValue();
//            if(notMofs!=null){
//                Double mofTest = containsMultiple(notMofs,mof);
//                if(mofTest>0)
//                {
//                    logger.trace("The number {} in the NotMof List is multiple of {}", mofTest, mof);
//                    throw new Exception("NotMof List contains a multiple of Mof");
//                }
//            }
//
//        }
//
//        if(notMofsOptional.isPresent()){
//            notMofs=notMofsOptional.get().stream()
//                    .map(e->e.getValue()).collect(Collectors.toList());
//            notMofs.sort(Comparator.naturalOrder());
//            if(containsPairMultiple(notMofs))
//                throw new Exception("NotMof List contains a pair of multiples");
//        }
//
//    }