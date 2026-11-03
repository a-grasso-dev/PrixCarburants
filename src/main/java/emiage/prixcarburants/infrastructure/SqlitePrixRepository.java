package emiage.prixcarburants.infrastructure;

import emiage.prixcarburants.application.PrixRepository;
import emiage.prixcarburants.domaine.Prix;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SqlitePrixRepository implements PrixRepository {
    private final Connection connection;

    public SqlitePrixRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Prix> trouverPrixParStation(int idStation) {
        String sql = """
                SELECT id_prix,
                       id_station,
                       id_carburant,
                       prix,
                       date_maj
                FROM Prix
                WHERE id_station = ?""";

        List<Prix> prixList = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idStation);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prixList.add(new Prix(
                            rs.getInt("id_prix"),
                            rs.getInt("id_station"),
                            rs.getInt("id_carburant"),
                            rs.getDouble("prix"),
                            LocalDateTime.parse(rs.getString("date_maj"), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                            ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de lecture des prix de la station " + idStation, e);
        }
        return prixList;
    }

    @Override
    public double prixMoyenParRegion(int idCarburant, String codeRegion) {
        return 0;
    }
}
