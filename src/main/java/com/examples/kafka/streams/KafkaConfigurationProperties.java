package com.examples.kafka.streams;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class KafkaConfigurationProperties {
    private String applicationId;
    private String clientIdConfig;
    private String bootstrapServers;
    private String inputTopic;
    private String outputTopic;

    @JsonCreator
    public KafkaConfigurationProperties(@JsonProperty("applicationId") String applicationId,
                                        @JsonProperty("clientIdConfig") String clientIdConfig,
                                        @JsonProperty("bootstrapServers") String bootstrapServers,
                                        @JsonProperty("inputTopic") String inputTopic,
                                        @JsonProperty("outputTopic") String outputTopic) {
        this.applicationId = applicationId;
        this.clientIdConfig = clientIdConfig;
        this.bootstrapServers = bootstrapServers;
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getClientIdConfig() {
        return clientIdConfig;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }


    public String getInputTopic() {
        return inputTopic;
    }


    public String getOutputTopic() {
        return outputTopic;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KafkaConfigurationProperties)) return false;
        KafkaConfigurationProperties that = (KafkaConfigurationProperties) o;
        return Objects.equals(applicationId, that.applicationId) &&
                Objects.equals(clientIdConfig, that.clientIdConfig) &&
                Objects.equals(bootstrapServers, that.bootstrapServers) &&
                Objects.equals(inputTopic, that.inputTopic) &&
                Objects.equals(outputTopic, that.outputTopic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, clientIdConfig, bootstrapServers, inputTopic, outputTopic);
    }
}
