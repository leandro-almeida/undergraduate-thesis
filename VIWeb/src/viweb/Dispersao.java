package viweb;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import json.JSONException;
import json.JSONString;
import json.JSONStringer;
import modelo.BaseArquivo;
import modelo.Coluna;
import modelo.Item;
import util.FactoryCor;
import util.FactoryForma;
import util.FormatadorValores;

public class Dispersao implements JSONString{
	
	// valores para configurar no GET (pelo cliente HTML)
	private int tamanhoEixo = 580; // tamanho em pixels
	private int pixelMinX = 0; // pixel X do ponto 0,0 do sistema
	private int pixelMinY = 0; // pixel Y do ponto 0,0 do sistema
	private int pixelMaxX = 0; // pixel X maximo do sistema (fim do eixo x)
	private int pixelMaxY = 0; // pixel Y maximo do sistema (fim do eixo y) ? duvida aki...
	
	// nome das colunas para os atributos
	private String nomeColunaEixoX = "";
	private String nomeColunaEixoY = "";
	private String nomeColunaForma = "";
	private String nomeColunaCor = "";
	
	// tipos das colunas
	private byte tipoColunaEixoX = 0;
	private byte tipoColunaEixoY = 0;
	private byte tipoColunaForma = 0;
	private byte tipoColunaCor = 0;
	
	// valores min e max dos eixos
	private double valorMaxEixoX;
	private double valorMinEixoX;
	private double valorMaxEixoY;
	private double valorMinEixoY;
	
	// rotulos dos eixos
	private int qtdRotulos = 5;
	private String rotulosEixoX[] = new String[qtdRotulos];
	private String rotulosEixoY[] = new String[qtdRotulos];
	
	public Dispersao(){
	}
	
	/**
	 * Configura a visualizacao Dispersao automaticamente.
	 * - Escolhe atributos para: eixo X, eixo Y, forma e cor.
	 * - Eixos: somente atributos continuos.
	 * - Forma: somente atributos discretos com no maximo 3 valores diferentes.
	 * - Cor: discreto ou contínuo. 
	 */
	public void configurar() {
		ArrayList<Coluna> colunas = BaseArquivo.getInstance().getColunas();
		ArrayList<Coluna> colunasInseridas = new ArrayList<Coluna>();
		
		// EIXO X
		if(this.nomeColunaEixoX.equals("")){
			for (Coluna coluna : colunas) {
				if(coluna.getDescricao() == Coluna.CONTINUO){
					this.nomeColunaEixoX = coluna.getNome();
					colunasInseridas.add(coluna);
					break;
				}
			}
		}
		
		// EIXO Y
		if(this.nomeColunaEixoY.equals("")){
			for (Coluna coluna : colunas) {
				if(coluna.getDescricao() == Coluna.CONTINUO 
						&& !colunasInseridas.contains(coluna)){
					this.nomeColunaEixoY = coluna.getNome();
					colunasInseridas.add(coluna);
					break;
				}
			}
		}
		
		colunasInseridas.clear();
		// FORMA
		if(this.nomeColunaForma.equals("")){
			for (Coluna coluna : colunas) {
				if(coluna.getDescricao() == Coluna.DISCRETO
						&& coluna.getQtdDistinta() <= FactoryForma.LIMITE_FORMAS){
					this.nomeColunaForma = coluna.getNome();
					colunasInseridas.add(coluna);
					FactoryForma.getInstance().setColunaForma(coluna);
					break;
				}
			}
		}
		
		// COR
		if(this.nomeColunaCor.equals("")){
			for (Coluna coluna : colunas) {
				if( !colunasInseridas.contains(coluna) ){
					this.nomeColunaCor = coluna.getNome();
					FactoryCor.getInstance().setColunaCor(coluna);
					break;
				}
			}
		}
		
		// checa se ficou nula
		if(this.nomeColunaEixoY.equals(""))
			this.setNomeColunaEixoY(this.nomeColunaEixoX);
		if(this.nomeColunaCor.equals(""))
			this.setNomeColunaCor(this.nomeColunaForma);
		
		// apos escolher eixos
		this.configurarColunas();
		this.configurarRotulos();
	}
	
	/**
	 * Configura valores (min e max) dos eixos.
	 * Configura tipos das colunas.
	 */
	public void configurarColunas(){
		Coluna colunaEixoX = BaseArquivo.getInstance().getHashMapColunas().get(this.nomeColunaEixoX);
		Coluna colunaEixoY = BaseArquivo.getInstance().getHashMapColunas().get(this.nomeColunaEixoY);
		Coluna colunaForma = BaseArquivo.getInstance().getHashMapColunas().get(this.nomeColunaForma);
		Coluna colunaCor = BaseArquivo.getInstance().getHashMapColunas().get(this.nomeColunaCor);

		this.tipoColunaEixoX = colunaEixoX.getTipo();
		this.tipoColunaEixoY = colunaEixoY.getTipo();
		this.tipoColunaForma = colunaForma.getTipo();
		this.tipoColunaCor = colunaCor.getTipo();
		
		this.valorMinEixoX = FormatadorValores.getValorDouble(colunaEixoX.getTipo(), colunaEixoX.getValores().first());
		this.valorMinEixoY = FormatadorValores.getValorDouble(colunaEixoY.getTipo(), colunaEixoY.getValores().first());
		this.valorMaxEixoX = FormatadorValores.getValorDouble(colunaEixoX.getTipo(), colunaEixoX.getValores().last());
		this.valorMaxEixoY = FormatadorValores.getValorDouble(colunaEixoY.getTipo(), colunaEixoY.getValores().last());
	}
	
