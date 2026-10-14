package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {
    private final ProductRepository productRepository;

    public HomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "index";
    }

    @GetMapping("/products")
    public String products(@RequestParam(required = false) String category,
                           @RequestParam(required = false) Double maxPrice,
                           Model model) {
        List<Product> products = productRepository.findAll();

        if (category != null && !category.isBlank()) {
            products = productRepository.findByCategoryIgnoreCase(category);
        }
        if (maxPrice != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() <= maxPrice)
                    .toList();
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category == null ? "" : category);
        model.addAttribute("maxPrice", maxPrice == null ? "" : maxPrice);
        return "products";
    }

    @GetMapping("/products/{id}")
    public String details(@PathVariable int id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/products";
        model.addAttribute("product", product);
        return "details";
    }
}
