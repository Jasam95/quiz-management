package com.example.quiz_management;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.quiz_management")
@EntityScan(basePackages = "com.example.quiz_management.entity")
@EnableJpaRepositories(basePackages = "com.example.quiz_management.repository")

public class   QuizManagementApplication {

	private PasswordEncoder passwordEncoder;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper(){

		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(QuizManagementApplication.class, args);
	}

}
