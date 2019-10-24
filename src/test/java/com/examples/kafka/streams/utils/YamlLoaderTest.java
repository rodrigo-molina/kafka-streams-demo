package com.examples.kafka.streams.utils;

import com.examples.kafka.streams.KafkaConfigurationProperties;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class YamlLoaderTest {
    @Test
    public void it_should_load_properties_file_configuration() {
        final YamlLoader yamlLoader = new YamlLoader();

        final KafkaConfigurationProperties mappedConfig = yamlLoader.readYaml("src/test/resources/application.yaml", KafkaConfigurationProperties.class);

        assertEquals(new KafkaConfigurationProperties(
                "TestKafkaStreamApps",
                "TestMyClientIdConfiguration",
                "localhost:9092",
                "my-topic",
                "my-topic-out"), mappedConfig);
    }
}
