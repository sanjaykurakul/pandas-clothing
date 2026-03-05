package com.sanjay.PandasClothing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import com.sanjay.PandasClothing.entity.Cart;
import com.sanjay.PandasClothing.entity.Product;
import com.sanjay.PandasClothing.repository.CartRepository;
import com.sanjay.PandasClothing.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;


    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long productId,
            @RequestParam String size,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        String email = principal.getName();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔥 Check if same product + same size already exists
        Optional<Cart> existingCartItem =
                cartRepository.findByUserEmailAndProductIdAndSize(email, productId, size);

        if (existingCartItem.isPresent()) {

            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartRepository.save(cartItem);

            return ResponseEntity.ok("Quantity updated");

        } else {

            Cart cart = new Cart();
            cart.setUserEmail(email);
            cart.setProduct(product);
            cart.setQuantity(1);
            cart.setSize(size);   // 🔥 SAVE SIZE HERE

            cartRepository.save(cart);

            return ResponseEntity.ok("Added to cart");
        }
    }
    @PutMapping("/update/{cartId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long cartId,
                                            @RequestParam int quantity) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartRepository.delete(cart);
            return ResponseEntity.ok("Item removed");
        }

        cart.setQuantity(quantity);
        cartRepository.save(cart);

        return ResponseEntity.ok("Quantity updated");
    }

    @GetMapping("/my")
    public List<Cart> getMyCart(Principal principal) {
        if (principal == null) return null;
        return cartRepository.findByUserEmail(principal.getName());
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartId) {

        cartRepository.deleteById(cartId);
        return ResponseEntity.ok("Removed from cart");
    }
}