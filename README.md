# modulo1 - Projeto Spring Boot (CRUD Pessoa)

## Objetivo
Projeto Spring Boot (Java 17, Spring Boot 3.x, Maven) que implementa CRUD de `Pessoa` com PostgreSQL. Pronto para execução via Docker Compose, com logs enviados ao Graylog (GELF).

## Estrutura
- src/main/java - código-fonte
- src/main/resources - application.yml e logback-spring.xml
- init-db/init.sql - script para inicializar o banco (3 registros, 2 ativos + 1 inativo)
- Dockerfile / docker-compose.yml - infra para aplicação, Postgres e Graylog
- tests/ - testes de integração

## Como rodar (local)
1. Build:
```bash
mvn clean package -DskipTests
```

2. Subir com Docker Compose:
```bash
docker-compose up --build
```

A aplicação estará em `http://localhost:8080`.

## Endpoints
- `POST /api/pessoas` - criar (201)
- `GET  /api/pessoas?page=0&size=10` - listar apenas `ativo=true` (com paginação e metadados)
- `GET  /api/pessoas/{id}` - obter por id (404 se não existir ou ativo=false)
- `PUT  /api/pessoas/{id}` - atualizar (200)
- `DELETE /api/pessoas/{id}` - deletar (hard delete) (204)

Exemplo curl - criar:
```bash
curl -X POST http://localhost:8080/api/pessoas -H "Content-Type: application/json" -d '{"nome":"Ana","dtNascimento":"2005-03-20","ativo":true}'
```

## Graylog
Acesse Graylog via `http://localhost:9000` (as credenciais padrão estão no docker-compose, mas o login é admin e a senha é admin123). Logs são enviados via GELF para host `graylog` na porta `12201` conforme `logback-spring.xml`.

## Testes e CI
Incluí um workflow do GitHub Actions (`.github/workflows/build.yml`) que faz build e executa testes.

## Observações
- `application.yml` lê variáveis de ambiente (DB_HOST, DB_PORT, DB_USER, DB_PASS, DB_NAME, GRAYLOG_HOST, GRAYLOG_PORT).
- Para desenvolvimento rápido, `spring.jpa.hibernate.ddl-auto=update`.
