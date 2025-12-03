import time
from app.kafka_client import get_consumer

def lambda_handler(event, context):
    """Handler compatível com AWS Lambda para demonstração.
    Observação: iniciar um consumidor dentro de uma função Lambda para operações long-running **não é recomendado**.
    Aqui o handler inicializa um consumer, consome mensagens disponíveis por até 5 segundos e encerra,
    retornando a quantidade de mensagens consumidas.
    Isso permite testar a lógica de consumo em execução síncrona.
    """
    consumer = get_consumer()
    start = time.time()
    consumed = 0
    timeout_seconds = 5
    try:
        # consumer is iterable; we'll poll messages até timeout
        for msg in consumer:
            try:
                value = msg.value.decode('utf-8') if isinstance(msg.value, (bytes, bytearray)) else str(msg.value)
            except Exception:
                value = str(msg.value)
            print('A mensagem chegou: ', value)
            consumed += 1
            if time.time() - start > timeout_seconds:
                break
    except Exception as e:
        print('Erro ao consumir:', e)
    finally:
        try:
            consumer.close()
        except Exception:
            pass
    return {'consumed': consumed}
