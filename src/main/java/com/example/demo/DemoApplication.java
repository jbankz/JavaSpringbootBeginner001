package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate){

		return args -> {
			Address address =new Address(
					"Nigeria",
					"Abuja",
					"10000234"
			);

			String email = "jay@gmail.com";

			Student student = new Student("Jaycee", "Jaycee",
			 email, Gender.MALE, address,
			  List.of("Computer Science", "Maths"), BigDecimal.TEN, LocalDateTime.now());
			
			repository.findStudentByEmail(email)
			.ifPresentOrElse(s-> {
				System.out.println("Email address already exists " + email);
			}, ()->{
				repository.insert(student);
			} );
		};
	}

	private void usingMongoTemplateAndQury(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
		Query query = new Query();
		  query.addCriteria(Criteria.where("email").is(email));
		List<Student> students =  mongoTemplate.find(query, Student.class);
		
		if(students.size()> 1){
			throw new IllegalStateException("Found too many students with thesame email " + email);
		}

		if(students.isEmpty()){
			repository.insert(student);
		}else{
			System.out.println(students + " alredy exists");
		}
	}

}
