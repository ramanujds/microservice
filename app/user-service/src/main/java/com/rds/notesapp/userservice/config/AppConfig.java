package com.rds.notesapp.userservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean(name = "restTemplateLoadBalanced")
    @LoadBalanced
    public RestTemplate restTemplateLoadBalanced() {
        return new RestTemplate();
    }

    @Bean(name = "restTemplate")
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
