<%@ page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <title>${eventName}</title>
    <link rel="stylesheet" type="text/css"
          href="<c:url value="../static/css/style.css"/>" />
    <link rel="icon" type="image/png" sizes="180x180" href="../static/images/favicon.png">
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
<div class="titles">
<h1>${eventName}</h1>
<h3>Lista de Participantes</h3>
</div>

<div class="participants">
<table>
    <thead>
    <tr>
        <th>Username</th>
        <th>Nome</th>
        <th>Tipo</th>
        <th>Género</th>
        <th>Pago?</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="participant" items="${participantes}" varStatus="loop">
        <tr>
            <td>${participant.participantUsername}</td>
            <td>${participant.participantRealName}</td>
            <td>${participant.participantType}</td>
            <td>${participant.participantGender}</td>
            <td>${participant.isPaid ? 'Sim' : 'Não'}</td>
            <c:if test="${tempos[loop.index].participantSituation == 'start'}">
                <td>${tempos[loop.index].participantSituation}: ${tempos[loop.index].timestamp}</td>
            </c:if>

            <c:if test="${tempos[loop.index].participantSituation != null && tempos[loop.index].participantSituation != 'start'}">
                <td>${tempos[loop.index].participantSituation}: ${tempos[loop.index].time} minutos</td>
            </c:if>

            <c:if test="${tempos[loop.index].participantSituation == null}">
                <td>Sem tempos registados</td>
            </c:if>

            <td>
                <div id="${loop.index + 1}"></div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

    <div class="times">
        <h3 class="titles">Classificação Geral</h3>
        <label for="filterByName">Filtrar por Nome:</label>
        <input type="text" id="filterByName" oninput="filterTable()" placeholder="Digite um username...">

        <label for="filterByType">Filtrar por Escalão:</label>
        <select id="filterByType" onchange="filterTable()">
            <option value="júnior">Júnior</option>
            <option value="sénior">Sénior</option>
            <option value="vet35">Vet35</option>
            <option value="vet50">Vet50</option>
            <option value="vet65">Vet65</option>
            <option value="">Todos</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category}">${category}</option>
            </c:forEach>
        </select>

        <table>
            <thead>
            <tr>
                <th>Posição</th>
                <th>Username</th>
                <th>Tempo</th>
                <th>Tipo</th>
                <th>Género</th>
            </tr>
            </thead>
            <tbody id="body">
            <c:forEach var="participant" items="${times}" varStatus="loop">
                <tr>
                    <td>${loop.index + 1}º</td>
                    <td>${participant.participantUsername}</td>
                    <td>${participant.time} minutos</td>
                    <td>${participant.participantType}</td>
                    <td>${participant.participantGender}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>