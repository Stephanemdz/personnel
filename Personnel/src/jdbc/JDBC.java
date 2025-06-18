package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import personnel.*;

public class JDBC implements Passerelle 
{
	Connection connection;

	public JDBC()
	{
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public boolean employeExisteParEmail(String email) throws SauvegardeImpossible {
        try {
            PreparedStatement instruction = connection.prepareStatement("SELECT COUNT(*) FROM compte_employe WHERE email = ?");
            instruction.setString(1, email);
            ResultSet resultat = instruction.executeQuery();
            resultat.next();
            return resultat.getInt(1) > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }
	
	@Override
	public GestionPersonnel getGestionPersonnel() {
	    GestionPersonnel gestionPersonnel = new GestionPersonnel();
	    try {
	        String requete = "select * from ligue";
	        Statement instruction = connection.createStatement();
	        ResultSet ligues = instruction.executeQuery(requete);
	        while (ligues.next()) {
	            int ligueId = ligues.getInt("id");
	            String ligueNom = ligues.getString("nom");
	            int EmployeAdmin = ligues.getInt("admin_ligue");
	            Ligue ligue = gestionPersonnel.addLigue(ligueId, ligueNom, EmployeAdmin);

	            // Utilisation d'un Set pour éviter les doublons
	            Set<Integer> employeIds = new HashSet<>();

	            String requestEmployes = "SELECT * from compte_employe where ligue_id = ?;";
	            PreparedStatement instructionEmployes = connection.prepareStatement(requestEmployes);
	            instructionEmployes.setInt(1, ligueId);
	            ResultSet employes = instructionEmployes.executeQuery();

	            while (employes.next()) {
	                int employeId = employes.getInt("id");

	                // Vérification si l'employé existe déjà
	                if (!employeIds.contains(employeId)) {
	                    String employeNom = employes.getString("nom");
	                    String employePrenom = employes.getString("prenom");
	                    String employeMail = employes.getString("email");
	                    String employePassword = employes.getString("password");
	                    LocalDate employeDateArrivee = employes.getObject("dateArrivee", LocalDate.class);
	                    LocalDate employeDateDepart = employes.getObject("dateDepart", LocalDate.class);

	                    // Utilisation de la méthode addEmploye() existante
	                    ligue.addEmploye(employeId, employeNom, employePrenom, employeMail, employePassword, employeDateArrivee, employeDateDepart);

	                    // Ajout de l'ID de l'employé au Set
	                    employeIds.add(employeId);
	                }else {
	                    System.out.println("Employé avec l'ID " + employeId + " existe déjà.");
	                }
	            }
	        }

	     // Récupérer les informations de root
	        String requeteRoot = "SELECT * FROM compte_employe WHERE ligue_id IS NULL";
	        Statement instructionRoot = connection.createStatement();
	        ResultSet rootResult = instructionRoot.executeQuery(requeteRoot);

	        if (rootResult.next()) {
	        	System.out.println("J'ai trouvé le root");
	            int id = rootResult.getInt("id");
	            String nom = rootResult.getString("nom");
	            String password = rootResult.getString("password");

	            // Vérifier si root existe déjà    
	                gestionPersonnel.addRoot(id, nom, password);
	        }
	        else {
	        	System.out.println("J'ai pas trouvé le root");
	        }
	    } catch (SQLException e) {
	        System.out.println(e);
	    } catch (SauvegardeImpossible e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return gestionPersonnel;
	}

	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());		
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	//Méthode pour la modification de la ligue
	public int update(Ligue ligue) throws SauvegardeImpossible {
		try {
	        PreparedStatement instruction;
	        instruction = connection.prepareStatement("UPDATE ligue SET nom = ? WHERE id = ?");
	        instruction.setString(1, ligue.getNom());
	        instruction.setInt(2, ligue.getId()); // Ajout de la clause WHERE

	        return instruction.executeUpdate(); // Retourne le nombre de lignes affectées
	    } catch (SQLException exception) {
	        exception.printStackTrace();
	        throw new SauvegardeImpossible(exception);
	    }
    }

	//Méthode permettant d'insérer un employé
	public int insert(Employe employe) throws SauvegardeImpossible {
	    try {
	        // Vérifier si un employé avec cet email existe déjà
	        if (employeExisteParEmail(employe.getMail())) {
	            System.out.println("Avertissement : Employé avec l'email " + employe.getMail() + " existe déjà dans la base de données. L'insertion a été ignorée.");
	            return -1; // Retourner une valeur indiquant que l'insertion n'a pas eu lieu
	        }

	        PreparedStatement instruction;
	        instruction = connection.prepareStatement("INSERT INTO compte_employe (nom, prenom, email, password, ligue_id, dateArrivee, dateDepart) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
	        instruction.setString(1, employe.getNom());
	        instruction.setString(2, employe.getPrenom());
	        instruction.setString(3, employe.getMail());
	        instruction.setString(4, employe.getPassword());
	        if (employe.getLigue() == null) {
	            instruction.setNull(5, java.sql.Types.INTEGER);
	        } else {
	            instruction.setInt(5, employe.getLigue().getId());
	        }
	        instruction.setObject(6, employe.getDateArrivee());
	        instruction.setObject(7, employe.getDateDepart());
	        instruction.executeUpdate();
	        ResultSet id = instruction.getGeneratedKeys();
	        id.next();
	        return id.getInt(1);
	    } catch (SQLException exception) {
	        exception.printStackTrace();
	        throw new SauvegardeImpossible(exception);
	    }
	}
	

	//méthode permettant d'update l'employé
	public int update(Employe employe) throws SauvegardeImpossible {
		try {
	        PreparedStatement instruction;
	        instruction = connection.prepareStatement("UPDATE compte_employe SET nom = ?, prenom = ?, email = ?, password = ?, ligue_id = ?, dateArrivee = ?, dateDepart = ? WHERE id = ?");
	        instruction.setString(1, employe.getNom());
	        instruction.setString(2, employe.getPrenom());
	        instruction.setString(3, employe.getMail());
	        instruction.setString(4, employe.getPassword());
	        if (employe.getLigue() == null) {
	            instruction.setNull(5, java.sql.Types.INTEGER);
	        } else {
	            instruction.setInt(5, employe.getLigue().getId());
	        }
	        instruction.setObject(6, employe.getDateArrivee());
	        instruction.setObject(7, employe.getDateDepart());
	        instruction.setInt(8, employe.getId());
	        return instruction.executeUpdate();
	    } catch (SQLException exception) {
	        exception.printStackTrace();
	        throw new SauvegardeImpossible(exception);
	    }
	}
	
	//Méthode permettant de supprimer un employé de la base de données

public int deleteEmploye(int employeId) throws SauvegardeImpossible {
    try {
        PreparedStatement instruction = connection.prepareStatement("DELETE FROM compte_employe WHERE id = ?");
        instruction.setInt(1, employeId);

        int rowsAffected = instruction.executeUpdate();
        return rowsAffected;
    } catch (SQLException exception) {
        System.err.println("Erreur SQL : " + exception.getMessage());
        throw new SauvegardeImpossible(exception);
    }
}

	
	public int deleteLigue(int ligueId) throws SauvegardeImpossible {
	    try {
	        PreparedStatement instruction;
	        instruction = connection.prepareStatement("DELETE FROM ligue WHERE id = ?");
	        instruction.setInt(1, ligueId);
	        return instruction.executeUpdate();
	    } catch (SQLException exception) {
	        exception.printStackTrace();
	        throw new SauvegardeImpossible(exception);
	    }
	}

	
	public boolean rootExiste() throws SauvegardeImpossible {
        try {
            PreparedStatement instruction = connection.prepareStatement("SELECT COUNT(*) FROM compte_employe WHERE nom = 'root'");
            ResultSet resultat = instruction.executeQuery();
            resultat.next();
            return resultat.getInt(1) > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }

	public int updateAdministrateur(Ligue ligue) throws SauvegardeImpossible {
	    try {
	        PreparedStatement instruction = connection.prepareStatement(
	            "UPDATE ligue SET admin_ligue = ? WHERE id = ?"
	        );
	        if (ligue.getAdministrateur() == null) {
	            instruction.setNull(1, java.sql.Types.INTEGER);
	        } else {
	            instruction.setInt(1, ligue.getAdministrateur().getId());
	        }
	        instruction.setInt(2, ligue.getId());

	        int rowsAffected = instruction.executeUpdate();
	        System.out.println("Administrateur mis à jour avec succès pour la ligue " + ligue.getNom() + ". Lignes affectées : " + rowsAffected);
	        return rowsAffected;
	    } catch (SQLException exception) {
	        System.err.println("Erreur SQL lors de la mise à jour de l'administrateur : " + exception.getMessage());
	        throw new SauvegardeImpossible(exception);
	    }
	}

}
