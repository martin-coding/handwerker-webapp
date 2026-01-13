package de.othr.hwwa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HwwaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HwwaApplication.class, args);
	}

}
