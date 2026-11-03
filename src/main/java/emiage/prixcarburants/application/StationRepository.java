package emiage.prixcarburants.application;

import emiage.prixcarburants.domaine.Station;

import java.sql.SQLException;
import java.util.List;

public interface StationRepository {
    Station trouverParId(int idStation);
    List<Station> trouverParDepartement(String numeroDepartement);
}
