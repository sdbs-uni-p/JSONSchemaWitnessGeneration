package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings.*;


public class GenEnv {

	private static Logger logger = LogManager.getLogger(GenEnv.class);

	//    private HashMap<GenVar, List<GenAssertion>> varList;
	private Map<GenVar, GenTypedAssertion> varMap;
	private BiMap<GenVar,GenVar> coVar;
	private GenVar rootVar;


	//    private Map<GenVar, List<GenVar>> uses;
	//    private Map<GenVar, List<GenVar>> usedBy;

	//    private List<GenVar> openVars, sleepingVars, emptyVars, popVars;
	private String _sep = "\r\n";
	private int _iterationFactor = 10;
	private openVars openVars;
//	private sleepingVars sleepingVars;
	private emptyVars emptyVars;
	private popVars popVars;

	/**
	 *
	 * @return
	 */
	private Map<String,Integer> varAssetLength(){
		Map<String,Integer> map = new HashMap<>();
		varMap.forEach((v,a)->map.put(v.getName(),a.size()));
		return map;
	}


	private class varQueue {
		public List<GenVar> queue;

		public List<GenVar> getQueue() {
			return queue;
		}

		public int headIndex = 0;

		public varQueue() {
			this.queue = new LinkedList<>();
		}

		public void sort(Comparator comparator){queue.sort(comparator);}

		public boolean isEmpty(){return queue.isEmpty();}

		@Override
		public String toString() {
			return "varQueue{" +
					"queue=" + queue +
					'}';
		}

		public GenVar removeHead() {
			return queue.remove(headIndex);
		}
		public GenVar getHead() {
			return queue.get(headIndex);
		}

		/**
		 * removes populated and empty vars
		 */
		public void keepOpenVars(){
			this.queue = this.queue.stream().filter(v->v.isOpen()).collect(Collectors.toList());
//			for(GenVar v: this.queue)
//				if(!v.isOpen())
//					this.queue.remove(v);
		}
	}

	private class openVars extends varQueue {
		public void add(GenVar v) {
			super.queue.add(v);
			v.setStatus(GenAssertion.statuses.Open);
		}
		public int size(){
			return super.queue.size();
		}
	}

//	private class sleepingVars extends varQueue {
//		public void add(GenVar v) {
//			super.queue.add(v);
//			v.setStatus(GenAssertion.statuses.Sleeping);
//		}
//	}

	private class emptyVars extends varQueue {
		public void add(GenVar v) {
			super.queue.add(v);
			v.setStatus(GenAssertion.statuses.Empty);
		}
	}

	private class popVars extends varQueue {
		public void add(GenVar v) {
			super.queue.add(v);
			v.setStatus(GenAssertion.statuses.Populated);
		}
	}



	private int nbVar(){
		return varMap.size();
	}

	private List<String> queueToString(List<GenVar> listGenVar) {
		return  listGenVar.stream().map(v->v.getName()).collect(Collectors.toList());
	}


	@Override
	public String toString() {
		return "GenEnv{" + _sep +
				"varList=" + varMap + _sep +
				", coVar=" + coVar +  _sep +
				", rootVar=" + rootVar + _sep +
				", openVars=" + openVars + _sep +
				", emptyVars=" + emptyVars + _sep +
				", popVars=" + popVars + _sep +
				'}'+ _sep;
	}

	/**
	 * Default constructor
	 */
	public GenEnv() {
		varMap = new HashMap<>();
		coVar = HashBiMap.create();
		openVars = new openVars(); //LinkedList<>();
		emptyVars = new emptyVars(); // LinkedList<>();
		popVars = new popVars(); //LinkedList<>();
	}

	/**
	 * chain may be cyclic
	 * @param root
	 * @return
	 */
	private List<GenVar> reachableFrom(GenVar root, List<GenVar> met){
		List<GenVar> toExplore = root.usedVars(), metextended;
		toExplore.remove(root);
		if(toExplore.isEmpty())
			return Stream.of(root).collect(Collectors.toList());
		else {
			toExplore.removeAll(met);
			metextended = met.stream().collect(Collectors.toList());
			metextended.add(root);
			metextended.addAll(toExplore.stream().flatMap(e-> reachableFrom(e, metextended).stream())
					.collect(Collectors.toList()));
			return metextended;
		}
	}
//Moved to WitnessEnv
//	/**
//	 * chain may be cyclic
//	 * @param root
//	 * @return
//	 */
//	private List<WitnessVar> reachableFrom(WitnessVar root, List<WitnessVar> met, WitnessEnv witnessEnv){
//		List<WitnessVar> toExplore = witnessEnv.getDefinition(root).uses(),
//				metextended;
//		if(toExplore.isEmpty())
//			return Stream.of(root).collect(Collectors.toList());
//		else {
//			toExplore.removeAll(met);
//			metextended = met.stream().collect(Collectors.toList());
//			metextended.add(root);
//			metextended.addAll(toExplore.stream().flatMap(e-> reachableFrom(e, metextended, witnessEnv).stream())
//					.distinct()
//					.collect(Collectors.toList()));
//			return metextended.stream().distinct().collect(Collectors.toList());
//		}
//	}



