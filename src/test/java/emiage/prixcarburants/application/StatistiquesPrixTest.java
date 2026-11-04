package emiage.prixcarburants.application;

import emiage.prixcarburants.domaine.Prix;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatistiquesPrixTest {

    static class FausPrixRepository implements PrixRepository {
        protected int idCarburant;
        protected String codeRegion;

        @Override
        public List<Prix> trouverPrixParStation(int idStation) {
            return List.of();
        }

        @Override
        public double prixMoyenParRegion(int idCarburant, String codeRegion) {
            this.idCarburant = idCarburant;
            this.codeRegion = codeRegion;
            return 1.5;
        }
    }

    @Test
    public void appelStatistiquesPrixTest() {
        FausPrixRepository fausPrixRepository = new FausPrixRepository();
        StatistiquesPrix statistiquesPrix = new StatistiquesPrix(fausPrixRepository);
        double prixMoyenParRegion =  statistiquesPrix.prixMoyenParRegion(1, "84");
        assertEquals(1, fausPrixRepository.idCarburant);
        assertEquals("84", fausPrixRepository.codeRegion);
        assertEquals(1.5, prixMoyenParRegion);
    }
}
