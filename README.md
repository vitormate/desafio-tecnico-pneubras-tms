# Desafio Técnico | TMS — Ticket Management System

## Sobre o projeto

Este projeto implementa uma API de gerenciamento de tickets de suporte (TMS).

## Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Security + JWT (Auth0)
- Spring Data JPA + Flyway
- PostgreSQL
- Springdoc OpenAPI (Swagger)
- Lombok
- JUnit 5 + Mockito
- Docker

## Como executar

### Pré-requisitos

- Docker instalado

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/tms
cd desafio-tecnico-pneubras-tms
```

### 2. Suba os containers

```bash
docker-compose up
```

O Docker baixa automaticamente a imagem da API e o banco de dados PostgreSQL — nenhuma configuração adicional necessária.

## Documentação (Swagger)

Acesse: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Autenticação

A API utiliza autenticação JWT.

Ao iniciar o projeto já existirá um usuário com role ADMIN para utilizar:

- login: ADMIN
- password: 123456

### 2. Use o token retornado no header das requisições

```http
Authorization: Bearer <token>
```

## Endpoints

| Método | Rota | Descrição | Roles |
|--------|------|-----------|-------|
| POST | `/api/v1/auth/register` | Registrar usuário | ADMIN |
| POST | `/api/v1/auth/login` | Login | Público |
| POST | `/api/v1/tickets` | Criar ticket | ADMIN, AGENT, USER |
| GET | `/api/v1/tickets` | Listar tickets (paginado) | ADMIN, AGENT |
| GET | `/api/v1/tickets/{id}` | Detalhar ticket | ADMIN, AGENT, USER |
| PATCH | `/api/v1/tickets/{id}` | Atualizar título/descrição | ADMIN, AGENT, USER |
| PATCH | `/api/v1/tickets/{id}/assign` | Atribuir agente | ADMIN, AGENT |
| PATCH | `/api/v1/tickets/{id}/resolve` | Resolver ticket | ADMIN, AGENT |
| PATCH | `/api/v1/tickets/{id}/close` | Fechar ticket | ADMIN, AGENT, USER |

## Transições de Status

As transições de status seguem obrigatoriamente a seguinte ordem:

```
ABERTO → EM_PROGRESSO → RESOLVIDO → FECHADO
```

Transições inválidas retornam HTTP 400 com mensagem explicativa.

## Prazo automático (dueAt)

O campo `dueAt` é calculado automaticamente no momento da criação com base na prioridade:

| Prioridade | Prazo |
|------------|-------|
| BAIXA | +72h |
| MEDIA | +48h |
| ALTA | +24h |
| CRITICA | +8h |

## Hierarquia de Roles

```
ADMIN → AGENT → USER
```

Um ADMIN possui todas as permissões de AGENT e USER.

## Comando cURL


### Login
```
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "ADMIN", "password": "123456"}'
```

### Registrar usuário
```
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"login": "agent01", "password": "123456", "role": "AGENT"}'
```

### Criar ticket
```
curl -X POST http://localhost:8080/api/v1/tickets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "title": "Test",
    "description": "Test",
    "priority": "ALTA",
    "login": "agent01"
  }'
```

### Listar tickets
```
curl -X GET http://localhost:8080/api/v1/tickets \
  -H "Authorization: Bearer TOKEN"
```

### Listar tickets
```
curl -X GET "http://localhost:8080/api/v1/tickets?page=0&size=10&sort=id" \
  -H "Authorization: Bearer TOKEN"
```

### Detalhar ticket
```
curl -X GET http://localhost:8080/api/v1/tickets/1 \
  -H "Authorization: Bearer TOKEN"
```

### Atualizar título/descrição
```
curl -X PATCH http://localhost:8080/api/v1/tickets/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "title": "Título atualizado",
    "description": "Descrição atualizada"
  }'
```

### Atribuir agente — ABERTO → EM_PROGRESSO
```
curl -X PATCH http://localhost:8080/api/v1/tickets/1/assign \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"login": "agent01"}'
```

### Resolver ticket — EM_PROGRESSO → RESOLVIDO
```
curl -X PATCH http://localhost:8080/api/v1/tickets/1/resolve \
  -H "Authorization: Bearer TOKEN"
```

### Fechar ticket — RESOLVIDO → FECHADO
```
curl -X PATCH http://localhost:8080/api/v1/tickets/1/close \
  -H "Authorization: Bearer TOKEN"
```
