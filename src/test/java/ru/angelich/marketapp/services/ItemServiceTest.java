package ru.angelich.marketapp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.angelich.marketapp.exceptions.ItemNotFoundException;
import ru.angelich.marketapp.models.*;
import ru.angelich.marketapp.repositories.ItemRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartProvider cartProvider;

    @InjectMocks
    private ItemService itemService;

    private Item testItem;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        testItem = new Item(1L, "Test Item", "Test Description", "/img/test.jpg", 100L);
        testCart = new Cart(1L, List.of());
    }

    @Test
    void getItemByIdOrThrow_WithValidId_ReturnsItem() {
        when(itemRepository.findItemById(1L)).thenReturn(Optional.of(testItem));

        Item result = itemService.getItemByIdOrThrow(1L);

        assertEquals("Test Item", result.getTitle());
        assertEquals(100L, result.getPrice());
        verify(itemRepository, times(1)).findItemById(1L);
    }

    @Test
    void getItemByIdOrThrow_WithInvalidId_ThrowsException() {
        when(itemRepository.findItemById(9999L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemByIdOrThrow(9999L));
        verify(itemRepository, times(1)).findItemById(9999L);
    }

    @Test
    void getItemDtoById_WithValidId_ReturnsItemDto() {
        when(itemRepository.findItemById(1L)).thenReturn(Optional.of(testItem));
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);

        ItemDto result = itemService.getItemDtoById(1L);

        assertEquals(1L, result.id());
        assertEquals("Test Item", result.title());
        assertEquals(100L, result.price());
    }

    @Test
    void findItems_WithSearchQuery_ReturnsPaginatedResults() {
        Page<Item> mockPage = new PageImpl<>(List.of(testItem));
        when(itemRepository.findItems(eq("Test"), any())).thenReturn(mockPage);
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);

        ItemsResponse result = itemService.findItems("Test", SortItems.NO, 1, 5);

        assertNotNull(result);
        assertNotNull(result.paging());
        assertEquals(1, result.paging().pageNumber());
    }

    @Test
    void findItems_WithSortByPrice_SortsCorrectly() {
        Page<Item> mockPage = new PageImpl<>(List.of(testItem));
        when(itemRepository.findItems(any(), argThat(p -> p.getSort().equals(Sort.by("price")))))
                .thenReturn(mockPage);
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);

        ItemsResponse result = itemService.findItems(null, SortItems.PRICE, 1, 5);

        assertNotNull(result);
    }

    @Test
    void findItems_WithMultiplePages_CalculatesPagingCorrectly() {
        Page<Item> mockPage = new PageImpl<>(List.of(testItem), PageRequest.of(0, 5), 15);
        when(itemRepository.findItems(any(), any())).thenReturn(mockPage);
        when(cartProvider.getOrCreateSingletonCart()).thenReturn(testCart);

        ItemsResponse result = itemService.findItems(null, SortItems.NO, 1, 5);

        assertTrue(result.paging().hasNext());
        assertFalse(result.paging().hasPrevious());
        assertEquals(1, result.paging().pageNumber());
    }
}

