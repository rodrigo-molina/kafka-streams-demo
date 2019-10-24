package com.examples.kafka.streams;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;

import static com.examples.kafka.streams.utils.KafkaUtils.STRING_SERDES;
import static com.examples.kafka.streams.utils.KafkaUtils.printValueForStep;

public class KafkaStreamExample {

    private final StreamsBuilder builder = new StreamsBuilder();

    public StreamsBuilder createStream(final String inputTopic, final String outputTopic) {

        builder.stream(inputTopic, Consumed.with(STRING_SERDES, STRING_SERDES))
                .peek(printValueForStep("EVENT RECEIVED"))
                .map(uppercaseValue)
                .peek(printValueForStep("EVENT MAPPED TO UPPERCASE"))
                .filter(isValueLengthEven)
                .peek(printValueForStep("EVENT FILTERED"))
                .to(outputTopic, Produced.with(STRING_SERDES, STRING_SERDES));

        return builder;
    }

    final KeyValueMapper<String, String, KeyValue<? extends String, ? extends String>> uppercaseValue =
            (key, value) -> KeyValue.pair(key, value.toUpperCase());
    final Predicate<String, String> isValueLengthEven =
            (key, value) -> value.length() % 2 == 0;
}
