package mypicday.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyPicDayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyPicDayApplication.class, args);
	}

}
