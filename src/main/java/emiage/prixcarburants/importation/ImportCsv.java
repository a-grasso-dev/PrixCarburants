package emiage.prixcarburants.importation;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Outil d'import : lit le fichier CSV et remplit la base SQLite.
 * On le lance à la main, une fois par mise à jour du fichier.
 */
public class ImportCsv {

    // Chemins relatifs à la racine du projet
    private static final String URL_BASE = "jdbc:sqlite:data/prix_carburants.db";
    private static final String FICHIER_CSV = "data/prix-carburants.csv";

    // Séparateur de colonnes du CSV (le fichier utilise le point-virgule)
    private static final String SEPARATEUR = ";";

    public static void main(String[] args) throws Exception {
        try (Connection conn = DriverManager.getConnection(URL_BASE);
             BufferedReader lecteur = Files.newBufferedReader(Path.of(FICHIER_CSV), StandardCharsets.UTF_8)) {

            // Active la vérification des liens entre les tables (à faire à chaque connexion)
            try (Statement st = conn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }

            // Toutes les insertions ne seront enregistrées qu'à la fin
            conn.setAutoCommit(false);

            // On efface les prix d'un import précédent, pour éviter les doublons
            try (Statement st = conn.createStatement()) {
                st.execute("DELETE FROM Prix");
            }

            // 1. La première ligne contient les noms des colonnes.
            //    On garde la position de chaque colonne (ex. "id" -> position 22).
            //    replace(...) supprime un caractère invisible parfois ajouté au début du fichier.
            String entete = lecteur.readLine().replace("\uFEFF", "");
            Map<String, Integer> colonnes = new HashMap<>();
            String[] nomsColonnes = entete.split(SEPARATEUR, -1);
            for (int i = 0; i < nomsColonnes.length; i++) {
                colonnes.put(nomsColonnes[i].trim(), i);
            }

            // 2. Les requêtes d'insertion. Chaque ? sera remplacé par une valeur.
            PreparedStatement insRegion = conn.prepareStatement(
                    "INSERT OR IGNORE INTO Region (code_region, nom_region) VALUES (?, ?)");
            PreparedStatement insDepartement = conn.prepareStatement(
                    "INSERT OR IGNORE INTO Departement (numero, nom_departement, code_region) VALUES (?, ?, ?)");
            PreparedStatement insStation = conn.prepareStatement(
                    "INSERT OR IGNORE INTO Station (id_station, adresse, code_postal, ville, latitude, longitude, automate_24_24, numero_departement) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement insPrix = conn.prepareStatement(
                    "INSERT INTO Prix (id_station, id_carburant, prix, date_maj) VALUES (?, ?, ?, ?)");
            PreparedStatement chercheCarburant = conn.prepareStatement(
                    "SELECT id_carburant FROM Carburant WHERE nom_carburant = ?");

            // Mémoire des carburants déjà trouvés, pour ne pas refaire la requête à chaque ligne
            Map<String, Integer> cacheCarburants = new HashMap<>();

            int nbLignes = 0;
            int nbPrix = 0;
            int nbIgnorees = 0;

            // 3. On lit le fichier ligne par ligne
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                nbLignes++;
                String[] c = ligne.split(SEPARATEUR, -1);

                String idStation = lire(c, colonnes, "id");
                String codeRegion = lire(c, colonnes, "Code Officiel Région");
                String numDep = lire(c, colonnes, "Numéro Département");

                // Une ligne sans station, région ou département ne peut pas être importée
                if (idStation.isEmpty() || codeRegion.isEmpty() || numDep.isEmpty()) {
                    nbIgnorees++;
                    continue;
                }

                // Région (INSERT OR IGNORE : pas d'erreur si elle existe déjà)
                insRegion.setString(1, codeRegion);
                insRegion.setString(2, lire(c, colonnes, "Région"));
                insRegion.executeUpdate();

                // Département
                insDepartement.setString(1, numDep);
                insDepartement.setString(2, lire(c, colonnes, "Département"));
                insDepartement.setString(3, codeRegion);
                insDepartement.executeUpdate();

                // Station
                insStation.setInt(1, Integer.parseInt(idStation));
                insStation.setString(2, lire(c, colonnes, "adresse"));
                insStation.setString(3, lire(c, colonnes, "Code postal"));
                insStation.setString(4, lire(c, colonnes, "ville"));

                // La colonne geom contient "46.201, 5.198" : on la coupe en deux nombres
                String[] gps = lire(c, colonnes, "geom").split(",");
                if (gps.length == 2) {
                    insStation.setDouble(5, Double.parseDouble(gps[0].trim()));
                    insStation.setDouble(6, Double.parseDouble(gps[1].trim()));
                } else {
                    insStation.setNull(5, Types.REAL);
                    insStation.setNull(6, Types.REAL);
                }

                // "Oui" devient 1, tout le reste devient 0
                boolean ouvert24 = lire(c, colonnes, "Automate 24-24 (oui/non)").equalsIgnoreCase("Oui");
                insStation.setInt(7, ouvert24 ? 1 : 0);
                insStation.setString(8, numDep);
                insStation.executeUpdate();

                // Prix : seulement si la ligne contient un prix ET un carburant
                String textePrix = lire(c, colonnes, "Prix");
                String nomCarburant = lire(c, colonnes, "Carburant");
                if (!textePrix.isEmpty() && !nomCarburant.isEmpty()) {
                    Integer idCarburant = trouverCarburant(chercheCarburant, cacheCarburants, nomCarburant);
                    if (idCarburant != null) {
                        insPrix.setInt(1, Integer.parseInt(idStation));
                        insPrix.setInt(2, idCarburant);
                        insPrix.setDouble(3, Double.parseDouble(textePrix));
                        insPrix.setString(4, lire(c, colonnes, "Mise à jour des prix"));
                        insPrix.executeUpdate();
                        nbPrix++;
                    }
                }
            }

            // 4. Enregistrement définitif de tout l'import
            conn.commit();

            System.out.println("Lignes lues : " + nbLignes);
            System.out.println("Lignes ignorées : " + nbIgnorees);
            System.out.println("Prix enregistrés : " + nbPrix);
        }
    }

    /** Renvoie la valeur d'une colonne, sans espaces autour. */
    private static String lire(String[] c, Map<String, Integer> colonnes, String nom) {
        Integer position = colonnes.get(nom);
        if (position == null) {
            throw new IllegalArgumentException("Colonne introuvable dans le CSV : " + nom);
        }
        return position < c.length ? c[position].trim() : "";
    }

    /** Renvoie l'id d'un carburant (E10, SP95…), ou null s'il n'existe pas. */
    private static Integer trouverCarburant(PreparedStatement chercher, Map<String, Integer> cache, String nom)
            throws SQLException {
        if (cache.containsKey(nom)) {
            return cache.get(nom);
        }
        chercher.setString(1, nom);
        try (ResultSet rs = chercher.executeQuery()) {
            Integer id = rs.next() ? rs.getInt(1) : null;
            cache.put(nom, id);
            return id;
        }
    }
}