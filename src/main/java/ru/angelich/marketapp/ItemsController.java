package ru.angelich.marketapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.angelich.marketapp.models.Action;
import ru.angelich.marketapp.models.ItemDto;
import ru.angelich.marketapp.models.Sort;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ItemsController {

    @GetMapping(path = {"/", "/items"})
    String getItems(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "NO") Sort sort,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            Model model) {



        //response
        List<List<ItemDto>> items = new ArrayList<>();
        model.addAttribute("items", items);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("paging", paging);

        return "items";
    }

    @PostMapping("/items")
    String postItems(
            @RequestParam String id,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "NO") Sort sort,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam Action action,
            RedirectAttributes redirectAttributes
    ) {
        if (search != null) {
            redirectAttributes.addAttribute("search", search);
        }
        redirectAttributes.addAttribute("sort", sort);
        if (pageNumber != null) {
            redirectAttributes.addAttribute("pageNumber", pageNumber);
        }
        if (pageSize != null) {
            redirectAttributes.addAttribute("pageSize", pageSize);
        }
        return "redirect:/items";
    }

    @GetMapping("/items/{id}")
    String getItemById(@PathVariable String id, Model model) {
        model.addAttribute("item", item);
        return "item";
    }
}
