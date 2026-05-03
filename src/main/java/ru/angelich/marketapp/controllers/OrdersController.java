package ru.angelich.marketapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.angelich.marketapp.services.CartService;
import ru.angelich.marketapp.services.OrderService;

@Controller
public class OrdersController {
    private final OrderService orderService;
    private final CartService cartService;

    public OrdersController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/orders")
    String getOrders(Model model) {
        var orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String getOrder(@PathVariable Long id,
                           @RequestParam(required = false, defaultValue = "false") boolean newOrder,
                           Model model) {
        var order = orderService.getOrderDtoById(id);
        model.addAttribute("orders", order);
        model.addAttribute("newOrder", newOrder);
        return "order";
    }

    @PostMapping("/buy")
    public String buy(RedirectAttributes redirectAttributes) {
        var cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart/items";
        }
        var order = orderService.createOrder(cartItems);
        redirectAttributes.addAttribute("id", order.getId());
        redirectAttributes.addAttribute("newOrder", true);
        return "redirect:/orders/{id}";
    }
}
