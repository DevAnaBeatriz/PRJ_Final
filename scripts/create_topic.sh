#!/usr/bin/env bash
set -e

echo "Aguardando inicialização dos brokers..."
sleep 10

BOOTSTRAP=kafka1:9092

echo "Criando tópico modulo4-topic com 5 partições e replication-factor 3..."
docker exec kafka1 bash -c "/usr/bin/kafka-topics --create --if-not-exists --topic modulo4-topic --partitions 5 --replication-factor 3 --bootstrap-server ${BOOTSTRAP}"

echo "Tópico criado (ou já existia)."
