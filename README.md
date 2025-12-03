# Módulo 03 Consumer Kafka (Lambda + Local) 

**Resumo:** este README explica, passo-a-passo e com comandos exatos (PowerShell / Bash), como rodar o projeto do Módulo 03 localmente com Docker Compose, testar o consumer contínuo e o handler `lambda_handler`, como publicar a imagem no DockerHub via GitHub Actions e dicas de troubleshooting. T

> Se você estiver usando PowerShell (Windows) use os comandos mostrados como *PowerShell*. Se estiver no Linux/macOS, use a versão *bash* (quando especificado).

---

# Índice

1. Requisitos
2. Estrutura do projeto (resumo)
3. Variáveis de ambiente / Secrets
4. Como subir o ambiente (Docker Compose)
5. Enviar mensagens de teste (producer)
6. Ver logs do consumer contínuo (`app`)
7. Testar o handler Lambda (modo `lambda`)
8. Porque o Lambda às vezes consome 0 mensagens (consumer group)
9. Alternativas para testar Lambda (parar app / outro consumer group)
10. Build e execução manual da imagem Docker
11. Publicar no DockerHub com GitHub Actions (configurar secrets)
12. Gerar `modulo3.zip` para envio
13. Troubleshooting rápido
14. Alteração opcional para `KAFKA_GROUP` via env (exemplo)
15. Licença

---

# 1) Requisitos

- Docker (versão recente) e Docker Compose.  
- (Opcional) Conta Docker Hub (para publicar imagem).  
- (Opcional) Conta GitHub para o repositório e Actions.  
- Terminal: PowerShell (Windows) ou Bash (Linux/macOS).  
- Rede/portas 9092 e 2181 livres localmente (ou adapte `docker-compose.yml`).

---

# 2) Estrutura do projeto (resumo)

```
/ (raiz do projeto)
├─ README.md
├─ Dockerfile
├─ docker-compose.yml
├─ .dockerignore
├─ requirements.txt
├─ LICENSE
├─ scripts/
│  └─ make_zip.sh
├─ app/
│  ├─ __init__.py
│  ├─ consumer.py          # consumer contínuo (modo local)
│  ├─ lambda_function.py   # handler lambda (consome por até 5s)
│  └─ kafka_client.py      # cria KafkaConsumer lendo env vars
└─ .github/
   └─ workflows/
      └─ docker-publish.yml
```

---

# 3) Variáveis de ambiente / Secrets

## Variáveis (local / runtime)
- `KAFKA_BOOTSTRAP_SERVERS` — ex: `kafka:9092` (padrão no docker-compose)
- `KAFKA_TOPIC` — ex: `modulo3-topic`
- `KAFKA_USERNAME` / `KAFKA_PASSWORD` — opcionais para SASL/PLAIN
- `IMAGE_NAME` — ex: `meuusuario/modulo3` (só convenção)

## GitHub Secrets (para GitHub Actions)
- `DOCKERHUB_USERNAME` — seu usuário Docker Hub
- `DOCKERHUB_TOKEN` — token do Docker Hub (gerado no hub.docker.com → Account Settings → Security → New Access Token)

### Como gerar o DOCKERHUB_TOKEN
1. Acesse `https://hub.docker.com` e faça login.
2. Clique no seu avatar → **Account Settings** → **Security** → **New Access Token**.
3. Nomeie (ex: `github-actions-modulo3`), selecione scope `write`/`publish`.
4. Gere o token e copie.
5. No GitHub do repositório: **Settings → Secrets and variables → Actions → New repository secret**:
   - `DOCKERHUB_USERNAME` = seu usuário
   - `DOCKERHUB_TOKEN` = token copiado

---

# 4) Como subir o ambiente Kafka + app (docker-compose)

Na raiz do projeto, execute:

### PowerShell (Windows)
```powershell
docker-compose up --build
```

### Bash (Linux/macOS)
```bash
docker-compose up --build
```

O `docker-compose.yml` faz subir:
- `zookeeper` (Confluent)
- `kafka` (Confluent)
- `kafka-producer-cli` (Confluent)
- `app` — seu serviço Python que roda o consumer contínuo

Aguarde até que `kafka` e `zookeeper` sinalizem "started".

---

# 5) Enviar mensagens de teste

### Abrir shell no producer
```powershell
docker-compose exec kafka-producer-cli bash
```

Dentro:
```bash
kafka-console-producer --broker-list kafka:9092 --topic modulo3-topic
> teste1
> teste2
```

