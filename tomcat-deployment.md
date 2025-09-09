# Guia de Deployment no Apache Tomcat

## Configuração do Tomcat no IntelliJ IDEA

### 1. Configurar o Servidor Tomcat no IntelliJ

1. Abra o IntelliJ IDEA
2. Vá em **File** → **Settings** (ou **IntelliJ IDEA** → **Preferences** no macOS)
3. Navegue até **Build, Execution, Deployment** → **Application Servers**
4. Clique no **+** e selecione **Tomcat Server**
5. Aponte para o diretório de instalação do Tomcat (ex: `C:\apache-tomcat-9.0.xx`)
6. Clique **OK**

### 2. Configurar Run Configuration

1. No IntelliJ, vá em **Run** → **Edit Configurations**
2. Clique no **+** e selecione **Tomcat Server** → **Local**
3. Configure:
   - **Name**: `Salao Beleza App`
   - **Application server**: Selecione o Tomcat configurado
   - **HTTP port**: `8080` (padrão)
   - **JRE**: Java 8 ou superior

4. Na aba **Deployment**:
   - Clique no **+** e selecione **Artifact**
   - Escolha `salao-beleza-app:war exploded`
   - **Application context**: `/salao-beleza` (ou deixe vazio para root)

5. Clique **Apply** e **OK**

### 3. Build e Deploy

1. **Build do projeto**:
   ```bash
   mvn clean compile
   ```

2. **Executar no IntelliJ**:
   - Clique no botão **Run** (▶️) ou pressione `Shift + F10`
   - O Tomcat será iniciado automaticamente
   - A aplicação estará disponível em: `http://localhost:8080/salao-beleza`

### 4. Deploy Manual (WAR file)

1. **Gerar WAR**:
   ```bash
   mvn clean package
   ```

2. **Deploy no Tomcat**:
   - Copie o arquivo `target/salao-beleza-app.war` para `TOMCAT_HOME/webapps/`
   - Inicie o Tomcat: `TOMCAT_HOME/bin/startup.bat` (Windows) ou `startup.sh` (Linux/Mac)
   - Acesse: `http://localhost:8080/salao-beleza-app`

## Configurações Importantes

### 1. Configuração do Banco de Dados

Certifique-se de que o PostgreSQL está rodando e as configurações em `application.properties` estão corretas:

```properties
# Database Configuration
db.url=jdbc:postgresql://localhost:5432/salao_beleza
db.username=salao_user
db.password=salao_password
db.driver=org.postgresql.Driver
```

### 2. Logs do Tomcat

- **Logs de aplicação**: `TOMCAT_HOME/logs/catalina.out`
- **Logs de acesso**: `TOMCAT_HOME/logs/localhost_access_log.yyyy-mm-dd.txt`

### 3. Troubleshooting

**Problema**: Erro de conexão com banco de dados
- **Solução**: Verifique se o PostgreSQL está rodando e as credenciais estão corretas

**Problema**: Erro 404 ao acessar a aplicação
- **Solução**: Verifique se o contexto da aplicação está correto na URL

**Problema**: Erro de compilação
- **Solução**: Execute `mvn clean compile` e verifique se todas as dependências foram baixadas

**Problema**: Erro de memória
- **Solução**: Aumente a memória do Tomcat:
  ```bash
  # No arquivo setenv.bat (Windows) ou setenv.sh (Linux/Mac)
  set CATALINA_OPTS=-Xms512m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m
  ```

## URLs da Aplicação

Após o deploy, a aplicação estará disponível nas seguintes URLs:

- **Home**: `http://localhost:8080/salao-beleza/`
- **Login**: `http://localhost:8080/salao-beleza/login`
- **Registro**: `http://localhost:8080/salao-beleza/register`
- **Serviços**: `http://localhost:8080/salao-beleza/servicos`
- **Profissionais**: `http://localhost:8080/salao-beleza/profissionais`

## Usuários de Teste

Use os seguintes usuários para testar a aplicação (criados pelo script `db_schema.sql`):

### Cliente
- **Email**: `cliente@teste.com`
- **Senha**: `senha123`

### Profissional
- **Email**: `maria.silva@salao.com`
- **Senha**: `senha123`

### Administrador
- **Email**: `admin@salao.com`
- **Senha**: `senha123`

## Monitoramento

### 1. Manager App do Tomcat

Acesse `http://localhost:8080/manager` para gerenciar aplicações (necessário configurar usuário admin no Tomcat).

### 2. Logs da Aplicação

Monitore os logs em tempo real:
```bash
tail -f TOMCAT_HOME/logs/catalina.out
```

### 3. Status da Aplicação

Verifique se a aplicação está rodando:
```bash
curl http://localhost:8080/salao-beleza/
```

## Backup e Manutenção

### 1. Backup do Banco de Dados

```bash
pg_dump -U salao_user -h localhost salao_beleza > backup_salao_beleza.sql
```

### 2. Restaurar Backup

```bash
psql -U salao_user -h localhost salao_beleza < backup_salao_beleza.sql
```

### 3. Limpeza de Logs

```bash
# Limpar logs antigos do Tomcat
find TOMCAT_HOME/logs -name "*.log" -mtime +30 -delete
```

