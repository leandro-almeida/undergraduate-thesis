package modelo;

import java.util.Properties;
 
public class ConfBaseDados extends Properties {
	private static final long serialVersionUID = -6225050115950085448L;
	private static ConfBaseDados confBaseDados;
	private String tipoDados;
	private String caminho;

	public static ConfBaseDados getInstance(){
		if (confBaseDados == null) {
			confBaseDados = new ConfBaseDados();
		}
		return confBaseDados;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.setProperty("Caminho", caminho);
		this.caminho = caminho;
	}

	public String getTipoDados() {
		return tipoDados;
	}

	public void setTipoDados(String tipoDados) {
		this.setProperty("TipoDados", tipoDados);
		this.tipoDados = tipoDados;
	}
}
