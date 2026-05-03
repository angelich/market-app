package ru.angelich.marketapp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.angelich.marketapp.models.*;
import ru.angelich.marketapp.repositories.CartRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private CartProvider cartProvider;

    @InjectMocks
    private CartService cartService;

    private Cart testCart;
    private Item testItem;

    @BeforeEach
    void setUp() {
        testItem = new Item(1L, "Test Item", "Test Description", "/img/test.jpg", 100L);
        testCart = new Cart(1L, new ArrayList<>(List.of(testItem, testItem)));
    }

    @Test
    void getCartItems_ReturnsGroupedItems() {
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);
        when(itemService.getItemByIdOrThrow(1L)).thenReturn(testItem);

        List<ItemDto> result = cartService.getCartItems();

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(1L, result.get(0).id());
        assertEquals(2, result.get(0).count());
    }

    @Test
    void updateItemCount_WithPlusAction_AddsItem() {
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);
        when(itemService.getItemByIdOrThrow(1L)).thenReturn(testItem);

        cartService.updateItemCount(1L, Action.PLUS);

        verify(cartRepository, times(1)).save(testCart);
        assertEquals(3, testCart.getItems().size());
    }

    @Test
    void updateItemCount_WithMinusAction_RemovesOneItem() {
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);
        when(itemService.getItemByIdOrThrow(1L)).thenReturn(testItem);

        cartService.updateItemCount(1L, Action.MINUS);

        verify(cartRepository, times(1)).save(testCart);
        assertEquals(1, testCart.getItems().size());
    }

    @Test
    void updateItemCount_WithDeleteAction_RemovesAllItems() {
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);
        when(itemService.getItemByIdOrThrow(1L)).thenReturn(testItem);

        cartService.updateItemCount(1L, Action.DELETE);

        verify(cartRepository, times(1)).save(testCart);
        assertEquals(0, testCart.getItems().size());
    }

    @Test
    void clearCart_ClearsAllItems() {
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);

        cartService.clearCart();

        verify(cartRepository, times(1)).save(testCart);
        assertEquals(0, testCart.getItems().size());
    }
}

