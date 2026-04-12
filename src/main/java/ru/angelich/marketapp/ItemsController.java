package ru.angelich.marketapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.angelich.marketapp.models.Action;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.ItemsSort;
import ru.angelich.marketapp.services.ItemService;

@Controller
public class ItemsController {
    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(path = {"/", "/items"})
    String getItems(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "NO") ItemsSort itemsSort,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            Model model) {

        var response = itemService.findItems(search, itemsSort, pageNumber, pageSize);

        model.addAttribute("items", response.items());
        model.addAttribute("search", search);
        model.addAttribute("sort", itemsSort);
        model.addAttribute("paging", response.paging());

        return "items";
    }

    @PostMapping("/items")
    String postItems(
            @RequestParam String id,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "NO") ItemsSort itemsSort,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam Action action,
            RedirectAttributes redirectAttributes
    ) {
        if (search != null) {
            redirectAttributes.addAttribute("search", search);
        }
        redirectAttributes.addAttribute("sort", itemsSort);
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
        Item item = itemService.getItemById(id);

        model.addAttribute("item", item);
        return "item";
    }
}
