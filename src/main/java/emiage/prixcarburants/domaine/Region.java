package emiage.prixcarburants.domaine;

public class Region {
    private final String codeRegion;
    private final String nomRegion;

    public Region(String codeRegion, String nomRegion) {
        this.codeRegion = codeRegion;
        this.nomRegion = nomRegion;
    }

    public String getNomRegion() {
        return nomRegion;
    }

    public String getCodeRegion() {
        return codeRegion;
    }
}
