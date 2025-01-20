package com.quizlabs.algebrator;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlgebratorApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AlgebratorApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", getPort()));
        app.run(args);
    }

    private static String getPort() {
        String port = System.getenv("PORT");
        return port != null ? port : "8080";
    }
}
