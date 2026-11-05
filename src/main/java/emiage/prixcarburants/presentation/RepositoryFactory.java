package emiage.prixcarburants.presentation;

import emiage.prixcarburants.application.StationRepository;
import emiage.prixcarburants.infrastructure.SqliteStationRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RepositoryFactory {
    private Connection connection;

    public StationRepository stationRepository() throws SQLException {
        if(connection == null){
            connection = DriverManager.getConnection("jdbc:sqlite:data/prix_carburants.db");
        }
        return new SqliteStationRepository(connection);
    }
}
