package emiage.prixcarburants.application;

public class StatistiquesPrix {
    private final PrixRepository prixRepository;

    public StatistiquesPrix(PrixRepository prixRepository) {
        this.prixRepository = prixRepository;
    }

    public double prixMoyenParRegion(int idCarburant, String codeRegion){
        return prixRepository.prixMoyenParRegion(idCarburant, codeRegion);
    }
}