	public void configurarRotulos(){
		double variacaoX = (this.valorMaxEixoX - this.valorMinEixoX) / this.qtdRotulos;
		double variacaoY = (this.valorMaxEixoY - this.valorMinEixoY) / this.qtdRotulos;
//		System.out.println("varX="+variacaoX);
//		System.out.println("varY="+variacaoY);
		
		// rotulos do meio
		String x, y;
		
		for(int i=1; i<this.qtdRotulos-1; i++){
			x = removeChar(FormatadorValores.formata(this.valorMinEixoX + (variacaoX*i), 2), '.').replace(',', '.');
			y = removeChar(FormatadorValores.formata(this.valorMinEixoY + (variacaoY*i), 2), '.').replace(',', '.');
			
			this.rotulosEixoX[i] = x;
			this.rotulosEixoY[i] = y;
		}

		// primeiro e ultimo rotulos
		x = removeChar(FormatadorValores.formata(this.valorMinEixoX, 2), '.').replace(',', '.');
		y = removeChar(FormatadorValores.formata(this.valorMaxEixoX, 2), '.').replace(',', '.');
		this.rotulosEixoX[0] = x;
		this.rotulosEixoX[this.rotulosEixoX.length-1] = y;
		
		x = removeChar(FormatadorValores.formata(this.valorMinEixoY, 2), '.').replace(',', '.');
		y = removeChar(FormatadorValores.formata(this.valorMaxEixoY, 2), '.').replace(',', '.');
		this.rotulosEixoY[0] = x;
		this.rotulosEixoY[this.rotulosEixoY.length-1] = y; 
	}
	
