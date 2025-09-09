<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agendar Serviço - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Agendar Novo Serviço</h1>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>

        <form:form action="${pageContext.request.contextPath}/cliente/agendar" method="post" modelAttribute="agendamento">
            <div class="form-group">
                <label for="servicoId">Serviço:</label>
                <form:select path="servico.id" id="servicoId" class="form-control" required="true">
                    <option value="">Selecione um serviço</option>
                    <c:forEach var="servico" items="${servicos}">
                        <option value="${servico.id}">${servico.nome} (R$ ${servico.preco}) - ${servico.duracaoMinutos} min</option>
                    </c:forEach>
                </form:select>
            </div>
            
            <div class="form-group">
                <label for="profissionalId">Profissional:</label>
                <form:select path="profissional.id" id="profissionalId" class="form-control" required="true">
                    <option value="">Selecione um profissional</option>
                    <%-- Opções serão carregadas via AJAX --%>
                </form:select>
            </div>
            
            <div class="form-group">
                <label for="dataAgendamento">Data:</label>
                <input type="date" id="dataAgendamento" name="dataAgendamentoStr" class="form-control" required="true">
            </div>
            
            <div class="form-group">
                <label for="horaAgendamento">Hora:</label>
                <form:select path="horaAgendamento" id="horaAgendamento" name="horaAgendamentoStr" class="form-control" required="true">
                    <option value="">Selecione um horário</option>
                    <%-- Opções serão carregadas via AJAX --%>
                </form:select>
            </div>
            
            <div class="form-group">
                <label for="observacoes">Observações (opcional):</label>
                <form:textarea path="observacoes" id="observacoes" class="form-control" rows="3"/>
            </div>
            
            <button type="submit" class="btn btn-primary">Agendar</button>
        </form:form>
        
        <p class="mt-20 text-center"><a href="${pageContext.request.contextPath}/cliente/dashboard" class="btn btn-secondary">Voltar para o Dashboard</a></p>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const servicoSelect = document.getElementById("servicoId");
            const profissionalSelect = document.getElementById("profissionalId");
            const dataInput = document.getElementById("dataAgendamento");
            const horaSelect = document.getElementById("horaAgendamento");

            // Função para carregar profissionais com base no serviço selecionado
            servicoSelect.addEventListener("change", function() {
                const servicoId = this.value;
                profissionalSelect.innerHTML = '<option value="">Carregando profissionais...</option>';
                profissionalSelect.disabled = true;
                horaSelect.innerHTML = '<option value="">Selecione um horário</option>';
                horaSelect.disabled = true;

                if (servicoId) {
                    fetch('${pageContext.request.contextPath}/cliente/api/profissionais-por-servico?servicoId=' + servicoId)
                        .then(response => response.json())
                        .then(data => {
                            profissionalSelect.innerHTML = '<option value="">Selecione um profissional</option>';
                            data.forEach(prof => {
                                const option = document.createElement('option');
                                option.value = prof.id;
                                option.textContent = prof.usuario.nomeCompleto + (prof.especialidade ? ' (' + prof.especialidade + ')' : '');
                                profissionalSelect.appendChild(option);
                            });
                            profissionalSelect.disabled = false;
                        })
                        .catch(error => {
                            console.error('Erro ao carregar profissionais:', error);
                            profissionalSelect.innerHTML = '<option value="">Erro ao carregar</option>';
                        });
                } else {
                    profissionalSelect.innerHTML = '<option value="">Selecione um profissional</option>';
                    profissionalSelect.disabled = false;
                }
            });

            // Função para carregar horários disponíveis com base no profissional e data
            function loadHorariosDisponiveis() {
                const profissionalId = profissionalSelect.value;
                const data = dataInput.value;
                horaSelect.innerHTML = '<option value="">Carregando horários...</option>';
                horaSelect.disabled = true;

                if (profissionalId && data) {
                    fetch('${pageContext.request.contextPath}/cliente/api/horarios-disponiveis?profissionalId=' + profissionalId + '&data=' + data)
                        .then(response => response.json())
                        .then(data => {
                            horaSelect.innerHTML = '<option value="">Selecione um horário</option>';
                            if (data.length > 0) {
                                data.forEach(hora => {
                                    const option = document.createElement('option');
                                    option.value = hora;
                                    option.textContent = hora.substring(0, 5); // Formata HH:MM
                                    horaSelect.appendChild(option);
                                });
                            } else {
                                horaSelect.innerHTML = '<option value="">Nenhum horário disponível</option>';
                            }
                            horaSelect.disabled = false;
                        })
                        .catch(error => {
                            console.error('Erro ao carregar horários:', error);
                            horaSelect.innerHTML = '<option value="">Erro ao carregar</option>';
                        });
                } else {
                    horaSelect.innerHTML = '<option value="">Selecione um horário</option>';
                    horaSelect.disabled = false;
                }
            }

            profissionalSelect.addEventListener("change", loadHorariosDisponiveis);
            dataInput.addEventListener("change", loadHorariosDisponiveis);
            
            // Define a data mínima para hoje
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0'); // Months start at 0!
            const dd = String(today.getDate()).padStart(2, '0');
            dataInput.min = `${yyyy}-${mm}-${dd}`;
        });
    </script>
</body>
</html>

