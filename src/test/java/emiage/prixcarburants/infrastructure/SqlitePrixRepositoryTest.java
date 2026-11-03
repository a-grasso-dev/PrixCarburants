package emiage.prixcarburants.infrastructure;

import emiage.prixcarburants.domaine.Prix;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlitePrixRepositoryTest {
    private Connection connection;
    private SqlitePrixRepository repository;

    @BeforeEach
    public void preparerBase() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:data/test_prix.db");

        try (Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Prix");
            st.execute("""
                CREATE TABLE Prix (
                    id_prix INTEGER PRIMARY KEY,
                    id_station INTEGER,
                    id_carburant INTEGER,
                    prix REAL,
                    date_maj TEXT
                )""");
            st.execute("""
                INSERT INTO Prix VALUES
                (1,1000001,3,2.412,'2026-10-28T13:11:00+00:00'),
                (2,1000001,5,2.238,'2026-10-16T15:04:00+00:00')
                """);
        }

        repository = new SqlitePrixRepository(connection);
    }

    @AfterEach
    public void fermerBase() throws Exception {
        connection.close();
    }

    @Test
    void trouverPrixParStationTest() {
        List<Prix> prixList = repository.trouverPrixParStation(1000001);
        assertEquals(2, prixList.size());
    }

    @Test
    void trouverPrixParStationContenuTest() {
        List<Prix> prixList = repository.trouverPrixParStation(1000001);

        for (Prix prix : prixList) {
            assertEquals(1000001, prix.getIdStation());
        }
        assertEquals(2.412, prixList.get(0).getPrix(), 0.001);
    }
}
