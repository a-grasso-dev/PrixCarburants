package emiage.prixcarburants.domaine;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrixTest {
    LocalDateTime localDateTime = LocalDateTime.of(2026, 11, 2, 15, 13);
    Prix prix = new Prix(1,1000001,3,2.412,localDateTime);

    @Test
    public void idPrixTest(){
        assertEquals(1, prix.getIdPrix());
    }

    @Test
    public void idStationTest(){
        assertEquals(1000001, prix.getIdStation());
    }

    @Test
    public void idCarburantTest(){
        assertEquals(3, prix.getIdCarburant());
    }

    @Test
    public void prixTest(){
        assertEquals(2.412, prix.getPrix(), 0.001);
    }

    @Test
    public void localDateTimeTest(){
        assertEquals(localDateTime, prix.getDateMaj());
    }
}
