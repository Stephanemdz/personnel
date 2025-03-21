package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import personnel.*;

public class JDBC implements Passerelle 
{
	Connection connection;
	private Ligue ligue;

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
	
	@Override
	public GestionPersonnel getGestionPersonnel() 
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try 
		{
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next())
				gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(2));
			
			// Récupérer les informations de root
	        String requeteRoot = "SELECT * FROM employe WHERE nom = 'root'"; // Adaptez la requête selon votre base
	        Statement instructionRoot = connection.createStatement();
	        ResultSet rootResult = instructionRoot.executeQuery(requeteRoot);

	        if (rootResult.next()) {
	            // Créer l'objet Employe root à partir des résultat
	            String nom = rootResult.getString("nom");
	            String password = rootResult.getString("password");
	            Employe root = new Employe(gestionPersonnel, nom, password);
	            gestionPersonnel.addRoot(gestionPersonnel, nom, password);
	        }
		}
		catch (SQLException e)
		{
			System.out.println(e);
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
	public int insert (Employe employe) throws SauvegardeImpossible
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into compte_employe (nom, prenom, email, password) values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, employe.getNom());	
			instruction.setString(2, employe.getPassword());
			instruction.setString(3, employe.getPrenom());
			instruction.setString(4, employe.getMail());
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
	
	//méthode permettant d'update l'employé
	public int update(Employe employe) throws SauvegardeImpossible {
	    try {
	        PreparedStatement instruction;
	        instruction = connection.prepareStatement("UPDATE compte_employe SET nom = ?, prenom = ?, email = ?, password = ? WHERE id = ?");
	        instruction.setString(1, employe.getNom());
	        instruction.setString(2, employe.getPrenom());
	        instruction.setString(3, employe.getMail());
	        instruction.setString(4, employe.getPassword());
	        instruction.setInt(5, employe.getId()); 
	        return instruction.executeUpdate(); // Retourne le nombre de lignes affectées
	    } catch (SQLException exception) {
	        exception.printStackTrace();
	        throw new SauvegardeImpossible(exception);
	    }
	}

	
}
