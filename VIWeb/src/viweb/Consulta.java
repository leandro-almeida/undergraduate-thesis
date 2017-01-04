package viweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import json.JSONStringer;
import modelo.BaseArquivo;
import modelo.Coluna;
import modelo.ConfBaseDados;
import modelo.Item;
import util.FactoryCor;
import util.FormatadorValores;
import filtro.FiltroContinuo;
import filtro.FiltroDiscreto;
import filtro.ModeloFiltro;

public class Consulta extends HttpServlet implements Servlet {
	static final long serialVersionUID = 1L;

	private Dispersao dispersao = null;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 * 
	 * Inicializacao do Servlet: 
	 *  - carrega a base (apenas uma vez)
	 *  - constroi os itens (mantendo em memoria).
	 */

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * Chamada GET: primeira execucao da aplicacao.
	 * - Retorna todos os itens para desenhar + colunas (nomes, tipos e valores) + cores + configuracoes iniciais.
	 * - No lado cliente: montar interface (config. visualizacao, filtros) a partir das colunas e seus tipos (INTEGER, DOUBLE, STRING).
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		String strBase = request.getParameter("base");
		String strEixo = request.getParameter("eixo");
		String strPixelMinX = request.getParameter("pixelMinX");
		String strPixelMinY = request.getParameter("pixelMinY");
		
		int idBase=0;
		int tamEixo;
		int pixelMinX=20, pixelMaxX=600;
		int pixelMinY=20, pixelMaxY=600;
		try {
			idBase = Integer.parseInt(strBase);
			tamEixo = Integer.parseInt(strEixo);
			pixelMinX = Integer.parseInt(strPixelMinX);
			pixelMinY = Integer.parseInt(strPixelMinY);
		} catch (NumberFormatException e) {
			tamEixo = 580;
			e.printStackTrace();
		}
		
