package ru.angelich.marketapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.angelich.marketapp.models.Action;
import ru.angelich.marketapp.services.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/items")
    String getCartItems(Model model) {
        var items = cartService.getCartItems();
        var total = items.stream().mapToLong(item -> item.price() * item.count()).sum();
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/items")
    String updateCartItem(@RequestParam Long id,
                          @RequestParam Action action) {
        cartService.updateItemCount(id, action);
        return "redirect:/cart/items";
    }
}
