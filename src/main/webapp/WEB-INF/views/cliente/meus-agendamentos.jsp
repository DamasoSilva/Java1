<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Meus Agendamentos - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Meus Agendamentos</h1>
        <p class="text-center">Aqui você pode visualizar e gerenciar seus agendamentos.</p>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>

        <div class="card mt-20">
            <div class="card-header">
                <h2 class="card-title">Lista de Agendamentos</h2>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty agendamentos}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Serviço</th>
                                    <th>Profissional</th>
                                    <th>Data</th>
                                    <th>Hora</th>
                                    <th>Duração</th>
                                    <th>Status</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="agendamento" items="${agendamentos}">
                                    <tr>
                                        <td>${agendamento.servico.nome}</td>
                                        <td>${agendamento.profissional.usuario.nomeCompleto}</td>
                                        <td><fmt:formatDate value="${agendamento.dataAgendamento}" pattern="dd/MM/yyyy"/></td>
                                        <td><fmt:formatTime value="${agendamento.horaAgendamento}" pattern="HH:mm"/></td>
                                        <td>${agendamento.duracaoMinutos} min</td>
                                        <td>
                                            <span class="badge 
                                                <c:choose>
                                                    <c:when test="${agendamento.status == 'PENDENTE'}">badge-warning</c:when>
                                                    <c:when test="${agendamento.status == 'CONFIRMADO'}">badge-success</c:when>
                                                    <c:when test="${agendamento.status == 'CANCELADO'}">badge-danger</c:when>
                                                    <c:otherwise>badge-info</c:otherwise>
                                                </c:choose>
                                            ">
                                                ${agendamento.status.descricao}
                                            </span>
                                        </td>
                                        <td>
                                            <c:if test="${agendamento.status == 'PENDENTE' || agendamento.status == 'CONFIRMADO'}">
                                                <form action="${pageContext.request.contextPath}/cliente/agendamento/cancelar/${agendamento.id}" method="post" style="display:inline;" onsubmit="return confirm('Tem certeza que deseja cancelar este agendamento?');">
                                                    <button type="submit" class="btn btn-danger btn-sm">Cancelar</button>
                                                </form>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">Você não possui agendamentos.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <p class="mt-20 text-center"><a href="${pageContext.request.contextPath}/cliente/dashboard" class="btn btn-primary">Voltar para o Dashboard</a></p>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>

