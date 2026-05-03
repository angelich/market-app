package ru.angelich.marketapp.models;

public record ItemDto(
        Long id,
        String title,
        String description,
        String imgPath,
        long price,
        long count
) {
}
