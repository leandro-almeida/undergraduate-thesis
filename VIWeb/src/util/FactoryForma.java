package util;

import java.util.HashMap;
import java.util.Iterator;

import modelo.BaseArquivo;
import modelo.Coluna;
import modelo.Item;

/**
 * @author Administrador
 * Associa um valor a um inteiro, que ira representar a forma, comecando em 0.
 * Exemplo:
 * - NRPORTAS: 2 sera 0, 4 sera 1.
 */
public class FactoryForma {
	public static final int LIMITE_FORMAS = 3;
	
	private static FactoryForma instance = null;
	private Coluna colunaForma;
	private HashMap<Object, Integer> hashFormas = new HashMap<Object, Integer>();
	
	public static FactoryForma getInstance(){
		if(instance == null)
			instance = new FactoryForma();
		return instance;
	}

	public Coluna getColunaForma() {
		return colunaForma;
	}

	public void setColunaForma(String colunaForma){
		Coluna coluna = BaseArquivo.getInstance().getHashMapColunas().get(colunaForma);
		this.setColunaForma(coluna);
	}
	
	public void setColunaForma(Coluna colunaForma) {
		if(colunaForma.getQtdDistinta() > LIMITE_FORMAS)
			return;
		this.colunaForma = colunaForma;
		this.gerarFormas();
	}
	
	public void gerarFormas(){
		if(colunaForma == null)
			return;
		hashFormas.clear();
		Iterator it = colunaForma.getValores().iterator();
		Object valor;
		int c = 0;
		while(it.hasNext()){
			valor = it.next();
			this.hashFormas.put(valor, new Integer(c));
			c++;
		}
	}
	
	/**
	 * @param item
	 * @return O valor int para a FORMA deste item.
	 */
	public int getForma(Item item){
		return hashFormas.get( item.getValorColuna(colunaForma.getNome()) ).intValue();
	}
	
}
