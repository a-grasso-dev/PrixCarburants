package emiage.prixcarburants.application;

import emiage.prixcarburants.domaine.Prix;
import java.util.List;

public interface PrixRepository {
    List<Prix> trouverPrixParStation(int idStation);
    double prixMoyenParRegion(int idCarburant, String codeRegion);
}
