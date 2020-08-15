package com.jakob.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerTest {
    private static Properties kafkaProps = new Properties();

    static {
        kafkaProps.put("bootstrap.servers", "172.22.107.20:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    public static void main(String[] args) throws InterruptedException {

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps);
        ProducerRecord<String, String> record1 = new ProducerRecord<>("CustomerCountry", "Precision Products", "France");
        ProducerRecord<String, String> record2 = new ProducerRecord<>("CustomerCountry", "Biomedical Materials", "USA");
        try {
            producer.send(record1);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        producer.send(record2, (rm, e) -> {
            if (e != null) e.printStackTrace();
        });
        Thread.sleep(1000);
    }
}
