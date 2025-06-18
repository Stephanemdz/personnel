package personnel;

public interface Passerelle 
{
	public GestionPersonnel getGestionPersonnel();
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)  throws SauvegardeImpossible;
	public int insert(Ligue ligue) throws SauvegardeImpossible;
	
	public int insert (Employe employe) throws SauvegardeImpossible;
	public int update(Ligue ligue) throws SauvegardeImpossible;
	public int update(Employe employe) throws SauvegardeImpossible;
	public int deleteEmploye(int employeId) throws SauvegardeImpossible;
	public int deleteLigue(int ligueId) throws SauvegardeImpossible;
	public int updateAdministrateur(Ligue ligue) throws SauvegardeImpossible;
	
	// Nouvelle méthode pour vérifier l'existence de l'utilisateur "root"
    public boolean rootExiste() throws SauvegardeImpossible;
    
}
