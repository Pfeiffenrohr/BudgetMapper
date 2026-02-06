package de.lechner.budgetmapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BudgetMapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetMapperApplication.class, args);
    }

}
