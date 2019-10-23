package com.examples.kafka.streams.utils;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static java.util.Optional.ofNullable;

public class KafkaUtils {
    private static final Logger LOGGER = LogManager.getLogger(KafkaUtils.class);

    public static final String EMPTY_STRING = "<Null>";
    public static final Serde<String> STRING_SERDES = Serdes.String();


    private KafkaUtils() {
    }

    public static <K, V> ForeachAction<K, V> printValueForStep(final String step) {
        return (key, value) -> LOGGER.info(step + " key: " + toString(key) + ", value: " + toString(value));
    }


    private static <T> String toString(T object) {
        try {
            return ofNullable(object)
                    .map(T::toString)
                    .orElse(EMPTY_STRING);
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }
}
