package eventticketsystem.preference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class PreferenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreferenceServiceApplication.class, args);
    }

}
