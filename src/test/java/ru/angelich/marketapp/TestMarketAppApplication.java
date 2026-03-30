package ru.angelich.marketapp;

import org.springframework.boot.SpringApplication;

public class TestMarketAppApplication {

    public static void main(String[] args) {
        SpringApplication.from(MarketAppApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
