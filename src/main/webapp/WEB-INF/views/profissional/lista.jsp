<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nossos Profissionais - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Nossos Profissionais</h1>
        <p class="text-center">Conheça a nossa equipe de especialistas.</p>

        <div class="card mt-20">
            <div class="card-header">
                <h2 class="card-title">Profissionais Disponíveis</h2>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty profissionais}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Nome</th>
                                    <th>Especialidade</th>
                                    <th>Email</th>
                                    <th>Telefone</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="profissional" items="${profissionais}">
                                    <tr>
                                        <td>${profissional.usuario.nomeCompleto}</td>
                                        <td>${profissional.especialidade}</td>
                                        <td>${profissional.usuario.email}</td>
                                        <td>${profissional.usuario.telefone}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">Nenhum profissional cadastrado no momento.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <p class="mt-20 text-center"><a href="${pageContext.request.contextPath}/" class="btn btn-primary">Voltar para a Home</a></p>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>

