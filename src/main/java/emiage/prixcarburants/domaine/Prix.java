package emiage.prixcarburants.domaine;

import java.time.LocalDateTime;

public class Prix {
    private final int idPrix;
    private final int idStation;
    private final int idCarburant;
    private final double prix;
    private final LocalDateTime dateMaj;

    public Prix(int idPrix, int idStation, int idCarburant, double prix, LocalDateTime dateMaj) {
        this.idPrix = idPrix;
        this.idStation = idStation;
        this.idCarburant = idCarburant;
        this.prix = prix;
        this.dateMaj = dateMaj;
    }

    public int getIdPrix() {
        return idPrix;
    }

    public int getIdStation() {
        return idStation;
    }

    public int getIdCarburant() {
        return idCarburant;
    }

    public double getPrix() {
        return prix;
    }

    public LocalDateTime getDateMaj() {
        return dateMaj;
    }
}
