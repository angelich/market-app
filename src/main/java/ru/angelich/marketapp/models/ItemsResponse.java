package ru.angelich.marketapp.models;

import java.util.List;

public record ItemsResponse(
        List<List<ItemDto>> items,
        Paging paging
) {
}
