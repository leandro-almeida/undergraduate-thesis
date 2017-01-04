package modelo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Rede de Informatica
 *
 */
public abstract class BaseDados implements IBaseDados{
	
	public ArrayList<Item> itens = new ArrayList<Item>();
	public HashMap<String, Coluna> colunas = new HashMap<String, Coluna>();
	
	public void inicializaEstruturas(){
		colunas.clear();
		itens.clear();
	}
	
	public ArrayList<Coluna> getColunas(){
		ArrayList<Coluna> lista = new ArrayList<Coluna>();
		for (Coluna coluna: colunas.values()) {
			lista.add(coluna);
		}		
		return lista;
	}
	
	public HashMap<String, Coluna> getHashMapColunas(){
		return colunas;
	}
}