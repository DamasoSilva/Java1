<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Erro interno - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Erro interno do servidor</h1>
        <p class="text-center">Ocorreu um erro interno no servidor. Tente novamente mais tarde.</p>
        
        <div class="alert alert-danger">
            <p>Erro 500 - Erro interno do servidor</p>
        </div>
        
        <p class="mt-20 text-center">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Voltar para a Home</a>
        </p>
    </div>
</body>
</html>

