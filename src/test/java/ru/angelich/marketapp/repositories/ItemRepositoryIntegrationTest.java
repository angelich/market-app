package ru.angelich.marketapp.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.angelich.marketapp.BaseIntegrationTest;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.TestcontainersConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    private Item testItem;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        testItem = new Item();
        testItem.setTitle("Test Item");
        testItem.setDescription("Test Description");
        testItem.setPrice(100L);
        testItem.setImgPath("/path/to/image.jpg");
        testItem = itemRepository.save(testItem);
    }

    @Test
    void findItemById_WithValidId_ReturnsItem() {
        Optional<Item> foundItem = itemRepository.findItemById(testItem.getId());

        assertTrue(foundItem.isPresent());
        assertEquals("Test Item", foundItem.get().getTitle());
        assertEquals(100L, foundItem.get().getPrice());
    }

    @Test
    void findItemById_WithInvalidId_ReturnsEmpty() {
        Optional<Item> foundItem = itemRepository.findItemById(9999L);

        assertFalse(foundItem.isPresent());
    }

    @Test
    void findItems_WithSearch_ReturnsMatchingItems() {
        Item item2 = new Item();
        item2.setTitle("Another Product");
        item2.setDescription("Test Description");
        item2.setPrice(200L);
        item2.setImgPath("/path/to/image2.jpg");
        itemRepository.save(item2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> result = itemRepository.findItems("Test", pageable);

        assertTrue(result.getTotalElements() >= 1);
    }

    @Test
    void findItems_WithNullSearch_ReturnsAllItems() {
        Item item2 = new Item();
        item2.setTitle("Another Product");
        item2.setDescription("Another Description");
        item2.setPrice(200L);
        item2.setImgPath("/path/to/image2.jpg");
        itemRepository.save(item2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> result = itemRepository.findItems(null, pageable);

        assertTrue(result.getTotalElements() >= 2);
    }
}

