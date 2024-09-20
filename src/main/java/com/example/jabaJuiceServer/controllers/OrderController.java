package com.example.jabaJuiceServer.controllers;

import com.example.jabaJuiceServer.MyDAOs.OrderDTO;
import com.example.jabaJuiceServer.MyDAOs.OrderProductMap;
import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import com.example.jabaJuiceServer.entities.Order;
import com.example.jabaJuiceServer.entities.OrderRequest;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.services.OrderService;
import com.example.jabaJuiceServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sanny/orders")
public class OrderController {

    private OrderService orderService;
    private UserService userService;
    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // Endpoint to create a new order
    @PostMapping("/new-pending-order")
    public ResponseEntity<Order> createOrder(@RequestParam String userEmail,
                                             @RequestParam Long deliveryOptionId,
                                             @RequestBody List<OrderProductMap> orderProductMaps) {
        try {
            Order createdOrder = orderService.createOrderWithUserAndDeliveryOption(userEmail, deliveryOptionId, orderProductMaps);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Handle invalid input (e.g., non-existing user or delivery option)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to get all orders
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        List<Order> orders = orderService.getAll();
        return orders;
    }

    // Endpoint to get an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete an order by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint to update an existing order
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order existingOrder = orderService.getById(id);
        if (existingOrder != null) {
            order.setId(id);
            orderService.update(order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to toggle the status of an order between "delivered" and "pending"
    @PatchMapping("/{id}/toggleStatus")
    public ResponseEntity<Order> toggleOrderStatus(@PathVariable Long id) {
        orderService.toggleStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Endpoint to toggle the confirmation status of an order between "confirmed" and "canceled"
    @PatchMapping("/{id}/toggleConfirmationStatus")
    public ResponseEntity<Order> toggleOrderConfirmationStatus(@PathVariable Long id) {
        orderService.toggleConfirmationStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Endpoint to get orders by user ID
    @GetMapping("/user")
    public List<Order> getOrdersByUser(@RequestParam("email") String email) {
        User user = userService.getUserByEmail(email);
        List<Order> orders = orderService.getOrderByUser(user);
        return orders;
    }

    // Endpoint to get orders by delivery option ID
    @GetMapping("/deliveryOption/{deliveryOptionId}")
    public ResponseEntity<List<Order>> getOrdersByDeliveryOption(@PathVariable Long deliveryOptionId) {
        DeliveryOption deliveryOption = new DeliveryOption();
        deliveryOption.setId(deliveryOptionId);
        List<Order> orders = orderService.getOrdersByDeliveryOption(deliveryOption);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Endpoint to recommend a shortlist of most common orders
    @GetMapping("/recommendShortlist")
    public ResponseEntity<List<Order>> recommendAShortListOfMostCommonOrders() {
        List<Order> shortlist = orderService.recommendAShortListOfMostCommonOrders();
        return new ResponseEntity<>(shortlist, HttpStatus.OK);
    }
    @GetMapping("/byEmail")
    public Page<OrderDTO> getOrdersByEmailPaginated(
            @RequestParam String email,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        return orderService.getOrdersByEmailPaginated(email, pageNumber, pageSize);
    }
}

