package com.examples.kafka.streams;

import com.examples.kafka.streams.utils.YamlLoader;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.Properties;

import static com.examples.kafka.streams.utils.KafkaWindowsUtils.deleteTemporalFolderIfWindows;

public class KafkaStreamApp {

    public static void main(String[] args) {
        final YamlLoader yamlLoader = new YamlLoader();
        final KafkaConfigurationProperties kafkaConfigurationProperties = yamlLoader.readYaml("src/main/resources/application.yaml", KafkaConfigurationProperties.class);

        final KafkaStreamExample kafkaStreamExample = new KafkaStreamExample();
        final StreamsBuilder builder = kafkaStreamExample.createTopology(kafkaConfigurationProperties.getInputTopic(), kafkaConfigurationProperties.getOutputTopic());

        deleteTemporalFolderIfWindows(kafkaConfigurationProperties.getApplicationId());

        final Properties streamsConfiguration = KafkaPropertiesBuilder.build(kafkaConfigurationProperties.getApplicationId(), kafkaConfigurationProperties.getClientIdConfig(), kafkaConfigurationProperties.getBootstrapServers());
        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); // Add shutdown hook to stop the Kafka Streams threads.

        streams.cleanUp();
        streams.start();
    }
}
