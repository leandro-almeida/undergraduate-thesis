package util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import json.JSONException;
import json.JSONString;
import json.JSONStringer;
import modelo.BaseArquivo;
import modelo.Coluna;
import modelo.Item;

public class FactoryCor implements JSONString{
	private static FactoryCor instance = null;
	
	// mapa para obter a cor dado o valor do item
	private HashMap<Object, Integer> mapaCores = new HashMap<Object, Integer>();
	
	private Coluna colunaCor;
	private Color corInicial = Color.BLACK;
	private Color corFinal = Color.GREEN;
	
	private final Color[] coresDiscretas = {
		Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA,
		Color.CYAN, Color.GRAY, Color.ORANGE, Color.PINK,
		Color.YELLOW, Color.DARK_GRAY
	};
	
	public static FactoryCor getInstance(){
		if(instance == null)
			instance = new FactoryCor();
		return instance;
	}

	public Coluna getColunaCor() {
		return colunaCor;
	}

	public void setColunaCor(String colunaCor){
		Coluna coluna = BaseArquivo.getInstance().getHashMapColunas().get(colunaCor);
		this.setColunaCor(coluna);
	}
	
	public void setColunaCor(Coluna colunaCor) {
		this.colunaCor = colunaCor;
		this.gerarCores();
	}
	
	/**
	 * Gera cores baseado na coluna setada.
	 * Essas cores (gradiente ou discretas) serao repassadas ao cliente (toJSONString)
	 */
	public void gerarCores(){
		this.mapaCores.clear();
		
		// gera mapa de valores da coluna associados a integer
		Object[] lista = colunaCor.getValores().toArray();
		for (int i=0; i<lista.length; i++)
			this.mapaCores.put(lista[i], new Integer(i));
		
		if(this.colunaCor.getQtdDistinta() > this.coresDiscretas.length){
			// gera degrade
			Gradient.getInstance().reset();
			Gradient.getInstance().addPoint(this.corInicial);
			Gradient.getInstance().addPoint(this.corFinal);
			Gradient.getInstance().createGradient(this.mapaCores.size());
		}
	}
	
	/**
	 * @param item
	 * @return O valor int para a COR deste item.
	 */
	public int getCor(Item item){
		Object valor = item.getValorColuna(this.colunaCor.getNome());
		return this.mapaCores.get(valor).intValue();
	}
	
	@Override
	public String toJSONString() {
		JSONStringer json = new JSONStringer();
		
		Color cor;
		int r, g, b;
		String strCor;
		
		try {
			json.array();

			if(this.colunaCor.getQtdDistinta() <= this.coresDiscretas.length){
				
				for (int i = 0; i < this.colunaCor.getQtdDistinta(); i++) {
					cor = this.coresDiscretas[i];
					r = cor.getRed();
					g = cor.getGreen();
					b = cor.getBlue();
					strCor = "rgb("+ r +","+ g +","+ b +")";
					json.value(strCor);
				}
				
			}else{
				
				Color[] cores = Gradient.getInstance().getGradient();
				for (int i = 0; i < cores.length; i++) {
					cor = cores[i];
					r = cor.getRed();
					g = cor.getGreen();
					b = cor.getBlue();
					strCor = "rgb("+ r +","+ g +","+ b +")";
					json.value(strCor);
				}
				
			}
			
			json.endArray();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}

	public Color getCorInicial() {
		return corInicial;
	}

	public void setCorInicial(Color corInicial) {
		this.corInicial = corInicial;
	}

	public Color getCorFinal() {
		return corFinal;
	}

	public void setCorFinal(Color corFinal) {
		this.corFinal = corFinal;
	}
	
//	public static void main(String[] args) {
//		Coluna c = new Coluna();
//		c.setNome("VALOR");
//		c.setTipo(Coluna.INTEGER);
//		c.addValor(new Integer(12));
//		c.addValor(new Integer(11));
//		c.addValor(new Integer(10));
//		c.addValor(new Integer(9));
//		c.addValor(new Integer(8));
//		c.addValor(new Integer(7));
//		c.addValor(new Integer(6));
//		c.addValor(new Integer(5));
//		c.addValor(new Integer(4));
//		c.addValor(new Integer(3));
//		c.addValor(new Integer(2));
//		c.addValor(new Integer(1));
//		c.setDescricao();
//		
//		Object[] vals = c.getValores().toArray();
//		for (int i = 0; i < vals.length; i++) {
//			System.out.println("valor: "+ ((Integer)vals[i]).intValue() );
//		}
//		
//		FactoryCor.getInstance().setColunaCor(c);
//		Color[] cor = Gradient.getInstance().getGradient();
//		for (int i = 0; i<cor.length; i++) {
//			System.out.println(cor[i].toString());
//		}
//		
//		System.out.println( FactoryCor.getInstance().toJSONString() );
//	}
}
