<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Página não encontrada - Salão de Beleza</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Página não encontrada</h1>
        <p class="text-center">A página que você está procurando não foi encontrada.</p>
        
        <div class="alert alert-warning">
            <p>Erro 404 - Página não encontrada</p>
        </div>
        
        <p class="mt-20 text-center">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Voltar para a Home</a>
        </p>
    </div>
</body>
</html>

