package commandLine;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;

import com.mysql.cj.exceptions.DataReadException;

import commandLineMenus.List;
import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;

import personnel.*;

public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;

	public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.employeConsole = employeConsole;
	}

	Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.add(afficherLigues());
		menu.add(ajouterLigue());
		menu.add(selectionnerLigue());
		menu.addBack("q");
		return menu;
	}

	private Option afficherLigues() {
	    return new Option("Afficher les ligues", "l", () -> {
	        gestionPersonnel.getLigues().forEach(ligue -> {
	            Employe administrateur = ligue.getAdministrateur();
	            String infoAdministrateur = (administrateur != null)
	                ? "Administrateur: " + administrateur.getNom() + " " + administrateur.getPrenom()
	                : "Aucun administrateur défini";
	            System.out.println("Ligue: " + ligue.getNom() + " - " + infoAdministrateur);
	            System.out.println("Employés: " + ligue.getEmployes().size());
	        });
	    });
	}

	private Option afficher(final Ligue ligue)
	{
		return new Option("Afficher la ligue", "l", 
				() -> 
				{
					System.out.println(ligue);
					System.out.println("administrée par " + ligue.getAdministrateur());
				}
		);
	}
	private Option afficherEmployes(final Ligue ligue)
	{
		return new Option("Afficher les employes", "l", () -> {System.out.println(ligue.getEmployes());});
	}

	private Option ajouterLigue()
	{
		return new Option("Ajouter une ligue", "a", () -> 
		{
			try
			{
				gestionPersonnel.addLigue(getString("nom : "));
			}
			catch(SauvegardeImpossible exception)
			{
				System.err.println("Impossible de sauvegarder cette ligue");
			}
		});
	}
	
	
	private Menu editerLigue(Ligue ligue)
    {
        Menu menu = new Menu("Editer " + ligue.getNom());
        menu.add(afficher(ligue));
        menu.add(gererEmployes(ligue));
        menu.add(changerAdministrateur(ligue));
        menu.add(changerNom(ligue));
        menu.add(supprimer(ligue));
        menu.addBack("q");
        return menu;
    }
	private Menu editerAdmin(Employe employe)
	{
		Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
		menu.add(afficherAdmin(employe));
		menu.add(changerAdmin(employe));
		menu.addBack("q");
		return menu;
	}
	
	 ListOption<Employe> editerAdmin()
	    {
	        return (employe) -> editerAdmin(employe);        
	    }
	
	private Option afficherAdmin(final Employe employe)
	{
		return new Option("Afficher l'Administrateur", "l", () -> {System.out.println(employe);});
	}
	
	private Option changerAdmin(final Employe employe)
	{
		return new Option("Changer le nom", "n", 
				() -> {employe.setNom(getString("Nouveau nom : "));}
			);
	}
	


	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() -> {ligue.setNom(getString("Nouveau nom : "));});
	}

	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
				);
	}
	
	
	private Option ajouterEmploye(final Ligue ligue)
	{
		return new Option("ajouter un employé", "a",
				() -> 
				{
					String nom = getString("nom : ");
	                String prenom = getString("prenom : ");
	                String mail = getString("mail : ");
	                String password = getString("password : ");

	                // Demander la date d'arrivée et de départ à l'utilisateur
	                String dateArriveeStr = getString("date d'arrivée (format JJ/MM/AAAA) : ");
	                String dateDepartStr = getString("date de départ (format JJ/MM/AAAA, laisser vide si pas de date) : ");
	             // Convertir la date en LocalDate
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    try {
                        LocalDate dateArrivee = LocalDate.parse(dateArriveeStr, formatter); // Parse dateArrivee *before* the if block
                        LocalDate dateDepart = null;

                        if (!dateDepartStr.isEmpty()) {
                            dateDepart = LocalDate.parse(dateDepartStr, formatter);
                         // Vérification de la date de départ AVANT d'ajouter l'employé
                            if (dateDepart.isBefore(dateArrivee)) {
                                throw new IllegalArgumentException("Erreur : La date de départ est antérieure à la date d'arrivée.");
                            }
                        }

                        ligue.addEmploye(nom, prenom, mail, password, dateArrivee, dateDepart);
                    } catch (DateTimeParseException e) { // Catch the correct exception
                        System.out.println("Erreur : Format de date incorrect. Veuillez utiliser le format JJ/MM/AAAA.");
                        // Gérer l'erreur, par exemple en demandant à l'utilisateur de ressaisir la date
                        return; // Or throw the exception, or take other appropriate action.
                    } 
                    catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        return; // Ou throw, ou autre action

                    }
                    catch (DataReadException e) { // If you still need to catch DataReadException
                        // ... handle DataReadException ...
                    }
                    catch(SauvegardeImpossible e) {
                    	System.out.println("Ecriture impossible dans la base de données");
      
                    }

				}
		);
	}
	
	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.add(selectionEmploye(ligue));
		menu.addBack("q");
		return menu;
	}
	
	private List<Employe> selectionEmploye(final Ligue ligue)
	{
		return new List<Employe>("Sélectionner un employe", "e", 
				() -> new ArrayList<>(ligue.getEmployes()),
				(element) -> selectEmploye(element)
				);
	}
	// Changement ligne 131 pour la modification des employés 
	Option selectEmploye(Employe employe)
	{
			Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
			menu.add(employeConsole.editEmploye(employe));
			menu.add(supprimer(employe));
			menu.addBack("q");
			return menu;
	}
	

	private List<Employe> changerAdministrateur(final Ligue ligue) {
	    return new List<>("Changer l'administrateur", "i",
	        () -> new ArrayList<>(ligue.getEmployes()),
	        (index, element) -> {
	            ligue.setAdministrateur(element); // Modification dans l'objet Ligue
	            try {
	                gestionPersonnel.updateAdministrateur(ligue); // Enregistrement dans la base de données
	                System.out.println("Administrateur mis à jour avec succès pour la ligue " + ligue.getNom());
	            } catch (SauvegardeImpossible e) {
	                System.err.println("Erreur lors de la mise à jour de l'administrateur : " + e.getMessage());
	            }
	        }
	    );
	}
		


	
	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {
	        if (demanderConfirmation("Êtes-vous sûr de vouloir supprimer la ligue " + ligue.getNom() + " ?")) {
	            try {
	                gestionPersonnel.deleteLigue(ligue.getId());
	                System.out.println("Ligue " + ligue.getNom() + " supprimée avec succès.");
	            } catch (SauvegardeImpossible e) {
	                System.err.println("Impossible de supprimer la ligue : " + e.getMessage());
	            }
	        }
	    });
	}
	private boolean demanderConfirmation(String message) {
	    String reponse = getString(message + " (o/n) : ");
	    return reponse.equalsIgnoreCase("o");
	} 
	
	private Option supprimer(Employe employe)
	{
		return new Option("Supprimer l'employé", "d", () -> {
	        if (demanderConfirmation("Êtes-vous sûr de vouloir supprimer l'employé " + employe.getNom() + " " + employe.getPrenom() + " ?")) {
	            try {
	                GestionPersonnel.deleteEmploye(employe.getId());
	                System.out.println("Employé " + employe.getNom() + " " + employe.getPrenom() + " supprimé avec succès.");
	            } catch (SauvegardeImpossible e) {
	                System.err.println("Impossible de supprimer l'employé : " + e.getMessage());
	            }
	        }
	    });
	}
	
}
