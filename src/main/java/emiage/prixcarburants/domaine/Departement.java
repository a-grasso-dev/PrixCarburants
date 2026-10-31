package emiage.prixcarburants.domaine;

public class Departement {
    private String numero;
    private String nomDepartement;
    private String codeRegion;

    public Departement(String numero, String nomDepartement, String codeRegion) {
        this.numero = numero;
        this.nomDepartement = nomDepartement;
        this.codeRegion = codeRegion;
    }

    public String getNumero() {
        return numero;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public String getCodeRegion() {
        return codeRegion;
    }
}
