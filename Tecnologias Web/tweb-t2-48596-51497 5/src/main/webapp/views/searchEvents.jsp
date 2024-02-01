<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pesquisar Eventos</title>
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
        	<p id="logout">${pageContext.request.userPrincipal.name} | <a href="<c:url value='logout'/>">LOGOUT</a></p>
        </c:if>

    </nav>
</header>

    <div class="titles">
        <h1>Pesquisar Eventos</h1>
    </div>
    <form class="acessoRForm" action="${pageContext.request.contextPath}/searchEvents" method="POST">
        <label for="searchType">Tipo de pesquisa:</label>
        <select id="searchType" name="searchType" required>
            <option value="nome">Nome</option>
            <option value="data">Data</option>
        </select><br>
        <label for="pesquisa">Escreva a informação correspondente:</label>
        <input type="text" id="pesquisa" name="input" placeholder="Pesquise..." required>
        <input type="submit" value="Pesquisar">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <h3 id="show">Eventos Encontrados</h3>
    <div id="listaPesq">
    </div>
    <div id="paginas">
    </div>

     <script>
        var lista_eventos='${lista_eventos}';
        listar_pesquisa(1);
     </script>

</body>
</html>