package com.examples.kafka.streams;

import com.examples.kafka.streams.utils.YamlLoader;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

import java.util.Properties;
import java.util.function.BiConsumer;

import static com.examples.kafka.streams.utils.KafkaWindowsUtils.deleteTemporalFolderIfWindows;

public class KafkaStreamApp {

    public static void main(String[] args) {
        final BiConsumer<Properties, Topology> runTopology = (streamsConfiguration, topology) -> {
            final KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration);

            Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); // Add shutdown hook to stop the Kafka Streams threads.

            streams.cleanUp();
            streams.start();
        };

        runApp("src/main/resources/application.yaml", runTopology);
    }

    public static void runApp(final String yamlFile, final BiConsumer<Properties, Topology> runTopology) {
        final YamlLoader yamlLoader = new YamlLoader();
        final KafkaConfigurationProperties kafkaConfigurationProperties = yamlLoader.readYaml(yamlFile, KafkaConfigurationProperties.class);

        final KafkaStreamExample kafkaStreamExample = new KafkaStreamExample();
        final StreamsBuilder builder = kafkaStreamExample.createStream(kafkaConfigurationProperties.getInputTopic(), kafkaConfigurationProperties.getOutputTopic());

        deleteTemporalFolderIfWindows(kafkaConfigurationProperties.getApplicationId());

        final Properties streamsConfiguration = KafkaPropertiesBuilder.build(kafkaConfigurationProperties.getApplicationId(), kafkaConfigurationProperties.getClientIdConfig(), kafkaConfigurationProperties.getBootstrapServers());
        final Topology topology = builder.build();

        runTopology.accept(streamsConfiguration, topology);
    }
}
