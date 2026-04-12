package ru.angelich.marketapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.ItemsResponse;
import ru.angelich.marketapp.models.ItemsSort;
import ru.angelich.marketapp.models.Paging;
import ru.angelich.marketapp.repositories.ItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item getItemById(Long id) {
        return itemRepository.findItemById(id)
                 .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));
    }

    public ItemsResponse findItems(String search, ItemsSort sort, Integer pageNumber, Integer pageSize) {
        if (pageNumber == null) pageNumber = 1;
        if (pageSize == null) pageSize = 5;

        Sort springSort = switch (sort) {
            case ALPHA -> Sort.by("title");
            case PRICE -> Sort.by("price");
            case NO -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, springSort);
        Page<Item> page = itemRepository.findItems(search, pageable);

        List<Item> flatItems = page.getContent();
        // вынести в отдельную функцию
        List<List<Item>> groupedItems = new ArrayList<>();
        for (int i = 0; i < flatItems.size(); i += 3) {
            List<Item> row = new ArrayList<>();
            for (int j = 0; j < 3 && i + j < flatItems.size(); j++) {
                row.add(flatItems.get(i + j));
            }

            while (row.size() < 3) {
                row.add(new Item(-1L, "", "", "", 0L, 0));
            }
            groupedItems.add(row);
        }

        boolean hasPrevious = pageNumber > 1;
        boolean hasNext = page.hasNext();

        Paging paging = new Paging(pageSize, pageNumber, hasPrevious, hasNext);

        return new ItemsResponse(groupedItems, paging);
    }
}
