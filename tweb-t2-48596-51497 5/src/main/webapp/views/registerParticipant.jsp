<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registar Atleta</title>
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
        <a href="${pageContext.request.contextPath}/registerParticipant">INSCRIÇÃO</a>
        <a href="${pageContext.request.contextPath}/checkRegistrations">CONSULTAR INSCRIÇÕES</a>
        <p class="logout">${pageContext.request.userPrincipal.name} | <a href="<c:url value='logout'/>">LOGOUT</a></p>
    </nav>
</header>
    <div class="titles">
        <h1 style="text-align: center">Registar numa Prova</h1>
    </div>
    <form class="acessoRForm" action="${pageContext.request.contextPath}/registerParticipant" method="POST">
        <label for="participant_real_name">Nome e Apelido:</label>
        <input type="text" id="participant_real_name" name="participant_real_name" required><br>

        <label for="participant_gender">Género:</label>
        <select id="participant_gender" name="participant_gender" required>
            <option value="M">Masculino</option>
            <option value="F">Feminino</option>
        </select><br>

        <label for="participant_type">Escalão:</label>
        <select id="participant_type" name="participant_type" required>
            <option value="júnior">Júnior</option>
            <option value="sénior">Sénior</option>
            <option value="vet35">Vet35</option>
            <option value="vet50">Vet50</option>
            <option value="vet65">Vet65</option>
        </select><br>

        <label for="nome_evento">Nome do Evento:</label>
        <input type="text" id="nome_evento" name="event_name" required><br>

        <input type="submit" value="Correr">

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="successMessage">${successMessage}</div>
    </c:if>

</body>
</html>