package serialisation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

public class Serialization implements personnel.Passerelle
{
	private static final String FILE_NAME = "GestionPersonnel.srz";

	@Override
	public GestionPersonnel getGestionPersonnel()
	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME)))
		{
			return (GestionPersonnel) ois.readObject();
		}
		catch (IOException | ClassNotFoundException e)
		{
			return null;
		}
	}
	
	/**
	 * Sauvegarde le gestionnaire pour qu'il soit ouvert automatiquement 
	 * lors d'une exécution ultérieure du programme.
	 * @throws SauvegardeImpossible Si le support de sauvegarde est inaccessible.
	 */
	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME)))
		{
			oos.writeObject(gestionPersonnel);
		}
		catch (IOException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible
	{
		return -1;
	}
	
	@Override
	public int insert (Employe employe) throws SauvegardeImpossible
	{
		return -1;
	}
	public int update(Ligue ligue) throws SauvegardeImpossible
	{
		return -1;
	}
	public int update(Employe employe) throws SauvegardeImpossible
	{
		return -1;
	}
	public int deleteEmploye(int employeId) throws SauvegardeImpossible
	{
		return -1;
	}
	public int deleteLigue(int ligueId) throws SauvegardeImpossible
	{
		return -1;
	}
	public boolean rootExiste()throws SauvegardeImpossible
	{
		return true;
	}

	public int updateAdministrateur(Ligue ligue) throws SauvegardeImpossible {
		return -1;
	}


	public String getAdminLigueNom(Ligue ligue) throws SQLException, SauvegardeImpossible {
		return null;
	}
}
