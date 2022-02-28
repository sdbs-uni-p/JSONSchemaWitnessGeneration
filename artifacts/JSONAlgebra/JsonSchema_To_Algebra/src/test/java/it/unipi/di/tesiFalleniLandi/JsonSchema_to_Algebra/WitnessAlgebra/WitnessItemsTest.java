package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra;

import org.junit.Test;
import de.uni_passau.sds.patterns.REException;

import static org.junit.Assert.*;

public class WitnessItemsTest {

    @Test
    public void mergeItemsTest1() throws REException {
        WitnessItems items1 = new WitnessItems();
        items1.addItems(new WitnessMof(2.0));
        items1.addItems(new WitnessMof(4.0));
        items1.setAdditionalItems(new WitnessMof(3.0));

        WitnessItems items2 = new WitnessItems();
        items2.addItems(new WitnessMof(3.0));
        items2.addItems(new WitnessMof(3.0));
        items2.setAdditionalItems(new WitnessMof(7.0));

        WitnessItems output = new WitnessItems();
        output.addItems(new WitnessMof(6.0));
        output.addItems(new WitnessMof(12.0));
        output.setAdditionalItems(new WitnessMof(21.0));

        assertEquals(items1.mergeElement(items2, null, null), output);
    }

    @Test
    public void mergeItemsTest2() throws REException {
        WitnessItems items1 = new WitnessItems();
        items1.addItems(new WitnessMof(2.0));
        items1.addItems(new WitnessMof(4.0));
        items1.setAdditionalItems(new WitnessMof(3.0));

        WitnessItems items2 = new WitnessItems();
        items2.addItems(new WitnessMof(3.0));
        items2.setAdditionalItems(new WitnessMof(7.0));

        WitnessItems output = new WitnessItems();
        output.addItems(new WitnessMof(6.0));
        output.addItems(new WitnessMof(28.0));
        output.setAdditionalItems(new WitnessMof(21.0));

        assertEquals(items1.mergeElement(items2, null, null), output);
    }

}