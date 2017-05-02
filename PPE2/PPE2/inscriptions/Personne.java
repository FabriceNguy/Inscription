package inscriptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import src.Connect;

/**
 * Représente une personne physique pouvant s'inscrire à une compétition.
 */

public class Personne extends Candidat
{
	private static final long serialVersionUID = 4434646724271327254L;
	private String prenom, mail;
	private Set<Equipe> equipes;
	private Connect c;
	private static final boolean Serializable = false;
	
	Personne(Inscriptions inscriptions, String nom, String prenom, String mail)
	{
		super(inscriptions, nom);
		this.prenom = prenom;
		this.mail = mail;
		equipes = new TreeSet<>();
	}

	/**
	 * Retourne le prénom de la personne.
	 * @return
	 */
	
	public String getPrenom()
	{
		return prenom;
	}

	/**
	 * Modifie le prénom de la personne.
	 * @param prenom
	 */
	
	public void setPrenom(String prenom)
	{
		if(!Serializable)
			c.setPrenomPersonne(prenom,this.getIdCandidat());
		this.prenom = prenom;
	}

	/**
	 * Retourne l'adresse électronique de la personne.
	 * @return
	 */
	public String getMail() {
		// TODO Auto-generated method stub
		return mail;
	}
	/**
	 * Modifie l'adresse électronique de la personne.
	 * @param mail
	 */
	
	public void setMail(String mail)
	{
		if(!Serializable)
			c.setMailPersonne(mail,this.getIdCandidat());
		this.mail = mail;
	}

	/**
	 * Retoure les équipes dont cette personne fait partie.
	 * @return
	 */
	
	public Set<Equipe> getEquipes()
	{
		
		ResultSet rs = c.resultatRequete("SELECT * "
				+ "FROM etre_dans, candidat "
				+ "WHERE NumCandidatPers ="+getIdCandidat()
				+ "AND etre_dans.NumCandidatPers = personne.NumCandidat");
		try {
			while (rs.next()) {
				Equipe equipe = new Equipe(getInscriptions(), rs.getString("NomEquipe"));
				equipes.add(equipe);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.unmodifiableSet(equipes);
	}
	
	boolean add(Equipe equipe)
	{
		
		if(!Serializable){
			try {
				c.add(equipe);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return equipes.add(equipe);
	}

	boolean remove(Equipe equipe)
	{
		if(!Serializable){
			c.delMembreEquipe(equipe.getIdCandidat(), getIdCandidat());
		}
		return equipes.remove(equipe);
	}
	
	@Override
	public void delete()
	{
		super.delete();
		for (Equipe e : equipes)
			e.remove(this);
	}
	
	@Override
	public String toString()
	{
		return super.toString() + " membre de " + equipes.toString();
	}


}