package com.lawfirm.apps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.lawfirm")
public class LawFirmApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LawFirmApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LawFirmApplication.class);
    }
//https://www.tutorialspoint.com/spring_boot/spring_boot_cors_support.htm
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/auth").allowedOrigins("http://localhost:8080");
                registry.addMapping("/employee").allowedOrigins("http://localhost:8080");
                registry.addMapping("/engagement").allowedOrigins("http://localhost:8080");
                registry.addMapping("/loan").allowedOrigins("http://localhost:8080");
            }
        };
    }
}
