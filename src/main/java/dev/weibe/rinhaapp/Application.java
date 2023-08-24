package dev.weibe.rinhaapp;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.Executors;

@SpringBootApplication
public class Application {

    @Bean
    public TomcatProtocolHandlerCustomizer<?> tomcatProtocolHandlerCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

    @Bean
    public ApplicationRunner applicationRunner(JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.queryForObject("select 1+1", Long.class);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
