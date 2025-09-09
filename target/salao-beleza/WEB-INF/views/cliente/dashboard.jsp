<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard do Cliente - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Bem-vindo, ${cliente.usuario.nome}!</h1>
        <p>Este é o seu painel de controle. Aqui você pode ver seus próximos agendamentos e gerenciar suas informações.</p>

        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/cliente/agendar">Novo Agendamento</a></li>
                <li><a href="${pageContext.request.contextPath}/cliente/meus-agendamentos">Meus Agendamentos</a></li>
                <li><a href="${pageContext.request.contextPath}/servicos">Ver Serviços</a></li>
                <li><a href="${pageContext.request.contextPath}/profissionais">Ver Profissionais</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Sair</a></li>
            </ul>
        </nav>

        <div class="card mt-20">
            <div class="card-header">
                <h2 class="card-title">Próximos Agendamentos</h2>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty proximosAgendamentos}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Serviço</th>
                                    <th>Profissional</th>
                                    <th>Data</th>
                                    <th>Hora</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="agendamento" items="${proximosAgendamentos}">
                                    <tr>
                                        <td>${agendamento.servico.nome}</td>
                                        <td>${agendamento.profissional.usuario.nomeCompleto}</td>
                                        <td><fmt:formatDate value="${agendamento.dataAgendamento}" pattern="dd/MM/yyyy"/></td>
                                        <td><fmt:formatTime value="${agendamento.horaAgendamento}" pattern="HH:mm"/></td>
                                        <td><span class="badge badge-info">${agendamento.status.descricao}</span></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">Você não possui agendamentos futuros.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>

