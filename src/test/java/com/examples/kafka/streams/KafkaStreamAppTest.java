package com.examples.kafka.streams;

import com.examples.kafka.streams.utils.YamlLoader;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.apache.kafka.streams.test.OutputVerifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.Random;

import static com.examples.kafka.streams.utils.KafkaWindowsUtils.deleteTemporalFolderIfWindows;
import static org.apache.kafka.streams.StreamsConfig.STATE_DIR_CONFIG;

public class KafkaStreamAppTest {
    public static final String STATE_DIRECTORY = "test-garbage";

    private TopologyTestDriver testDriver;
    private KafkaConfigurationProperties kafkaConfigurationProperties;

    @Before
    public void setUp() {
        final YamlLoader yamlLoader = new YamlLoader();
        kafkaConfigurationProperties = yamlLoader.readYaml("src/test/resources/application.yaml", KafkaConfigurationProperties.class);

        final KafkaStreamExample kafkaStreamExample = new KafkaStreamExample();
        final StreamsBuilder builder = kafkaStreamExample.createTopology(kafkaConfigurationProperties.getInputTopic(), kafkaConfigurationProperties.getOutputTopic());

        deleteTemporalFolderIfWindows(kafkaConfigurationProperties.getApplicationId());

        final Properties streamsConfiguration = KafkaPropertiesBuilder.build(kafkaConfigurationProperties.getApplicationId(), kafkaConfigurationProperties.getClientIdConfig(), kafkaConfigurationProperties.getBootstrapServers());
        streamsConfiguration.setProperty(STATE_DIR_CONFIG, STATE_DIRECTORY + "/test-state" + (new Random()).nextInt()); // only for testing reasons

        final Topology topology = builder.build();
        System.out.println(topology.describe());
        testDriver = new TopologyTestDriver(topology, streamsConfiguration);

    }

    @Test
    public void it_should_publish_upper_case_message() {
        ConsumerRecordFactory record = givenConsumer(kafkaConfigurationProperties.getInputTopic());
        givenTopicMessage(record, kafkaConfigurationProperties.getInputTopic(), "key", "Pepe");

        whenMessageIsProcessed();

        thenAMessageIsPublishedInTopic(kafkaConfigurationProperties.getOutputTopic(), "key", "PEPE");
    }

    @Test
    public void it_should_not_publish_message_when_odd_input_length() {
        ConsumerRecordFactory record = givenConsumer(kafkaConfigurationProperties.getInputTopic());
        givenTopicMessage(record, kafkaConfigurationProperties.getInputTopic(), "key", "Test me");

        whenMessageIsProcessed();

        thenNoMessageIsPublishedInTopic(kafkaConfigurationProperties.getOutputTopic());
    }

    private void whenMessageIsProcessed() {
        // do nothing! It's already declared
    }


    public ConsumerRecordFactory givenConsumer(String topic) {
        return new ConsumerRecordFactory(topic, new StringSerializer(), new StringSerializer());
    }

    public <Z, T> void givenTopicMessage(ConsumerRecordFactory<Z, T> consumerFactory, String topic, Z key, T message) {
        testDriver.pipeInput(consumerFactory.create(topic, key, message));
    }

    public void thenAMessageIsPublishedInTopic(String topic, String key, String message) {
        ProducerRecord<String, String> outputRecord = testDriver.readOutput(topic, new StringDeserializer(), new StringDeserializer());
        OutputVerifier.compareKeyValue(outputRecord, key, message);
    }

    public void thenNoMessageIsPublishedInTopic(String topic) {
        Assert.assertNull(testDriver.readOutput(topic, new StringDeserializer(), new StringDeserializer()));
    }
}
