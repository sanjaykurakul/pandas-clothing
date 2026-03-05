package com.sanjay.PandasClothing.service;

import com.sanjay.PandasClothing.entity.*;
import com.sanjay.PandasClothing.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public void placeOrder(User user) {

        List<Cart> cartItems = cartRepository.findByUserEmail(user.getEmail());

        double total = 0;

        for (Cart cart : cartItems) {
            total += cart.getProduct().getPrice() * cart.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setStatus("PLACED");

        Order savedOrder = orderRepository.save(order);

        for (Cart cart : cartItems) {

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(cart.getProduct());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getProduct().getPrice());

            orderItemRepository.save(item);
        }

        cartRepository.deleteByUserEmail(user.getEmail());
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}