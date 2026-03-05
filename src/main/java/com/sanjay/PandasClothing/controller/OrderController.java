package com.sanjay.PandasClothing.controller;

import com.sanjay.PandasClothing.entity.*;
import com.sanjay.PandasClothing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(Principal principal) {

        User user = userService.findByEmail(principal.getName());

        orderService.placeOrder(user);

        return ResponseEntity.ok("Order placed successfully");
    }

    @GetMapping
    public List<Order> getOrders(Principal principal) {

        User user = userService.findByEmail(principal.getName());

        return orderService.getOrdersByUser(user.getId());
    }
}