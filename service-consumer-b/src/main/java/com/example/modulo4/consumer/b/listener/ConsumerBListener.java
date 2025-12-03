package com.example.modulo4.consumer.b.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBListener {

    @KafkaListener(
            topics = "${KAFKA_TOPIC}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String,String> record) {
        String key = record.key();
        String value = record.value();
        int partition = record.partition();
        long offset = record.offset();
        System.out.printf(
                "Consumer-B recebeu: chave=%s mensagem=%s partition=%d offset=%d%n",
                key, value, partition, offset
        );
    }
}
