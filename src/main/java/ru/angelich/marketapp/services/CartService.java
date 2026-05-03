package ru.angelich.marketapp.services;

import org.springframework.stereotype.Service;
import ru.angelich.marketapp.models.Action;
import ru.angelich.marketapp.models.Cart;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.ItemDto;
import ru.angelich.marketapp.repositories.CartRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final CartProvider cartProvider;


    public CartService(CartRepository cartRepository, ItemService itemService, CartProvider cartProvider) {
        this.cartRepository = cartRepository;
        this.itemService = itemService;
        this.cartProvider = cartProvider;
    }


    public List<ItemDto> getCartItems() {
        Cart cart = cartProvider.getOrCreateSingletonCart();

        Map<Long, Long> itemCounts = cart.getItems().stream()
                .collect(Collectors.groupingBy(Item::getId, Collectors.counting()));

        return itemCounts.entrySet().stream()
                .map(entry -> {
                    Item item = itemService.getItemByIdOrThrow(entry.getKey());
                    return new ItemDto(item.getId(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getPrice(), entry.getValue().intValue());
                })
                .toList();
    }

    public void updateItemCount(Long id, Action action) {
        Item item = itemService.getItemByIdOrThrow(id);
        Cart cart = cartProvider.getOrCreateSingletonCart();
        List<Item> items = cart.getItems();

        switch (action) {
            case PLUS -> items.add(item);
            case MINUS -> items.stream().filter(i -> i.getId().equals(id)).findFirst().ifPresent(items::remove);
            case DELETE -> items.removeIf(i -> i.getId().equals(id));
        }

        cartRepository.save(cart);
    }

    public void clearCart() {
        Cart cart = cartProvider.getOrCreateSingletonCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
