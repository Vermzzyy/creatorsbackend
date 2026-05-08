package com.creatorshub.creatorshub.features.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.creatorshub.creatorshub.features.service.dto.CreateServiceRequest;
import com.creatorshub.creatorshub.features.service.dto.ServiceResponse;
import com.creatorshub.creatorshub.features.service.dto.UpdateServiceRequest;
import com.creatorshub.creatorshub.features.service.service.ServiceService;
import com.creatorshub.creatorshub.features.user.service.UserService;
import com.creatorshub.creatorshub.shared.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/services")
@CrossOrigin(origins = "http://localhost:3000")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private void verifyAdmin(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Unauthorized");
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        String role = userService.findByEmail(email).getRole();
        
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Forbidden: Admin access required");
        }
    }

    // GET /api/v1/services?search=keyword&category=UI/UX
    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServices(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {

        List<ServiceResponse> services = serviceService.searchServices(search, category);
        return ResponseEntity.ok(services);
    }

    // GET /api/v1/services/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        try {
            ServiceResponse service = serviceService.getServiceById(id);
            return ResponseEntity.ok(service);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // POST /api/v1/services
    @PostMapping
    public ResponseEntity<?> createService(
            HttpServletRequest request,
            @RequestBody CreateServiceRequest body) {
        try {
            verifyAdmin(request);
            ServiceResponse created = serviceService.createService(body);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Forbidden") || e.getMessage().contains("Unauthorized")) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/v1/services/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody UpdateServiceRequest body) {
        try {
            verifyAdmin(request);
            ServiceResponse updated = serviceService.updateService(id, body);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Forbidden") || e.getMessage().contains("Unauthorized")) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // DELETE /api/v1/services/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(
            HttpServletRequest request,
            @PathVariable Long id) {
        try {
            verifyAdmin(request);
            serviceService.deleteService(id);
            return ResponseEntity.ok("Service deleted successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Forbidden") || e.getMessage().contains("Unauthorized")) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
