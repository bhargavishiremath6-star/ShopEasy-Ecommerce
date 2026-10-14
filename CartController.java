package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CartController {
    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> cart(HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping("/cart")
    public String cartPage() { return "redirect:/cart/view"; }

    @GetMapping("/cart/view")
    public String viewCart(HttpSession session, org.springframework.ui.Model model) {
        Map<Integer, Integer> cart = cart(session);
        List<Product> products = new ArrayList<>();
        double total = 0;

        for (Map.Entry<Integer, Integer> e : cart.entrySet()) {
            productRepository.findById(e.getKey()).ifPresent(p -> {
                products.add(p);
            });
        }

        for (Product p : products) {
            total += p.getPrice() * cart.get(p.getId());
        }

        model.addAttribute("products", products);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String add(@PathVariable int id, HttpSession session) {
        if (productRepository.existsById(id)) {
            Map<Integer, Integer> cart = cart(session);
            cart.put(id, cart.getOrDefault(id, 0) + 1);
        }
        return "redirect:/cart/view";
    }

    @PostMapping("/cart/remove/{id}")
    public String remove(@PathVariable int id, HttpSession session) {
        Map<Integer, Integer> cart = cart(session);
        cart.remove(id);
        return "redirect:/cart/view";
    }
}
