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
                registry.addMapping("/auth").allowedOrigins("*");
                registry.addMapping("/employee").allowedOrigins("*");
                registry.addMapping("/engagement").allowedOrigins("*");
                registry.addMapping("/loan").allowedOrigins("*");
                registry.addMapping("/reimbursement").allowedOrigins("*");
                registry.addMapping("/reimbursements").allowedOrigins("*");
                registry.addMapping("/case").allowedOrigins("*");
                registry.addMapping("/disburse").allowedOrigins("*");
                registry.addMapping("/disbursement").allowedOrigins("*");
                registry.addMapping("/**").allowedOrigins("*");

            }
        };
    }
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/auth").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/employee").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/engagement").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/loan").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/reimbursement").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/reimbursements").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/case").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/disburse").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/disbursement").allowedOrigins("http://localhost:8080");
//                registry.addMapping("/**").allowedOrigins("http://localhost:8080");
//
//            }
//        };
//    }

//        @Bean
//    	public MultipartConfigElement multipartConfigElement() {
//    	  MultipartConfigFactory factory = new MultipartConfigFactory();
//    	  // single file maximum
//    	  factory.setMaxFileSize(10240);
//    	   /// Set the total size of the total upload data
//    	  factory.setMaxRequestSize("100MB");
//    	  return factory.createMultipartConfig();
//    	}
}
