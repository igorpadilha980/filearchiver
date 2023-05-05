package com.github.igorpadilha980.filearchiver.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableJpaRepositories
@EnableAutoConfiguration
public class FileArchiverRest {

    public static void main(String[] args) {
        SpringApplication.run(FileArchiverRest.class, args);
    }

}
