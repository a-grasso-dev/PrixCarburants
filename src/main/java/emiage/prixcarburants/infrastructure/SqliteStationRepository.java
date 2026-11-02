package emiage.prixcarburants.infrastructure;

import emiage.prixcarburants.application.StationRepository;
import emiage.prixcarburants.domaine.Station;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SqliteStationRepository implements StationRepository {
    private Connection connection;

    public SqliteStationRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Station trouverParId(int idStation) throws SQLException {
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

        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idStation);

            try (ResultSet rs = ps.executeQuery()){
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
        }
    }

    @Override
    public List<Station> trouverParDepartement(String numeroDepartement) {
        return List.of();
    }
}
