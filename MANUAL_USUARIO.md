# Manual do Usuário - Sistema de Salão de Beleza

## Introdução

Bem-vindo ao Sistema de Gerenciamento de Salão de Beleza! Este sistema foi desenvolvido para facilitar o agendamento de serviços e o gerenciamento de um salão de beleza, oferecendo interfaces específicas para clientes, profissionais e administradores.

## Funcionalidades Principais

### Para Clientes
- Cadastro e login no sistema
- Visualização de serviços disponíveis
- Visualização de profissionais
- Agendamento de serviços
- Gerenciamento de agendamentos (visualizar, cancelar)
- Dashboard personalizado

### Para Profissionais
- Login no sistema
- Visualização de agendamentos
- Confirmação e cancelamento de agendamentos
- Gerenciamento de horários disponíveis
- Dashboard com estatísticas

### Para Administradores
- Acesso completo ao sistema
- Gerenciamento de usuários, serviços e profissionais

## Como Usar o Sistema

### 1. Acesso Inicial

1. Abra seu navegador web
2. Acesse: `http://localhost:8080/salao-beleza/`
3. Você verá a página inicial com opções de login e registro

### 2. Registro de Novo Cliente

1. Clique em "Registrar" na página inicial
2. Preencha o formulário com suas informações:
   - Nome completo
   - Email (será usado para login)
   - Telefone
   - Senha (mínimo 6 caracteres)
   - Confirmação de senha
3. Clique em "Registrar"
4. Após o cadastro, você será redirecionado para a página de login

### 3. Login no Sistema

1. Na página de login, insira:
   - Email cadastrado
   - Senha
2. Clique em "Entrar"
3. Você será redirecionado para o dashboard apropriado ao seu tipo de usuário

### 4. Dashboard do Cliente

Após fazer login como cliente, você terá acesso a:

#### Visualizar Próximos Agendamentos
- Lista dos seus próximos agendamentos
- Informações sobre serviço, profissional, data e hora

#### Fazer Novo Agendamento
1. Clique em "Novo Agendamento"
2. Selecione o serviço desejado
3. Escolha o profissional (lista será filtrada automaticamente)
4. Selecione a data
5. Escolha o horário disponível
6. Adicione observações se necessário
7. Clique em "Agendar"

#### Gerenciar Agendamentos
1. Clique em "Meus Agendamentos"
2. Visualize todos os seus agendamentos
3. Cancele agendamentos se necessário (apenas pendentes ou confirmados)

### 5. Dashboard do Profissional

Após fazer login como profissional, você terá acesso a:

#### Visualizar Agendamentos
- Agendamentos do dia atual
- Estatísticas rápidas (pendentes, confirmados)

#### Gerenciar Agendamentos
1. Clique em "Meus Agendamentos"
2. Visualize todos os agendamentos
3. Confirme agendamentos pendentes
4. Cancele agendamentos se necessário
5. Marque agendamentos como concluídos

#### Gerenciar Horários
1. Clique em "Gerenciar Horários"
2. Adicione novos horários disponíveis:
   - Selecione a data
   - Defina hora de início e fim
3. Marque horários como disponíveis/indisponíveis
4. Remova horários se necessário

## Usuários de Teste

Para testar o sistema, use os seguintes usuários pré-cadastrados:

### Cliente de Teste
- **Email**: `cliente@teste.com`
- **Senha**: `senha123`

### Profissional de Teste
- **Email**: `maria.silva@salao.com`
- **Senha**: `senha123`

### Administrador
- **Email**: `admin@salao.com`
- **Senha**: `senha123`

## Dicas de Uso

### Para Clientes

1. **Agendamento Antecipado**: Agende seus serviços com antecedência para garantir disponibilidade
2. **Cancelamento**: Cancele agendamentos com antecedência para liberar horários para outros clientes
3. **Informações Atualizadas**: Mantenha suas informações de contato atualizadas

### Para Profissionais

1. **Horários Regulares**: Configure seus horários disponíveis regularmente
2. **Confirmação Rápida**: Confirme agendamentos rapidamente para melhor experiência do cliente
3. **Comunicação**: Use o campo de observações para se comunicar com clientes

## Resolução de Problemas

### Problemas Comuns

#### Não Consigo Fazer Login
- Verifique se o email e senha estão corretos
- Certifique-se de que está usando o email cadastrado
- Tente redefinir a senha se necessário

#### Não Vejo Horários Disponíveis
- Verifique se selecionou um profissional
- Certifique-se de que a data selecionada é futura
- O profissional pode não ter horários cadastrados para a data

#### Erro ao Agendar
- Verifique se todos os campos obrigatórios estão preenchidos
- O horário pode ter sido ocupado por outro cliente
- Tente selecionar outro horário

#### Página Não Carrega
- Verifique sua conexão com a internet
- Certifique-se de que o servidor está rodando
- Tente atualizar a página (F5)

### Suporte Técnico

Em caso de problemas técnicos:

1. Verifique se o PostgreSQL está rodando
2. Confirme se o Tomcat está iniciado
3. Consulte os logs do sistema
4. Entre em contato com o administrador do sistema

## Segurança

### Boas Práticas

1. **Senhas Seguras**: Use senhas com pelo menos 6 caracteres
2. **Logout**: Sempre faça logout ao terminar de usar o sistema
3. **Informações Pessoais**: Não compartilhe suas credenciais de login
4. **Navegador Atualizado**: Use um navegador web atualizado

### Privacidade

- Suas informações pessoais são protegidas
- Dados de agendamento são confidenciais
- Apenas profissionais autorizados têm acesso aos seus dados

## Atualizações e Melhorias

O sistema está em constante evolução. Futuras atualizações podem incluir:

- Notificações por email
- Sistema de avaliações
- Integração com pagamentos online
- Aplicativo móvel
- Relatórios avançados

## Contato

Para sugestões, dúvidas ou problemas:

- Email: suporte@salaobeleza.com
- Telefone: (11) 9999-9999
- Horário de atendimento: Segunda a Sexta, 9h às 18h

---

**Versão do Manual**: 1.0  
**Data de Atualização**: Dezembro 2023  
**Sistema**: Salão de Beleza v1.0

