package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

public class ReportResults {
    private int numOfElementInAnyOf;
    private int numOfElementInAllOf;

    public ReportResults(){
        numOfElementInAnyOf = 0;
        numOfElementInAllOf = 0;
    }

    public void increaseNumOfElementInAnyOf(){
        numOfElementInAnyOf++;
    }

    public void increaseNumOfElementInAllOf(){
        numOfElementInAnyOf++;
    }

    public void setNumOfElementInAnyOf(int numOfElementInAnyOf) {
        this.numOfElementInAnyOf = numOfElementInAnyOf;
    }

    public void setNumOfElementInAllOf(int numOfElementInAllOf) {
        this.numOfElementInAllOf = numOfElementInAllOf;
    }

    public int getNumOfElementInAnyOf() {
        return numOfElementInAnyOf;
    }

    public int getNumOfElementInAllOf() {
        return numOfElementInAllOf;
    }

    @Override
    public String toString() {
        return "ReportResults{" +
                "numOfElementInAnyOf=" + numOfElementInAnyOf +
                ", numOfElementInAllOf=" + numOfElementInAllOf +
                '}';
    }
}
