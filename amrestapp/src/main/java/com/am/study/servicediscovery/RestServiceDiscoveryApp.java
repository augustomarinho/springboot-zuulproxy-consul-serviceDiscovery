package com.am.study.servicediscovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@RestController
@EnableDiscoveryClient
public class RestServiceDiscoveryApp {

    private static final Logger logger = LoggerFactory.getLogger(RestServiceDiscoveryApp.class);

    public static void main(String args[]) {
        SpringApplication.run(RestServiceDiscoveryApp.class, args);
    }


    @GetMapping(value = "/lista/nomes")
    public ResponseEntity<String> listaNomes() {
        logger.info("Respondendo chamada");
        return new ResponseEntity<String>("Augusto Marinho", HttpStatus.OK);
    }
}