<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Registrar Nova Conta</h1>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>

        <form:form action="${pageContext.request.contextPath}/register" method="post" modelAttribute="cliente">
            <div class="form-group">
                <label for="nome">Nome:</label>
                <form:input path="usuario.nome" type="text" id="nome" class="form-control" required="true"/>
                <form:errors path="usuario.nome" cssClass="text-danger"/>
            </div>
            <div class="form-group">
                <label for="sobrenome">Sobrenome:</label>
                <form:input path="usuario.sobrenome" type="text" id="sobrenome" class="form-control"/>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <form:input path="usuario.email" type="email" id="email" class="form-control" required="true"/>
                <form:errors path="usuario.email" cssClass="text-danger"/>
            </div>
            <div class="form-group">
                <label for="telefone">Telefone:</label>
                <form:input path="usuario.telefone" type="tel" id="telefone" class="form-control"/>
            </div>
            <div class="form-group">
                <label for="senha">Senha:</label>
                <form:password path="usuario.senha" id="senha" class="form-control" required="true"/>
                <form:errors path="usuario.senha" cssClass="text-danger"/>
            </div>
            <div class="form-group">
                <label for="confirmSenha">Confirmar Senha:</label>
                <input type="password" id="confirmSenha" name="confirmPassword" class="form-control" required="true"/>
            </div>
            <button type="submit" class="btn btn-primary">Registrar</button>
        </form:form>
        
        <p class="mt-20 text-center">Já tem uma conta? <a href="${pageContext.request.contextPath}/login">Faça login aqui</a></p>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>

