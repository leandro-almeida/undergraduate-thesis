package viweb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UsuarioDAO;
import entidades.Usuario;

 public class ServletLogon extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		if(login == null || senha == null){
			request.setAttribute("msg", "Login ou senha inválidos.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}else if(login.isEmpty() || senha.isEmpty()){
			request.setAttribute("msg", "Login ou senha vazios.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}else{
			UsuarioDAO user = new UsuarioDAO();
			Usuario usuario = user.logon(login, senha);
			if(usuario == null){
				request.setAttribute("msg", "Login ou senha inválidos.");
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}else{
				request.getSession(true).setAttribute("usuario", usuario);
				request.getRequestDispatcher("viweb.jsp").forward(request, response);
			}
		}
	}
}