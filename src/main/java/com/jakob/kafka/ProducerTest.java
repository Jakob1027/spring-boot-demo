package com.jakob.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerTest {

    private static Properties producerProps = new Properties();

    static {
        producerProps.put("bootstrap.servers", "192.168.118.79:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }


    public static void main(String[] args) {
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);
        ProducerRecord<String, String> r1 = new ProducerRecord<>("CustomerCountry", "Precision Products", "France");
        try {
            producer.send(r1).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProducerRecord<String, String> r2 = new ProducerRecord<>("CustomerCountry", "Biomedical Materials", "USA");
        producer.send(r2, (rm, e) -> {
            if (e != null)
                e.printStackTrace();
        });
    }
}
