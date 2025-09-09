<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bem-vindo ao Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Bem-vindo ao Salão de Beleza!</h1>
        <p>Seu destino para beleza e bem-estar.</p>
        
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                <li><a href="${pageContext.request.contextPath}/register">Registrar</a></li>
                <li><a href="${pageContext.request.contextPath}/servicos">Nossos Serviços</a></li>
                <li><a href="${pageContext.request.contextPath}/profissionais">Nossos Profissionais</a></li>
            </ul>
        </nav>
        
        <div class="footer">
            <p>&copy; 2023 Salão de Beleza. Todos os direitos reservados.</p>
        </div>
    </div>
</body>
</html>

