package modelo;

import java.awt.Point;
import java.util.HashMap;

import json.JSONException;
import json.JSONString;
import json.JSONStringer;

public class Item implements JSONString {
	private HashMap<String, Object> valores = new HashMap<String, Object>();
	private Point ponto; // (x,y) ja em pixels (convertido)
	private int forma; // apenas referencia para o cliente
	private int cor; // apenas referencia para o cliente
	
	private double valorX; // valor deste item para o eixo X (valor da coluna)
	private double valorY; // valor deste item para o eixo Y (valor da coluna)
	
	public Item(){
	}
	
	public void setValor(String atributo, Object valor){
		this.valores.put(atributo, valor);
	}
	
	public Object getValorColuna(String col){
		return this.valores.get(col);
	}
	public void setValorColuna(String atributo, Object valor) {		
		this.valores.put(atributo, valor);
	}

	public String toString(){
		return "";
	}

	public Point getPonto() {
		return ponto;
	}

	public void setPonto(Point ponto) {
		this.ponto = ponto;
	}

	public int getForma() {
		return forma;
	}

	public void setForma(int forma) {
		this.forma = forma;
	}

	public int getCor() {
		return cor;
	}

	public void setCor(int cor) {
		this.cor = cor;
	}

	public double getValorX() {
		return valorX;
	}

	public void setValorX(double valorX) {
		this.valorX = valorX;
	}

	public double getValorY() {
		return valorY;
	}

	public void setValorY(double valorY) {
		this.valorY = valorY;
	}

	@Override
	public String toJSONString() {
		JSONStringer json = new JSONStringer();
		
		try {
			json.object();
			
				json.key("x");
				json.value(this.ponto.x);
				
				json.key("y");
				json.value(this.ponto.y);
				
				json.key("f");
				json.value(this.forma);
				
				json.key("c");
				json.value(this.cor);
				
			json.endObject();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}

//	public static void main(String[] args) {
//		Item i = new Item();
//		i.setCor(1);
//		i.setForma(2);
//		i.setPonto(new Point(10, 15));
//		i.setValorColuna("MARCA", "audi");
//		
//		System.out.println(i.toJSONString());
//	}
}
