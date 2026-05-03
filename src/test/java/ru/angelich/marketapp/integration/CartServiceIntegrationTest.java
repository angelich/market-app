package ru.angelich.marketapp.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.angelich.marketapp.BaseIntegrationTest;
import ru.angelich.marketapp.models.Action;
import ru.angelich.marketapp.models.Cart;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.ItemDto;
import ru.angelich.marketapp.repositories.CartRepository;
import ru.angelich.marketapp.repositories.ItemRepository;
import ru.angelich.marketapp.services.CartService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item testItem;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        cartRepository.deleteAll();
        itemRepository.deleteAll();

        testItem = new Item();
        testItem.setTitle("Cart Test Item");
        testItem.setDescription("Cart Test Description");
        testItem.setPrice(200L);
        testItem.setImgPath("/img/cart-test.jpg");
        testItem = itemRepository.save(testItem);

        testCart = new Cart();
        testCart.setId(1L);
        testCart.setItems(new ArrayList<>(List.of(testItem)));
        testCart = cartRepository.save(testCart);
    }

    @Test
    void getCartItems_IntegrationTest_ReturnsCartItems() {
        List<ItemDto> items = cartService.getCartItems();

        assertNotNull(items);
        assertTrue(items.size() > 0);
    }

    @Test
    void updateItemCount_WithPlus_IntegrationTest_AddsItem() {
        int initialCount = testCart.getItems().size();

        cartService.updateItemCount(testItem.getId(), Action.PLUS);

        Cart updatedCart = cartRepository.findById(testCart.getId()).orElseThrow();
        assertEquals(initialCount + 1, updatedCart.getItems().size());
    }

    @Test
    void updateItemCount_WithMinus_IntegrationTest_RemovesItem() {
        int initialCount = testCart.getItems().size();

        cartService.updateItemCount(testItem.getId(), Action.MINUS);

        Cart updatedCart = cartRepository.findById(testCart.getId()).orElseThrow();
        assertEquals(initialCount - 1, updatedCart.getItems().size());
    }

    @Test
    void updateItemCount_WithDelete_IntegrationTest_RemovesAllItems() {
        cartService.updateItemCount(testItem.getId(), Action.DELETE);

        Cart updatedCart = cartRepository.findById(testCart.getId()).orElseThrow();
        assertEquals(0, updatedCart.getItems().size());
    }

    @Test
    void clearCart_IntegrationTest_ClearsAllItems() {
        cartService.clearCart();

        Cart updatedCart = cartRepository.findById(testCart.getId()).orElseThrow();
        assertEquals(0, updatedCart.getItems().size());
    }
}

