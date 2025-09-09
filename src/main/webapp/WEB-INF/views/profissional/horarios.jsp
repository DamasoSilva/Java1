<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Horários - Profissional - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Gerenciar Horários</h1>
        <p class="text-center">Aqui você pode adicionar e gerenciar seus horários disponíveis para agendamentos.</p>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>

        <div class="card mt-20">
            <div class="card-header">
                <h2 class="card-title">Adicionar Novo Horário</h2>
            </div>
            <div class="card-body">
                <form:form action="${pageContext.request.contextPath}/profissional/horarios/adicionar" method="post" modelAttribute="novoHorario">
                    <div class="form-group">
                        <label for="data">Data:</label>
                        <input type="date" id="data" name="dataStr" class="form-control" required="true">
                    </div>
                    <div class="form-group">
                        <label for="horaInicio">Hora de Início:</label>
                        <input type="time" id="horaInicio" name="horaInicioStr" class="form-control" required="true">
                    </div>
                    <div class="form-group">
                        <label for="horaFim">Hora de Fim:</label>
                        <input type="time" id="horaFim" name="horaFimStr" class="form-control" required="true">
                    </div>
                    <button type="submit" class="btn btn-primary">Adicionar Horário</button>
                </form:form>
            </div>
        </div>

        <div class="card mt-20">
            <div class="card-header">
                <h2 class="card-title">Meus Horários Disponíveis</h2>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty horarios}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Data</th>
                                    <th>Hora Início</th>
                                    <th>Hora Fim</th>
                                    <th>Status</th>
                                    <th>Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="horario" items="${horarios}">
                                    <tr>
                                        <td><fmt:formatDate value="${horario.data}" pattern="dd/MM/yyyy"/></td>
                                        <td><fmt:formatTime value="${horario.horaInicio}" pattern="HH:mm"/></td>
                                        <td><fmt:formatTime value="${horario.horaFim}" pattern="HH:mm"/></td>
                                        <td>
                                            <span class="badge 
                                                <c:choose>
                                                    <c:when test="${horario.disponivel}">badge-success</c:when>
                                                    <c:otherwise>badge-danger</c:otherwise>
                                                </c:choose>
                                            ">
                                                <c:if test="${horario.disponivel}">Disponível</c:if>
                                                <c:if test="${!horario.disponivel}">Indisponível</c:if>
                                            </span>
                                        </td>
                                        <td>
                                            <c:if test="${horario.disponivel}">
                                                <form action="${pageContext.request.contextPath}/profissional/horarios/marcar-indisponivel/${horario.id}" method="post" style="display:inline;" onsubmit="return confirm(\'Marcar este horário como indisponível?\');">
                                                    <button type="submit" class="btn btn-warning btn-sm">Indisponível</button>
                                                </form>
                                            </c:if>
                                            <c:if test="${!horario.disponivel}">
                                                <form action="${pageContext.request.contextPath}/profissional/horarios/marcar-disponivel/${horario.id}" method="post" style="display:inline;" onsubmit="return confirm(\'Marcar este horário como disponível?\');">
                                                    <button type="submit" class="btn btn-success btn-sm">Disponível</button>
                                                </form>
                                            </c:if>
                                            <form action="${pageContext.request.contextPath}/profissional/horarios/remover/${horario.id}" method="post" style="display:inline;" onsubmit="return confirm(\'Tem certeza que deseja remover este horário?\');">
                                                <button type="submit" class="btn btn-danger btn-sm">Remover</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">Nenhum horário cadastrado no momento.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <p class="mt-20 text-center"><a href="${pageContext.request.contextPath}/profissional/dashboard" class="btn btn-primary">Voltar para o Dashboard</a></p>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const dataInput = document.getElementById("data");
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const dd = String(today.getDate()).padStart(2, '0');
            dataInput.min = `${yyyy}-${mm}-${dd}`;
        });
    </script>
</body>
</html>

