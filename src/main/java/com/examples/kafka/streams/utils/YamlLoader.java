package com.examples.kafka.streams.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlLoader {
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public <T> T readYaml(final String sourceFile, Class<T> valueType) {
        try {
            return mapper.readValue(new File(sourceFile), valueType);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading yaml file", e);
        }

    }
}