### Alternativa — enviar mensagem sem shell
```powershell
docker-compose exec -T kafka-producer-cli bash -c "kafka-console-producer --broker-list kafka:9092 --topic modulo3-topic <<EOF
mensagem-teste
EOF"
```

---

# 6) Ver logs do consumer contínuo

```powershell
docker-compose logs -f app
```

Saída esperada:
```
A mensagem chegou: teste1
A mensagem chegou: teste2
```

---

# 7) Testar o handler Lambda (modo lambda)

### Passo 1 — Descubra o nome da imagem criada pelo compose
```powershell
docker images
```

Exemplo real:
```
REPOSITORY       TAG      IMAGE ID
modulo31-app     latest   1c6b94adcec0
```

### Passo 2 — Descubra a rede criada pelo compose
```powershell
docker network ls
```

Exemplo:
```
modulo31_default
```

### Passo 3 — Rodar lambda (PowerShell, uma linha)
```powershell
docker run --rm --network=modulo31_default -e KAFKA_BOOTSTRAP_SERVERS=kafka:9092 -e KAFKA_TOPIC=modulo3-topic modulo31-app:latest lambda
```

Saída esperada:
```
A mensagem chegou: teste1
A mensagem chegou: teste2
Lambda retornou: {'consumed': 2}
```

---

# 8) Por que o Lambda às vezes retorna `consumed: 0`

Porque tanto o consumer contínuo (`app`) quanto o Lambda usam **o mesmo consumer group**:

```
modulo3-group
```

Em Kafka:

- Um consumer group **divide as partições** entre seus membros.
- Se o consumer contínuo já pegou todas as partições, **o Lambda não recebe nenhuma**.

Por isso a saída pode ser:
```
Updated partition assignment: []
Lambda retornou: {'consumed': 0}
```

**Solução:** parar o consumer contínuo antes, ou usar outro consumer group (ver seção 9).

---

# 9) Como garantir que o Lambda consuma mensagens

## Opção A — Pare o consumer contínuo
```powershell
docker-compose stop app
# ou
docker-compose down
docker-compose up -d zookeeper kafka kafka-producer-cli
```
Depois produza mensagens e rode novamente o Lambda.

## Opção B — Use um consumer group diferente
Altere o código para permitir:
```
-e KAFKA_GROUP=lambda-group
```

Assim o Lambda sempre recebe mensagens.

---

# 10) Build manual + run manual

### Build:
```powershell
docker build -t meuusuario/modulo3:local .
```

### Rodar consumer contínuo:
```powershell
docker run --rm --network=modulo31_default -e KAFKA_BOOTSTRAP_SERVERS=kafka:9092 -e KAFKA_TOPIC=modulo3-topic meuusuario/modulo3:local
```

### Rodar lambda:
```powershell
docker run --rm --network=modulo31_default -e KAFKA_BOOTSTRAP_SERVERS=kafka:9092 -e KAFKA_TOPIC=modulo3-topic meuusuario/modulo3:local lambda
```

---

# 11) Publicar no DockerHub via GitHub Actions

1. Suba o projeto para um repositório GitHub.  
2. Crie os secrets:
   - `DOCKERHUB_USERNAME`
   - `DOCKERHUB_TOKEN`
3. Faça push para a branch `main`.
4. O workflow `docker-publish.yml` automaticamente:
   - constrói a imagem
   - loga no Docker Hub
   - publica em:  
     ```
     docker.io/SEU_USUARIO/modulo3:latest
     ```

Para forçar o workflow:
```bash
git commit --allow-empty -m "Trigger CI"
git push
```

---


# 13) Troubleshooting

### Erro: `invalid reference format`
→ No PowerShell **NÃO USE \** para quebrar linha. Comandos do docker run devem ir em **uma única linha**.

### Erro: `pull access denied for modulo3_app`
→ Use o nome real da imagem (veja `docker images`) — normalmente `modulo31-app`.

### Erro: `kafka: Name or service not known`
→ Inclua `--network=<network>` correto, ex.: `--network=modulo31_default`.

### Lambda consumiu 0 mensagens
→ O app contínuo estava rodando no mesmo consumer group (ver seção 8).

---

# 14) Habilitar consumer group customizado (opcional)

Adicionar em `kafka_client.py`:

```python
group_id = os.getenv("KAFKA_GROUP", group_id)
```

Chamar Lambda assim:

```powershell
docker run --rm --network=modulo31_default -e KAFKA_BOOTSTRAP_SERVERS=kafka:9092 -e KAFKA_TOPIC=modulo3-topic -e KAFKA_GROUP=lambda-group modulo31-app:latest lambda
```

