package ru.angelich.marketapp.models;

public enum Action {
    PLUS,
    MINUS;

    public static Action fromString(String action) {
        return switch (action) {
            case "PLUS" -> PLUS;
            case "MINUS" -> MINUS;
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
