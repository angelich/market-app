package ru.angelich.marketapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.angelich.marketapp.models.Action;
import ru.angelich.marketapp.models.ItemDto;
import ru.angelich.marketapp.models.SortItems;
import ru.angelich.marketapp.services.CartService;
import ru.angelich.marketapp.services.ItemService;

@Controller
public class ItemsController {
    private final ItemService itemService;
    private final CartService cartService;

    public ItemsController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping(path = {"/", "/items"})
    String getItems(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "NO") SortItems sortItems,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize,
            Model model) {

        var response = itemService.findItems(search, sortItems, pageNumber, pageSize);

        model.addAttribute("items", response.items());
        model.addAttribute("search", search);
        model.addAttribute("sort", sortItems);
        model.addAttribute("paging", response.paging());

        return "items";
    }

    @PostMapping("/items")
    String postItems(
            @RequestParam Long id,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "NO") SortItems sortItems,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam Action action,
            RedirectAttributes redirectAttributes
    ) {
        cartService.updateItemCount(id, action);

        redirectAttributes.addAttribute("sort", sortItems);

        if (search != null) {
            redirectAttributes.addAttribute("search", search);
        }
        if (pageNumber != null) {
            redirectAttributes.addAttribute("pageNumber", pageNumber);
        }
        if (pageSize != null) {
            redirectAttributes.addAttribute("pageSize", pageSize);
        }
        return "redirect:/items";
    }

    @GetMapping("/items/{id}")
    String getItemById(@PathVariable Long id, Model model) {
        ItemDto itemDto = itemService.getItemDtoById(id);
        model.addAttribute("item", itemDto);
        return "item";
    }

    @PostMapping("/items/{id}")
    String updateItemCart(@PathVariable Long id, @RequestParam Action action, Model model) {
        cartService.updateItemCount(id, action);
        ItemDto itemDto = itemService.getItemDtoById(id);
        model.addAttribute("item", itemDto);
        return "item";
    }
}
