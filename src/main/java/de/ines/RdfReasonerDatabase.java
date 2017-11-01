package de.ines;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "de.ines.domain" })
@EnableJpaRepositories(basePackages = { "de.ines.repositories" })
public class RdfReasonerDatabase {

    public static void main(String[] args) {
        SpringApplication.run(RdfReasonerDatabase.class, args);
    }
}

