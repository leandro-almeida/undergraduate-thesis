package filtro;

import java.util.HashMap;


public class FiltroDiscreto extends ModeloFiltro{
	private HashMap<String, Boolean> valoresFiltro = new HashMap<String, Boolean>();
	
	public HashMap<String, Boolean> getValoresFiltro() {
		return this.valoresFiltro;
	}
	
	public void add(String chave, boolean b){
		this.valoresFiltro.put(chave, b);
	}
}