	private String removeChar(String s, char c) {
		String r = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != c)
				r += s.charAt(i);
		}
		return r;
	}
	
	/**
	 * Configura cada item:
	 * - ponto(x,y) em pixels
	 * - forma (int)
	 * - cor (int)
	 */
	public void configurarItens() {
		for(Item item : BaseArquivo.getInstance().itens){
			this.configurarPonto(item);
			this.configurarForma(item);
			this.configurarCor(item);
		}
	}
	private void configurarPonto(Item item) {
		Object valorX;
		Object valorY;
		valorX = item.getValorColuna(this.nomeColunaEixoX);
		valorY = item.getValorColuna(this.nomeColunaEixoY);
		
		// guarda valores da coluna para os eixos
		item.setValorX(FormatadorValores.getValorDouble(this.tipoColunaEixoX, valorX));
		item.setValorY(FormatadorValores.getValorDouble(this.tipoColunaEixoY, valorY));
		
		// converte pontos
		int pixelX;
		int pixelY;
		pixelX = calculaCoordenadaX(item.getValorX(), this.pixelMinX, this.pixelMaxX);
		pixelY = calculaCoordenadaY(item.getValorY(), this.pixelMinY, this.pixelMaxY);
		item.setPonto(new Point(pixelX, pixelY));
	}
	private void configurarForma(Item item) {
		item.setForma( FactoryForma.getInstance().getForma(item) );
	}
	private void configurarCor(Item item) {
		item.setCor( FactoryCor.getInstance().getCor(item) );
	}


	
	/**
	 * A formula usada para este calculo e
	 * valorItem - minValor = pixelItem - pixelMin
	 * --------------------   --------------------
	 * maxValor  - minValor   pixelMax  - pixelMin
	 */
	public int calculaCoordenadaX(double valorX, int pixelMinX, int pixelMaxX){
		return (int) (((valorX - getValorMinEixoX()) * (pixelMaxX - pixelMinX)) / (getValorMaxEixoX() - getValorMinEixoX())) + pixelMinX;
	}
	public int calculaCoordenadaY(double valorY, int pixelMinY, int pixelMaxY){
		return (int) (((valorY - getValorMinEixoY()) * (pixelMaxY - pixelMinY)) / (getValorMaxEixoY() - getValorMinEixoY())) + pixelMinY;
	}


	public int getTamanhoEixo() {
		return tamanhoEixo;
	}
	public void setTamanhoEixo(int tamanhoEixo) {
		this.tamanhoEixo = tamanhoEixo;
	}
	public double getValorMaxEixoX() {
		return valorMaxEixoX;
	}
	public void setValorMaxEixoX(double valorMaxEixoX) {
		this.valorMaxEixoX = valorMaxEixoX;
	}
	public double getValorMinEixoX() {
		return valorMinEixoX;
	}
	public void setValorMinEixoX(double valorMinEixoX) {
		this.valorMinEixoX = valorMinEixoX;
	}
	public double getValorMaxEixoY() {
		return valorMaxEixoY;
	}
	public void setValorMaxEixoY(double valorMaxEixoY) {
		this.valorMaxEixoY = valorMaxEixoY;
	}
	public double getValorMinEixoY() {
		return valorMinEixoY;
	}
	public void setValorMinEixoY(double valorMinEixoY) {
		this.valorMinEixoY = valorMinEixoY;
	}
	public byte getTipoColunaEixoX() {
		return tipoColunaEixoX;
	}
	public void setTipoColunaEixoX(byte tipoColunaEixoX) {
		this.tipoColunaEixoX = tipoColunaEixoX;
	}
	public byte getTipoColunaEixoY() {
		return tipoColunaEixoY;
	}
	public void setTipoColunaEixoY(byte tipoColunaEixoY) {
		this.tipoColunaEixoY = tipoColunaEixoY;
	}
	public byte getTipoColunaForma() {
		return tipoColunaForma;
	}
	public void setTipoColunaForma(byte tipoColunaForma) {
		this.tipoColunaForma = tipoColunaForma;
	}
	public byte getTipoColunaCor() {
		return tipoColunaCor;
	}
	public void setTipoColunaCor(byte tipoColunaCor) {
		this.tipoColunaCor = tipoColunaCor;
	}
	public int getPixelMinX() {
		return pixelMinX;
	}
	public void setPixelMinX(int pixelMinX) {
		this.pixelMinX = pixelMinX;
	}
	public int getPixelMinY() {
		return pixelMinY;
	}
	public void setPixelMinY(int pixelMinY) {
		this.pixelMinY = pixelMinY;
	}
	public int getPixelMaxX() {
		return pixelMaxX;
	}
	public void setPixelMaxX(int pixelMaxX) {
		this.pixelMaxX = pixelMaxX;
	}
	public int getPixelMaxY() {
		return pixelMaxY;
	}
	public void setPixelMaxY(int pixelMaxY) {
		this.pixelMaxY = pixelMaxY;
	}

	public String getNomeColunaEixoX() {
		return nomeColunaEixoX;
	}

	public void setNomeColunaEixoX(String nomeColunaEixoX) {
		this.nomeColunaEixoX = nomeColunaEixoX;
	}

	public String getNomeColunaEixoY() {
		return nomeColunaEixoY;
	}

	public void setNomeColunaEixoY(String nomeColunaEixoY) {
		this.nomeColunaEixoY = nomeColunaEixoY;
	}

	public String getNomeColunaForma() {
		return nomeColunaForma;
	}

	public void setNomeColunaForma(String nomeColunaForma) {
		this.nomeColunaForma = nomeColunaForma;
		FactoryForma.getInstance().setColunaForma(nomeColunaForma);
	}

	public String getNomeColunaCor() {
		return nomeColunaCor;
	}

	public void setNomeColunaCor(String nomeColunaCor) {
		this.nomeColunaCor = nomeColunaCor;
		FactoryCor.getInstance().setColunaCor(nomeColunaCor);
	}

	@Override
	public String toJSONString() {
		JSONStringer json = new JSONStringer();
		try {
			json.object();
				json.key("eixoX");
				json.value(this.nomeColunaEixoX);
				json.key("eixoY");
				json.value(this.nomeColunaEixoY);
				json.key("forma");
				json.value(this.nomeColunaForma);
				json.key("cor");
				json.value(this.nomeColunaCor);
				
				json.key("rotulosX");
				json.array();
					for(int i=0; i<this.rotulosEixoX.length; i++)
						json.value(this.rotulosEixoX[i]);
				json.endArray();
				
				json.key("rotulosY");
				json.array();
				for(int i=0; i<this.rotulosEixoY.length; i++)
					json.value(this.rotulosEixoY[i]);
				json.endArray();
				
			json.endObject();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}

	public Item getItemAt(int x, int y) {
		int tamObj = 15; // salvar isso no escopo global
		Rectangle rect;
		ArrayList<Item> itens = BaseArquivo.getInstance().itens;
		int ptX, ptY; 
		for(Item item : itens){
			// centraliza o rect no ponto do item
			ptX = item.getPonto().x - tamObj/2;
			ptY = item.getPonto().y - tamObj/2;
			
			// cria um rect pra fazer o contains()
			rect = new Rectangle(ptX, ptY, tamObj, tamObj);
			if(rect.contains(x, y))
				return item;
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		Dispersao d = new Dispersao();
//		d.setNomeColunaEixoX("VALOR");
//		d.setNomeColunaEixoY("POTENCIA");
//		d.setNomeColunaForma("NRPORTAS");
//		d.setNomeColunaCor("MARCA");
//		System.out.println(d.toJSONString());
//	}
}
