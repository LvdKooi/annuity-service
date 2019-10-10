package nl.kooi.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Laurens van der Kooi on 10/10/19.
 */

@Slf4j
@SpringBootApplication
public class AnnuityApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnnuityApplication.class, args);
    }

}