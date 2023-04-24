package node_value.project.sketch_transfer_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication @EnableScheduling
public class SketchTransferServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SketchTransferServerApplication.class, args);
	}
}
