package com.promotion.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.promotion.engine"})
public class PromotionEngineApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(PromotionEngineApplication.class, args);

    }

}
