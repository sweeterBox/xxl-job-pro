package com.xxl.job.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.xxl.job")
@EnableDiscoveryClient
@SpringBootApplication
public class XxlJobProAdminCloudApplication {

	public static void main(String[] args) {
        SpringApplication.run(XxlJobProAdminApplication.class, args);
	}

}
