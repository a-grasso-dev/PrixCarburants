package emiage.prixcarburants.domaine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarburantTest {
    @Test
    public void idCarburantTest() {
        Carburant carburant = new Carburant(3,"Gazole");
        assertEquals(3, carburant.getIdCarburant());
    }

    @Test
    public void nomCarburantTest() {
        Carburant carburant = new Carburant(3,"Gazole");
        assertEquals("Gazole", carburant.getNomCarburant());
    }
}
