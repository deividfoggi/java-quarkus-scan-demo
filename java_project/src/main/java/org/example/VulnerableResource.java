package org.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This class contains intentional security vulnerabilities for CodeQL testing.
 * DO NOT use this code in production!
 */
@Path("/vulnerable")
public class VulnerableResource {

    // SQL Injection vulnerability
    @GET
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUserInfo(@QueryParam("id") String userId) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
            Statement stmt = conn.createStatement();
            
            // VULNERABLE: Direct concatenation of user input into SQL query
            String query = "SELECT * FROM users WHERE id = '" + userId + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return "User: " + rs.getString("name");
            }
            return "User not found";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Path Traversal vulnerability
    @GET
    @Path("/file")
    @Produces(MediaType.TEXT_PLAIN)
    public String readFile(@QueryParam("filename") String filename) {
        try {
            // VULNERABLE: No validation of filename - allows path traversal
            File file = new File("/tmp/" + filename);
            return Files.readString(file.toPath());
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    // Command Injection vulnerability
    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String pingHost(@QueryParam("host") String host) {
        try {
            // VULNERABLE: Executing OS command with user input
            Process process = Runtime.getRuntime().exec("ping -c 1 " + host);
            process.waitFor();
            return "Ping completed";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Hardcoded credentials vulnerability
    private static final String DB_PASSWORD = "supersecret123";
    private static final String API_KEY = "1234567890abcdef";

    @GET
    @Path("/connect")
    @Produces(MediaType.TEXT_PLAIN)
    public String connect() {
        // VULNERABLE: Hardcoded credentials
        return "Connecting with password: " + DB_PASSWORD;
    }
}
