package ru.angelich.marketapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrdersController {

    @GetMapping("/orders")
    String getOrders(Model model) {
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String getOrders(@PathVariable Integer id,
                            @RequestParam(required = false) boolean newOrder,
                            Model model) {


        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping("/buy")
    public String buy(RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("id", newOrder.getId());
        redirectAttributes.addAttribute("newOrder", true);
        return "redirect:/orders";
    }
}