	/**
	 *
	 * @param env
	 * @throws Exception
	 */
	public GenEnv(WitnessEnv env) throws Exception {
		logger.debug("Creating a GenEnv from a WitnessEnv");

		//intializations
		varMap = new HashMap<>();
		openVars = new openVars();
		emptyVars = new emptyVars();
		popVars = new popVars();

		//two-step process to ensure that all variables are created before being used
		List<WitnessVar> reachableVars = env.reachableFrom(env.getRootVar(),new LinkedList<>());

		logger.debug("#vars reachable from root: {}",reachableVars.size());

		//initialize varMap
		env.getVarList().keySet().stream()
				.filter(v->reachableVars.contains(v))
				.forEach(var->varMap.put(new GenVar(var.getName()),new GenTypedAssertion()));

//		Set<WitnessVar> collectedVar =  new HashSet<>();
//		env.reachableRefs(collectedVar,env);
//		System.out.println("=======collectedVar=====");
//		System.out.println(collectedVar);

		logger.debug("Setting rootvar");
		//rootvar
		rootVar = getByNameElseCreate(env.getRootVar().getName());


		//fill varMap starting from intersection of env with varMap
		env.getVarList()
				.entrySet().stream()
//				.filter(v->varMap.containsKey(this.getByName(v.getKey().getName())))
				.filter(v->reachableVars.contains(v.getKey()))
				.collect(Collectors.toMap(e->e.getKey(),e->e.getValue()))
				.forEach((var, asser)->
		{
			try {
				varMap.put(this.getByName(var.getName()), fromWitnessDNF(asser));
			} catch (Exception e) {
				e.printStackTrace();
				// TODO - throw the exception
			}
		});

	initializeAuxDS();
	}


	/**
	 * initialize auxiliary data structures
	 */
	public void initializeAuxDS(){
		//dependency graph
		varMap.forEach((var, typedAssertion)->
			var.setUses(typedAssertion.getTypedAssertion()));

		varMap.keySet().forEach(var->{
			var.getUses().forEach(innerv ->
							innerv.addIsUsedBy(var));
		});


		//prepare lists
		varMap.forEach((var, typedAssertion)->var.setEvalOrder(typedAssertion.containsBaseType()));

		//initially set all vars to open
		varMap.keySet().forEach(var->openVars.add(var));

		openVars.sort(Comparator.comparing(GenVar::getEvalOrder));

		//        varMap.forEach((var, typedAssertion)->{
		//            List<GenVar> localUses = typedAssertion.getTypedAssertion().stream()
		//                    .flatMap(gen-> gen.usedVars().stream())
		//                    .collect(Collectors.toList());
		//            uses.put(var,localUses);
		//        });

	}


