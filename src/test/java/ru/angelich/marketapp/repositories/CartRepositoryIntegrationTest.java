package ru.angelich.marketapp.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.angelich.marketapp.BaseIntegrationTest;
import ru.angelich.marketapp.models.Cart;
import ru.angelich.marketapp.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Cart testCart;

    @BeforeEach
    void setUp() {
        cartRepository.deleteAll();
        itemRepository.deleteAll();

        Item testItem = new Item();
        testItem.setTitle("Test Item");
        testItem.setDescription("Test Description");
        testItem.setPrice(100L);
        testItem.setImgPath("/path/to/image.jpg");
        testItem = itemRepository.save(testItem);

        testCart = new Cart();
        testCart.setId(1L);
        testCart.setItems(new ArrayList<>(List.of(testItem)));
        testCart = cartRepository.save(testCart);
    }

    @Test
    void saveCart_WithItems_SavesSuccessfully() {
        Cart retrievedCart = cartRepository.findById(testCart.getId()).orElse(null);

        assertNotNull(retrievedCart);
        assertEquals(1, retrievedCart.getItems().size());
        assertEquals("Test Item", retrievedCart.getItems().get(0).getTitle());
    }

    @Test
    void findCartById_WithValidId_ReturnsCart() {
        Optional<Cart> foundCart = cartRepository.findById(testCart.getId());

        assertTrue(foundCart.isPresent());
        assertEquals(1, foundCart.get().getItems().size());
    }

    @Test
    void updateCart_WithNewItems_UpdatesSuccessfully() {
        Cart cart = cartRepository.findById(testCart.getId()).orElseThrow();
        
        Item newItem = new Item();
        newItem.setTitle("New Item");
        newItem.setDescription("New Description");
        newItem.setPrice(200L);
        newItem.setImgPath("/path/to/new.jpg");
        newItem = itemRepository.save(newItem);

        cart.getItems().add(newItem);
        cartRepository.save(cart);

        Cart updatedCart = cartRepository.findById(testCart.getId()).orElseThrow();
        assertEquals(2, updatedCart.getItems().size());
    }
}

