# Sistema de Gerenciamento de Salão de Beleza

Uma aplicação web completa desenvolvida em Java para gerenciamento de salão de beleza, com funcionalidades de agendamento, gerenciamento de serviços, profissionais e clientes.

## 🚀 Funcionalidades

### Para Clientes
- ✅ Cadastro e autenticação
- ✅ Visualização de serviços e profissionais
- ✅ Agendamento de serviços online
- ✅ Gerenciamento de agendamentos
- ✅ Dashboard personalizado

### Para Profissionais
- ✅ Dashboard com agendamentos do dia
- ✅ Gerenciamento de horários disponíveis
- ✅ Confirmação e controle de agendamentos
- ✅ Estatísticas de agendamentos

### Para Administradores
- ✅ Acesso completo ao sistema
- ✅ Gerenciamento de usuários e serviços

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 8+**
- **Spring MVC** - Framework web
- **Spring Security** - Autenticação e autorização
- **Hibernate/JPA** - ORM para persistência
- **Maven** - Gerenciamento de dependências

### Frontend
- **JSP** - Páginas dinâmicas
- **JSTL** - Tag libraries
- **CSS3** - Estilização moderna e responsiva
- **JavaScript** - Interatividade e validações

### Banco de Dados
- **PostgreSQL** - Banco de dados principal
- **HikariCP** - Pool de conexões

### Servidor
- **Apache Tomcat 9+** - Servidor de aplicação

## 📋 Pré-requisitos

Antes de executar a aplicação, certifique-se de ter instalado:

- **Java JDK 8 ou superior**
- **Apache Maven 3.6+**
- **PostgreSQL 12+**
- **Apache Tomcat 9+**
- **IntelliJ IDEA** (recomendado) ou outra IDE Java

## 🔧 Configuração e Instalação

### 1. Configuração do PostgreSQL

#### Windows 11

1. **Baixar e Instalar PostgreSQL:**
   - Acesse: https://www.postgresql.org/download/windows/
   - Baixe a versão mais recente
   - Execute o instalador e siga as instruções
   - Anote a senha do usuário `postgres`

2. **Configurar Banco de Dados:**
   ```bash
   # Abra o SQL Shell (psql) ou pgAdmin
   
   # Criar usuário
   CREATE USER salao_user WITH PASSWORD 'salao_password';
   
   # Criar banco de dados
   CREATE DATABASE salao_beleza OWNER salao_user;
   
   # Conceder privilégios
   GRANT ALL PRIVILEGES ON DATABASE salao_beleza TO salao_user;
   ```

3. **Executar Script de Criação:**
   ```bash
   # No diretório do projeto
   psql -U salao_user -d salao_beleza -f db_schema.sql
   ```

### 2. Configuração do Apache Maven

#### Windows 11

1. **Baixar Maven:**
   - Acesse: https://maven.apache.org/download.cgi
   - Baixe o arquivo `apache-maven-x.x.x-bin.zip`

2. **Instalar Maven:**
   - Extraia o arquivo para `C:\apache-maven-x.x.x`
   - Adicione `C:\apache-maven-x.x.x\bin` ao PATH do sistema

3. **Verificar Instalação:**
   ```bash
   mvn --version
   ```

### 3. Configuração do Apache Tomcat

#### Windows 11

1. **Baixar Tomcat:**
   - Acesse: https://tomcat.apache.org/download-90.cgi
   - Baixe o arquivo ZIP da versão 9.x

2. **Instalar Tomcat:**
   - Extraia para `C:\apache-tomcat-9.x.x`
   - Configure variável de ambiente `CATALINA_HOME`

3. **Configurar Usuário Admin (Opcional):**
   - Edite `conf/tomcat-users.xml`:
   ```xml
   <tomcat-users>
     <role rolename="manager-gui"/>
     <user username="admin" password="admin" roles="manager-gui"/>
   </tomcat-users>
   ```

### 4. Configuração no IntelliJ IDEA

1. **Importar Projeto:**
   - File → Open → Selecione a pasta do projeto
   - Aguarde o Maven baixar as dependências

2. **Configurar Tomcat:**
   - File → Settings → Build, Execution, Deployment → Application Servers
   - Adicione o Tomcat Server apontando para o diretório de instalação

