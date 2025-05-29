package Clase3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // This is the main annotation for a Spring Boot application. It combines:
// - @Configuration: Tags the class as a source of bean definitions.
// - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings,
//   other beans, and various property settings.
// - @ComponentScan: Tells Spring to look for other components, configurations, and services
//   in the `Clase3` package (and its sub-packages), allowing it to find your
//   @Repository, @Service, and @RestController classes.
public class Application {

    public static void main(String[] args) {
        // This is the entry point of your Spring Boot application.
        // SpringApplication.run() bootstraps and launches a Spring application.
        SpringApplication.run(Application.class, args);
    }

}