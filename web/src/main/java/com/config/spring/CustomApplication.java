package com.config.spring;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sb_jwt_secu.utils.CustomUserProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com"} )
@EntityScan( basePackages = {"com"} )
@ComponentScan( basePackages = {"com" } )
@EnableConfigurationProperties({
        CustomUserProperties.class
})
public class CustomApplication {

    public static void main(String[] args){
        SpringApplication.run(CustomApplication.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    JsonFactory jsonFactory() {
        return new JacksonFactory();
    }

    @Bean
    HttpTransport httpTransport() {
        return new NetHttpTransport();
    }

}
