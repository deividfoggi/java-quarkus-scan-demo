package org.example;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            name = "World";
        }
        return "Hello, " + name + "!";
    }

    public String greetInLanguage(String name, String language) {
        if (name == null || name.isEmpty()) {
            name = "World";
        }
        
        return switch (language.toLowerCase()) {
            case "pt" -> "Olá, " + name + "!";
            case "es" -> "Hola, " + name + "!";
            case "fr" -> "Bonjour, " + name + "!";
            default -> "Hello, " + name + "!";
        };
    }
}
