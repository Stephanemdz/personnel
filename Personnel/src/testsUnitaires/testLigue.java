package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import personnel.*;

class testLigue 
{
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
	@Test
	void createLigue() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}

	@Test
	void addEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2024, 11, 5), null); 
		assertEquals(employe, ligue.getEmployes().first());
	}
	

	//Test pour la date de départ antérieure à la date d'arrivée
	@Test
	void testDateDepartAnterieureDateArrivee()throws IllegalArgumentException, SauvegardeImpossible {
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2025, 01, 24), null); 
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> employe.setDateDepart(LocalDate.of(2024, 01, 24)));
        assertEquals("La date de départ doit être postérieure ou égale à la date d'arrivée.", exception.getMessage());
	}
	
	//Test pour la date de départ postérieure à la date d'arrivée
	@Test
	void testDateDepartPosterieureDateArrivee()throws IllegalArgumentException, SauvegardeImpossible{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2025, 01, 24), null); 
		assertDoesNotThrow(() -> employe.setDateDepart(LocalDate.of(2026, 10, 27)));
        assertEquals(LocalDate.parse("2026-10-27"), employe.getDateDepart());
	}
	
	
	//Test pour le setNom
    @Test
    void testSetNom() throws SauvegardeImpossible {
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
    	Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2025, 01, 24), null);
        employe.setNom("Bouchard");
        assertEquals("Bouchard", employe.getNom());
    }
    
    
  //Test pour le setPrenom
    @Test
    void testSetPrenom() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
    	Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2025, 01, 24), null);
        employe.setPrenom("Gérard");
        assertEquals("Gérard", employe.getPrenom());
    }
    
    
  //Test pour le setMail
    @Test
    void testSetEmail() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
    	Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2025, 01, 24), null);
        employe.setMail("g.bouchard@gmail.com");
        assertEquals("g.bouchard@gmail.com", employe.getMail());
    }

    
  //Test pour le setPassword
    @Test
    void testSetPassword() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
    	Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.of(2025, 01, 24), null);
        employe.setPassword("azerty");
        assertEquals("azerty", employe.getPassword());
    }
    
    
 
}
