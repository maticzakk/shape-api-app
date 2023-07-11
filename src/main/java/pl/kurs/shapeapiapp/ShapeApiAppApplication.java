package pl.kurs.shapeapiapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EntityScan("pl.kurs.shapeapiapp.model")
public class ShapeApiAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShapeApiAppApplication.class, args);
    }

}
