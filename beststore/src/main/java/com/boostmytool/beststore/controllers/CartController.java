package com.boostmytool.beststore.controllers;

import com.boostmytool.beststore.models.Product;
import com.boostmytool.beststore.services.CartService;
import com.boostmytool.beststore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Shto produkt në shportë dhe ridrejto te karroca
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("id") Long productId, HttpSession session) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            cartService.addToCart(product, session);
        }
        return "redirect:/cart";
    }

    public void removeProductFromCart(HttpServletRequest request, Long productId) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            // Trajtoni rastin kur sesioni është null
            return;
        }

        List<Long> cart = (List<Long>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(productId);
            session.setAttribute("cart", cart);
        }
    }
    

    // Shfaq shportën
    @GetMapping
    public String showCart(Model model, HttpSession session) {
        List<Product> cartProducts = cartService.getCartProducts(session);
        double totalPrice = cartService.calculateTotalPrice(session);

        model.addAttribute("cartProducts", cartProducts);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

  

    // Konfirmimi i porosisë
    @GetMapping("/order/confirmation")
    public String orderConfirmation(Model model) {
        return "orderConfirmation";
    }

    // Merr produktin sipas ID (për thirrjet AJAX)
    @GetMapping("/api/products/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/shopingCart")
    public String checkout(HttpSession session, Model model) {
        List<Product> cartProducts = (List<Product>) session.getAttribute("cartProducts");
        if (cartProducts == null) {
            cartProducts = Collections.emptyList(); // Sigurohuni që të mos keni null
        }

        // Bashkoni ID-të e produkteve
        String productIds = cartProducts.stream()
                                        .map(product -> String.valueOf(product.getId()))
                                        .collect(Collectors.joining(","));
        model.addAttribute("productIds", productIds);

        // Shto të dhënat e klientit në model, për shembull nëse janë të disponueshme
        model.addAttribute("cartProducts", cartProducts);

        return "shopingCart"; // Kthe në faqen e pagesës
    }
    @PostMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam("id") Long productId) {
        // Logika për të hequr produktin nga karroca
        return "redirect:/cart";
    }

 // Metoda për të trajtuar formularin e dorëzuar dhe për të shfaqur faqen e konfirmimit
    @PostMapping("/checkout")
    public String checkout(@RequestParam String customerName,
                           @RequestParam String customerEmail,
                           @RequestParam String customerAddress,
                           @RequestParam String customerPhone,
                           @RequestParam("productIds") String productIds,
                           Model model) {
        // Këtu mund të shtoni logjikën për të ruajtur porosinë në databazë ose për të kryer veprime të tjera

        // Shtoni një mesazh në model për të shfaqur në faqen e konfirmimit
        model.addAttribute("message", "Porosia juaj u konfirmua me sukses!");

        // Kthehu në faqen e konfirmimit
        return "confirmation";
    }
    
}

    

