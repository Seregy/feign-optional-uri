package com.seregy77;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FeignOptionalUriApplication {

  public static void main(String[] args) {
    SpringApplication.run(FeignOptionalUriApplication.class, args);
  }
}
