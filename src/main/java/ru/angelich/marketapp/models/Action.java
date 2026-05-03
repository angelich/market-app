package ru.angelich.marketapp.models;

public enum Action {
    PLUS,
    MINUS,
    DELETE;

    public static Action fromString(String action) {
        return switch (action) {
            case "PLUS" -> PLUS;
            case "MINUS" -> MINUS;
            case "DELETE" -> DELETE;
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
