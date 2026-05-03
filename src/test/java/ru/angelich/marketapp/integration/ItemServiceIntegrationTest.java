package ru.angelich.marketapp.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.angelich.marketapp.BaseIntegrationTest;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.ItemDto;
import ru.angelich.marketapp.models.ItemsResponse;
import ru.angelich.marketapp.models.SortItems;
import ru.angelich.marketapp.repositories.CartRepository;
import ru.angelich.marketapp.repositories.ItemRepository;
import ru.angelich.marketapp.services.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartRepository cartRepository;

    private Item testItem;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        cartRepository.deleteAll();

        testItem = new Item();
        testItem.setTitle("Integration test item");
        testItem.setDescription("Integration test description");
        testItem.setPrice(150L);
        testItem.setImgPath("/img/test.jpg");
        testItem = itemRepository.save(testItem);
    }

    @Test
    void findItems_IntegrationTest_ReturnsItemsSuccessfully() {
        ItemsResponse response = itemService.findItems(null, SortItems.NO, 1, 10);

        assertNotNull(response);
        assertNotNull(response.items());
        assertNotNull(response.paging());
    }

    @Test
    void getItemById_IntegrationTest_ReturnsCorrectItem() {
        ItemDto itemDto = itemService.getItemDtoById(testItem.getId());

        assertNotNull(itemDto);
        assertEquals("Integration test item", itemDto.title());
        assertEquals(150L, itemDto.price());
    }

    @Test
    void findItems_WithSearch_IntegrationTest_ReturnsFilteredResults() {
        ItemsResponse response = itemService.findItems("Integration", SortItems.NO, 1, 10);

        assertNotNull(response);
        assertTrue(response.items().stream()
                .flatMap(List::stream)
                .anyMatch(item -> item.id() > 0));
    }
}