3. **Configurar Run Configuration:**
   - Run → Edit Configurations
   - Adicione Tomcat Server → Local
   - Configure o artifact `salao-beleza-app:war exploded`
   - Application context: `/salao-beleza`

## 🚀 Executando a Aplicação

### Método 1: Via IntelliJ IDEA (Recomendado)

1. **Build do Projeto:**
   ```bash
   mvn clean compile
   ```

2. **Executar:**
   - Clique no botão Run (▶️) no IntelliJ
   - Ou pressione `Shift + F10`

3. **Acessar:**
   - URL: `http://localhost:8080/salao-beleza/`

### Método 2: Deploy Manual

1. **Gerar WAR:**
   ```bash
   mvn clean package
   ```

2. **Deploy:**
   - Copie `target/salao-beleza-app.war` para `TOMCAT_HOME/webapps/`
   - Inicie o Tomcat: `bin/startup.bat`

3. **Acessar:**
   - URL: `http://localhost:8080/salao-beleza-app/`

## 👥 Usuários de Teste

Use os seguintes usuários para testar a aplicação:

### Cliente
- **Email:** `cliente@teste.com`
- **Senha:** `senha123`

### Profissional
- **Email:** `maria.silva@salao.com`
- **Senha:** `senha123`

### Administrador
- **Email:** `admin@salao.com`
- **Senha:** `senha123`

## 📁 Estrutura do Projeto

```
salao-beleza-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/salaobeleza/
│   │   │       ├── controller/     # Controllers MVC
│   │   │       ├── model/          # Entidades JPA
│   │   │       ├── repository/     # Repositórios de dados
│   │   │       └── service/        # Lógica de negócio
│   │   ├── resources/
│   │   │   ├── static/             # CSS, JS, imagens
│   │   │   └── application.properties
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── views/          # Páginas JSP
│   │       │   ├── spring/         # Configurações Spring
│   │       │   └── web.xml
│   │       └── index.jsp
├── db_schema.sql                   # Script do banco de dados
├── tomcat-deployment.md           # Guia de deployment
├── MANUAL_USUARIO.md              # Manual do usuário
├── pom.xml                        # Configuração Maven
└── README.md                      # Este arquivo
```

## 🔧 Configurações Importantes

### Banco de Dados

As configurações do banco estão em `src/main/resources/application.properties`:

```properties
# Database Configuration
db.url=jdbc:postgresql://localhost:5432/salao_beleza
db.username=salao_user
db.password=salao_password
db.driver=org.postgresql.Driver

# Hibernate Configuration
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.hbm2ddl.auto=validate
hibernate.show_sql=false
hibernate.format_sql=true
```

### Logs

- **Aplicação:** Configurado via Logback
- **Tomcat:** `TOMCAT_HOME/logs/catalina.out`

## 🐛 Resolução de Problemas

### Erro de Conexão com Banco
```bash
# Verificar se PostgreSQL está rodando
pg_ctl status

# Testar conexão
psql -U salao_user -d salao_beleza -h localhost
```

### Erro de Compilação Maven
```bash
# Limpar e recompilar
mvn clean compile

# Verificar dependências
mvn dependency:tree
```

### Erro 404 na Aplicação
- Verifique se o contexto da aplicação está correto na URL
- Confirme se o Tomcat está rodando na porta 8080

### Problemas de Memória
Adicione ao `setenv.bat` do Tomcat:
```bash
set CATALINA_OPTS=-Xms512m -Xmx1024m -XX:PermSize=256m
```

## 📚 Documentação Adicional

- **[Manual do Usuário](MANUAL_USUARIO.md)** - Guia completo para usuários finais
- **[Guia de Deployment](tomcat-deployment.md)** - Instruções detalhadas de deployment
- **[Script do Banco](db_schema.sql)** - Estrutura completa do banco de dados

## 🤝 Contribuição

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Suporte

Para dúvidas ou suporte:

- **Email:** suporte@salaobeleza.com
- **Documentação:** Consulte os arquivos de documentação inclusos
- **Issues:** Abra uma issue no repositório do projeto

---

**Desenvolvido com ❤️ para facilitar o gerenciamento de salões de beleza**

