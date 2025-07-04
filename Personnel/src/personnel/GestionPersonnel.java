package personnel;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Gestion du personnel. Un seul objet de cette classe existe.
 * Il n'est pas possible d'instancier directement cette classe, 
 * la méthode {@link #getGestionPersonnel getGestionPersonnel} 
 * le fait automatiquement et retourne toujours le même objet.
 * Dans le cas où {@link #sauvegarder()} a été appelé lors 
 * d'une exécution précédente, c'est l'objet sauvegardé qui est
 * retourné.
 */

public class GestionPersonnel implements Serializable
{
	private static final long serialVersionUID = -105283113987886425L;
	private static GestionPersonnel gestionPersonnel = null;
	private SortedSet<Ligue> ligues;
	private Employe root;
	public final static int SERIALIZATION = 1, JDBC = 2, 
			TYPE_PASSERELLE = JDBC;  
	private static Passerelle passerelle = new jdbc.JDBC();	
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Crée cet objet s'il n'existe déjà.
	 * @return l'unique objet de type {@link GestionPersonnel}.
	 */

	
	public static GestionPersonnel getGestionPersonnel()
	{
		
		if (gestionPersonnel == null)
		{
			gestionPersonnel = passerelle.getGestionPersonnel();
			if (gestionPersonnel == null)
				gestionPersonnel = new GestionPersonnel();
			try {
				if (gestionPersonnel.getRoot() == null) {
					//Initialiser root seulement s'il n'existe pas
					gestionPersonnel.addRoot("root", "root");
				}	
			} catch (SauvegardeImpossible e) {
				e.printStackTrace();
			}
		}
		return gestionPersonnel;
	}

	public GestionPersonnel()
	{
		if (gestionPersonnel != null)
	        throw new RuntimeException("Vous ne pouvez créer qu'une seuls instance de cet objet.");
	    ligues = new TreeSet<>();
	    gestionPersonnel = this;
	}
	
	public void sauvegarder() throws SauvegardeImpossible
	{
		passerelle.sauvegarderGestionPersonnel(this);
	}
	
	/**
	 * Retourne la ligue dont administrateur est l'administrateur,
	 * null s'il n'est pas un administrateur.
	 * @param administrateur l'administrateur de la ligue recherchée.
	 * @return la ligue dont administrateur est l'administrateur.
	 */
	
	public Ligue getLigue(Employe administrateur)
	{
		if (administrateur.estAdmin(administrateur.getLigue()))
			return administrateur.getLigue();
		else
			return null;
	}

	/**
	 * Retourne toutes les ligues enregistrées.
	 * @return toutes les ligues enregistrées.
	 */
	
	public SortedSet<Ligue> getLigues()
	{
		return Collections.unmodifiableSortedSet(ligues);
	}

	public Ligue addLigue(String nom) throws SauvegardeImpossible
	{
		Ligue ligue = new Ligue(this, nom); 
		ligues.add(ligue);
		return ligue;
	}
	
	public Ligue addLigue(int id, String nom, int employe_id) throws SauvegardeImpossible
	{
		Ligue ligue = new Ligue(this, id, nom, employe_id);
		ligues.add(ligue);
		return ligue;
	}

	public void remove(Ligue ligue)
	{
		ligues.remove(ligue);
	}
	
	int insert(Ligue ligue) throws SauvegardeImpossible
	{
		return passerelle.insert(ligue);
	}
	
	int update(Ligue ligue) throws SauvegardeImpossible
	{
		return passerelle.update(ligue);
	}
	
	int insert(Employe employe) throws SauvegardeImpossible
	{
		return passerelle.insert(employe);
	}
	
	int update(Employe employe) throws SauvegardeImpossible
	{
		return passerelle.update(employe);
	}

	public int updateAdministrateur(Ligue ligue) throws SauvegardeImpossible {
		return passerelle.updateAdministrateur(ligue);
	}
	
	public static int deleteEmploye(int employeId) throws SauvegardeImpossible
	{
		return passerelle.deleteEmploye(employeId);
	}
	
	public int deleteLigue(int ligueId) throws SauvegardeImpossible
	{
		return passerelle.deleteLigue(ligueId);
	}



	public String getAdminLigueNom(Ligue ligue) throws SauvegardeImpossible, SQLException {
		return passerelle.getAdminLigueNom(ligue);
	}

	/**
	 * Retourne le root (super-utilisateur).
	 * @return le root.
	 */
	
	public Employe getRoot()
	{
		return root;
	}
	
	 /**
     * Crée le root à partir de son nom et de son mot de passe,
     * puis l'affecte à la variable d'instance root.
     * @param nom le nom du root.
     * @param motDePasse le mot de passe du root.
     */
	public void addRoot(String nom, String password) throws SauvegardeImpossible {
		 Employe employe = new Employe(this, null, nom, null, null, password, null, null);

	        // Définir l'attribut root
	        this.root = employe;
	}
	public void addRoot( int id, String nom, String password){
		 Employe employe = new Employe(this, null, id, nom, null, null, password, null, null);

	        // Définir l'attribut root
	        this.root = employe;
	}


	
	
	
}
