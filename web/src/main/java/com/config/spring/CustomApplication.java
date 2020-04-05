package com.config.spring;

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

}
