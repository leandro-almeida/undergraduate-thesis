package entidades;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(schema="viweb")
@NamedQueries({
	@NamedQuery(
		name="Usuario.listarUsuarios",
		query="SELECT u FROM Usuario u"
	),
	@NamedQuery(
			name="Usuario.listarUsuariosNaoAdmin",
			query="SELECT u FROM Usuario u WHERE u.admin = 0"
	),
	@NamedQuery(
		name="Usuario.efetuarLogon",
		query="SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha"
	)
})
public class Usuario {
	@Id
	@GeneratedValue
	private int id;

	@Column(length=60, nullable=false)
	private String nome;
	
	@Column(length=15, nullable=false, updatable=false)
	private String login;

	@Column(length=50)
	private String email;

	@Column(length=32, nullable=false)
	private String senha;
	
	@Column(nullable=false)
	private Boolean admin;
	
	@OneToMany
	private Collection<BaseDados> bases;

	public Usuario() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Collection<BaseDados> getBases() {
		return bases;
	}

	public void setBases(Collection<BaseDados> bases) {
		this.bases = bases;
	}

}
