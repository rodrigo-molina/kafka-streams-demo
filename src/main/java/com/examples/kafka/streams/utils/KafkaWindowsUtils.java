package com.examples.kafka.streams.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class KafkaWindowsUtils {

    public static final String OS_NAME_PROPERTY = "os.name";
    public static final String TMP_KAFKA_STREAMS_WINDOWS_DIRECTORY = "/tmp/kafka-streams/";

    private KafkaWindowsUtils() {
    }

    public static void deleteTemporalFolderIfWindows(final String applicationId) {
        if (System.getProperty(OS_NAME_PROPERTY).toLowerCase().contains("win")) {
            try {
                FileUtils.deleteDirectory(new File(TMP_KAFKA_STREAMS_WINDOWS_DIRECTORY + applicationId));
                FileUtils.forceMkdir(new File(TMP_KAFKA_STREAMS_WINDOWS_DIRECTORY + applicationId));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
