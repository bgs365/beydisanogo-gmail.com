package com.config.initData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication public class InitAll {

    private SuperUserIntiator superUserIntiator;

    public InitAll(SuperUserIntiator superUserIntiator) {
        this.superUserIntiator = superUserIntiator;
    }

    @PostConstruct
    public void init(){
        superUserIntiator.createCustomUser();
    }

    public static void main(String[] args) {
        SpringApplication.run(InitAll.class, args);
    }
}
