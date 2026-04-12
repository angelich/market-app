package ru.angelich.marketapp.services;

import org.springframework.stereotype.Service;
import ru.angelich.marketapp.models.Cart;
import ru.angelich.marketapp.repositories.CartRepository;

import java.util.ArrayList;

@Service
public class CartProvider {
    private final CartRepository cartRepository;

    public CartProvider(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getOrCreateSingletonCart() {
        return cartRepository.findById(1L).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setId(1L);
            cart.setItems(new ArrayList<>());
            return cartRepository.save(cart);
        });
    }
}