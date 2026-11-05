package emiage.prixcarburants.presentation;

import emiage.prixcarburants.application.StationRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class RepositoryFactoryTest {
    @Test
    public void stationRepositoryTest() throws SQLException {
        RepositoryFactory repositoryFactory = new RepositoryFactory();
        assertInstanceOf(StationRepository.class, repositoryFactory.stationRepository());
    }
}
