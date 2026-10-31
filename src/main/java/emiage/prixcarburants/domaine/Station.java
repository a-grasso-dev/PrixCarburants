package emiage.prixcarburants.domaine;

public class Station {
    private final int idStation;
    private final String adresse;
    private final String codePostal;
    private final String ville;
    private final double latitude;
    private final double longitude;
    private final boolean ouverte24;
    private final String numeroDepartement;

    public Station(int idStation, String adresse, String codePostal, String ville, double latitude, double longitude, boolean ouverte24, String numeroDepartement) {
        this.idStation = idStation;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ouverte24 = ouverte24;
        this.numeroDepartement = numeroDepartement;
    }

    public int getIdStation() {
        return idStation;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public String getVille() {
        return ville;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isOuverte24() {
        return ouverte24;
    }

    public String getNumeroDepartement() {
        return numeroDepartement;
    }
}
