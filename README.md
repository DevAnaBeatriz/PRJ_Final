
# Projeto Módulo 2 — Autenticação com Arquitetura Limpa + API Gateway

Este projeto implementa um sistema de autenticação seguindo **Arquitetura Limpa (Clean Architecture)**, com:
- **Login API (login-app)**  
- **API Gateway (api-gateway)**  
- **Banco PostgreSQL via Docker Compose**  
Somente o Gateway expõe porta ao host.

---

## Objetivo
Atender aos requisitos do Módulo 2, garantindo:
- Arquitetura limpa real  
- Separação clara de camadas  
- Testes unitários e de integração  
- Execução local (H2) e com Docker (Postgres)  
- Rotas protegidas com JWT  
- API versionada (`/api/v1/...`)

---

## Estrutura do Repositório e Decisões de Nome

A pasta raiz obrigatória é **modulo2/**.  
Cada microserviço possui seu próprio `pom.xml` e `Dockerfile`.

```
modulo2/
  README.md
  docker-compose.yml
  mvnw / mvnw.cmd
  .github/workflows/ci.yml
  api-gateway/
  login-app/
```

### login-app (Aplicação de Autenticação)
Seguiu **clean architecture** com pastas explícitas:

| Pasta | Justificativa |
|-------|---------------|
| `domain/` | Entidades puras (ex.: `User`) sem anotações JPA. Mantém independência do framework. |
| `port/` | Interfaces que definem contratos usados pelos casos de uso (ex.: `UserRepository`). |
| `usecase/` | Regras de negócio como `AuthService`, `JwtService`, `LoadUserByUsernameUseCase`. |
| `adapter/persistence/` | Implementações JPA (`UserEntity`, `JpaUserRepository`, `SpringDataUserRepo`). Conecta domínio ⇄ banco. |
| `web/` | Controllers e filtros (`AuthController`, `JwtAuthenticationFilter`, DTOs). |
| `config/` | Configuração de segurança, beans e seeds (`SecurityConfig`, `AppConfig`, `DataSeed`). |

###  api-gateway
Estrutura mínima:
- `GatewayApplication.java` → app Spring Cloud Gateway  
- `application.yml` → define rotas  
  - `/api/v1/auth/**` → encaminha para login-app  
  - `/api/v1/api/**` → encaminha para login-app  

---

##  Execução Local (sem Docker)

### Rodar login-app com H2
```bash
set SPRING_PROFILES_ACTIVE=local
set JWT_SECRET=uma_chave_bem_grande_para_jwt_256bits

./mvnw -pl login-app spring-boot:run
```

### Rodar api-gateway
```bash
./mvnw -pl api-gateway spring-boot:run
```

---

## 🐳 Execução com Docker

### Gere os JARs
```bash
./mvnw clean package -DskipTests
```

###  Suba tudo
```bash
docker-compose up --build
```

Gateway estará em:  
 **http://localhost:8080**

Login-app NÃO expõe porta (conforme exigido).

---

##  Requisições de Exemplo

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login   -H "Content-Type: application/json"   -d '{ "username": "aluno", "password": "senha123" }'
```

### Endpoint Protegido
```bash
curl -X GET http://localhost:8080/api/v1/auth/me   -H "Authorization: Bearer <token>"
```

---

##  Testes

### Unit Test (Mockito)
- `AuthServiceTest` valida:
  - credenciais válidas  
  - geração de token  
  - falha com credenciais inválidas  

### Integration Test (MockMvc)
- `AuthControllerIntegrationTest` garante:
  - `/api/v1/auth/login` funciona no contexto Spring real  

Rodar:
```bash
./mvnw test
```

---


## 📝 Checklist contra Perda de Pontos

✔ Pasta raiz correta  
✔ Arquitetura limpa real (domain/usecase/port/adapter/web/config)  
✔ Sem `@Autowired` em campos (apenas construtor)  
✔ Entidade desacoplada do ORM (User vs UserEntity)  
✔ API versionada  
✔ JWT configurado com variável de ambiente  
✔ Gateway expõe porta, login-app não  
✔ Seed com usuário aluno/senha123 (BCrypt)  
✔ Testes incluídos  
✔ Dockerfile para cada microserviço  
✔ docker-compose apontando para cada serviço pelo nome  
✔ README completo explicando decisões  

---

##  Autor
Ana Beatriz Martins Batista — Módulo 2 — Arquitetura Limpa 

