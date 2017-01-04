/**
 * 
 */
package modelo;

import java.util.ArrayList;
import java.util.Collection;

import filtro.ModeloFiltro;

/**
 * @author Rede de Informatica
 *
 */
public interface IBaseDados {

	public abstract boolean carregaBaseDados();
	
	public abstract ArrayList<Item> getBaseTotal();
	
	public abstract void filtrar(Collection<ModeloFiltro> paramFiltros);
}
