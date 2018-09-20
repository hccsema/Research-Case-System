package oil;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class OilApplication {

    public static void main(String[] args) {
        SpringApplication.run(OilApplication.class, args);
    }



}
