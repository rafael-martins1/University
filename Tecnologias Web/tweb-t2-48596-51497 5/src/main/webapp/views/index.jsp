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
    <div class="event-block">
        <h1>Eventos a decorrer</h1>
        <c:forEach var="ongoingEvent" items="${ongoingEvents}">
            <div class="event">
                <h2>${ongoingEvent.eventName}</h2>
                <p>Data: ${ongoingEvent.eventDate}</p>
                <p>Descrição: ${ongoingEvent.eventDescription}</p>
                <p>Valor de Participação: ${ongoingEvent.joinValue} €</p>
            </div>
        </c:forEach>

        <div class="pagination">
            <c:url var="prevUrlOngoing" value="?pageOngoing=${pageOngoing - 1}&sizeOngoing=${size}" />
            <c:url var="nextUrlOngoing" value="?pageOngoing=${pageOngoing + 1}&sizeOngoing=${size}" />

            <c:choose>
                <c:when test="${not empty ongoingEvents && ongoingEvents.size() == sizeOngoing}">
                    <a href="${nextUrlOngoing}">Próxima</a>
                </c:when>
                <c:when test="${empty ongoingEvents}">
                    <p>Não existem mais eventos.</p>
                </c:when>
            </c:choose>

            <c:if test="${pageOngoing > 0}">
                <a href="${prevUrlOngoing}">Anterior</a>
            </c:if>

        </div>


        <h1 style="text-align:center">Eventos Futuros</h1>

        <c:forEach var="futureEvent" items="${futureEvents}">
            <div class="event">
                <h2>${futureEvent.eventName}</h2>
                <p>Data: ${futureEvent.eventDate}</p>
                <p>Descrição: ${futureEvent.eventDescription}</p>
                <p>Valor de Participação: ${futureEvent.joinValue} €</p>
            </div>
        </c:forEach>

        <div class="pagination">
            <c:url var="prevUrlFuture" value="?pageFuture=${pageFuture - 1}&sizeFuture=${size}" />
            <c:url var="nextUrlFuture" value="?pageFuture=${pageFuture + 1}&sizeFuture=${size}" />

            <c:choose>
                <c:when test="${not empty futureEvents && futureEvents.size() == sizeFuture}">
                    <a href="${nextUrlFuture}">Próxima</a>
                </c:when>
                <c:when test="${empty futureEvents}">
                    <p>Não existem mais eventos.</p>
                </c:when>
            </c:choose>

            <c:if test="${pageFuture > 0}">
                <a href="${prevUrlFuture}">Anterior</a>
            </c:if>
        </div>

        <h1 style="text-align:center">Eventos Realizados</h1>

        <c:forEach var="pastEvent" items="${pastEvents}">
            <div class="event">
                <h2>${pastEvent.eventName}</h2>
                <p>Data: ${pastEvent.eventDate}</p>
                <p>Descrição: ${pastEvent.eventDescription}</p>
                <p>Valor de Participação: ${pastEvent.joinValue} €</p>
            </div>
        </c:forEach>

        <div class="pagination">
            <c:url var="prevUrlPast" value="?pagePast=${pagePast - 1}&sizePast=${size}" />
            <c:url var="nextUrlPast" value="?pagePast=${pagePast + 1}&sizePast=${size}" />

            <c:if test="${pagePast > 0}">
                <a href="${prevUrlPast}">Anterior</a>
            </c:if>

            <c:if test="${not empty pastEvents && pastEvents.size() == sizePast}">
                <a href="${nextUrlPast}">Próxima</a>
            </c:if>

            <c:if test="${empty pastEvents}">
                <p>Não existem mais eventos.</p>
            </c:if>
        </div>
    </div>

<footer>
	<section>
		<h3>Patrocinadores</h3>
	</section>
	<section>
		<h3>Contactos</h3>
		<p>Email: <a href="mailto:nomeEmpresa@hotmail.com">nomeEmpresa@hotmail.com</a></p>
		<p>Telefone:<a> 932399232</a></p>
	</section>
	<section>
		<h3>Sobre a Empresa</h3>
		<p>Créditos da aplicação web:</p>
		<p><a href="https://github.com/rafael-martins1">Rafael Martins</a></p>
		<p>João Sousa</p>
	</section>
</footer>
</body>
</html>
