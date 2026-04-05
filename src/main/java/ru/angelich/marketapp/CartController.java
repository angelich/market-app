package ru.angelich.marketapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.angelich.marketapp.models.Action;

@Controller
@RequestMapping("/cart")
public class CartController {


    @GetMapping("/items")
    String getCartItems(Model model) {
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/items")
    String addToCart(@RequestParam Long id,
                     @RequestParam Action action) {

        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "cart";
    }

}
