package com.jakob.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG;

public class ProducerTest {

    public static void main(String[] args) {
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", args[0]);
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(COMPRESSION_TYPE_CONFIG, "snappy");

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
