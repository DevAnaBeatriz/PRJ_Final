# Módulo 04 — Produção/Consumo com Kafka (3 brokers)

Este repositório contém 3 aplicações Spring Boot (Java 17 + Maven):
- `service-producer` — endpoint HTTP POST `/produce` que publica mensagens no tópico `modulo4-topic`.
- `service-consumer-a` — consumidor com `group.id=modulo4-consumer-a`.
- `service-consumer-b` — consumidor com `group.id=modulo4-consumer-b`.

Também inclui um `docker-compose.yml` que levanta:
- 1 Zookeeper
- 3 brokers Kafka (kafka1,kafka2,kafka3)
- 3 serviços apps (build local)


## Pré-requisitos

1. Docker Desktop instalado (Windows / macOS / Linux).  
2. WSL2 recomendado no Windows.  
3. Git (opcional).  
4. Porta 8080 livre (ou ajuste no docker-compose).  
5. No mínimo 4GB livres para os containers.

---

## Estrutura (raiz do projeto)

```
README.md
docker-compose.yml
scripts/
  create_topic.sh
service-producer/
service-consumer-a/
service-consumer-b/
LICENSE
```


## Objetivo

- Enviar mensagens via endpoint HTTP POST `/produce` (producer).
- Garantir que **ambos** os consumidores recebam todas as mensagens (cada consumer com `group.id` diferente).
- Cluster Kafka com 3 brokers para resiliência.
- Tópico `modulo4-topic` com **5 partições** e `replication-factor=3`.

---

## Comandos principais 

### 1) Parar e limpar (recomendado antes de startar)
No Windows PowerShell:
```powershell
docker-compose down --remove-orphans
docker system prune -af
wsl --shutdown
```

### 2) (Opcional) Remover volumes antigos
```bash
docker volume prune -f
```

### 3) Buildar imagens (reconstruir)
```bash
docker-compose build --no-cache
```

### 4) Subir todo o ambiente (Kafka + apps)
```bash
docker-compose up -d
```

### 5) Verificar containers em execução
```bash
docker ps
```

Você deve ver os containers:
- zookeeper
- kafka1, kafka2, kafka3
- service-producer
- service-consumer-a
- service-consumer-b

---

## 6) Verificar/criar o tópico com 5 partições (se não foi criado automaticamente)

**Opção A — automatic (se `KAFKA_CREATE_TOPICS` no docker-compose foi configurado):**
Nenhuma ação necessária — o tópico já será criado no startup.

**Opção B — criar manualmente (se preferir):**
```bash
docker exec -it kafka1 bash
kafka-topics.sh --create --topic modulo4-topic --partitions 5 --replication-factor 3 --bootstrap-server kafka1:9092
kafka-topics.sh --describe --topic modulo4-topic --bootstrap-server kafka1:9092
exit
```

Confirme que `PARTITIONS = 5` e `ReplicationFactor = 3`.

---

## 7) Testar o endpoint do Producer

No PowerShell (Windows) use `Invoke-WebRequest`:

```powershell
Invoke-WebRequest `
  -Uri "http://localhost:8080/produce" `
  -Method POST `
  -Headers @{ "Content-Type" = "application/json" } `
  -Body '{ "key": "k1", "message": "Olá Kafka!!!" }'
```

Resposta esperada:  
`Mensagem enviada: offset=X / partition=Y` (HTTP 200)

No Linux/Mac ou Git Bash com curl:
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"key":"k1","message":"Olá Kafka!!!"}' \
  http://localhost:8080/produce
```

---

## 8) Verificar logs dos Consumers

Acompanhe os logs para confirmar que ambos receberam:

```bash
docker-compose logs -f service-consumer-a
docker-compose logs -f service-consumer-b
```

Saída esperada em ambos:
```
Consumer-A recebeu: chave=k1 mensagem=Olá Kafka!!! partition=<n> offset=<m>
Consumer-B recebeu: chave=k1 mensagem=Olá Kafka!!! partition=<n> offset=<m>
```

---

## 9) Testes de resiliência simples

1. Pare um broker (ex: kafka2) e envie mensagens:
```bash
docker stop kafka2
# enviar mensagem via Producer
```
2. Verifique se consumidores continuam recebendo mensagens (desde que haja líder para partições).
3. Reinicie o broker:
```bash
docker start kafka2
```
4. Verifique replicação e reassignment de líderes:
```bash
docker exec -it kafka1 bash
kafka-topics.sh --describe --topic modulo4-topic --bootstrap-server kafka1:9092
exit
```

---

## 10) Variáveis e Configurações importantes

As aplicações usam `application.yml` com placeholders (padrões) — variáveis importantes expostas no docker-compose:

- `SPRING_KAFKA_BOOTSTRAP_SERVERS` — ex: `kafka1:9092,kafka2:9093,kafka3:9094`
- `KAFKA_TOPIC` — `modulo4-topic`
- `KAFKA_CONSUMER_GROUP` — cada consumer tem seu `group.id` no código:
  - `modulo4-consumer-a`
  - `modulo4-consumer-b`



