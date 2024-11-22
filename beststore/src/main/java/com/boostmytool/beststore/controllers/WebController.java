package com.boostmytool.beststore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boostmytool.beststore.models.Product;
import com.boostmytool.beststore.services.ProductService;

@Controller
@RequestMapping
public class WebController {
    @Autowired
    private ProductService productService;
	 @GetMapping("/")
	    public String showCustomerList(Model model) {
	        List<Product> products = productService.getAllProducts();
	        model.addAttribute("products", products);
	        return "customer/customer";
	    }
	
}
