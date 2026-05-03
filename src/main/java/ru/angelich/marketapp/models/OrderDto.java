package ru.angelich.marketapp.models;

import java.util.List;

public record OrderDto(
        long id,
        List<ItemDto> items,
        long totalSum
) {
}
