import os
import time
from kafka import KafkaConsumer
import logging

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO, format='%(asctime)s %(levelname)s %(message)s')

def get_consumer(group_id: str = "modulo3-group"):
    bootstrap = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "kafka:9092")
    topic = os.getenv("KAFKA_TOPIC", "modulo3-topic")
    username = os.getenv("KAFKA_USERNAME")
    password = os.getenv("KAFKA_PASSWORD")

    common = dict(
        bootstrap_servers=bootstrap.split(","),
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        group_id=group_id,
        consumer_timeout_ms=1000,  
    )

   
    if username and password:
        logger.info("Configurando SASL/PLAIN com usuário fornecido (KAFKA_USERNAME).")
        sasl = {
            'security_protocol': 'SASL_PLAINTEXT',
            'sasl_mechanism': 'PLAIN',
            'sasl_plain_username': username,
            'sasl_plain_password': password,
        }
        common.update(sasl)

    logger.info(f"Conectando ao Kafka em {bootstrap}, tópico={topic}")
    consumer = KafkaConsumer(topic, **common)
    return consumer
