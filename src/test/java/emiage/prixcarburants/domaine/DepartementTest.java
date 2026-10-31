package emiage.prixcarburants.domaine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartementTest {
    @Test
    public void numeroDepartementTest() {
        Departement departement = new Departement("01", "Ain","84");
        assertEquals("01", departement.getNumero());
    }

    @Test
    public void nomDepartementTest() {
        Departement departement = new Departement("01", "Ain","84");
        assertEquals("Ain", departement.getNomDepartement());
    }

    @Test
    public void codeRegionDepartementTest() {
        Departement departement = new Departement("01", "Ain","84");
        assertEquals("84", departement.getCodeRegion());
    }
}
