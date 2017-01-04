package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidades.Usuario;

public class UsuarioDAO {
	EntityManagerFactory emf;// = Persistence.createEntityManagerFactory("VIWeb");
    EntityManager em;// = emf.createEntityManager();
	
	public UsuarioDAO(){
		emf = Persistence.createEntityManagerFactory("VIWeb");
		em = emf.createEntityManager();
	}
	
	public void adicionar(Usuario u) {
		em.persist(u);
	}

	public void alterar(Usuario u) {
		Usuario user = em.find(Usuario.class, new Integer(u.getId()));
		if(user == null) // id nao existe, fara insert!
			return;
		em.merge(u);
	}
	
	public void remover(int id) {
		Usuario u = em.find(Usuario.class, new Integer(id));
		if(u == null)
			return;
		em.remove(u);
	}
	
	public Usuario getUsuario(int id) {
		return em.find(Usuario.class, new Integer(id));
	}

	/* (non-Javadoc)
	 * @see ejb.UsuarioRemote#logon(java.lang.String, java.lang.String)
	 * Retorna um objeto Usuario se o login e senha forem validos no BD.
	 * Senha deve ser SEM md5.
	 */
	public Usuario logon(String login, String senha) {
		Query q = em.createNamedQuery("Usuario.efetuarLogon");
		q.setParameter("login", login);
		q.setParameter("senha", senha);
		
		Usuario u;
		try {
			u = (Usuario)q.getSingleResult();
		} catch (NoResultException e) {
			u = null;
		}
		
		return u;
	}

	public ArrayList<Usuario> listarUsuarios() {
		Query q = em.createNamedQuery("Usuario.listarUsuarios");
		
		List l = q.getResultList();
		ArrayList<Usuario> users = new ArrayList<Usuario>();
		for (int i = 0; i < l.size(); i++)
			users.add(((Usuario) l.get(i)));
		
		return users;
	}

	public ArrayList<Usuario> listarUsuariosNaoAdmin() {
		Query q = em.createNamedQuery("Usuario.listarUsuariosNaoAdmin");
		
		List l = q.getResultList();
		ArrayList<Usuario> users = new ArrayList<Usuario>();
		for (int i = 0; i < l.size(); i++)
			users.add(((Usuario) l.get(i)));
		
		return users;
	}
}
