package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.ComplexPattern.ComplexPattern;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.uni_passau.sds.patterns.REException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class GenObject implements GenAssertion {
    private static Logger logger = LogManager.getLogger(GenObject.class);
    private JsonElement witness;


    private Double minPro, maxPro;
    private List<GProperty> CPart;
    private List<GPattReq> RPart;
    private List<GChoice> choiceList;
    private boolean _randomStrategy;



    /*generic functions*/
    public static List<String> generateNames(ComplexPattern pattern, Double maxSize) {
        return generateNames(pattern, maxSize, new LinkedList<>());
    }

    /**
     * returns the set of strings generated by key and excluding excludeNames up to maxSize
     * if cannot generate maxSize names then returns an empty list (?)
     *
     * @param maxSize
     * @param excludedNames
     * @return
     */
    public static List<String> generateNames(ComplexPattern originalPattern, Double maxSize, List<String> excludedNames) {
        List<String> excluded = new LinkedList<>(),
                result = new LinkedList<>();
        String currentName;
        int count = 0;

        // commented out by GG
        //if (originalPattern.domainSize() < maxSize)
          //  return result;

        excluded.addAll(excludedNames);

        //ComplexPattern key = getDefaultPattern(pattern);
        ComplexPattern letterDigitPattern = originalPattern;

        try { letterDigitPattern = originalPattern.intersect(ComplexPattern.createFromRegexp("^[a-zA-Z0-9]+$"));}
        catch (REException e) { e.printStackTrace(); }

        ComplexPattern currentPattern = letterDigitPattern;

        while (count < maxSize) {
            try {
                currentName = currentPattern.generateNewWord(excluded);
                if (currentName != null) {
                    excluded.add(currentName);
                    result.add(currentName);
                    count++;
                } else if (currentPattern == letterDigitPattern) {
                    currentPattern = originalPattern;
                } else
                    return result;
            } catch (REException e) {
                e.printStackTrace();
            }
        }
        return result;

    }
  /*
    /**
     * to make sure that the pattern produces a non-empty string
     *
     * @param pattern
     * @return
     *
    public ComplexPattern getDefaultPattern(ComplexPattern pattern) {

        try {
            ComplexPattern defaultPattern = ComplexPattern.createFromRegexp("[a-zA-Z0-9]+");
            if (defaultPattern.intersect(pattern).domainSize() >= minPro) {
                pattern = pattern.intersect(defaultPattern);
            }
        } catch (REException e) {
            e.printStackTrace();
        }

        return pattern;

    }
*/

/*    public String generateName(ComplexPattern key) {
        ComplexPattern letterDigitKey = key.intersect(ComplexPattern.createFromRegexp("^[a-zA-Z0-9]+$"));
        String name = null;
        try {
            name = letterDigitKey.generateNewWord();
        } catch (Exception e) {
            try {
                name = key.generateNewWord();
            } catch (REException e) {
                e.printStackTrace();
            }
        }
        return name;
    }*/

    @Override
    public String toString() {
        return "GenObject{" + _sep +
                "minPro=" + minPro + _sep +
                ", maxPro=" + maxPro + _sep +
                ", CPart=" + CPart + _sep +
                ", RPart=" + RPart + _sep +
                ", choices=" + choiceList + _sep +
                '}' + _sep;
    }

    @Override
    public JsonElement getWitness() {
        return witness;
    }

    /**
     *
     * @param solution
     * @return
     */
    private String printSolutionWithStatus(List<GPattReq> solution){
        String output ="";// = "==variable status==\n";
        Map<String,String> outputMap = new HashMap<>();
        solution.forEach(gPattReq ->
                outputMap.put(gPattReq.getSchema().getName(),
                        gPattReq.getStatus().toString()));
        for(Map.Entry<String,String> entry:outputMap.entrySet())
            output+=entry.getKey()+"\t"+entry.getValue()+"\n";

        return output;
    }

    /*local classes*/
    /* WitnessProperty counterpart*/
    public class GProperty {
        private ComplexPattern key;
        private GenVar schema;

        public GenVar usedVar() {
            return schema;
        }

        @Override
        public String toString() {
            return "GProperty{" +
                    "key=" + key +
                    ", schema=" + schema +
                    '}';
        }

        public GProperty(WitnessProperty prop, GenEnv env) {
            /*String varname;
            WitnessAssertion value = prop.getValue();
            if (value.getClass() == WitnessBoolean.class)
                schema = ((WitnessBoolean) value).getValue() == true ? new GenVarTrue("dummy") : new GenVarFalse("dummy");
            else if (value.getClass() == WitnessVar.class) {
                //either variable already created in env or needs to be created
                varname = ((WitnessVar) value).getName();
                schema = env.getByNameElseCreate(varname);
            } else
                try {
                    throw new Exception("Properties must be normalized and map to WitnessVar or WitnessBool");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            schema = env.retrieveVar(prop.getValue());
            key = prop.getPattern();
        }

        public float patternDomainSize() {
            return key.domainSize();
        }

        /**
         * TODO temporary (to be removed)
         *
         * @param minPro
         * @return
         */
        public List<String> dummyGenerateNames(Double minPro) {
            String[] alph = {"a", "b", "c", "d", "e"};
            List<String> names = new LinkedList<>();

            for (int i = 0; i <= minPro; i++)
                names.add(alph[i % alph.length] + i);
            return names;
        }

        /**
         * @param maxSize
         * @return
         */
        public List<String> generateNames(Double maxSize) {
            return generateNames(maxSize, new LinkedList<>());
        }


        /**
         * TODO remove redundancy Giorgio: I hope I did remove it
         * returns the set of strings generated by key and excluding excludeNames up to maxSize
         *
         * @param maxSize
         * @param excludedNames
         * @return
         */
        public List<String> generateNames(Double maxSize, List<String> excludedNames) {
            return GenObject.generateNames(this.key, maxSize, excludedNames);
        }
/*            List<String> excluded = new LinkedList<>(),
                    result = new LinkedList<>();
            String current;
            int count = 0;
            if (key.domainSize() < maxSize)
                return result;

            excluded.addAll(excludedNames);

            key = getDefaultPattern(key);

            while (count < maxSize) {
                try {
                    current = key.generateNewWord(excluded);
                    if (current != null) {
                        excluded.add(current);
                        result.add(current);
                    } else
                        return result;
                } catch (REException e) {
                    e.printStackTrace();
                }
                count++;
            }
            return result;
        }

 */
    }

    /*WitnessPattReq counterpart*/
    public class GPattReq {
        private ComplexPattern key;
        private GenVar schema;
        //       private List<GPattReq> PattReqList;
//        private boolean isSimple;

        public statuses getStatus() {
            return this.schema.getStatus();
        }

        public ComplexPattern getKey() {
            return key;
        }

        public GenVar getSchema() {
            return schema;
        }

        @Override
        public int hashCode() {
            return key.hashCode() * schema.hashCode();
        }

        /**
         * R1 = R2 iff pattern(R1)=pattern(R2) and Var(R1) hasSameNameAs Var(R2)
         *
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            GPattReq other = (GPattReq) obj;
            boolean result = (this.key.equals(other.key) && this.schema.equals(other.schema));
//            System.out.println("comparing "+ this + " with "+ obj + " yields " + result) ;
            return result;
        }

        public float patternDomainSize() {
            return key.domainSize();
        }

        public JsonElement getWitness() {
            return schema.getWitness();
        }

        public GenVar usedVar() {
            return schema;
        }

        @Override
        public String toString() {
            return "GPattReq{" +
                    "key=" + key +
                    ", schema=" + schema +
//                    ", PattReqList=" + PattReqList +
                    '}';
        }


        /**
         * @param pattReq
         * @param env     pattReq must be normalized
         */
        public GPattReq(WitnessPattReq pattReq, GenEnv env) {
            //this.PattReqList = new LinkedList<>();
            schema = env.retrieveVar(pattReq.getValue());
            key = pattReq.getPattern();
        }

    }


    /*Methods*/

    public GenObject() {
        this.CPart = new LinkedList<>();
        this.RPart = new LinkedList<>();
        this.choiceList = new LinkedList<>();
        this.setDefaultMinMaxPro();
        //TODO remove after object preparation fixed
        _randomStrategy = false;
    }

    /*public void setCPart(List<WitnessProperty> propList, GenEnv env) {
        this.CPart = propList.stream().map(p -> new GProperty(p, env)).collect(Collectors.toList());
    }*/

    public Map<WitnessPattReq,GPattReq> setRPart(List<WitnessPattReq> pattReqList, GenEnv env) {
        /*List<WitnessPattReq> fragments = PattReqattReqList.stream().
                flatMap(e->e.getSubList().stream()).
                collect(Collectors.toList());*/
        Map<WitnessPattReq,GPattReq> reqMap = pattReqList.stream().
                collect(Collectors.toMap(w -> w, w -> new GPattReq(w, env)));
        //this.RPart = pattReqList.stream().map(p -> new GPattReq(p, env))
        //      .collect(Collectors.toList());
        this.RPart = reqMap.values().stream().
                collect(Collectors.toList());
        return reqMap;
    }

    public List<GProperty> getCPartFromChoices() {
        return choiceList.stream().map(c->c.getConstraint()).collect(toList());
    }

    public Map<WitnessProperty,GProperty > setCPart(List<WitnessProperty> propList, GenEnv env) {
        this.CPart = propList.stream().map(p -> new GProperty(p, env)).collect(Collectors.toList());

         Map<WitnessProperty,GProperty> constMap = propList.stream().
                collect(Collectors.toMap(w -> w, w -> new GProperty(w, env)));
        this.CPart = constMap.values().stream().
                         collect(Collectors.toList());
        return constMap;
    }

    /**
     *
     */
    public void setChoiceList(List<WitnessChoice> choices, Map<WitnessProperty, GProperty > constMap,
                                                           Map<WitnessPattReq,GPattReq> reqMap, GenEnv env) {
        choiceList = new LinkedList<>();
        for (WitnessChoice choice : choices) {
            List<GPattReq> gReqList = choice.getReqList().stream().
                    map(w -> reqMap.get(w)).collect(Collectors.toList());
            List<GPattReq> gSubList = choice.getSubList().stream().
                    map(w -> reqMap.get(w)).collect(Collectors.toList());
            List<GPattReq> gReqFullSet = choice.getReqFullSet().stream().
                    map(w -> reqMap.get(w)).collect(Collectors.toList());
            WitnessProperty wProp = choice.getConstraint();
            GProperty gConstraint =
                    (constMap.containsKey(wProp)
                    ? constMap.get(wProp)
                    : new GProperty(wProp,env));
            GChoice newChoice = new GChoice(gConstraint,
                    gReqList, gSubList,
                    gReqFullSet,choice.getPattern());
            newChoice.setSchema(choice.getSchema(),env);
            choiceList.add(newChoice);
        }
    }

    public void setMinMaxPro(WitnessPro minMaxPro) {
        minPro = minMaxPro.getMin();
        maxPro = minMaxPro.getMax();
    }

    public void setDefaultMinMaxPro() {
        minPro = 0.0;
        maxPro = Double.POSITIVE_INFINITY;
    }

    //TODO implement invariants
/*
    private boolean isRPartEmpty() {
        Optional<Boolean> opt = this.RPart.stream().map(PattReq -> PattReq.reqList.isEmpty()).reduce((a, b) -> a && b);
        if (opt.isPresent())
            return opt.get();
        else
            return true; //RPart is true
    }*/
    //aux methods

    /**
     * removes PattReqs that are  pointed by headChoice
     * @param PattReqList
     * @param headChoice
     * @return returns a copy of those PattRes that remain to satisfy
     */
    private  List<GPattReq> removePattReqs(List<GPattReq> PattReqList, GChoice headChoice){
        //for (GPattReq headPattReq: headChoice.PattReqList) {
        //    headPattReq.reqList.remove(headChoice);
        //}
        List<GPattReq> result = new LinkedList<>(PattReqList);
        result.removeAll(headChoice.getSubList());
        return result;
    }

    /**
     * removes  Choices that are pointed by an PattReq of headChoice
     * @param exploredChoices
     * @param tailPattReqList
     * @return
     */
    /*private List<GChoice> reduceChoices(List<GChoice> exploredChoices, List<GPattReq> tailPattReqList){
        HashSet<GChoice> result = new HashSet<>();
        for(GPattReq PattReq: tailPattReqList)
            result.addAll(PattReq.reqList);
        result.removeAll(exploredChoices);
        List<GChoice> listResult = new ArrayList<>(result);
        return listResult;
    }*/

    /**
     * removes  Choices that are useful for at least one missing request
     * @param choices
     * @param tailPattReqList
     * @return
     */
    private List<GChoice> usefulChoices(List<GChoice> choices, List<GPattReq> tailPattReqList){
        List<GChoice> result = new ArrayList<>();
        for(GChoice choice: choices) {
            boolean useful = false;
            for (GPattReq req : choice.getSubList())
                if (tailPattReqList.contains(req))
                    useful = true;
            if (useful)
                result.add(choice);
        }
        return result;
    }

    /**
     *
     * @param choices all choices that appear in the choice list???
     * @param reqList the reqList that we want to hit
     * @return all different sets of choices that are found in choices minus exploredChoices
     *         that hit all elements of reqList
     */
    private List<List<GChoice>> hittingSet(List<GChoice> choices, List<GPattReq> reqList) {

        if(reqList.isEmpty()) {
            // beware: new ArrayList<>(ArrayList<>()) WOULD NOT WORK!!!!
            // neither would new ArrayList<>(trivialSolution)
            List<GChoice> trivialSolution = new ArrayList<>();
            List<List<GChoice>> singletonOfTrivial = new ArrayList<>();
            singletonOfTrivial.add(trivialSolution);
            return singletonOfTrivial;
        }

        if(choices.isEmpty())
            return new ArrayList<>();

        GChoice headChoice = choices.get(0);
        //choices.remove(0);

        //remove the PattReqs that are already satisfied by headChoice
        List<GPattReq> tailPattReq = removePattReqs(reqList, headChoice);
        //focus on the choices that are still useful - of course headChoice
        //will not be one of them
        List<GChoice> tailChoices = usefulChoices(choices, tailPattReq);

        List<List<GChoice>> solWithHead = new LinkedList<>();

        List<List<GChoice>> solutionsOfRest = hittingSet(tailChoices, tailPattReq);
        for(List<GChoice> solutionOfRest: solutionsOfRest) {
            solutionOfRest.add(headChoice);
            solWithHead.add(solutionOfRest);
        }

        choices.remove(0);
        List<List<GChoice>> solWithoutHead = hittingSet(choices, reqList);

        solWithHead.addAll(solWithoutHead);

        return solWithHead;
    }

//    /**
//     *
//     * @param choices all choices that appear in the choice list???
//     * @param exploredChoices choices that we do not want to use in the solution
//     *                        so that we actually use choices minus exploredChoices
//     * @param reqList the reqList that we want to hit
//     * @return all different sets of choices that are found in choices minus exploredChoices
//     *         that hit all elements of reqList
//     */
    /*private List<List<GChoice>> obsoleteHittingSet(List<GChoice> choices,
                                           List<GChoice> exploredChoices,
                                           List<GPattReq> reqList) {

        if(reqList.isEmpty()){
            List<GChoice> emptyList = new ArrayList<>();
            List<List<GChoice>> trivialResult = new ArrayList<>();
            trivialResult.add(emptyList);
            return trivialResult;
        }

        List<GChoice> unexploredChoices = new ArrayList<>();
        unexploredChoices.addAll(choices);
        unexploredChoices.removeAll(exploredChoices);
        if(unexploredChoices.isEmpty())
            return new LinkedList<>();

        GChoice headChoice = unexploredChoices.get(0);
        unexploredChoices.remove(0);

        //remove the PattReqs that are already satisfied by headChoice
        List<GPattReq> tailPattReq = removePattReqs(reqList, headChoice);
        //List<GChoice> tailChoices = removeChoices(choices, headChoice);
        //We eliminate those choices that only satisfy PattReqs
        //that have been eliminated
        List<GChoice> tailChoices = usefulChoices(unexploredChoices, tailPattReq);
        //List<GChoice> tailChoices = reduceChoices(exploredChoices, tailPattReq);
        //We have removed all PattReqs where headChoice did appear, hence headChoice
        //does not appear in tailChoices any more

        List<List<GChoice>> solWithHead = new LinkedList<>();


        // either { headChoice } is a solution, hence solWith
        // if tailPattReq is empty, then the singleton "Current"
        // ie the singleton form by headChoice is a solution
        // when tailChoice is empty
        if(tailChoices.isEmpty()){
              solWithHead.add(new ArrayList<>(headChoice));
        }
        else {
            exploredChoices.add(headChoice);
            for(List<GChoice> solutionOfRest: hittingSet(tailChoices, exploredChoices, tailPattReq)){
                solutionOfRest.add(headChoice);
                solWithHead.add(solutionOfRest);
            }
            // the caller may use again this parameter, I must restore its value
            exploredChoices.remove(headChoice);
        }
        //choices.remove(headChoice);
        List<List<GChoice>> solWithoutHead = hittingSet(choices, exploredChoices, reqList);

        solWithHead.addAll(solWithoutHead);

        return solWithHead;
    }
    */

    // for each choice in sol it is not the case that all of its PattReqs
    // are included in the set of pattreqs of the other choices
    private boolean isMinimal(List<GChoice> sol, List<GPattReq> PattReqList){
//        if(sol.isEmpty())
//            return false;
        boolean result = true;
        for (GChoice choice : sol) {
            Set<GPattReq> satisfiedByOtherChoices = sol.stream().
                    filter(c->c!=choice).
                    flatMap(c->c.getSubList().stream()).
                    collect(Collectors.toSet());
            if (choice.getSubList().stream().allMatch(req->satisfiedByOtherChoices.contains(req)))
                result = false;
        }
        return result;
    }

    private List<List<GChoice>> optimizedHittingSet() throws Exception {
        List<List<GChoice>> result = new ArrayList<>();
        List<GChoice> forcedChoices;
        //ensure variant 1
        //if(isRPartEmpty())
        //  throw new Exception("invariant1 non verified!");
        //collect all requests that are only satisfied by 1 Choice 
        forcedChoices = new ArrayList<>();
        for (GPattReq req: RPart) {
            List<GChoice> choicesOfReq = choiceList.stream().
                    filter(c -> c.getSubList().contains(req)).collect(toList());
            if (choicesOfReq.size()==1)
                forcedChoices.add(choicesOfReq.get(0));
        }
        
       if(forcedChoices.size()==RPart.size()){
            result.add(List.copyOf(forcedChoices));
            return result;
        }
       
//        result.add(List.copyOf(forcedChoices));

        //proceed with the remaining Choices
        List<GPattReq> remainingPattReqs = RPart.stream().collect(Collectors.toList());
        remainingPattReqs.removeAll(forcedChoices.stream()
                                    .flatMap(c->c.getSubList().stream())
                                    .collect(Collectors.toList()));

        // the forcedChoices only satisfy requests that do not appear in any other
        // choice, hence any non-forced choice is useful to satisfy some of the
        // remaining PattReq's
        List<GChoice> remainingChoices = choiceList.stream().collect(Collectors.toList());
        remainingChoices.removeAll(forcedChoices);

        result = hittingSet(remainingChoices,remainingPattReqs)
                .stream().filter(l->isMinimal(l,remainingPattReqs))
                .collect(Collectors.toList());

        //if(result.isEmpty())
        //    result.add(List.copyOf(forcedChoices));
        //else
        //combine simple result with each solution
        for(int i=0; i<result.size(); i++)
        {
            List<GChoice> current = result.get(i);
            current.addAll(forcedChoices);
            result.set(i,current);
        }

//        System.out.println("this " + this + "\n \t result "+ result);
        return  result;
    }

    /**
     * TODO verify
     * checks whether all patterns can generate at least as many words as the number of their occurrence
      * @param set
     * @return
     */
    private boolean noPatternIsRepeatedTooOften(List<GChoice> set){
        Map<ComplexPattern,Long> grouped = set.stream().collect(groupingBy(GChoice::getPattern, Collectors.counting()));
        /*Optional<Boolean> opt = grouped.keySet().stream().map(p->grouped.get(p)<=p.domainSize())
                .reduce((a,b)->a&&b);
        if(opt.isPresent())
            return opt.get();
        else
            return false;*/
        return grouped.keySet().stream().allMatch(p->grouped.get(p)<=p.domainSize());
     }

    private boolean allVarsPop(List<GChoice> list){
        Optional<Boolean> tv = list.stream().map(p->p.usedVar().isPop()).reduce((a,b)->a&&b);
        if(tv.isPresent())
            return tv.get();
        else
            return true;
    }


    /**
     *
     * @param populatedCpart
     * @return
     */
    private float domainSize(List<GProperty> populatedCpart ){
        Optional<Float> optsum = populatedCpart.stream().map(p->p.patternDomainSize()).reduce((a,b)->a+b);
        float s = 0;
        if(optsum.isPresent())
            s=optsum.get();
        return s;
    }

    @Override
    public statuses generate()  {
        logger.debug("used variables {}",this.usedVars().stream().map(genVar -> genVar.getName()).collect(Collectors.toList()));

        HashMap<String,JsonElement> properties = new HashMap<>();
        witness = new JsonObject();


        //TODO (low) logging for coverage and elapsed time of optimization tests

        //if at least one request is not fulfilled, then the problem does not admit an instance
        /*for(GPattReq PattReq: RPart)
            if(PattReq.allVarsEmpty())
            {
                logger.debug("exit due to {} has all its Vars Empty", PattReq.usedVars().stream().map(genVar -> genVar.getName()).collect(Collectors.toList()));
                return statuses.Empty;
            }*/

        //if the constraining part fails to reach min, then the problem does not admit an instance
        float s = domainSize(CPart.stream().filter(p->p.usedVar().isOpen()||p.usedVar().isPop()).collect(Collectors.toList()));
        if(s<minPro)
        {
            logger.debug("exit due to  s<minPro");
            return statuses.Empty;
        }

        //deal with the case where RPart is empty. Propagate to the article
        if(RPart.size()>0)
        {
            List<List<GChoice>> hittingSets = null,
                            reducedSolutionSet = null;
            try {
                hittingSets = optimizedHittingSet();
                reducedSolutionSet = hittingSets.stream()
                        .filter(set->set.size()<=maxPro&&noPatternIsRepeatedTooOften(set))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(reducedSolutionSet.isEmpty())
            {
                logger.debug("exit due to  reducedSolutionSet.isEmpty() optimizedHittingSet() size {}", hittingSets.size());
                return statuses.Empty;
            }

            List<List<GChoice>> popSolutionSet = reducedSolutionSet.stream().filter(l->allVarsPop(l))
                    .collect(Collectors.toList());
            if(popSolutionSet.isEmpty())
                return statuses.Open;

            List<GChoice> solution;

//            System.out.println("popSolutionSet "+ popSolutionSet);
            //choose the solution either randomly or by taking the one which generates the  largest set of names
            if(_randomStrategy)
                solution = random(popSolutionSet);
            else
                //solution = minLengthStrategy(popSolutionSet);
               solution = maxLengthStrategy(popSolutionSet);

            //the following verification is an assertion
            if(solution == null)
                try {
                    throw new Exception("Solution must be assigned a value");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            logger.debug("solution {}", solution);
//            logger.debug("status {}", printSolutionWithStatus(solution));


            //old solution which tends to overwrite property names
            // solution.stream().forEach(req->properties.put(this.generateName(req.key),req.getWitness()));
            //grouped : (p, [x])  = solution group by p
            // for each (p, [x]) do properties + = mapping(generateNames(p,len([x])), [x])
            // Still, grouping by GChoice::getKey is not sufficient for two reasons:
            // (1) group by uses object equality, hence may fail to recognize equivalence
            // (2) whenever two patterns intersect they may generate the same name, hence
            // grouping by pattern equivalence solves the problem only if we are sure that every
            // two non-equivalent patterns have ampty intersection

            List<String> props ;
            List<GenVar> listVars;
            Map<ComplexPattern, List<GenVar>> grouped =
                    solution.stream().collect(groupingBy(GChoice::getPattern, mapping(GChoice::getSchema, toList())));

            for(Map.Entry<ComplexPattern,List<GenVar>> entry:grouped.entrySet()){
                listVars  = entry.getValue();
                props = generateNames(entry.getKey(), Double.valueOf(listVars.size()));
                //ensure that len(props) = len([x])
                if(props.size()!=listVars.size())
                    try {
                        throw new Exception("Pattern did not generate enough words!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                for(int i=0; i<props.size();i++) {
                    if (properties.containsKey(props.get(i))) {
                        try {
                            throw new Exception("Generating two properties with the same name: '" + props.get(i) + "'");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    properties.put(props.get(i), listVars.get(i).getWitness());
                }
            }

           if(properties.size()>=minPro){
                //no need to generate more, we can not generate more than maxPro
                properties.forEach((p,v)->((JsonObject) witness).add(p,v));
                return statuses.Populated;
            }
        }

        //complete with properties from CPart
        List<String> usedNames = Lists.newArrayList(properties.keySet()); //TODO go back to Set implementation
        int usedNamesSize = usedNames.size();
        List<GProperty> populatedCpart = CPart.stream().filter(p->p.usedVar().isPop()).collect(Collectors.toList());
        s = domainSize(populatedCpart);
        if(s < minPro) //TODO: check if s + usedNamesSize < minPro ||  s < minPro
            return statuses.Open;//var remains open until more usedVars become pop

        List<String> generatedNames;

        /**
         * many possible strategies:
         * - depth-first: which exhausts one patterns before moving to the other
         * - width-first: which uses round-robin to satisfy minPro by using every pattern equally
         * - weighted width-first: assign different weights to the branches
         */

        while(properties.size()<minPro){
            for(GProperty prop:populatedCpart)
            {
                generatedNames = prop.generateNames(minPro-usedNamesSize,usedNames);
                if(generatedNames.size()>0)
                {
                    generatedNames.forEach(name->properties.put(name, prop.usedVar().getWitness()));
                    usedNames.addAll(generatedNames);
                    usedNamesSize = usedNames.size(); //update
                    break; //pick behavior
                }
            }
        }

//        while(properties.size()<minPro){
//            //pick (p:x) in PopulatedCP with size (p - usedNames) > 0
//            for(GProperty prop:populatedCpart)
//            {
//                if(prop.usedVar().getStatus()==statuses.Populated)
//                {
//                    generatedNames = prop.generateNames(minPro-usedNamesSize,usedNames);
//                    if(generatedNames.size()>0)
//                    {
//                        generatedNames.forEach(name->properties.put(name, prop.usedVar().getWitness()));
//                        usedNames.addAll(generatedNames);
//                        usedNamesSize = usedNames.size(); //update
//                        break; //pick behavior
//                    }
//                }
//
//            }
//            //TODO check whether the solution is valid by checking the reason of exit
//        }

//        while(properties.size()<minPro){
//            populatedCpartStream = CPart.stream().filter(p->p.usedVar().isPop());
//            populatedCpart = populatedCpartStream.filter(p->!usedNames.contains(this.generateName(p.key)))
//                    .collect(Collectors.toList());
//            if(populatedCpart.size()>0)
//                newp = populatedCpart.remove(0);
//            List<String> names = newp.generateNames(minPro-usedNamesSize); //minPro as upper bound to the number of properties
//            GProperty finalNewp = newp;
//            names.forEach(name->properties.put(name, finalNewp.usedVar().getWitness()));
//            usedNames.addAll(names);
//            usedNamesSize = usedNames.size(); //update
//        }

//        while(properties.size()<minPro){
//            populatedCpart = populatedCpart.stream().filter(p->!usedNames.contains(p.generateName()))
//                    .collect(Collectors.toList());
//            GProperty newp = populatedCpart.remove(0);
//            String name = newp.generateName();
//            properties.put(name,newp.usedVar().getWitness());
//            usedNames.add(name);
//        }
        properties.forEach((p,v)->((JsonObject) witness).add(p,v));
        return statuses.Populated;
    }

    /**
     * pick solution randomly
     * @param solutionSet
     * @return
     */
    private List<GChoice> random(List<List<GChoice>> solutionSet){
        Random r = new Random();
        int position = r.nextInt(solutionSet.size());
        return solutionSet.get(position);
    }

    /**
     *
     * @param solutionSet
     * @return
     */
    private List<GChoice> maxLengthStrategy(List<List<GChoice>> solutionSet){
        HashMap<Integer,Long> map = new HashMap<>();
        List<GChoice> solution = null;
        //pick solution with the max number of distinct patterns
        for(int i=0; i<solutionSet.size(); i++)
            map.put(i,solutionSet.get(i).stream().map(c -> c.getPattern()).distinct().count());
        Long max = Collections.max(map.values());
//        Long min = Collections.min(maxMap.values());
        for(int i=0; i<solutionSet.size(); i++)
            if(map.get(i)==max){
                solution = solutionSet.get(i);
                break;
            }
        return solution;
    }

    /**
     *
     * @param solutionSet
     * @return
     */
    private List<GChoice> minLengthStrategy(List<List<GChoice>> solutionSet){
        HashMap<Integer,Long> map = new HashMap<>();
        List<GChoice> solution = null;
        //pick solution with the min number of distict patterns
        for(int i=0; i<solutionSet.size(); i++)
            map.put(i,solutionSet.get(i).stream().map(c -> c.getPattern()).distinct().count());
        Long min = Collections.min(map.values());
        for(int i=0; i<solutionSet.size(); i++)
            if(map.get(i)==min){
                solution = solutionSet.get(i);
                break;
            }
        return solution;
    }


    /** //TODO in case we need to deal with problem of
     * sort based on de.uni_passau.sds.patterns than decreasing size of associated assertions
     * @param solution
     * @return
     */
    private List<GChoice> simplify(List<GChoice> solution){
        return solution;
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
        List<GenVar> cpart_vars = CPart.stream().map(gp->gp.usedVar()).collect(Collectors.toList());
        List<GenVar> choice_vars = CPart.stream().map(c->c.usedVar()).collect(Collectors.toList());
        //List<GenVar> choice_vars = choiceList.stream().flatMap(c->c.usedVar().stream()).collect(Collectors.toList());
        return Stream.concat(cpart_vars.stream(), choice_vars.stream())
                .distinct()
                .collect(Collectors.toList());
    }


}
