package personnel;

import java.io.Serializable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.TreeSet;

/**
 * Employé d'une ligue hébergée par la M2L. Certains peuvent 
 * être administrateurs des employés de leur ligue.
 * Un seul employé, rattaché à aucune ligue, est le root.
 * Il est impossible d'instancier directement un employé, 
 * il faut passer la méthode {@link Ligue#addEmploye addEmploye}.
 */

public class Employe implements Serializable, Comparable<Employe>
{
	private static final long serialVersionUID = 4795721718037994734L;
	private String nom, prenom, password, mail;
	private LocalDate dateArrivee;
	private LocalDate dateDepart;
	private Ligue ligue;
	private GestionPersonnel gestionPersonnel;
	private int id;
	
	/*Surcharge constructeur por ajouter le root */
	Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom, String mail, String password, LocalDate dateArrivee, LocalDate dateDepart) throws SauvegardeImpossible
	{
		this(gestionPersonnel, ligue, -1, nom, prenom, mail, password, dateArrivee, dateDepart);
		this.id = gestionPersonnel.insert(this); 
	}
	
	
	public Employe(GestionPersonnel gestionPersonnel, Ligue ligue, int id, String nom, String prenom, String mail, String password, LocalDate dateArrivee, LocalDate dateDepart)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.nom = nom;
		this.prenom = prenom;
		this.password = password;
		this.mail = mail;
		this.ligue = ligue;
		this.dateArrivee = dateArrivee;
		this.dateDepart = dateDepart;
		this.id = id;
	}

	
	/**
	 * Retourne vrai ssi l'employé est administrateur de la ligue 
	 * passée en paramètre.
	 * @return vrai ssi l'employé est administrateur de la ligue 
	 * passée en paramètre.
	 * @param ligue la ligue pour laquelle on souhaite vérifier si this 
	 * est l'admininstrateur.
	 */
	
	public boolean estAdmin(Ligue ligue)
	{
		return ligue.getAdministrateur() == this;
	}
	
	/**
	 * Retourne vrai ssi l'employé est le root.
	 * @return vrai ssi l'employé est le root.
	 */
	
	public boolean estRoot()
	{
		return gestionPersonnel.getRoot() == this;
	}
	
	/**
	 * Retourne le nom de l'employé.
	 * @return le nom de l'employé. 
	 */
	
	public String getNom()
	{
		return nom;
	}

	/**
	 * Change le nom de l'employé.
	 * @param nom le nouveau nom.
	 */
	
	public void setNom(String nom)
	{
		this.nom = nom;
		//IT3: Modification root
		try {
            gestionPersonnel.update(this);
        } catch (SauvegardeImpossible e) {
            e.printStackTrace(); 
        }
	}

	/**
	 * Retourne le prénom de l'employé.
	 * @return le prénom de l'employé.
	 */
	
	public String getPrenom()
	{
		return prenom;
	}
	
	/**
	 * Change le prénom de l'employé.
	 * @param prenom le nouveau prénom de l'employé. 
	 */

	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
		try {
            gestionPersonnel.update(this);
        } catch (SauvegardeImpossible e) {
            e.printStackTrace(); 
        }
	}

	/**
	 * Retourne le mail de l'employé.
	 * @return le mail de l'employé.
	 */
	
	public String getMail()
	{
		return mail;
	}
	
	/**
	 * Change le mail de l'employé.
	 * @param mail le nouveau mail de l'employé.
	 */

	public void setMail(String mail)
	{
		this.mail = mail;
		try {
            gestionPersonnel.update(this);
        } catch (SauvegardeImpossible e) {
            e.printStackTrace(); 
        }
	}
	
	public LocalDate getDateArrivee() {
		return dateArrivee;
	}
	
	public void setDateArrivee(LocalDate dateArrivee) {
		this.dateArrivee = dateArrivee;
		try {
            gestionPersonnel.update(this);
        } catch (SauvegardeImpossible e) {
            e.printStackTrace(); 
        }
	}
	
	public LocalDate getDateDepart() {
		return dateDepart;
	}
	
	 public void setDateDepart(LocalDate dateDepart) throws IllegalArgumentException {
		 if (dateDepart == null) {
	            this.dateDepart = null; // Autoriser une date de départ nulle
	            return;
	        }

	        try {
	            if (dateDepart.isBefore(this.dateArrivee)) {
	                throw new IllegalArgumentException("La date de départ doit être postérieure ou égale à la date d'arrivée.");
	            }
	            this.dateDepart = dateDepart;
	        } catch (DateTimeParseException e) {
	            throw new IllegalArgumentException("Format de date invalide (AAAA-MM-JJ) : " + dateDepart, e);
	        }
	        try {
	            gestionPersonnel.update(this);
	        } catch (SauvegardeImpossible e) {
	            e.printStackTrace(); 
	        }
	    }

	/**
	 * Retourne vrai ssi le password passé en paramètre est bien celui
	 * de l'employé.
	 * @return vrai ssi le password passé en paramètre est bien celui
	 * de l'employé.
	 * @param password le password auquel comparer celui de l'employé.
	 */
	
	public boolean checkPassword(String password)
	{
		return this.password.equals(password);
	}

	/**
	 * Change le password de l'employé.
	 * @param password le nouveau password de l'employé. 
	 */
	
	public void setPassword(String password)
	{
		this.password= password;
		try {
            gestionPersonnel.update(this);
        } catch (SauvegardeImpossible e) {
            e.printStackTrace(); 
        }
	}

	/**
	 * Retourne la ligue à laquelle l'employé est affecté.
	 * @return la ligue à laquelle l'employé est affecté.
	 */
	
	public Ligue getLigue()
	{
		return ligue;
	}
	
	 public void setLigue(Ligue ligue) {
	        this.ligue = ligue;
	        try {
	            gestionPersonnel.update(this);
	        } catch (SauvegardeImpossible e) {
	            e.printStackTrace();
	        }
	    }

	/**
	 * Supprime l'employé. Si celui-ci est un administrateur, le root
	 * récupère les droits d'administration sur sa ligue.
	 */
	
	 public void remove() throws SauvegardeImpossible
		{
			Employe root = gestionPersonnel.getRoot();
			if (this != root)
			{
				if (estAdmin(getLigue()))
					getLigue().setAdministrateur(root);
				getLigue().remove(this);
			}
			else
				throw new ImpossibleDeSupprimerRoot();
		}

	@Override
	public int compareTo(Employe autre)
	{
		int cmp = getNom().compareTo(autre.getNom());
		if (cmp != 0)
			return cmp;
		return getPrenom().compareTo(autre.getPrenom());
	}
	
	@Override
	public String toString() {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    String dateArriveeStr = (dateArrivee != null) ? dateArrivee.format(formatter) : "N/A";
	    String dateDepartStr = (dateDepart != null) ? dateDepart.format(formatter) : "N/A";

	    String res = "Nom: " + nom + ", Prénom: " + prenom + ", Email: " + mail + ", Date d'arrivée: " + dateArriveeStr + ", Date de départ: " + dateDepartStr + " (";
	    if (estRoot()) {
	        res += "super-utilisateur";
	    } else {
	        res += ligue.toString();
	    }
	    return res + ")";
	}
	
	public String getPassword() {
		return password;
	}


	public int getId() {
	    return id;
	}
	 public void setId(int id) {
	        this.id = id;
	        try {
	            gestionPersonnel.update(this);
	        } catch (SauvegardeImpossible e) {
	            e.printStackTrace(); 
	        }
	    }
}
