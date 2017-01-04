<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>VIWeb</title>
	<link rel="stylesheet" href="estilo.css" />
</head>
<body>
<jsp:useBean id="usuario" class="entidades.Usuario" scope="session"></jsp:useBean>
<h2>VIWeb</h2>
<p>Bem-vindo <i>${usuario.nome}</i>!</p>
<p>Você deseja:</p>
<ul>
	<li><a href="carregarbase.jsp">Carregar base de dados</a></li>
	<li><a href="enviarbase.jsp">Enviar e carregar base de dados em texto</a></li>
	<li><a href="sair.jsp">Sair</a></li>
</ul>
</body>
</html>