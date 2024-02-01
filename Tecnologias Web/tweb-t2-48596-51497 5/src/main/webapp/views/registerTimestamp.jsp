<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registar Tempo</title>
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
        <a href="${pageContext.request.contextPath}/registerEvent">REGISTAR EVENTO</a>
        <a href="${pageContext.request.contextPath}/registerTimestamp">REGISTAR TEMPO</a>
        <p class="logout">${pageContext.request.userPrincipal.name} | <a href="<c:url value='logout'/>">LOGOUT</a></p>
    </nav>
</header>
    <div class="titles">
        <h1 style="text-align: center">Registar Tempo</h1>
    </div>
    <form class="acessoRForm" action="${pageContext.request.contextPath}/registerTimestamp" method="POST">
        <label for="nomeEvento">Nome do Evento:</label>
        <input type="text" id="nomeEvento" name="event_name" required><br>

        <label for="localProva">Local da Prova:</label>
        <select id="localProva" name="race_local" required>
            <option value="start">Start</option>
            <option value="P1">P1</option>
            <option value="P2">P2</option>
            <option value="P3">P3</option>
            <option value="finish">Finish</option>
        </select><br>

        <label for="numeroParticipante">Username do Participante:</label>
        <input type="text" id="numeroParticipante" name="participant_username" required><br>

        <label for="timestamp">Timestamp:</label>
        <input type="time" id="timestamp" name="time_timestamp" required><br>

        <input type="submit" value="Registar Tempo">

        <input type="hidden" name="${_csrf.parameterName}"
               value="${_csrf.token}" />
    </form>

    <c:if test="${not empty timeError}">
        <div class="error">${timeError}</div>
    </c:if>
    <c:if test="${not empty timeSuccess}">
        <div class="successMessage">${timeSuccess}</div>
    </c:if>

</body>
</html>