# login-app

Como executar local (H2):
- Export JWT_SECRET antes de rodar:
  - `export JWT_SECRET='12345678901234567890123456789012345678901234567890123456789012'`
- Rodar: `./mvnw -f login-app clean spring-boot:run -Dspring-boot.run.profiles=default`

Como executar com Docker (Postgres):
- Configure variáveis de ambiente no docker-compose (ou exporte)
- `docker-compose up --build`
- Gateway expõe porta 8080; o login-app não expõe porta.

Endpoints:
- POST /api/v1/auth/login  -> { "username", "password" }
- GET  /api/v1/auth/me    -> Requires Authorization: Bearer <token>

Notas de arquitetura:
- Domain layer (domain/) não tem anotações JPA.
- Adapter.persistence contém entidades JPA separadas.
- Injeção por construtor, sem @Autowired em campos.
