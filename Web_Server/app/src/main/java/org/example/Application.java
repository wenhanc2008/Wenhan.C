package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().directory("/Users/hanbao/cheng_wenhan_002330833_assignments/WebServer").load();

        // 获取环境变量
        String databaseUrl = dotenv.get("DATABASE_URL");
        String databaseUser = dotenv.get("DATABASE_USERNAME");
        String databasePassword = dotenv.get("DATABASE_PASSWORD");

        // 将 Dotenv 环境变量传递给 Spring Boot
        System.setProperty("DATABASE_URL", databaseUrl);
        System.setProperty("DATABASE_USERNAME", databaseUser);
        System.setProperty("DATABASE_PASSWORD", databasePassword);

        // 打印检查
        System.out.println("Database URL: " + databaseUrl);
        System.out.println("Database Username: " + databaseUser);
        System.out.println("Database Password: " + databasePassword);

        SpringApplication.run(Application.class, args);
    }
}
