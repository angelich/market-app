package ru.angelich.marketapp.services;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.angelich.marketapp.exceptions.ItemNotFoundException;
import ru.angelich.marketapp.models.*;
import ru.angelich.marketapp.models.SortItems;
import ru.angelich.marketapp.repositories.ItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final CartProvider cartProvider;

    public ItemService(ItemRepository itemRepository, CartProvider cartProvider) {
        this.itemRepository = itemRepository;
        this.cartProvider = cartProvider;
    }

    public Item getItemByIdOrThrow(Long id) {
        return itemRepository.findItemById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + id));
    }

    public ItemDto getItemDtoById(Long id) {
        Item item = getItemByIdOrThrow(id);
        return toDto(item);
    }

    public ItemsResponse findItems(String search, SortItems sort, Integer pageNumber, Integer pageSize) {

        Sort springSort = switch (sort) {
            case ALPHA -> Sort.by("title");
            case PRICE -> Sort.by("price");
            case NO -> Sort.unsorted();
        };

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, springSort);
        Page<Item> page = itemRepository.findItems(search, pageable);

        List<List<ItemDto>> groupedItems = getLists(page);

        boolean hasPrevious = pageNumber > 1;
        boolean hasNext = page.hasNext();

        Paging paging = new Paging(pageSize, pageNumber, hasPrevious, hasNext);

        return new ItemsResponse(groupedItems, paging);
    }

    private List<List<ItemDto>> getLists(Page<Item> page) {
        List<Item> flatItems = page.getContent();

        List<List<ItemDto>> groupedItems = new ArrayList<>();
        for (int i = 0; i < flatItems.size(); i += 3) {
            List<ItemDto> row = new ArrayList<>();
            for (int j = 0; j < 3 && i + j < flatItems.size(); j++) {
                Item item = flatItems.get(i + j);
                row.add(toDto(item));
            }

            while (row.size() < 3) {
                row.add(new ItemDto(-1L, "", "", "", 0L, 0));
            }
            groupedItems.add(row);
        }
        return groupedItems;
    }

    private ItemDto toDto(Item item) {
        Cart cart = cartProvider.getOrCreateSingletonCart();
        long count = cart.getItems()
                .stream()
                .filter(i -> i.getId().equals(item.getId()))
                .count();

        return new ItemDto(item.getId(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getPrice(), count);
    }
}
