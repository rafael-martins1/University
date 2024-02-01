<%@ page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<link rel="stylesheet" type="text/css"
	href="<c:url value="../static/css/style.css"/>" />
	<link rel="icon" type="image/png" sizes="180x180" href="../static/images/favicon.png">

</head>

<body>

<header>
	<a href="${pageContext.request.contextPath}/"><img src="../static/images/logotipo.png" alt="logo"></a>

	<nav>
		<a href="${pageContext.request.contextPath}/searchEvents">PESQUISAR EVENTOS</a>
		<a href="${pageContext.request.contextPath}/acessoReservado">ACESSO RESERVADO</a>
		<a href="${pageContext.request.contextPath}/login">LOGIN/SIGN-IN</a>

	</nav>
</header>

<h2 class="titles">Entre</h2>
	<div id="login-box">
		<form class="loginRegister" name='loginForm'
			action="<c:url value='j_spring_security_check' />" method='POST'>
			<table>
				<tr>
					<td>Username:</td>
					<td><input type='text' name='username' value=''></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='password' /></td>
				</tr>
				<tr>
					<td colspan='2'><input name="submit" type="submit"
						value="Entrar" /></td>
				</tr>
			</table>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
		<p>Ainda não tem conta? <a href="${pageContext.request.contextPath}/register">Registe-se</a></p>
		<c:if test="${not empty error}">
        	<div class="error">${error}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
        	<div class="successMessage">${successMessage}</div>
        </c:if>
	</div>
</body>
</html>