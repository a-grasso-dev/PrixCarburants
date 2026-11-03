package emiage.prixcarburants.infrastructure;

import emiage.prixcarburants.application.StationRepository;
import emiage.prixcarburants.domaine.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteStationRepository implements StationRepository {
    private final Connection connection;

    public SqliteStationRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Station trouverParId(int idStation) {
        String sql = """
                SELECT id_station,
                        adresse,
                        code_postal,
                        ville,
                        latitude,
                        longitude,
                        automate_24_24,
                        numero_departement
                FROM Station
                WHERE id_station = ?""";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idStation);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Station(
                            rs.getInt("id_station"),
                            rs.getString("adresse"),
                            rs.getString("code_postal"),
                            rs.getString("ville"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude"),
                            rs.getInt("automate_24_24") == 1,
                            rs.getString("numero_departement"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de lecture de la station " + idStation, e);
        }
    }

    @Override
    public List<Station> trouverParDepartement(String numeroDepartement) {
        String sql = """
                SELECT id_station,
                        adresse,
                        code_postal,
                        ville,
                        latitude,
                        longitude,
                        automate_24_24,
                        numero_departement
                FROM Station
                WHERE numero_departement = ?""";

        List<Station> stations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, numeroDepartement);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    stations.add(new Station(
                            rs.getInt("id_station"),
                            rs.getString("adresse"),
                            rs.getString("code_postal"),
                            rs.getString("ville"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude"),
                            rs.getInt("automate_24_24") == 1,
                            rs.getString("numero_departement")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de lecture des stations du département " + numeroDepartement, e);
        }
        return stations;
    }
}

