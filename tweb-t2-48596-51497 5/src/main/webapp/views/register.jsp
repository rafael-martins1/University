<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <title>Registar</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="../static/css/style.css"/>" />
    <link rel="icon" type="image/png" sizes="180x180" href="../static/images/favicon.png">
</head>
<body>

<header>
    <a href="${pageContext.request.contextPath}/"><img src="../static/images/logotipo.png" alt="logo"></a>

    <nav>
        <a href="${pageContext.request.contextPath}/searchEvents">PESQUISAR EVENTOS</a>
        <a href="${pageContext.request.contextPath}/acessoReservado">ACESSO RESERVADO</a>
        <a id="login" href="${pageContext.request.contextPath}/login">LOGIN/SIGN-IN</a>

    </nav>
</header>

    <h2 class="titles">Registe-se</h2>
    <div id="register-box">
        <form class="loginRegister" name="registerForm" action="/register" method="POST">
            <table>
                <tr>
                    <td>Username:</td>
                    <td><input type="text" name="username" required></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" required></td>
                </tr>
                <tr>
                    <td>Role:</td>
                    <td>
                        <div class="role_input">
                            <label><input type="radio" name="role" value="ROLE_ATLETA" required>Atleta</label>
                            <label><input type="radio" name="role" value="ROLE_STAFF" required>Staff</label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><input name="submit" type="submit" value="Register" /></td>
                </tr>
            </table>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
        <p>Já possui conta? <a href="${pageContext.request.contextPath}/login">Faça login</a></p>
        <c:if test="${not empty successMessage}">
            <div class="successMessage">${successMessage}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </div>

</body>
</html>