	/**
	 * Maps a DNF to a list of GenAssertion objects
	 * @param witAssert can be either WitnessBoolean, WitnessOr, WitnessAnd
	 *                 WitnessBoolean -> List[GenVarBool]
	 *                 WitnessOr(w1,w2,..)-> List[ [[w1]], [[w2]], ...]
	 *                 WitnessAnd(w1, w2,...) -> List[ [[w1, w2, ... ]] ]
	 *                 WitnessType -> List [ [|w1|], [|w2|], ... ]
	 *                  [[w]] = fromWitness(w)
	 *                  [|w|] = fromSingletonTypeOnly(w)
	 * @return
	 * @throws Exception 
	 */
	private GenTypedAssertion fromWitnessDNF(WitnessAssertion witAssert) throws Exception {
		logger.debug("Extracting assertions from {} ...", witAssert.toString());
		List<GenAssertion> result = new LinkedList<GenAssertion>();

		if(witAssert instanceof WitnessBoolean){
			result.add(((WitnessBoolean) witAssert).getValue()==true ?
					new GenVarTrue(""):new GenVarFalse(""));
		}
		else
			if(witAssert instanceof WitnessOr){
				for(List<WitnessAssertion> listWA: ((WitnessOr) witAssert).getOrList().values())
					for(WitnessAssertion WA: listWA) {
						try {
							//deal with the case type[t1, t2, ...] nested in an or
							//this is an duplicated treatment that should be eliminated
							//by flattening type[t1, ...] inside or
							//TODO
							if(WA instanceof WitnessType)
							{
								Set<String> types = ((WitnessType) WA).getType();
								types.forEach(typeName-> {
									try {
										result.add(fromSingletonTypeOnly(new WitnessType(typeName)));
									} catch (Exception e) {
										e.printStackTrace();
									}
								});
							}
							else
								result.add(fromWitness(WA));
						} catch (Exception e) {
							e.printStackTrace();
							throw e;
						}
					}
			}
			else if(witAssert instanceof WitnessAnd)
			{
				try {
					result.add(fromWitness(witAssert));
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
			else if(witAssert instanceof WitnessType){//solves the case type[t1, t2, ...] without any constraint
				Set<String> types = ((WitnessType) witAssert).getType();
				types.forEach(typeName-> {
					try {
						result.add(fromSingletonTypeOnly(new WitnessType(typeName)));
					} catch (Exception e) {
						e.printStackTrace();
//						throw e;
					}
				});
			}
			else
			{
				//TODO replace with test
				try {
					throw new Exception("Expected WitnessBoolean, WitnessOr, or WitnessAnd ;  found " +  witAssert.getClass() + " \n" + witAssert);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}

		logger.debug("... extracted {}",result);
		return new GenTypedAssertion(result);
	}



	/**
	 * Generates a GenAssertion for a typed group
	 * @param typedGroup can be either WitnessType, WitnessAnd, WitnessVar
	 *                   WitnessType -> Corresponding genType
	 *                   WitnessAnd -> Corresponding genType with constraints
	 *                   WitnessVar -> genVar
	 * @return
	 */
	private GenAssertion fromWitness(WitnessAssertion typedGroup) throws Exception {
		logger.debug("Converting assertion {} to GenAssertion", typedGroup);
		GenAssertion result = null;

		if(typedGroup instanceof WitnessBoolean){
			result = ((WitnessBoolean) typedGroup).getValue()==true ?
			 	new GenVarTrue(""):new GenVarFalse("");
		}
		else
		if(typedGroup instanceof WitnessType){
			result= fromSingletonTypeOnly((WitnessType) typedGroup);
		}
		else{
			if(typedGroup instanceof WitnessAnd){
				//retrieve the type assertion
				List<WitnessAssertion> wAssertList = ((WitnessAnd) typedGroup).getAndList()
						.values().stream().flatMap(List::stream)
						.collect(Collectors.toList());
				//            List<WitnessAssertion> wAssertList = new LinkedList<>();
				//            for(List<WitnessAssertion> witnessAssertionList:
				//                    ((WitnessAnd) typedGroup).getAndList().values()){
				//                for(WitnessAssertion witnessAssertion: witnessAssertionList)
				//                    wAssertList.add(witnessAssertion);
				//            }

				//1-retrieve the type assertion
				List<WitnessAssertion> wTypeList =  wAssertList.stream()
						.filter(o->o instanceof WitnessType)
						.collect(Collectors.toList());

				//retrieve the constraints
				List<WitnessAssertion> constraintsList =  wAssertList.stream()
						.filter(o->! (o instanceof WitnessType))
						.collect(Collectors.toList());

				if(wTypeList.size()<1)
					throw new Exception("No type construct in a typed group " + typedGroup);
				else if(wTypeList.size()>1)
					throw new Exception("More than one type construct in a typed group");
				//size()==1
				WitnessType wType = (WitnessType) wTypeList.get(0); //by default get the first element

				//2- retrieve the potential constraints
				result = fromSingletonTypeWithConstraints(wType,constraintsList);

			}
			else
			if(typedGroup instanceof WitnessVar){
				result = getByNameElseCreate(((WitnessVar)typedGroup).getName());
			}
			else
			{
				throw new Exception("Typed group must be either And or Type or ref, but found "+typedGroup);
			}
		}
		return result;

	}

	/**
	 * maps a type to its corresponding genType
	 * @param witnessType
	 * @return
	 */
	private GenAssertion fromSingletonTypeOnly(WitnessType witnessType) throws Exception {
		return fromSingletonTypeWithConstraints(witnessType, new LinkedList<>());
	}

	/**
	 *
	 * @param witnessType
	 * @param constraints
	 * @return
	 */
	private GenAssertion fromSingletonTypeWithConstraints(WitnessType witnessType,
			List<WitnessAssertion> constraints ) throws Exception {
		GenAssertion result = null;
		String[] typeList = witnessType.getType().toArray(String[]::new);
		if(typeList.length !=1)
			throw new Exception("Type assertion must contain ONE type. Current assertion contains  " + typeList.length + " types");
		//get the first (and unique) element of the list
		String typeName = typeList[0];
		//check to which case the group corresponds
		switch (typeName){
		case TYPE_BOOLEAN: //TODO attach ifBoolThen(b) constraint
			result = new GenBool();
			List<WitnessIfBoolThen> witnessIfBoolThenList =  constraints.stream()
					.filter(o->o instanceof WitnessIfBoolThen)
					.map(e->(WitnessIfBoolThen)e)
					.collect(Collectors.toList());
			if(witnessIfBoolThenList.size()>1)
				throw new Exception("More than one WitnessIfBoolThen constraints");
			else
			if(witnessIfBoolThenList.size()==1)
				((GenBool) result).setWitness(witnessIfBoolThenList.get(0).getValue());

			break;
		case TYPE_NULL:
			result = new GenNull();
			break;
		case TYPE_NUMBER: case TYPE_NUMNOTINT: case TYPE_INTEGER:
			if(typeName== TYPE_INTEGER)
				result = new GenNum(true);
			else
				result = new GenNum(false);
				if(constraints.size()>0){
				try {
					attachNumConstraints((GenNum) result, constraints);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case TYPE_STRING:
			result = new GenString();
			if(constraints.size()>0){
				try {
					attachStringConstraints((GenString)result, constraints);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case TYPE_OBJECT:
			result = new GenObject();
			if(constraints.size()>0)
				attachObjectConstraints((GenObject)result, constraints);
			break;
		case TYPE_ARRAY:
			result = new GenArray();
			if(constraints.size()>0)
				attachArrayConstraints((GenArray) result,constraints);
			else
				((GenArray) result).setGenArrayType(GenAssertion.GenArrayType.EMPTY);//TODO default behavior that may be changed when dealing with examples
			break;
		default:throw new Exception("Undefined Type "+typeName);
		}
		return result;
	}


	/**
	 *
	 * @param constraints
	 * @param wClass
	 */
	private List<WitnessAssertion> getInstanceOf(List<WitnessAssertion> constraints, Class<?> wClass) {
		return constraints.stream()
				.filter(o->o.getClass() == wClass)
				.collect(Collectors.toList());
	}

	/**
	 * side-effect on genArray
	 * @param genArray
	 * @param constraints
	 * @throws Exception
	 */
	private void attachArrayConstraintsOld(GenArray genArray, List<WitnessAssertion> constraints) throws Exception {
		int _idx = 0; //default index
		double max;

		//in principle only itemsList or prepItemsList set at time (invariant0)
		List<WitnessAssertion> itemsList = getInstanceOf(constraints,WitnessItems.class),
				prepItemsList = getInstanceOf(constraints, WitnessItemsPrepared.class),
				minMaxItemsList = getInstanceOf(constraints, WitnessContains.class),
				repeatedItemsList = getInstanceOf(constraints, WitnessRepeateditems.class),
				uniqueItemsList = getInstanceOf(constraints, WitnessUniqueItems.class);


		/**
		 * possible cases:
		 * WitnessItems
		 * WitnessItemsPrepared
		 */
		int ilSize = itemsList.size(), //items
				ilpSize = prepItemsList.size(), //prepared items
				minMaxItemsSize = minMaxItemsList.size(),
				repeatedItemsSize = repeatedItemsList.size(),
				uniqueItemsSize = uniqueItemsList.size() ;


		if((ilSize>0 && ilpSize>0)  //both items and prepared items
				||  (ilpSize>0 && minMaxItemsSize!=1) )
			throw new Exception("ill-formed  WitnessArray ");


		//set minItems and maxItems
		WitnessContains minMaxItems = (WitnessContains) minMaxItemsList.get(_idx);
		genArray.setMinItems( (int) Math.round(minMaxItems.getMin()));
		max = minMaxItems.getMax();
		if(max==Double.POSITIVE_INFINITY)
			genArray.setMaxItems(Integer.MAX_VALUE);
		else
			genArray.setMaxItems( (int) Math.round(max));

		if(ilpSize==0)
		{
			genArray.setGenArrayType(GenAssertion.GenArrayType.NOITEMS);
		}
			else
				if(ilpSize==1){

					//retrieve the prepared items
					WitnessItemsPrepared witnessItemsPrepared = (WitnessItemsPrepared) prepItemsList.get(_idx);

					if(witnessItemsPrepared.getContains().length==1)
						genArray.setGenArrayType(GenAssertion.GenArrayType.ONECONTAINS);
					else
						genArray.setGenArrayType(GenAssertion.GenArrayType.GENERALCASE);//TODO add many simple count case


					//items#
					genArray.setfItems(witnessItemsPrepared.getItems()
							.stream()
							.map(witnessAssertions -> new GenArray.GVarMap(this, witnessAssertions))
							.collect(Collectors.toList()));
					//addItems#
					WitnessAssertion[] addPItems = witnessItemsPrepared.getAdditionalItems();
					if(addPItems != null)
						genArray.setfAdditionalItems(new GenArray.GVarMap(this, addPItems));

					//contains
					//retrieving min, max, position and schemas
					WitnessContains[] witnessContainsArray = witnessItemsPrepared.getContains();
					int containsLength = witnessContainsArray.length;
					genArray.setContainsLength(containsLength);
					//map the min, max and positions
					Integer[] minArray = new Integer[containsLength],
							maxArray = new Integer[containsLength],
							afterIndexArray = new Integer[containsLength];
					for(int i=0; i<containsLength; i++){
						minArray[i]= (int) Math.round(witnessContainsArray[i].getMin());
						max = witnessContainsArray[i].getMax();
						if(max == Long.MAX_VALUE || max == Double.POSITIVE_INFINITY)
							maxArray[i]=Integer.MAX_VALUE;
						else
							maxArray[i]= (int) Math.round(max);
						afterIndexArray[i]= (int) Math.round(witnessContainsArray[i].getPosition());
					}
					genArray.setMinArray(minArray);
					genArray.setMaxArray(maxArray);
					genArray.setafterIndexArray(afterIndexArray);
					//set the schemas (vars)
					genArray.setAssertionArray(Arrays.stream(witnessItemsPrepared.getContains())
							.map(witnessContains -> retrieveVar(witnessContains.getContains()))
							.map(witnessVar -> getByNameElseCreate(witnessVar.getName()))
							.collect(Collectors.toList()));

				}
				else throw new Exception("Problem during array translation!");

		genArray.setTupleLength();
	}


	/**
	 * attaches array constraints
	 * side-effect on genArray
	 * @param genArray
	 * @param constraints
	 */
	private void attachArrayConstraints(GenArray genArray, List<WitnessAssertion> constraints)
			throws Exception {
		int _idx = 0; //default index
		double max;
		WitnessContains minMaxContains;
		//in principle only itemsList or prepItemsList set at time (invariant0)
		List<WitnessAssertion> itemsList = getInstanceOf(constraints,WitnessItems.class),
				prepItemsList = getInstanceOf(constraints, WitnessItemsPrepared.class),
				contains = getInstanceOf(constraints, WitnessContains.class),
				repeatedItemsList = getInstanceOf(constraints, WitnessRepeateditems.class),
				uniqueItemsList = getInstanceOf(constraints, WitnessUniqueItems.class);

		/**
		 * possible cases:
		 * witnessContains ?, (WitnessItems |WitnessItemsPrepared)
		 * */
		int ilSize = itemsList.size(), //items
			ilpSize = prepItemsList.size(), //prepared items
			containsSize = contains.size(),
		 	repeatedItemsSize = repeatedItemsList.size(),
			uniqueItemsSize = uniqueItemsList.size() ;

		/*at least one of the above variables >0
		* rule out inconsistent cases*/
		if((ilSize>0 && ilpSize>0) ||  //both items and prepared items
				(repeatedItemsSize>0 && uniqueItemsSize>0)) //both repeatedItems and uniqueItems
			throw new Exception("ill-formed  WitnessArray ");


		if(containsSize>0){
			//set minItems and maxItems
			minMaxContains = (WitnessContains) contains.get(_idx);
            //double min = minMaxContains.getMin();
            //long roundedMin = Math.round(min);
			genArray.setMinItems( (int) Math.round(minMaxContains.getMin()));
			max = minMaxContains.getMax();
			if(max==Double.POSITIVE_INFINITY)
				genArray.setMaxItems(Integer.MAX_VALUE);
			else
				genArray.setMaxItems( (int) Math.round(max));
		}

		//at least ilSize or ilpSize >0
		//Case 1: constraints consist of Items
		if(ilSize==1)
		{
			genArray.setGenArrayType(GenAssertion.GenArrayType.NOCONTAINS);

			WitnessItems witnessItem = (WitnessItems) itemsList.get(_idx);
			genArray.setItems(getInstanceOf(witnessItem.getItems(), WitnessVar.class)
					.stream().map(witnessVar-> getByNameElseCreate(((WitnessVar)witnessVar).getName()))
					.collect(Collectors.toList())
					);
//			additionalItemsVar = (WitnessVar)witnessItem.getAdditionalItems();
//			if(additionalItemsVar != null)
//			genArray.setAdditionalItems(
//					getByNameElseCreate(additionalItemsVar.getName())
//					);
			WitnessAssertion addItems = witnessItem.getAdditionalItems();
			if(addItems!=null)
				genArray.setAdditionalItems(retrieveVar(addItems));

		}
		else
			//Case 2: constraints consist of Items# (with at least one contains)
			if(ilpSize==1){

				WitnessItemsPrepared witnessItemsPrepared = (WitnessItemsPrepared) prepItemsList.get(_idx);

				genArray.setGenArrayType(GenAssertion.GenArrayType.GENERALCASE);//TODO add many simple count case

//					if(witnessItemsPrepared.getContains().length==1)
//						genArray.setGenArrayType(GenAssertion.GenArrayType.ONECONTAINS);
//					else
//						genArray.setGenArrayType(GenAssertion.GenArrayType.GENERALCASE);//TODO add many simple count case


//					//set minItems and maxItems
//					WitnessContains minMaxItems = (WitnessContains) minMaxItemsList.get(_idx);
//
//					genArray.setMinItems( (int) Math.round(minMaxItems.getMin()));
//					max = minMaxItems.getMax();
//					if(max==Double.POSITIVE_INFINITY)//TODO
//						genArray.setMaxItems(Integer.MAX_VALUE);
//					else
//						genArray.setMaxItems( (int) Math.round(max));


				//items#
				genArray.setfItems(witnessItemsPrepared.getItems()
						.stream()
						.map(witnessAssertions -> new GenArray.GVarMap(this, witnessAssertions))
						.collect(Collectors.toList()));
				//addItems
				WitnessAssertion[] addPItems = witnessItemsPrepared.getAdditionalItems();
				if(addPItems != null)
					genArray.setfAdditionalItems(new GenArray.GVarMap(this, addPItems));
				//contains
				//retrieving min, max, position and schemas
				WitnessContains[] witnessContainsArray = witnessItemsPrepared.getContains();
				int containsLength = witnessContainsArray.length;
				genArray.setContainsLength(containsLength);
				//map the min, max and positions
				Integer[] minArray = new Integer[containsLength],
									maxArray = new Integer[containsLength],
										afterIndexArray = new Integer[containsLength];
				for(int i=0; i<containsLength; i++){
					minArray[i]= (int) Math.round(witnessContainsArray[i].getMin());
					max = witnessContainsArray[i].getMax();
					if(max == Long.MAX_VALUE || max == Double.POSITIVE_INFINITY)
						maxArray[i]=Integer.MAX_VALUE;
					else
						maxArray[i]= (int) Math.round(max);
					afterIndexArray[i]= (int) Math.round(witnessContainsArray[i].getPosition());
				}
				genArray.setMinArray(minArray);
				genArray.setMaxArray(maxArray);
				genArray.setafterIndexArray(afterIndexArray);
				//set the schemas (vars)
				genArray.setAssertionArray(Arrays.stream(witnessItemsPrepared.getContains())
						.map(witnessContains -> retrieveVar(witnessContains.getContains()))
						.map(witnessVar -> getByNameElseCreate(witnessVar.getName()))
						.collect(Collectors.toList()));

			}
//			else
//				throw new Exception("Problem during array translation!");
		//valid for both cases
		genArray.setTupleLength();

	}


	public GenVar retrieveVar(WitnessAssertion witAssert) {
		GenVar v = null;
		if (witAssert instanceof WitnessBoolean)
			if (((WitnessBoolean) witAssert).getValue() == true)
				v = new GenVarTrue("");
			else
				v = new GenVarFalse("");
		else if (witAssert instanceof WitnessVar)
			v = this.getByNameElseCreate(((WitnessVar) witAssert).getName());

		if(v == null)
			try {
				throw new Exception("unable to retrieve variable " + witAssert + " from the environment");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return v;
	}


	/**
	 * attaches numeric constraints, if any
	 * side-effect on genNum
	 * Remark: does not verify the presence of non-applicable constraints
	 * @param genNum
	 * @param constraints
	 */
	private void attachNumConstraints(GenNum genNum, List<WitnessAssertion> constraints) throws Exception {
		//Between
		List<WitnessBet> witnessBetList = constraints.stream()
				.filter(o->o instanceof WitnessBet)
				.map(e->(WitnessBet)e)
				.collect(Collectors.toList());
		if(witnessBetList.size()>1)
			throw new Exception("More than one Witness Bet constraints");
		else
			if(witnessBetList.size()==1)
				genNum.addMinMax(witnessBetList.get(0));

		//XBetween
		List<WitnessXBet> witnessXBetList = constraints.stream()
				.filter(o->o instanceof WitnessXBet)
				.map(e->(WitnessXBet)e)
				.collect(Collectors.toList());
		if(witnessXBetList.size()>1)
			throw new Exception("More than one Witness XBet constraints");
		else
			if(witnessXBetList.size()==1)
				genNum.addMinMax(witnessXBetList.get(0));

		//WitnessMof
		List<WitnessMof> witnessMofList = constraints.stream()
				.filter(o->o instanceof WitnessMof)
				.map(e->(WitnessMof)e)
				.collect(Collectors.toList());
		if(witnessMofList.size()>1)
			throw new Exception("More than one mof constraints");
		else
			if(witnessMofList.size()==1)
				genNum.setMof(witnessMofList.get(0));

		//WitnessNotMof
		List<WitnessNotMof> witnessNotMofList = constraints.stream()
				.filter(o->o instanceof WitnessNotMof)
				.map(e->(WitnessNotMof)e)
				.collect(Collectors.toList());
		if(witnessNotMofList.size()>=1)
			genNum.setNotMofs(witnessNotMofList);
	}

	/**
	 * attaches string constraints, if any
	 * side-effect on genString
	 * Remark: does not verify the presence of non-applicable constraints
	 * @param genString
	 * @param constraints
	 * @throws Exception
	 */
	private void attachStringConstraints(GenString genString, List<WitnessAssertion> constraints) throws Exception {
		List<WitnessPattern> witnessPatternList = constraints.stream()
				.filter(o->o instanceof WitnessPattern)
				.map(e->(WitnessPattern)e)
				.collect(Collectors.toList());
		if(witnessPatternList.size()>1)
			throw new Exception("More than one pattern constraints");
		else
			if(witnessPatternList.size()==1)
				genString.setPattern(witnessPatternList.get(0).getPattern());
	}


	/**
	 * attaches object constraints, if any
	 * side-effect on genObject
	 * Remark: does not verify the presence of non-applicable constraints
	 * @param genObject
	 * @param constraints
	 */
	private void attachObjectConstraints(GenObject genObject, List<WitnessAssertion> constraints) throws Exception {
		//WitnessPro
		List<WitnessPro> witnessProList =  constraints.stream()
				.filter(o->o instanceof WitnessPro)
				.map(e->(WitnessPro)e)
				.collect(Collectors.toList());
		if(witnessProList.size()==0) {
			genObject.setDefaultMinMaxPro();
		}
		else
			if(witnessProList.size()==1)
				genObject.setMinMaxPro(witnessProList.get(0));
			else
				throw new Exception("More than one WitnessPro constraints");



		//WitnessProperty
		List<WitnessProperty> witnessPropertyList = constraints.stream()
				.filter(o->o instanceof WitnessProperty)
				.map(e->(WitnessProperty)e)
				.collect(Collectors.toList());
		genObject.setCPart(witnessPropertyList, this);

		//WitnessOrPattReq
		List<WitnessOrPattReq> witnessOrPattReqList = constraints.stream()
				.filter(o->o instanceof WitnessOrPattReq)
				//.filter(o->((WitnessOrPattReq) o).getReqList().size()!=0 ) commented by GG
				.map(e->(WitnessOrPattReq)e)
				.collect(Collectors.toList());
		genObject.setRPart(witnessOrPattReqList,this);
		genObject.setObjectReqList();
		genObject.setORPList(); //TODO check
	}


	public GenVar getByName(String name){
		GenVar res = null;
		for(GenVar v: varMap.keySet())
			if(v.getName().compareTo(name)==0){
				res = v;
				break;
			}
		return res;
	}
	/**
	 * returns variable named name if it exists in GenEnv
	 * otherwise creates a fresh one
	 * @param name
	 * @return
	 */
	public GenVar getByNameElseCreate(String name){
		GenVar res = null;
		for(GenVar v: varMap.keySet())
			if(v.getName().compareTo(name)==0){
				res = v;
				break;
			}
		if(res==null)
			res = new GenVar(name);
		return res;
	}

	private JsonElement dummyJson(int code){
		String reason = "";
		switch(code) {
		case 0:
			reason = "Max iterations reached";
			break;
		case 1:
			reason = "Empty witness";
			break;
		default:
			reason = "unknown reason";
		}        JsonObject innerObject = new JsonObject();
		innerObject.addProperty("Problem", reason);
		return innerObject;
	}

	//    private boolean noWitness(JsonElement e){
	//        return  e.getAsJsonObject().get(_status).getAsInt()==_noWitness;
	//    }


	/**
	 * policy for choosing variable to generate
	 * assume that at least one assertion yields an instance
	 * @return
	 */
	public JsonElement generate() {
		logger.debug("===generate===");
		logger.debug("env {}",varAssetLength());
		JsonElement witness = null;
		GenTypedAssertion currentAssertion;
		GenAssertion.statuses status;
		int nbOpenVarsPreviousState = 0,//used in fixpoint evaluation
						iteration = 0,
						openVarSize = openVars.size();


		//pick head and generate
		while (nbOpenVarsPreviousState != openVarSize  && openVarSize >0){
			logger.debug("iteration# {} | nbOpenVarsPreviousState {} | openVars.size() {}",iteration,nbOpenVarsPreviousState,openVarSize);
			iteration++;
			nbOpenVarsPreviousState = openVars.size();

			for(GenVar currentVar: openVars.getQueue()){
				currentAssertion = varMap.get(currentVar);
				logger.debug("currentVar {} has generators {} and {} used variables", currentVar.getName(),
						currentAssertion.getLength(),
						currentAssertion.usedVars().size());
				status = currentAssertion.generate();
				currentVar.setStatus(status);
				logger.debug("status {}", status);
				if(status == GenAssertion.statuses.Populated)
					currentVar.setWitness(currentAssertion.getWitness());

				//is root variable
				if (currentVar == rootVar || varMap.size() == 1){
					if(currentVar.isOpen())
						continue;
					else {
						if (currentVar.isPop())
							witness = currentVar.getWitness();
						else if(currentVar.isEmpty())
							witness = dummyJson(1);
						return witness;
					}
				}
			}
			//update openVars by removing pop and empty
			openVars.keepOpenVars();
			openVarSize = openVars.size();
		}

		if(nbOpenVarsPreviousState == openVars.size())
			witness =  dummyJson(1);

		return witness;
	}
//	public JsonElement generate() throws Exception {
////		long nbIterations = 0, maxIterations = _iterationFactor*nbVar();
//		JsonElement witness = null;
////		GenVar currentVar = null, v = null;
//		GenTypedAssertion currentAssertion = null;
//		GenAssertion.statuses status = null;
//		int nbOpenVarsPreviousState = 0; //used in fixpoint evaluation
//
//		//pick head and generate
//		while (nbOpenVarsPreviousState != openVars.size()  && openVars.size()>0){
//			nbOpenVarsPreviousState = openVars.size();
//
//			for(GenVar currentVar: openVars.getQueue()){
////				System.out.println("current var "+currentVar);
//				currentAssertion = varMap.get(currentVar);
//				status = currentAssertion.generate();
//				currentVar.setStatus(status);
//				if(status == GenAssertion.statuses.Populated)
//					currentVar.setWitness(currentAssertion.getWitness());
//
//				if ((currentVar == rootVar ||varMap.size() == 1) && currentVar.isPop()) {
//					witness = currentVar.getWitness();
//					return witness;
//				}
//				if(currentVar == rootVar && currentVar.isEmpty())
//					return dummyJson(1);
//			}
//			//update openVars by removing pop and empty
//			openVars.keepOpenVars();
//		}
//
//
//
//		if(nbOpenVarsPreviousState == openVars.size() || status== GenAssertion.statuses.Empty)
//			return dummyJson(1);
//		else
//			return witness;
//	}



}

//previous version of fixpoint evaluation that does not work with recursive definitions
//while (nbIterations<maxIterations && (!sleepingVars.isEmpty()||!openVars.isEmpty())){
//		nbIterations++;
//		if(openVars.isEmpty()) //sleepingVars can not be empty
//		openVars.add(sleepingVars.removeHead());
//
//		currentVar = openVars.getHead();
//		currentAssertion = varMap.get(currentVar);
//
//		if(currentAssertion.containsBaseType()||currentVar.allVarsPopOrEmp()){
//		status = currentAssertion.generate();
//
//		if(status == GenAssertion.statuses.Populated){
//		popVars.add(openVars.removeHead());
//		currentVar.setWitness(currentAssertion.getWitness());
//		}
//		else
//		if(status == GenAssertion.statuses.Empty)
//		emptyVars.add(openVars.removeHead());
//
//		if (currentVar.isRoot() ||varMap.size() == 1) {
//		witness = currentVar.getWitness();
//		}
//
//		}
//		else //wait for more vars to be generated
//		sleepingVars.add(openVars.removeHead());
//		}
//