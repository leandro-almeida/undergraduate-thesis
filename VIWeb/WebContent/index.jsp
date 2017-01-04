<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>VIWeb :: Login</title>
	<link rel="stylesheet" href="estilo.css" />
</head>
<body>
<h2>VIWeb</h2>
<%	
	String msg = (String)request.getAttribute("msg");
	if(msg != null){
%>
<p class="msg"><%=msg%></p>
<%  } %>

<div id="forms" style="width: 340px;">
	<form name="formLogin" method="post" action="ServletLogon">
		<input type="hidden" name="negocio" value="UsuarioLogar" />
		<fieldset>
			<legend>Login</legend>
			<label for="login">Usuário</label>
			<input type="text" name="login" />
			
			<br />
			
			<label for="senha">Senha</label>
			<input type="password" name="senha" />
			
			<br /><br />
			<center><input type="submit" value="Enviar" /></center>
		</fieldset>
	</form>
</div>
</body>
</html>