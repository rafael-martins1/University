<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Inscrições</title>
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

    <div id="inscricoes" style="display:none">
        <div id="pagarInfo">
        </div>
    </div>
    <div id="paginas">
    </div>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
     <script>
        var lista_eventos='${lista_eventos}';
        listar(1);
        var user='${pageContext.request.userPrincipal.name}';
        var token="${_csrf.token}";
    </script>

</body>
</html>