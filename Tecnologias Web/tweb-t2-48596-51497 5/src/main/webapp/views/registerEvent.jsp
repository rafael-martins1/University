<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registar Evento</title>
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
        <h1 style="text-align:center">Adicionar Evento</h1>
    </div>
    <form class="acessoRForm" action="${pageContext.request.contextPath}/registerEvent" method="POST">
        <label for="event_name">Nome do Evento:</label>
        <input type="text" id="event_name" name="event_name" required><br>

        <label for="event_description">Descrição do Evento:</label>
        <textarea id="event_description" name="event_description" required></textarea><br>

        <label for="event_date">Data do Evento:</label>
        <input type="date" id="event_date" name="event_date" required><br>

        <label for="join_value">Valor da Inscrição:</label>
        <input type="number" step=".01" id="join_value" name="join_value" required><br>

        <input type="submit" value="Registar Evento">

        <input type="hidden" name="${_csrf.parameterName}"
               value="${_csrf.token}" />
    </form>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <c:if test="${not empty successMessage}">
        <div class="successMessage">${successMessage}</div>
    </c:if>

</body>
</html>