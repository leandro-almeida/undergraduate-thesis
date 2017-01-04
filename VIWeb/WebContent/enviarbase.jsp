<%if(session.getAttribute("usuario") != null){%>
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
<h2>Enviar Base de Dados</h2>

<form name="enviarBase" method="post" action="ServletEnviarBase" enctype="application/x-www-form-urlencoded">
<table border="0">
	<tr>
		<td>Arquivo Texto:</td>
		<td><input type="file" name="arquivo"/></td>
		<td><input type="submit" value="Enviar"/></td>
	</tr>
</table>
</form>

<p><a href="viweb.jsp">Voltar</a></p>

</body>
</html>
<%}else{
	response.sendRedirect("index.jsp");
}
%>