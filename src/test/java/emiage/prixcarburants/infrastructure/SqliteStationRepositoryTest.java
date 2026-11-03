package emiage.prixcarburants.infrastructure;

import emiage.prixcarburants.domaine.Station;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SqliteStationRepositoryTest {
    private Connection connection;
    private SqliteStationRepository repository;

    @BeforeEach
    public void preparerBase() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:data/test_station.db");

        try (Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Station");
            st.execute("""
                CREATE TABLE Station (
                    id_station INTEGER PRIMARY KEY,
                    adresse TEXT,
                    code_postal TEXT,
                    ville TEXT,
                    latitude REAL,
                    longitude REAL,
                    automate_24_24 INTEGER NOT NULL DEFAULT 0,
                    numero_departement TEXT NOT NULL
                )""");
            st.execute("""
                INSERT INTO Station VALUES
                (1000001, '596, AVENUE DE TREVOUX', '01000', 'Saint-Denis-lès-Bourg', 46.201, 5.198, 0, '01'),
                (1000002, '16, Avenue de Marboz', '01000', 'BOURG-EN-BRESSE', 46.21842, 5.22767, 0, '01'),
                (75015030, '1, QUATER BOULEVARD VICTOR', '75015', 'PARIS', 48.834, 2.285, 1, '75')
                """);
        }

        repository = new SqliteStationRepository(connection);
    }

    @AfterEach
    public void fermerBase() throws Exception {
        connection.close();
    }

    @Test
    public void trouverParIdTest() {

        Station station = repository.trouverParId(1000001);

        assertEquals(1000001, station.getIdStation());
        assertEquals("596, AVENUE DE TREVOUX", station.getAdresse());
        assertEquals("01000", station.getCodePostal());
        assertEquals("Saint-Denis-lès-Bourg", station.getVille());
        assertEquals(46.201, station.getLatitude());
        assertEquals(5.198, station.getLongitude());
        assertFalse(station.isOuverte24());
        assertEquals("01", station.getNumeroDepartement());
    }

    @Test
    public void trouverParIdNullTest() {
        Station station = repository.trouverParId(999999);
        assertNull(station);
    }

    @Test
    public void trouverParDepartementTest() {
        List<Station> stations = repository.trouverParDepartement("01");
        assertEquals(2, stations.size());
        for (Station station : stations) {
            assertEquals("01", station.getNumeroDepartement());
        }
    }

    @Test
    public void trouverParDepartementVideTest() {
        List<Station> stations = repository.trouverParDepartement("99");
        assertTrue(stations.isEmpty());
    }
}
