package com.dimanche.kick;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.dimanche.kick.modules.Client;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.dimanche.kick.repository.ClientRepository;


@SpringBootApplication
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);

	}
	
	@Bean
	CommandLineRunner start (ClientRepository clientRepository) {
	
		List<Client> clients =  generationListClient();
		clientRepository.saveAll(clients);
		// Stream.of(clients).forEach(cu ->{clientRepository.save(cu);});
		System.out.print("*********************************************");
		clientRepository.findAll().forEach(System.out::println);
		return null;
	}

	private List<Client> generationListClient() {
		List<Client> clients = new ArrayList<>();
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
		for (int x = 0; x < 1000; x++) {
		    String generatedString = random.ints(leftLimit, rightLimit + 1)
		  	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		  	      .limit(targetStringLength)
		  	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		  	      .toString();
		    clients.add(new Client(null,generatedString));
		}
		return clients;
	}

}
