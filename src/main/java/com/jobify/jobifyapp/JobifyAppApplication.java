package com.jobify.jobifyapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jobify.controller", "com.jobify.service",
                               "com.jobify.config", "com.jobify.security",
                               "com.jobify.exception"})
@EntityScan(basePackages = {"com.jobify.entity"})
@EnableJpaRepositories(basePackages = {"com.jobify.repository"})
@EnableSwagger2
public class JobifyAppApplication
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        SpringApplication.run(JobifyAppApplication.class, args);
    }

    /**
     * @return
     */
    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }
}

