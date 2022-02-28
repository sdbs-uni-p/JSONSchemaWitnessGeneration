package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.GenAlgebra;

import com.google.gson.JsonElement;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessAssertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessVar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenVar implements GenAssertion{
    private static Logger logger = LogManager.getLogger(GenVar.class);
//    enum statuses {Open, Sleeping, Empty, Populated };

    private JsonElement witness;

    public void setWitness(JsonElement witness) {
        this.witness = witness;
    }

    private String name ;
    private List<GenVar> uses;
    private List<GenVar> isUsedBy;
    private int evalOrder;
    private statuses status;


    /**
     * by default hash code of name
     * @return
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GenVar otherVar = (GenVar) obj;
        boolean result =  (otherVar.getName().equals(this.name));
//        System.out.println("comparing "+ this + " with "+ obj + " yields " + result) ;
        return result;
    }

    public statuses getStatus() {
        return status;
    }

    public void setStatus(statuses status) {
        this.status = status;
    }

    public void setEvalOrder(boolean containsBaseType) {
        this.evalOrder = this.inDegree();
        if(containsBaseType) //increase the importance in case it is a baseType
            this.evalOrder --;
    }

    /**
     *
     * @return The fact that all variables that uses are PopOrEmp
     */
    public boolean allVarsPopOrEmp() {
        Optional<Boolean> obj = uses.stream().map(v -> v.isPop() || v.isEmpty()).reduce((b1, b2) -> b1 && b2);
        return obj.orElse(true); //if uses is empty then consider as empty
    }

    public int getEvalOrder() {
        return evalOrder;
    }

    public List<GenVar> getUses() {
        return this.uses;
    }

    public void addIsUsedBy(GenVar using) {
        this.isUsedBy.add(using);
    }

    private int inDegree(){
        return isUsedBy.size();
    }

    private int outDegree(){
        return uses.size();
    }

    public void setUses(List<GenAssertion> genAssertionList) {
//        this.uses = genAssertionList.stream()
//                .flatMap(a->a.usedVars().stream())
//                .collect(Collectors.toList());
//        genAssertionList.forEach(genAssertion -> this.uses.addAll(genAssertion.usedVars()));
        for(GenAssertion genAssertion : genAssertionList )
            this.uses.addAll(genAssertion.usedVars());
        this.uses = this.uses.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "GenVar{" +
                "name='" + name + '\'' +
                ", uses=" + uses.stream().map(v->v.getName()).collect(Collectors.toList())  +
//                ", isUsedBy=" + isUsedBy +
                ", isUsedBy=" + isUsedBy.stream().map(v->v.getName()).collect(Collectors.toList()) +
                '}'+_sep ;
    }

    public String getName() {
        return name;
    }

    public GenVar(String varname) {
        name=varname;
        this.isUsedBy = new LinkedList<>();
        this.uses = new LinkedList<>();
        logger.debug("Created a var named {} ",this.name);

    }


    public boolean isOpen() {
        return status==statuses.Open;
    }

    public boolean isEmpty() {return status==statuses.Empty;}

    public boolean isPop() {
        return status==statuses.Populated;
    }


//    public boolean isRoot() {return name.equals(it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.AlgebraStrings.ROOTDEF_DEFAULTNAME);}

    @Override
    public JsonElement getWitness() {
        return this.witness;
    }

    @Override
    public statuses generate() {
        return this.status;
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
        return this.getUses();
    }


}
