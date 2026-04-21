package ru.angelich.marketapp.services;

import org.springframework.stereotype.Service;
import ru.angelich.marketapp.mappers.OrderMapper;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.ItemDto;
import ru.angelich.marketapp.models.Orders;
import ru.angelich.marketapp.models.OrderDto;
import ru.angelich.marketapp.repositories.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, ItemService itemService, OrderMapper orderMapper, CartService cartService) {
        this.orderRepository = orderRepository;
        this.itemService = itemService;
        this.orderMapper = orderMapper;
        this.cartService = cartService;
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
    }

    public Orders getOrderByIdOrThrow(Long id) {
        return orderRepository.findOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    public OrderDto getOrderDtoById(Long id) {
        return orderMapper.toDto(getOrderByIdOrThrow(id));
    }

    public Orders createOrder(List<ItemDto> cartItems) {
        List<Item> items = cartItems
                .stream()
                .map(dto -> itemService.getItemByIdOrThrow(dto.id()))
                .toList();

        long totalSum = cartItems
                .stream()
                .mapToLong(dto -> dto.price() * dto.count())
                .sum();

        cartService.clearCart();
        return orderRepository.save(new Orders(null, items, totalSum));
    }
}
