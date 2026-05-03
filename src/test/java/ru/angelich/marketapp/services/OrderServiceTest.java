package ru.angelich.marketapp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.angelich.marketapp.exceptions.OrderNotFoundException;
import ru.angelich.marketapp.mappers.OrderMapper;
import ru.angelich.marketapp.models.*;
import ru.angelich.marketapp.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    private Item testItem;
    private Orders testOrder;
    private ItemDto testItemDto;

    @BeforeEach
    void setUp() {
        testItem = new Item(1L, "Test Item", "Test Description", "/img/test.jpg", 100L);
        testItemDto = new ItemDto(1L, "Test Item", "Test Description", "/img/test.jpg", 100L, 2);
        testOrder = new Orders(1L, List.of(testItem), 200L);
    }

    @Test
    void getAllOrders_ReturnsListOfOrderDtos() {
        OrderDto orderDto = new OrderDto(1L, List.of(testItemDto), 200L);
        when(orderRepository.findAll()).thenReturn(List.of(testOrder));
        when(orderMapper.toDto(testOrder)).thenReturn(orderDto);

        List<OrderDto> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderByIdOrThrow_WithValidId_ReturnsOrder() {
        when(orderRepository.findOrderById(1L)).thenReturn(Optional.of(testOrder));

        Orders result = orderService.getOrderByIdOrThrow(1L);

        assertEquals(1L, result.getId());
        assertEquals(200L, result.getTotalSum());
        verify(orderRepository, times(1)).findOrderById(1L);
    }

    @Test
    void getOrderByIdOrThrow_WithInvalidId_ThrowsException() {
        when(orderRepository.findOrderById(9999L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderByIdOrThrow(9999L));
        verify(orderRepository, times(1)).findOrderById(9999L);
    }

    @Test
    void getOrderDtoById_WithValidId_ReturnsOrderDto() {
        OrderDto expectedDto = new OrderDto(1L, List.of(testItemDto), 200L);
        when(orderRepository.findOrderById(1L)).thenReturn(Optional.of(testOrder));
        when(orderMapper.toDto(testOrder)).thenReturn(expectedDto);

        OrderDto result = orderService.getOrderDtoById(1L);

        assertEquals(1L, result.id());
        assertEquals(200L, result.totalSum());
    }

    @Test
    void createOrder_WithValidItems_CreatesAndReturnsOrder() {
        when(itemService.getItemByIdOrThrow(1L)).thenReturn(testItem);
        when(orderRepository.save(any(Orders.class))).thenReturn(testOrder);

        Orders result = orderService.createOrder(List.of(testItemDto));

        assertEquals(200L, result.getTotalSum());
        assertEquals(1, result.getItems().size());
        verify(cartService, times(1)).clearCart();
        verify(orderRepository, times(1)).save(any(Orders.class));
    }

    @Test
    void createOrder_WithMultipleItems_CalculatesTotalSumCorrectly() {
        Item item2 = new Item(2L, "Item 2", "Description 2", "/img/test2.jpg", 150L);
        ItemDto itemDto2 = new ItemDto(2L, "Item 2", "Description 2", "/img/test2.jpg", 150L, 1);
        Orders order = new Orders(1L, List.of(testItem, item2), 350L);

        when(itemService.getItemByIdOrThrow(1L)).thenReturn(testItem);
        when(itemService.getItemByIdOrThrow(2L)).thenReturn(item2);
        when(orderRepository.save(any(Orders.class))).thenReturn(order);

        Orders result = orderService.createOrder(List.of(testItemDto, itemDto2));

        assertEquals(350L, result.getTotalSum());
        assertEquals(2, result.getItems().size());
    }
}

