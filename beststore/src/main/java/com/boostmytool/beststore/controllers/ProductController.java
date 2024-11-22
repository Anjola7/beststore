package com.boostmytool.beststore.controllers;

import com.boostmytool.beststore.models.Product;
import com.boostmytool.beststore.models.ProductDto;
import com.boostmytool.beststore.services.CartService;
import com.boostmytool.beststore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Shfaq formën për krijimin e produktit
    @GetMapping("/create")
    public String showCreateProductForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return "products/CreateProduct";
    }

    
    
    // Procesi i krijimit të produktit
    @PostMapping("/create/submit")
    public String createProduct(@ModelAttribute("productDto") @Valid ProductDto productDto,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "products/CreateProduct";
        }
        try {
            // Ruaj imazhin nëse është ngarkuar njëri
            String imageFileName = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                imageFileName = saveImageFile(imageFile);
                productDto.setImageFileName(imageFileName);
            }

            // Krijo produktin dhe ruaje në bazën e të dhënave
            Product product = convertToEntity(productDto);
            productService.createProduct(product);

            return "redirect:/products/index";
        } catch (Exception ex) {
            model.addAttribute("error", "An error occurred while creating the product: " + ex.getMessage());
            return "products/CreateProduct";
        }
    }

    // Shfaq formën për editimin e produktit
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("productDto", convertToDto(product));
        } catch (RuntimeException ex) {
            return "redirect:/products";
        }
        return "products/EditProduct";
    }

    // Procesi i editimit të produktit
    @PostMapping("/edit/{id}/submit")
    public ResponseEntity<String> editProduct(@PathVariable Long id,
                                              @ModelAttribute("productDto") @Valid ProductDto productDto,
                                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                              BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors occurred.");
        }

        try {
            // Update image file if a new one is provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageFileName = saveImageFile(imageFile);
                productDto.setImageFileName(imageFileName);
            } else {
                Product existingProduct = productService.getProductById(id);
                productDto.setImageFileName(existingProduct.getImageFileName());
            }

            // Convert DTO to Product entity and update it
            Product product = convertToEntity(productDto);
            productService.updateProduct(id, product);

            return ResponseEntity.ok("Product updated successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the product: " + ex.getMessage());
        }
    }

    // Fshij produktin
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the product: " + ex.getMessage());
        }
    }

    // Shfaq detajet e produktit
    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
        } catch (RuntimeException ex) {
            return "redirect:/products";
        }
        return "products/productDetails";
    }

    // Shfaq faqen e kryesore të produkteve
    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "home";
    }

    // Shto produkt në shportë
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") Long productId) {
        try {
            Product product = productService.getProductById(productId);
            cartService.addToCart(product, null);
            return "redirect:/cart";
        } catch (RuntimeException ex) {
            return "redirect:/products/home";
        }
    }

    // Shfaq listën e produkteve për administratorët
    @GetMapping("/index")
    public String showAdminList(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/index";
    }

    // Redirekto në faqen e pagesës
    @GetMapping("/shoppingCart")
    public String checkoutOrder() {
        return "redirect:/checkout";
    }

    // Ruaj imazhin në disk
    private String saveImageFile(MultipartFile imageFile) throws IOException {
        String imageFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, imageFileName);
        Files.createDirectories(imagePath.getParent());
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        return imageFileName;
    }

    // Konverto produkt në DTO
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setImageFileName(product.getImageFileName());
        return dto;
    }

    // Konverto DTO në produkt
    private Product convertToEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setImageFileName(dto.getImageFileName());
        return product;
    }

    // Merr imazhin nga disk dhe e kthen si resurs
    @GetMapping("/image/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) 
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // Shtoni këtë endpoint për të ofruar informacionin e produktit në format JSON
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
