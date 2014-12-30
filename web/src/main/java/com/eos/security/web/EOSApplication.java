package com.eos.security.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.eos.security")
@ImportResource("eos-security.xml")
public class EOSApplication {

	public static void main(String[] args) {
		SpringApplication.run(EOSApplication.class, args);
	}
}
