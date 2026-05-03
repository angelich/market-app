package ru.angelich.marketapp.models;

public record Paging(
        int pageSize,
        int pageNumber,
        boolean hasPrevious,
        boolean hasNext
) {
}
