package com.jakob.kafka;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class ConsumerTest {

    private static Properties consumerProp = new Properties();

    static {
        consumerProp.put("bootstrap.servers", "192.168.118.79:9092");
        consumerProp.put("group.id", "CountryCounter");
        consumerProp.put("auto.commit.offset", "false");
        consumerProp.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProp.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProp);
        consumer.subscribe(Collections.singletonList("CustomerCountry"));
        Map<String, Integer> count = new HashMap<>();
        Gson gson = new Gson();
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    log.debug("topic={}, partition={}, offset={}, customer={}, country={}\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    int value = count.getOrDefault(record.value(), 0);
                    count.put(record.value(), value + 1);
                    String json = gson.toJson(count);
                    System.out.println(json);
                }
                try {
                    consumer.commitSync();
                } catch (Exception e) {
                    log.error("Commit failed");
                }
            }

        } finally {
            consumer.close();
        }
    }
}
