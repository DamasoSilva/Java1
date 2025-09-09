# Salão de Beleza - Sistema de Agendamento

Este é um sistema web para gerenciamento de um salão de beleza, desenvolvido em Java com Spring MVC e Hibernate, e utilizando PostgreSQL como banco de dados.

## Requisitos do Sistema

- Java Development Kit (JDK) 11 ou superior
- Apache Maven 3.6.0 ou superior
- Apache Tomcat 9.x ou superior
- PostgreSQL 13 ou superior
- IntelliJ IDEA (ou outra IDE de sua preferência)

## Configuração do Banco de Dados (PostgreSQL)

### 1. Instalação do PostgreSQL

Para usuários Windows 11, siga os passos abaixo para instalar o PostgreSQL:

1.  **Download:** Baixe o instalador do PostgreSQL para Windows no site oficial: [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)
2.  **Executar o Instalador:** Execute o arquivo `.exe` baixado. Siga as instruções do assistente de instalação.
    -   **Diretório de Instalação:** Mantenha o padrão ou escolha um diretório de sua preferência.
    -   **Componentes:** Certifique-se de que `PostgreSQL Server`, `pgAdmin 4` e `Command Line Tools` estejam selecionados.
    -   **Diretório de Dados:** Mantenha o padrão ou escolha um diretório para armazenar os dados do banco.
    -   **Senha do Superusuário (postgres):** Defina uma senha forte para o usuário `postgres`. **LEMBRE-SE DESSA SENHA!**
    -   **Porta:** Mantenha a porta padrão (5432).
    -   **Locale:** Mantenha o padrão ou escolha o de sua preferência.
3.  **Concluir a Instalação:** Após a instalação, o assistente pode perguntar se você deseja instalar o Stack Builder. Você pode ignorar isso por enquanto.

### 2. Criação do Banco de Dados

Após a instalação, você pode criar o banco de dados usando o `pgAdmin 4` (interface gráfica) ou o `psql` (linha de comando).

#### Opção A: Usando pgAdmin 4

1.  Abra o `pgAdmin 4` (geralmente encontrado no menu Iniciar).
2.  Conecte-se ao servidor PostgreSQL usando a senha que você definiu durante a instalação.
3.  No painel esquerdo, clique com o botão direito em `Databases` -> `Create` -> `Database...`.
4.  No campo `Database`, digite `salao_beleza`.
5.  No campo `Owner`, selecione `postgres` (ou outro usuário que você criar).
6.  Clique em `Save`.

#### Opção B: Usando psql (linha de comando)

1.  Abra o `SQL Shell (psql)` no menu Iniciar (ou use o Git Bash e navegue até o diretório `bin` da sua instalação do PostgreSQL, por exemplo, `C:\Program Files\PostgreSQL\13\bin`).
2.  Conecte-se ao servidor como usuário `postgres`:
    ```bash
    psql -U postgres
    ```
3.  Digite a senha que você definiu para o usuário `postgres`.
4.  No prompt `postgres=#`, crie o banco de dados:
    ```sql
    CREATE DATABASE salao_beleza;
    ```
5.  Para sair, digite `\q`.

### 3. Configuração no Projeto Java

O projeto Java será configurado para se conectar ao banco de dados `salao_beleza` usando o usuário `postgres` e a senha que você definiu. Certifique-se de que as credenciais no arquivo de configuração do Spring (que será criado posteriormente) correspondam às suas configurações do PostgreSQL.



## Configuração do Apache Maven

O projeto utiliza Maven para gerenciamento de dependências e build. Certifique-se de ter o Maven instalado e configurado em sua máquina.

### 1. Instalação do Maven

Siga as instruções oficiais para instalar o Maven em seu sistema operacional:
- [Instalação do Apache Maven](https://maven.apache.org/install.html)

### 2. Verificação da Instalação

Após a instalação, abra um terminal (Git Bash no Windows) e execute o comando para verificar a versão:

```bash
mvn -v
```

Você deverá ver uma saída similar a esta:

```
Apache Maven 3.x.x (xxxxxxx; 20xx-xx-xxTxx:xx:xxZ)
Maven home: C:\Program Files\Apache\apache-maven-3.x.x
Java version: 11.x.x, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-11.x.x
Default locale: en_US, platform encoding: Cp1252
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

### 3. Build do Projeto

Para compilar o projeto e gerar o arquivo `.war`, navegue até o diretório raiz do projeto (`salao-beleza-app`) no terminal e execute:

```bash
mvn clean install
```

Este comando irá baixar as dependências, compilar o código e empacotar a aplicação em um arquivo `.war` (por exemplo, `salao-beleza.war`) no diretório `target/`.

## Configuração e Deployment no Apache Tomcat

O Apache Tomcat é o servidor de aplicação onde a aplicação web será executada.

### 1. Instalação do Tomcat

1.  **Download:** Baixe a versão 9.x do Apache Tomcat no site oficial: [https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi). Recomenda-se a versão `zip` para Windows.
2.  **Extrair:** Extraia o conteúdo do arquivo `zip` para um diretório de sua preferência (ex: `C:\apache-tomcat-9.x.x`).

### 2. Configuração do Tomcat

Não são necessárias configurações complexas para este projeto. O `pom.xml` já inclui o plugin `tomcat7-maven-plugin` para facilitar o desenvolvimento e deployment.

### 3. Deployment da Aplicação

#### Opção A: Usando o Plugin Maven (Recomendado para Desenvolvimento)

Este método é o mais simples para testar a aplicação durante o desenvolvimento. Certifique-se de que o Tomcat não esteja rodando em outra instância na porta 8080.

1.  Navegue até o diretório raiz do projeto (`salao-beleza-app`) no terminal.
2.  Execute o comando:
    ```bash
    mvn tomcat7:run
    ```
    O Maven fará o deploy da aplicação no Tomcat embutido e a iniciará. Você poderá acessar a aplicação em `http://localhost:8080/salao-beleza`.

#### Opção B: Deployment Manual (Para Produção ou Teste Final)

1.  **Build do Projeto:** Certifique-se de ter compilado o projeto e gerado o arquivo `.war`:
    ```bash
    mvn clean install
    ```
    O arquivo `.war` estará em `salao-beleza-app/target/salao-beleza.war`.
2.  **Copiar o WAR:** Copie o arquivo `salao-beleza.war` para o diretório `webapps` da sua instalação do Tomcat (ex: `C:\apache-tomcat-9.x.x\webapps`).
3.  **Iniciar o Tomcat:**
    -   No Windows, navegue até o diretório `bin` da sua instalação do Tomcat (ex: `C:\apache-tomcat-9.x.x\bin`).
    -   Execute `startup.bat`.
4.  **Acessar a Aplicação:** Após o Tomcat iniciar, a aplicação será automaticamente descompactada e estará disponível em `http://localhost:8080/salao-beleza`.

### 4. Parar o Tomcat

-   Se estiver usando `mvn tomcat7:run`, pressione `Ctrl+C` no terminal.
-   Se for um Tomcat manual, execute `shutdown.bat` no diretório `bin` do Tomcat.

