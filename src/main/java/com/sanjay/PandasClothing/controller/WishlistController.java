package com.sanjay.PandasClothing.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.sanjay.PandasClothing.entity.Cart;
import com.sanjay.PandasClothing.entity.Product;
import com.sanjay.PandasClothing.entity.Wishlist;

import com.sanjay.PandasClothing.repository.CartRepository;
import com.sanjay.PandasClothing.repository.WishlistRepository;
import com.sanjay.PandasClothing.repository.ProductRepository;
@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId, Principal principal) {

        if (principal == null) return "Please login";

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return "Product not found";

        Wishlist wishlist = new Wishlist();
        wishlist.setUserEmail(principal.getName());
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);

        return "Added to wishlist";
    }

    @PostMapping("/move-to-cart/{wishlistId}")
    public ResponseEntity<?> moveToCart(
            @PathVariable Long wishlistId,
            Principal principal) {

        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        Product product = wishlist.getProduct();
        String size = wishlist.getSize();  // 🔥 GET SIZE FROM WISHLIST

        Optional<Cart> existingCart =
                cartRepository.findByUserEmailAndProductIdAndSize(
                        principal.getName(),
                        product.getId(),
                        size);

        if (existingCart.isPresent()) {

            Cart cartItem = existingCart.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartRepository.save(cartItem);

        } else {

            Cart cart = new Cart();
            cart.setUserEmail(principal.getName());
            cart.setProduct(product);
            cart.setQuantity(1);
            cart.setSize(size);   // 🔥 VERY IMPORTANT

            cartRepository.save(cart);
        }

        wishlistRepository.delete(wishlist);

        return ResponseEntity.ok("Moved to cart");
    }
    @GetMapping("/my")
    public List<Wishlist> getMyWishlist(Principal principal) {

        if (principal == null) return null;

        return wishlistRepository.findByUserEmail(principal.getName());
    }

    @DeleteMapping("/remove/{wishlistId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long wishlistId) {

        wishlistRepository.deleteById(wishlistId);
        return ResponseEntity.ok("Removed from wishlist");
    }
}