package ru.angelich.marketapp.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.angelich.marketapp.BaseIntegrationTest;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.ItemDto;
import ru.angelich.marketapp.models.OrderDto;
import ru.angelich.marketapp.models.Orders;
import ru.angelich.marketapp.repositories.CartRepository;
import ru.angelich.marketapp.repositories.ItemRepository;
import ru.angelich.marketapp.repositories.OrderRepository;
import ru.angelich.marketapp.services.OrderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartRepository cartRepository;

    private ItemDto testItemDto;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        cartRepository.deleteAll();
        itemRepository.deleteAll();

        Item testItem = new Item();
        testItem.setTitle("Order Test Item");
        testItem.setDescription("Order Test Description");
        testItem.setPrice(250L);
        testItem.setImgPath("/img/test.jpg");
        testItem = itemRepository.save(testItem);

        testItemDto = new ItemDto(testItem.getId(), testItem.getTitle(), testItem.getDescription(),
                testItem.getImgPath(), testItem.getPrice(), 1);
    }

    @Test
    void getAllOrders_IntegrationTest_ReturnsOrderList() {
        List<OrderDto> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertTrue(orders instanceof List);
    }

    @Test
    void createOrder_IntegrationTest_CreatesOrderSuccessfully() {
        Orders createdOrder = orderService.createOrder(List.of(testItemDto));

        assertNotNull(createdOrder);
        assertEquals(250L, createdOrder.getTotalSum());
        assertEquals(1, createdOrder.getItems().size());
    }

    @Test
    void createOrder_WithMultipleItems_IntegrationTest_CalculatesTotalCorrectly() {
        Item item2 = new Item();
        item2.setTitle("Second Item");
        item2.setDescription("Second Description");
        item2.setPrice(300L);
        item2.setImgPath("/img/item2.jpg");
        item2 = itemRepository.save(item2);

        ItemDto itemDto2 = new ItemDto(item2.getId(), item2.getTitle(), item2.getDescription(),
                item2.getImgPath(), item2.getPrice(), 1);

        Orders createdOrder = orderService.createOrder(List.of(testItemDto, itemDto2));

        assertEquals(550L, createdOrder.getTotalSum());
        assertEquals(2, createdOrder.getItems().size());
    }

    @Test
    void getOrderById_IntegrationTest_ReturnsCorrectOrder() {
        Orders createdOrder = orderService.createOrder(List.of(testItemDto));

        Orders retrievedOrder = orderService.getOrderByIdOrThrow(createdOrder.getId());

        assertEquals(createdOrder.getId(), retrievedOrder.getId());
        assertEquals(250L, retrievedOrder.getTotalSum());
    }
}

