<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Run Hub</title>
	<link rel="icon" type="image/png" sizes="180x180" href="../static/images/favicon.png">
	<link rel="stylesheet" type="text/css"
		  href="<c:url value="../static/css/style.css"/>" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="../static/js/main.js"></script>
</head>
<body>

<c:if test="${empty value}">
	<% response.sendRedirect("acessoReservado"); %>
</c:if>

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
	<div id="pagar"></div>
	    <div class="infoPagamento">

            <form id="pagar_tarde" action="${pageContext.request.contextPath}/registerInfoPayment" method="POST">
                <input style="display:none;" type="text "id="entidade" name="entity">
                <input style="display:none;" type="text "id="referencia" name="reference">
                <input style="display:none;" type="text "id="valor" name="value">
                <input style="display:none;" type="text "id="evento" name="event_id">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>

        </div>
	</div>
    <script>
        getMbReference(`${value}`, `${event_id}`)
    </script>
</body>
</html>