		if(idBase == 0){
			if (dispersao == null) {
				dispersao = new Dispersao();
				ConfBaseDados.getInstance().setTipoDados("arquivo");
				ConfBaseDados.getInstance().setCaminho(Bases.BASE_CARROS);
				BaseArquivo.getInstance().carregaBaseDados();
				dispersao.configurar();
			}
			dispersao.setTamanhoEixo(tamEixo);
			dispersao.setPixelMinX(pixelMinX);
			dispersao.setPixelMinY(pixelMinY);
			dispersao.setPixelMaxX(pixelMinX+tamEixo);
			dispersao.setPixelMaxY(pixelMinY+tamEixo);
			dispersao.configurarItens();
		
			out.print(this.getRespostaInicialJSON());
			out.close();
		}
	}

	private String getRespostaInicialJSON() {
		JSONStringer json = new JSONStringer();
		
		try {
			json.object();
			
				json.key("config");
				json.value(this.dispersao);
				
				json.key("colunas");
				json.array();
					ArrayList<Coluna> cols = BaseArquivo.getInstance().getColunas();
					Collections.sort(cols, new Comparator<Coluna>(){
						public int compare(Coluna colA, Coluna colB){
							return colA.getNome().compareToIgnoreCase( colB.getNome() );
						}
					});
				
					for(Coluna col : cols)
						json.value(col);
				json.endArray();
				
				json.key("cores");
				json.value(FactoryCor.getInstance());
				
				json.key("itens");
				json.array();
					for (Item item : BaseArquivo.getInstance().itens)
						json.value(item);
				json.endArray();
			
			json.endObject();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * Chamada POST: filtro e configuracoes da visualizacao.
	 * - Retorna apenas os itens que passaram no filtro.
	 * - Apos configuracao: retornar itens atualizados.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String filtro = request.getParameter("filtro");
		String config = request.getParameter("config");
		String click = request.getParameter("click");
		// verifica se tem param filtro
		if(filtro != null){
			if(!filtro.isEmpty()){ // filtrar
				BaseArquivo.getInstance().filtrar(this.getFiltros(filtro));
				out.print(this.getRespostaFiltroJSON());
			}
		}else if(config != null){ // verifica se tem param config
			if(!config.isEmpty()){ // configurar
				this.configurarVisualizacao(config);
				out.print(this.getRespostaConfigJSON());
			}
		}else if(click != null){ // obter detalhes do item
			if(!click.isEmpty()){
				out.print(this.obterDetalhesItem(click));
			}
		}else{// nada
			out.print("");
		}
		out.close();
	}
	
	private String obterDetalhesItem(String click){
		try {
			JSONObject json = new JSONObject(click);
			Item item = this.dispersao.getItemAt(json.getInt("x"), json.getInt("y"));
			if(item != null){
				// gera output
				StringBuffer saida = new StringBuffer();
				ArrayList<Coluna> cols = BaseArquivo.getInstance().getColunas();
				for(Coluna coluna : cols){
					saida.append("<b>"+coluna.getNome() + ":</b> "); 
					saida.append(FormatadorValores.formataValorColuna( coluna.getTipo(), item.getValorColuna(coluna.getNome()) ));
					saida.append("<br/>");
				}

				return saida.toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "Nenhum item.";
	}
	
	private void configurarVisualizacao(String config){
		try{
			JSONObject json = new JSONObject(config);
			String eixoX = json.getString("eixoX");
			String eixoY = json.getString("eixoY");
			String forma = json.getString("forma");
			String cor = json.getString("cor");
			
			if(!eixoX.isEmpty())
				dispersao.setNomeColunaEixoX(eixoX);
			if(!eixoY.isEmpty())
				dispersao.setNomeColunaEixoY(eixoY);
			if(!cor.isEmpty())
				dispersao.setNomeColunaCor(cor);
			if(!forma.isEmpty())
				dispersao.setNomeColunaForma(forma);
			
			dispersao.configurarColunas();
			dispersao.configurarRotulos();
			dispersao.configurarItens();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<ModeloFiltro> getFiltros(String postFiltro){
		ArrayList<ModeloFiltro> filtros = new ArrayList<ModeloFiltro>();
		try {
			JSONArray jsonArray = new JSONArray(postFiltro);
			JSONObject jsonFiltroObj;
			String nomeColuna;
			Coluna coluna;
			
			// para cada objeto vindo no array de filtro
			for(int i=0; i<jsonArray.length(); i++){
				jsonFiltroObj = jsonArray.getJSONObject(i);
				nomeColuna = jsonFiltroObj.getString("atributo");
				coluna = BaseArquivo.getInstance().getHashMapColunas().get(nomeColuna);
				if(coluna != null)
					filtros.add(this.getModeloFiltro(coluna, jsonFiltroObj));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return filtros;
	}
	
	// variaveis para o metodo getModeloFiltro (evitar desperdicio de memoria)
	private FiltroDiscreto filtroDiscreto;
	private FiltroContinuo filtroContinuo;
	private JSONObject jsonValorObj;
	private JSONArray valoresColuna;
	private Object min, max;
	
	private ModeloFiltro getModeloFiltro(Coluna coluna, JSONObject jsonFiltroObj) {
		try {
			if(coluna.getDescricao() == Coluna.DISCRETO){
				filtroDiscreto = new FiltroDiscreto();
				filtroDiscreto.setColuna(coluna);
				valoresColuna = jsonFiltroObj.getJSONArray("valores");
				
				// para cada valor que veio desse atributo: seta no modelo de filtro discreto
				for(int j=0; j<valoresColuna.length(); j++){
					jsonValorObj = valoresColuna.getJSONObject(j);
					filtroDiscreto.add(jsonValorObj.getString("valor"), jsonValorObj.getBoolean("marcado"));
				}
				return filtroDiscreto;
			}else{
				filtroContinuo = new FiltroContinuo();
				filtroContinuo.setColuna(coluna);
				if(coluna.getTipo() == Coluna.INTEGER){
					min = (Integer) jsonFiltroObj.getInt("min");
					max = (Integer) jsonFiltroObj.getInt("max");
				}else{
					min = (Double) jsonFiltroObj.getDouble("min");
					max = (Double) jsonFiltroObj.getDouble("max");
				}
				filtroContinuo.setMinAtual(min);
				filtroContinuo.setMaxAtual(max);
				
				return filtroContinuo;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private String getRespostaFiltroJSON() {
		JSONStringer json = new JSONStringer();
		JSONArray j = new JSONArray();
		try {
			json.array();
			for (Item item : BaseArquivo.getInstance().itens)
				json.value(item);
			json.endArray();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	private String getRespostaConfigJSON() {
		JSONStringer json = new JSONStringer();
				
		try {
			json.object();
			
				json.key("config");
				json.value(this.dispersao);
				
				json.key("cores");
				json.value(FactoryCor.getInstance());
				
				json.key("itens");
				json.array();
					for (Item item : BaseArquivo.getInstance().itens)
						json.value(item);
				json.endArray();
			
			json.endObject();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
}