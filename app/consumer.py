import signal
import sys
import os
import time
from app.kafka_client import get_consumer

running = True

def _handle_signal(sig, frame):
    global running
    print('Sinal recebido, encerrando...')
    running = False

def consume_loop():
    consumer = get_consumer()
    print('Consumidor iniciado. Aguardando mensagens... (pressione CTRL+C para sair)')
    try:
        while running:
            for msg in consumer:
                try:
                    value = msg.value.decode('utf-8') if isinstance(msg.value, (bytes, bytearray)) else str(msg.value)
                except Exception:
                    value = str(msg.value)
                print(f'A mensagem chegou: {value}')
                # loop continuará consumindo
                if not running:
                    break
            # sleep curto para não travar CPU se não houver mensagens
            time.sleep(0.5)
    except KeyboardInterrupt:
        print('KeyboardInterrupt — encerrando consumidor.')
    finally:
        try:
            consumer.close()
        except Exception:
            pass
        print('Consumidor finalizado.')

if __name__ == '__main__':
    signal.signal(signal.SIGTERM, _handle_signal)
    signal.signal(signal.SIGINT, _handle_signal)
    consume_loop()
