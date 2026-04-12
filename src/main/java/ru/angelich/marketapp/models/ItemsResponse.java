package ru.angelich.marketapp.models;

import java.util.List;

public record ItemsResponse(
        List<List<Item>> items,
        Paging paging
) {
}
