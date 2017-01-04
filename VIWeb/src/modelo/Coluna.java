package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import json.JSONException;
import json.JSONString;
import json.JSONStringer;


public class Coluna implements Serializable, JSONString{

	private static final long serialVersionUID = 4607023343133021187L;

	/**
	 * Nome da Coluna referente a Base de Dados
	 */
	private String nome;
	
	/**
	 * Tipo da coluna pode ser: INTEGER, DOUBLE, STRING, DATA
	 */
	private byte tipo;
	
	/**
	 * Uma colecao dos valores ordenados sem repeticao
	 */
	private TreeSet valores = new TreeSet();
    
    /**
     * Descricao da coluna que pode ser: CONTINUO, DISCRETO
     */
	private byte descricao;
	
	/**
	 * Numero de itens que corresponde a uma coluna DISCRETO
	 * Isto transforma uma coluna numerica em discreta.
	 * Por exemplo, a coluna "Ano" de 1980 a 1990 sera vista como DISCRETO
	 */
	public static final byte LIMITE_DISCRETO = 10;

    public static final byte STRING = 1;
    public static final byte INTEGER = 2;
    public static final byte DOUBLE = 3;
    public static final byte DATA = 4;
    
    public static final byte CONTINUO = 1;
    public static final byte DISCRETO = 0;

    public Coluna(){
    	nome = "";
    	tipo = -1;    	
    	descricao = -1;
    	valores = new TreeSet();
    }
    
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public byte getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		if(tipo.equals("STRING")){
			this.tipo = STRING;
		}else if(tipo.equals("INTEGER")){
			this.tipo = INTEGER;
		}else if(tipo.equals("DOUBLE")){
			this.tipo = DOUBLE;
		}else{
			this.tipo = DATA;
		}
	}
	public void setTipo(byte tipo) {
		this.tipo = tipo;
	}

	public byte getDescricao() {
		return descricao;
	}
	
	public void setDescricao(byte descricao) {
		this.descricao = descricao;
	}
	
	public int getQtdDistinta() {
		return this.valores.size();
	}
	
	public TreeSet getValores() {
		return this.valores;
	}
	
	@SuppressWarnings("unchecked")
	public void addValor(Object valor){
		if (tipo == INTEGER){
			valores.add((Integer) valor);
		}
		if (tipo == DOUBLE){
			valores.add((Double) valor);
		}
		if (tipo == DATA){
			valores.add((Date) valor);
		}
		if (tipo == STRING){
			valores.add(valor);
		}
	}
	
	public void setDescricao() {
		if(tipo == STRING | valores.size() <= LIMITE_DISCRETO){
			setDescricao(DISCRETO);
		}else{
        	setDescricao(CONTINUO);
        }
	}
	
	public String toString(){
		return this.nome;
	}

	@Override
	public String toJSONString() {
		JSONStringer json = new JSONStringer();
		
		try {
			json.object();
			
				json.key("nome");
				json.value(this.nome);
				
				json.key("tipo");
				json.value(this.tipo);
				
				json.key("desc");
				json.value(this.descricao);
				
				json.key("valores");
				json.array();
				switch (this.tipo) {
					case INTEGER:
						for(Object obj : valores){
							json.value( ((Integer)obj).intValue() );
						}
					break;
					
					case DOUBLE:
						for(Object obj : valores){
							json.value( ((Double)obj).doubleValue() );
						}
						break;
						
					case STRING:
						for(Object obj : valores){
							json.value( ((String)obj) );
						}
						break;
				}
				json.endArray();
				
			json.endObject();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
//	public static void main(String[] args) {
//		Coluna c = new Coluna();
//		c.setDescricao(CONTINUO);
//		c.setTipo(INTEGER);
//		c.setNome("MARCA");
//		c.addValor(new Integer(1));
//		c.addValor(new Integer(2));
//		c.addValor(new Integer(3));
//		
//		System.out.println(c.toJSONString());
//	}
}