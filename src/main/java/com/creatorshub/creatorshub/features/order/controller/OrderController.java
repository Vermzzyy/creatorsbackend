package com.creatorshub.creatorshub.features.order.controller;

import com.creatorshub.creatorshub.features.order.dto.CreateOrderRequest;
import com.creatorshub.creatorshub.features.order.dto.OrderResponse;
import com.creatorshub.creatorshub.features.order.service.OrderService;
import com.creatorshub.creatorshub.features.user.model.User;
import com.creatorshub.creatorshub.features.user.service.UserService;
import com.creatorshub.creatorshub.shared.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private void verifyAdmin(HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Forbidden: Admin access required");
        }
    }

    // POST /api/v1/orders - Book a service
    @PostMapping
    public ResponseEntity<?> createOrder(HttpServletRequest request, @RequestBody CreateOrderRequest body) {
        try {
            User user = getAuthenticatedUser(request);
            OrderResponse order = orderService.createOrder(user.getUserID(), body);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // GET /api/v1/orders/my - Get user's order history
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
        try {
            User user = getAuthenticatedUser(request);
            List<OrderResponse> orders = orderService.getOrdersByUserId(user.getUserID());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // GET /api/v1/orders - Get all orders (Admin)
    @GetMapping
    public ResponseEntity<?> getAllOrders(HttpServletRequest request) {
        try {
            verifyAdmin(request);
            return ResponseEntity.ok(orderService.getAllOrders());
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    // PATCH /api/v1/orders/{id}/status - Update order status (Admin)
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            verifyAdmin(request);
            OrderResponse updated = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            if (e.getMessage().contains("Forbidden")) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    private User getAuthenticatedUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization header");
        }
        String token = authHeader.substring(7).trim();
        String email = jwtUtil.extractEmail(token);
        return userService.findByEmail(email);
    }
}
