package com.eos.security.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.eos.security")
public class EOSApplication {

	public static void main(String[] args) {
		SpringApplication.run(EOSApplication.class, args);
	}
}
