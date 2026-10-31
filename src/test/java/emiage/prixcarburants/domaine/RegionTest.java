package emiage.prixcarburants.domaine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegionTest {
    @Test
    public void testCodeRegion() {
        Region region = new Region("20","Limousin");
        assertEquals("20", region.getCodeRegion());
    }
    @Test
    public void testNomRegion() {
        Region region = new Region("20","Limousin");
        assertEquals("Limousin", region.getNomRegion());
    }
}
