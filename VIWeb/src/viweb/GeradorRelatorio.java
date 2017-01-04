package viweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.BaseArquivo;
import modelo.Coluna;
import modelo.Item;
import util.FormatadorValores;

public class GeradorRelatorio extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {

	StringBuffer outHTML;
	
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
//		if(request.getSession().getAttribute("usuario") != null){
			// a ideia eh pegar a configuracao desse usuario e gerar o relatorio dos itens q ele ta visualizando
			// adicionar tb opcao de escolher as colunas
		
			ArrayList<Item> itens = BaseArquivo.getInstance().itens;
			ArrayList<Coluna> cols = BaseArquivo.getInstance().getColunas();
			outHTML = new StringBuffer();
			
			this.gerarCabecalhoHTML();
			this.gerarTableHeader(cols);
			this.gerarTableData(itens, cols);
			this.gerarRodapeHTML();
			
			PrintWriter out = response.getWriter();
			out.println(outHTML.toString());
			out.close();
//		}
	}

	private void gerarTableData(ArrayList<Item> itens, ArrayList<Coluna> cols) {
		 for(Item item : itens){
			 outHTML.append("<tr>");
			 for(Coluna coluna : cols){
				 outHTML.append("<td>"+ FormatadorValores.formataValorColuna( coluna.getTipo(), item.getValorColuna(coluna.getNome()) ) +"</td>"); 
			 }
			 outHTML.append("</tr>");
		 }
		outHTML.append("</table>");
	}

	private void gerarTableHeader(ArrayList<Coluna> cols) {
		outHTML.append("<table border=\"1\" align=\"center\">");
		outHTML.append("<thead>");
			outHTML.append("<tr>");
			for(Coluna coluna : cols)
				outHTML.append("<th>"+ coluna.getNome() +"</th>");
			outHTML.append("</tr>");
		outHTML.append("</thead>");
	}

	private void gerarCabecalhoHTML() {
		outHTML.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		outHTML.append("<html>");
		outHTML.append("<head>");
			outHTML.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
			outHTML.append("<title>VIWeb :: Relatório</title>");
			outHTML.append("<link rel=\"stylesheet\" href=\"estiloRelatorio.css\" />");
		outHTML.append("</head>");
		outHTML.append("<body>");
		outHTML.append("<h2>Relatório: Carros 85</h2>");
	}

	private void gerarRodapeHTML() {
		outHTML.append("</body>");
		outHTML.append("</html>");
	}

}