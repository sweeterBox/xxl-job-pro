package com.xxl.job.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@ComponentScan("com.xxl.job")
@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
public class XxlJobAdminCloudApplication {

	public static void main(String[] args) {
        SpringApplication.run(XxlJobProAdminApplication.class, args);
	}

}
