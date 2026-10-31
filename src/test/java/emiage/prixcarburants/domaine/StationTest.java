package emiage.prixcarburants.domaine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StationTest {
    final Station station = new Station(
            1000001,
            "596, AVENUE DE TREVOUX",
            "01000",
            "Saint-Denis-lès-Bourg",
            46.201,
            5.198,
            false,
            "01"
    );

    @Test
    public void idStationTest() {
        assertEquals(1000001, station.getIdStation());
    }
    
    @Test
    public void adresseTest() {
        assertEquals("596, AVENUE DE TREVOUX", station.getAdresse());
    }
    
    @Test
    public void codePostalTest() {
        assertEquals("01000", station.getCodePostal());
    }
    
    @Test
    public void villeTest() {
        assertEquals("Saint-Denis-lès-Bourg", station.getVille());
    }

    @Test
    public void latitudeTest() {
        assertEquals(46.201,station.getLatitude());
    }

    @Test
    public void longitudeTest() {
        assertEquals(5.198,station.getLongitude());
    }

    @Test
    public void ouverte24TrueTest(){
        final Station station = new Station(
                1000001,
                "596, AVENUE DE TREVOUX",
                "01000",
                "Saint-Denis-lès-Bourg",
                46.201,
                5.198,
                true,
                "01"
        );
        assertTrue(station.isOuverte24());
    }

    @Test
    public void ouverte24FalseTest(){
        assertFalse(station.isOuverte24());
    }

    @Test
    public void numeroDepartementTest(){
        assertEquals("01", station.getNumeroDepartement());
    }
}
