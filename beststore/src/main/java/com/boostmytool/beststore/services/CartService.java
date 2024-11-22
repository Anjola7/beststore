package com.boostmytool.beststore.services;

import com.boostmytool.beststore.models.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    // Krijo metodën për të marrë produktet nga sesioni
    public List<Product> getCartProducts(HttpSession session) {
        List<Product> cartProducts = (List<Product>) session.getAttribute("cartProducts");
        if (cartProducts == null) {
            cartProducts = new ArrayList<>();
            session.setAttribute("cartProducts", cartProducts);
        }
        return cartProducts;
    }

    // Shto produkt në shportë
    public void addToCart(Product product, HttpSession session) {
        List<Product> cartProducts = getCartProducts(session);
        cartProducts.add(product);
        session.setAttribute("cartProducts", cartProducts);
    }

    public void removeProductFromCart(Long productId, HttpSession session) {
        List<Product> cartProducts = (List<Product>) session.getAttribute("cartProducts");
        if (cartProducts == null) {
            cartProducts = new ArrayList<>();
        }

        // Krahasimi i id-ve duke konvertuar productId në long
       
        session.setAttribute("cartProducts", cartProducts);
    }


    // Kërko produktin në shportë dhe llogarit çmimin total
    public double calculateTotalPrice(HttpSession session) {
        List<Product> cartProducts = getCartProducts(session);
        return cartProducts.stream().mapToDouble(Product::getPrice).sum();
    }
}

