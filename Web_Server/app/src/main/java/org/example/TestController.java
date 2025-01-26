package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class TestController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/test-db")
    public String testDbConnection() {
        try {
            dataSource.getConnection().isValid(0);
            return "Database connection is successful!";
        } catch (Exception e) {
            return "Failed to connect to the database: " + e.getMessage();
        }
    }
}
