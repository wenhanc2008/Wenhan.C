package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
//class provides utility methods for managing the application’s database connection
// and ensuring the database structure is initialized.
public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:chat_history.db";
    //Ensures the database and the ChatHistory table are ready for use.
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                //The table includes the following columns:
                //	id: An auto-incrementing primary key.
                //	•	message: Stores the chat message text.
                //	•	isUser: A boolean indicating whether the message is from the user.
                //	•	timestamp: Defaults to the current date and time when a row is inserted.
                String createTableSQL = """
                        CREATE TABLE IF NOT EXISTS ChatHistory (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            message TEXT NOT NULL,
                            isUser BOOLEAN NOT NULL,
                            timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
                        );
                        """;
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSQL);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Provides a reusable method to create a new connection to the database using the defined DB_URL.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

}
