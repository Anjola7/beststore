package com.example.beststore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CheckoutController {

    @PostMapping("/checkout")
    public String processCheckout(
            @RequestParam String customerName,
            @RequestParam String customerEmail,
            @RequestParam String customerAddress,
            @RequestParam String customerPhone,
            @RequestParam String productIds,
            Model model) {
        
        // Procesoni të dhënat e porosisë këtu
        List<Long> productIdList = Arrays.stream(productIds.split(","))
                                          .map(Long::parseLong)
                                          .collect(Collectors.toList());

        // Këtu mund të shtoni logjikën për të dërguar të dhënat në bazën e të dhënave ose për të krijuar një porosi
        
        model.addAttribute("customerName", customerName);
        model.addAttribute("customerEmail", customerEmail);
        model.addAttribute("customerAddress", customerAddress);
        model.addAttribute("customerPhone", customerPhone);
        model.addAttribute("productIds", productIdList);

        return "orderConfirmation"; // Kjo është faqja e konfirmimit e porosisë
    }
}
