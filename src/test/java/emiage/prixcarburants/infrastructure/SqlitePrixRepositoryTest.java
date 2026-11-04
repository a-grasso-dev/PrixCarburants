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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SqlitePrixRepositoryTest {
    private Connection connection;
    private SqlitePrixRepository repository;

    @BeforeEach
    public void preparerBase() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:data/test_prix.db");

        try (Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Region");
            st.execute("DROP TABLE IF EXISTS Departement");
            st.execute("DROP TABLE IF EXISTS Station");
            st.execute("DROP TABLE IF EXISTS Carburant");
            st.execute("DROP TABLE IF EXISTS Prix");
            st.execute("""
                CREATE TABLE Region (
                    code_region TEXT PRIMARY KEY,
                    nom_region TEXT)
                """);
            st.execute("""
                CREATE TABLE Departement (
                    numero TEXT PRIMARY KEY,
                    nom_departement TEXT,
                    code_region TEXT)
                """);
            st.execute("""
                CREATE TABLE Station (
                    id_station INTEGER PRIMARY KEY,
                    adresse TEXT,
                    code_postal TEXT,
                    ville TEXT,
                    latitude DOUBLE,
                    longitude DOUBLE,
                    automate_24_24 INTEGER,
                    numero_departement TEXT)
                """);
            st.execute("""
                CREATE TABLE Carburant (
                    id_carburant INTEGER PRIMARY KEY,
                    nom_carburant TEXT)
                """);
            st.execute("""
                CREATE TABLE Prix (
                    id_prix INTEGER PRIMARY KEY,
                    id_station INTEGER,
                    id_carburant INTEGER,
                    prix REAL,
                    date_maj TEXT)
                """);
            st.execute("""
                INSERT INTO Region VALUES
                ('84','Auvergne-Rhône-Alpes');
                """);
            st.execute("""
                INSERT INTO Region VALUES
                ('99','Region sans département ni station');
                """);
            st.execute("""
                INSERT INTO Departement VALUES
                ('01','Ain','84');
                """);
            st.execute("""
                INSERT INTO Station VALUES
                (1000001,'596 AVENUE DE TREVOUX','01000','SAINT-DENIS-LèS-BOURG',46.201,5.198,0,'01');
                """);
            st.execute("""
                INSERT INTO Carburant VALUES
                (1,'E10');
                """);
            st.execute("""
                INSERT INTO Prix VALUES
                (1,1000001,1,1.0,'2026-10-28T13:11:00+00:00'),
                (2,1000001,1,2.0,'2026-10-16T15:04:00+00:00')
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
        assertEquals(1.0, prixList.get(0).getPrix(), 0.001);
    }

    @Test
    void prixMoyenParRegionTest() {
        assertEquals(1.5, repository.prixMoyenParRegion(1,"84"),0.001);
    }

    @Test
    void prixMoyenParRegionSansDepartementNiStationTest() {
        assertThrows(RuntimeException.class, () -> repository.prixMoyenParRegion(1, "99"));
    }
}
