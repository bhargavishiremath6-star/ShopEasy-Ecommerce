package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class CheckoutController {
    private final ProductRepository productRepository;

    public CheckoutController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Map<Integer, Integer> cart = getCart(session);
        double total = 0;
        for (Map.Entry<Integer, Integer> e : cart.entrySet()) {
            Product p = productRepository.findById(e.getKey()).orElse(null);
            if (p != null) total += p.getPrice() * e.getValue();
        }
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String customerName,
                             @RequestParam String address,
                             HttpSession session, Model model) {
        session.removeAttribute("cart");
        model.addAttribute("customerName", customerName);
        model.addAttribute("address", address);
        return "success";
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> getCart(HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new java.util.LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
