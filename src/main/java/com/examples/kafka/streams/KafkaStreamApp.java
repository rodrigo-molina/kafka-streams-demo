package com.examples.kafka.streams;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.Properties;

import static com.examples.kafka.streams.utils.KafkaWindowsUtils.deleteTemporalFolderIfWindows;

public class KafkaStreamApp {

    private final static String applicationId = "KafkaStreamApps";
    private final static String clientIdConfig = "MyClientIdConfiguration";
    private final static String bootstrapServers = "localhost:9092";
    private final static String inputTopic = "my-topic";
    private final static String outputTopic = "my-topic-out";


    public static void main(String[] args) {
        final KafkaStreamExample kafkaStreamExample = new KafkaStreamExample();
        final StreamsBuilder builder = kafkaStreamExample.createTopology(inputTopic, outputTopic);

        deleteTemporalFolderIfWindows(applicationId);

        final Properties streamsConfiguration = KafkaPropertiesBuilder.build(applicationId, clientIdConfig, bootstrapServers);
        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); // Add shutdown hook to stop the Kafka Streams threads.

        streams.cleanUp();
        streams.start();
    }
}
