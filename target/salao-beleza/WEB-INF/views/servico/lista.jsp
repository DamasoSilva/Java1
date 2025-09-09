<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nossos Serviços - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Nossos Serviços</h1>
        <p class="text-center">Conheça a variedade de serviços que oferecemos para você.</p>

        <div class="card mt-20">
            <div class="card-header">
                <h2 class="card-title">Serviços Disponíveis</h2>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty servicos}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Nome</th>
                                    <th>Descrição</th>
                                    <th>Preço</th>
                                    <th>Duração</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="servico" items="${servicos}">
                                    <tr>
                                        <td>${servico.nome}</td>
                                        <td>${servico.descricao}</td>
                                        <td>R$ <c:out value="${servico.preco}" /></td>
                                        <td>${servico.duracaoMinutos} minutos</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">Nenhum serviço cadastrado no momento.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <p class="mt-20 text-center"><a href="${pageContext.request.contextPath}/" class="btn btn-primary">Voltar para a Home</a></p>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>

