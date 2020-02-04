package com.github.wladox.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppConfig {

    private static final Integer NTHREADS = 20;
    private static final Integer PORT = 8080;
    private static final String HOST = "localhost";

    private Integer port;
    private String host;
    private Integer numberOfThreads;

    public static AppConfig getDefault() {
        return AppConfig.builder()
                .host(HOST)
                .numberOfThreads(NTHREADS)
                .port(PORT)
                .build();
    }
}
