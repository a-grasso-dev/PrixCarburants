package emiage.prixcarburants.domaine;

public class Carburant {
    private final int idCarburant;
    private final String nomCarburant;

    public Carburant(int idCarburant, String nomCarburant) {
        this.idCarburant = idCarburant;
        this.nomCarburant = nomCarburant;
    }

    public int getIdCarburant() {
        return idCarburant;
    }

    public String getNomCarburant() {
        return nomCarburant;
    }
}
