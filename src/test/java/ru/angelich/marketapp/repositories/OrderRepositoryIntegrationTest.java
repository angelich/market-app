package ru.angelich.marketapp.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.angelich.marketapp.BaseIntegrationTest;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.Orders;
import ru.angelich.marketapp.TestcontainersConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Orders testOrder;
    private Item testItem;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        
        testItem = new Item();
        testItem.setTitle("Test Item");
        testItem.setDescription("Test Description");
        testItem.setPrice(100L);
        testItem.setImgPath("/path/to/image.jpg");
        testItem = itemRepository.save(testItem);

        testOrder = new Orders();
        testOrder.setItems(new ArrayList<>(List.of(testItem)));
        testOrder.setTotalSum(100L);
        testOrder = orderRepository.save(testOrder);
    }

    @Test
    void findOrderById_WithValidId_ReturnsOrder() {
        Optional<Orders> foundOrder = orderRepository.findOrderById(testOrder.getId());

        assertTrue(foundOrder.isPresent());
        assertEquals(100L, foundOrder.get().getTotalSum());
        assertEquals(1, foundOrder.get().getItems().size());
    }

    @Test
    void findOrderById_WithInvalidId_ReturnsEmpty() {
        Optional<Orders> foundOrder = orderRepository.findOrderById(9999L);

        assertFalse(foundOrder.isPresent());
    }

    @Test
    void saveOrder_WithItems_SavesSuccessfully() {
        Item item2 = new Item();
        item2.setTitle("Another Item");
        item2.setDescription("Another Description");
        item2.setPrice(200L);
        item2.setImgPath("/path/to/image2.jpg");
        item2 = itemRepository.save(item2);

        Orders newOrder = new Orders();
        newOrder.setItems(new ArrayList<>(List.of(testItem, item2)));
        newOrder.setTotalSum(300L);
        newOrder = orderRepository.save(newOrder);

        Orders savedOrder = orderRepository.findOrderById(newOrder.getId()).orElseThrow();
        assertEquals(2, savedOrder.getItems().size());
        assertEquals(300L, savedOrder.getTotalSum());
    }
}

