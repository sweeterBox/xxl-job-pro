package com.xxl.job.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"com.xxl.job"})
@SpringBootApplication
public class XxlJobClientSpringApplication {

	public static void main(String[] args) {
        SpringApplication.run(XxlJobClientSpringApplication.class, args);
	}

}
