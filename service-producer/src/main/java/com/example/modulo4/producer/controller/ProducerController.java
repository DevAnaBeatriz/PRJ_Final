package com.example.modulo4.producer.controller;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class ProducerController {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final String topic;

    public ProducerController(KafkaTemplate<String, String> kafkaTemplate,
                            @Value("${KAFKA_TOPIC}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }


    public static record Payload(String key, String message) {}

    @PostMapping("/produce")
    public ResponseEntity<String> produce(@RequestBody Payload payload) throws ExecutionException, InterruptedException {
        SendResult<String,String> result = kafkaTemplate.send(topic, payload.key(), payload.message()).get();
        RecordMetadata meta = result.getRecordMetadata();
        String resp = String.format("Mensagem enviada: offset=%d / partition=%d", meta.offset(), meta.partition());
        System.out.println(resp);
        return ResponseEntity.ok(resp);
    }
}
