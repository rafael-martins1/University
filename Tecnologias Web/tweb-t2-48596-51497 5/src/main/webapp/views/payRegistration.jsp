<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Pagamento MB</title>
	<link rel="icon" type="image/png" sizes="180x180" href="../static/images/favicon.png">
	<link rel="stylesheet" type="text/css"
		  href="<c:url value="../static/css/style.css"/>" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="../static/js/main.js"></script>
</head>
<body>

<header>
	<a href="${pageContext.request.contextPath}/"><img src="../static/images/logotipo.png" alt="logo"></a>

	<nav>
		<a href="${pageContext.request.contextPath}/searchEvents">PESQUISAR EVENTOS</a>
		<a href="${pageContext.request.contextPath}/acessoReservado">ACESSO RESERVADO</a>
		<c:if test="${empty pageContext.request.userPrincipal}">
		    <a href="${pageContext.request.contextPath}/login">LOGIN/SIGN-IN</a>
		</c:if>
		<c:if test="${not empty pageContext.request.userPrincipal}">
		    <p class="logout">${pageContext.request.userPrincipal.name} | <a href="<c:url value='logout'/>">LOGOUT</a></p>
		</c:if>

	</nav>
</header>

	<div class="pagamento">
        <h1 align="center">Área de Pagamento</h1>
        <div id="pagar">
        </div>
        <h3>Pagamento MB</h3>
        <p>Entidade: ${entidade}</p>
        <p>Referência: ${referencia}</p>
        <p>Valor: ${valor}</p>
        <div class="infoPagamento">

            <form action="${pageContext.request.contextPath}/registerParticipantLate" method="POST">
                <input class="paymentButton" type="submit" value="Pagar mais tarde">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>

            <form action="${pageContext.request.contextPath}/pay" method="POST">
                <input style="display:none;" type="text "id="evento_pagar" name="event_id_pagar" value="${event_id_pagar}">
                <input class="paymentButton" type="submit" value="Pagar">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>

        </div>
    </div>
</body>
</html>