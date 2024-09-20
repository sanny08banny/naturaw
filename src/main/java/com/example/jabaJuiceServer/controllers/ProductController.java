package com.example.jabaJuiceServer.controllers;

import com.example.jabaJuiceServer.ProductNotFoundException;
import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.Comment;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.services.ProductService;
import com.example.jabaJuiceServer.services.UserService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("sanny/products")
public class ProductController {

    private final ProductService productService;
    private UserService userService;
    private ResourceLoader resourceLoader;

    public ProductController(ProductService productService, UserService userService, ResourceLoader resourceLoader) {
        this.productService = productService;
        this.userService = userService;
        this.resourceLoader = resourceLoader;
    }
    @PostMapping("/new")
    public ResponseEntity<?> addProduct(@RequestPart("product") Product product,
                                        @RequestPart("file") MultipartFile file,
                                        @RequestParam("email") String email) {
        productService.saveProduct(product, file,email);
        return ResponseEntity.ok("Product saved successfully.");
    }
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/for-email")
    public ResponseEntity<Page<Product>> getProductsForEmail(
            @RequestParam("email") String email,
            @RequestParam("page")int page,
            @RequestParam(defaultValue = "10", name = "pageSize") int pageSize) {
        Page<Product> products = productService.getAllProdustssForEmail(page, pageSize,email);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Product> getProductByName(@RequestParam("name") String name) {
        try {
            Product product = productService.getProductByName(name);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/random/{email}")
    public ResponseEntity<Page<Product>> getRandomPosts(
            @PathVariable String email,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<Product> randomPosts = productService.getRandomProducts(email, pageSize);
        return ResponseEntity.ok(randomPosts);
    }
    @PostMapping("/{itemId}/relate/{email}")
    public ResponseEntity<String> relateProduct(@PathVariable Long itemId, @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        productService.relateProduct(itemId, user.getId());
        return ResponseEntity.ok("Item related successfully.");
    }

    @PostMapping("/{itemId}/like/{email}")
    public ResponseEntity<String> likeProduct(@PathVariable Long itemId, @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        productService.likeProduct(itemId, user.getId());
        return ResponseEntity.ok("Item liked successfully.");
    }
    @DeleteMapping("/{itemId}/unRelate/{email}")
    public ResponseEntity<String> unRelateProduct(@PathVariable Long itemId, @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        productService.unRelateProduct(itemId, user.getId());
        return ResponseEntity.ok("Item unRelated successfully.");
    }

    @DeleteMapping("/{itemId}/unLike/{email}")
    public ResponseEntity<String> unLikeProduct(@PathVariable Long itemId, @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        productService.unLikeProduct(itemId, user.getId());
        return ResponseEntity.ok("Item unLiked successfully.");
    }
    @PostMapping("/commentProduct")
    public ResponseEntity<?> commentOnProduct(@RequestPart("comment") Comment comment,
                                            @RequestParam("email") String email,
                                            @RequestParam("id") Long productId) {
        productService.createComment(comment,email,productId);
        return ResponseEntity.ok("Product comment saved successfully.");
    }
}

