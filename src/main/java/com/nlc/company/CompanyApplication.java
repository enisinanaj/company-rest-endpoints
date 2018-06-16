package com.nlc.company;

import com.nlc.company.repositories.BenificialOwnerRepository;
import com.nlc.company.repositories.CompanyRepository;
import com.nlc.company.resources.BenificialOwner;
import com.nlc.company.resources.Company;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class CompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);
	}

	@Bean
	CommandLineRunner init(CompanyRepository companyRepository, BenificialOwnerRepository benificialOwnerRepository) {
		return (evt) -> Arrays.asList(
				"jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
				.forEach(
					a -> {
						Company cmp = companyRepository.save(new Company(System.currentTimeMillis(), a, "Infinity Loop 1", "Palo Alto", "California",
								"info@apple.com", "+1 333 555 7830", new ArrayList<>()));

						benificialOwnerRepository.save(new BenificialOwner(null, a, cmp));
					});
	}
}
