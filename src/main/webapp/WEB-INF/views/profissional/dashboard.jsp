<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard do Profissional - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Bem-vindo, ${profissional.usuario.nome}!</h1>
        <p>Este é o seu painel de controle. Aqui você pode gerenciar seus agendamentos e horários.</p>

        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/profissional/meus-agendamentos">Meus Agendamentos</a></li>
                <li><a href="${pageContext.request.contextPath}/profissional/horarios">Gerenciar Horários</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Sair</a></li>
            </ul>
        </nav>

        <div class="row mt-20">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h2 class="card-title">Agendamentos para Hoje</h2>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty agendamentosHoje}">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Hora</th>
                                            <th>Serviço</th>
                                            <th>Cliente</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="agendamento" items="${agendamentosHoje}">
                                            <tr>
                                                <td><fmt:formatTime value="${agendamento.horaAgendamento}" pattern="HH:mm"/></td>
                                                <td>${agendamento.servico.nome}</td>
                                                <td>${agendamento.cliente.usuario.nomeCompleto}</td>
                                                <td><span class="badge badge-info">${agendamento.status.descricao}</span></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <p class="text-center">Nenhum agendamento para hoje.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h2 class="card-title">Estatísticas Rápidas</h2>
                    </div>
                    <div class="card-body">
                        <p>Agendamentos Pendentes: <span class="badge badge-warning">${totalAgendamentosPendentes}</span></p>
                        <p>Agendamentos Confirmados: <span class="badge badge-success">${totalAgendamentosConfirmados}</span></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>

