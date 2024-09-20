package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.MyDAOs.OrderDTO;
import com.example.jabaJuiceServer.MyDAOs.OrderProductMap;
import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.Order;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.DeliveryOptionRepository;
import com.example.jabaJuiceServer.repositories.OrderRepository;
import com.example.jabaJuiceServer.repositories.ProductRepository;
import com.example.jabaJuiceServer.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private TransactionService transactionService;
    private UserRepository userRepository;
    private DeliveryOptionRepository deliveryOptionRepository;
    private ProductRepository productRepository;
    private NotificationService notificationService;

    @Autowired
    public OrderService(OrderRepository orderRepository, TransactionService transactionService,
                        UserRepository userRepository, DeliveryOptionRepository deliveryOptionRepository, ProductRepository productRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.deliveryOptionRepository = deliveryOptionRepository;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    public Order createOrderWithUserAndDeliveryOption(String userEmail, Long deliveryOptionId, List<OrderProductMap> orderProductMaps) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with the provided email: " + userEmail);
        }

        DeliveryOption deliveryOption = deliveryOptionRepository.findById(deliveryOptionId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryOption not found with the provided ID: " + deliveryOptionId));

        // Set the User and DeliveryOption in the Order entity
        Order order = new Order();
        order.setUser(user);
        order.setDeliveryOption(deliveryOption);
        order.setOrderNumber(generateOrderCode());
        order.setTimeStamp(getCurrentTimestamp());
        order.setOrderType("Credit");
        order.setDateTimePosted(String.valueOf(new Date()));
        order.setStatus("pending");
        order.setConfirmationStatus("confirmed");

        for (OrderProductMap orderProductMap : orderProductMaps){
            orderProductMap.setOrder(order);
        }
        order.setOrderProductMaps(orderProductMaps);

        // Fetch products by their IDs and set them in the Order entity
        Set<Product> products = orderProductMaps.stream()
                .map(orderRequest -> {
                    Product product = productRepository.findById(orderRequest.getProductId()).orElse(null);
                    if (product != null) {

                    }
                    return product;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        order.setProducts(products);
        double sum = calculateTotalPrice(orderProductMaps) + deliveryOption.getCharges();
        order.setTotalPrice(sum);
        order.setProductPrice(calculateTotalPrice(orderProductMaps));
        order.setDeliveryPrice(deliveryOption.getCharges());
        notificationService.sendNewOrderNotification(order);

        return orderRepository.save(order);
    }
    private double calculateTotalPrice(List<OrderProductMap> orderProductMaps) {
        double totalPrice = 0.0;
        for (OrderProductMap orderProductMap : orderProductMaps) {
            System.out.println(orderProductMap.getProductId());
            Product product = productRepository.findById(orderProductMap.getProductId()).orElse(null);
            if (product != null) {
                totalPrice += Integer.parseInt(product.getPrice()) * orderProductMap.getQuantity();
            }
        }
        return totalPrice;
    }
    // Method to get the current timestamp
    public String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // Method to generate an order code of 12 characters
    public String generateOrderCode() {
        // You can modify the characters set as per your requirements
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 12;
        return RandomStringUtils.random(length, characters);
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public void update(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getOrderByUser(User user) {
        return orderRepository.findByUser(user);
    }
    public Page<OrderDTO> getOrdersByEmailPaginated(String email, int pageNumber, int pageSize) {
        User user = userRepository.findByEmail(email);

        // Create a PageRequest for the specified page number and page size
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);

        // Retrieve a Page of Order objects associated with the user
        Page<Order> orderPage = orderRepository.findByUser(user, pageRequest);

        // Convert the Page of Order objects into a Page of OrderDTO objects
        Page<OrderDTO> orderDTOPage = orderPage.map(order -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderNumber(order.getOrderNumber());
            orderDTO.setConfirmationStatus(order.getConfirmationStatus());
            orderDTO.setOrderType(order.getOrderType());
            orderDTO.setDateTimePosted(order.getDateTimePosted());
            orderDTO.setDeliveryOptionDTO(order.getDeliveryOptionDTO());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setId(order.getId());
            orderDTO.setUserDTO(order.getUserDTO());
            orderDTO.setOrderProductMaps(order.getOrderProductMaps());
            orderDTO.setDeliveryPrice(order.getDeliveryPrice());
            orderDTO.setProductPrice(order.getProductPrice());
            orderDTO.setTotalPrice(order.getTotalPrice());
            return orderDTO;
        });

        return orderDTOPage;
    }
    public List<Order> getOrdersByDeliveryOption(DeliveryOption deliveryOption) {
        return orderRepository.findByDeliveryOption(deliveryOption);
    }

    public void toggleStatus(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            if ("delivered".equalsIgnoreCase(order.getStatus())) {
                order.setStatus("pending");
            } else {
                order.setStatus("delivered");
            }
            orderRepository.save(order);
        }
    }

    public void toggleConfirmationStatus(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            if ("complete".equalsIgnoreCase(order.getConfirmationStatus())) {
                order.setConfirmationStatus("incomplete");
                // Delete the order if confirmation status is canceled
                orderRepository.delete(order);
            } else {
                order.setConfirmationStatus("incomplete");
            }
            orderRepository.save(order);
        }
    }

    public List<Order> recommendAShortListOfMostCommonOrders() {
        List<Order> allOrders = orderRepository.findAll();
        List<OrderSimilarity> orderSimilarities = new ArrayList<>();

        // Calculate similarity between each pair of orders
        for (int i = 0; i < allOrders.size(); i++) {
            for (int j = i + 1; j < allOrders.size(); j++) {
                Order order1 = allOrders.get(i);
                Order order2 = allOrders.get(j);

                double similarityScore = calculateSimilarity(order1.getProducts(), order2.getProducts());

                // Add the similarity score and order pair to the list
                orderSimilarities.add(new OrderSimilarity(order1, order2, similarityScore));
            }
        }

        // Sort the order similarities in descending order based on similarity score
        orderSimilarities.sort((o1, o2) -> Double.compare(o2.getSimilarityScore(), o1.getSimilarityScore()));

        // Get the shortlist of most similar orders
        List<Order> shortlist = new ArrayList<>();
        Set<Long> addedOrderIds = new HashSet<>();
        for (OrderSimilarity orderSimilarity : orderSimilarities) {
            if (!addedOrderIds.contains(orderSimilarity.getOrder1().getId())
                    && !addedOrderIds.contains(orderSimilarity.getOrder2().getId())) {
                // Add both orders to the shortlist
                shortlist.add(orderSimilarity.getOrder1());
                shortlist.add(orderSimilarity.getOrder2());

                // Mark the orders as added to avoid duplicates
                addedOrderIds.add(orderSimilarity.getOrder1().getId());
                addedOrderIds.add(orderSimilarity.getOrder2().getId());
            }
        }

        // Return the shortlist
        return shortlist;
    }

    // Helper method to calculate similarity between two sets of products
    private double calculateSimilarity(Set<Product> products1, Set<Product> products2) {
        // Implement your logic to calculate the similarity score between the two sets of products.
        // This can be done using set operations or more advanced algorithms like Jaccard similarity
        // or cosine similarity. For simplicity, let's assume the similarity score is the number of common products.
        Set<Product> commonProducts = new HashSet<>(products1);
        commonProducts.retainAll(products2);
        return commonProducts.size();
    }

    public Order createOrderWithUserAndDeliveryOptionPending(String email, Long deliveryOptionId,
                                                     List<OrderProductMap> orderProductMaps, String merchantRequestId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with the provided email: " + email);
        }

        DeliveryOption deliveryOption = deliveryOptionRepository.findById(deliveryOptionId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryOption not found with the provided ID: " + deliveryOptionId));

        // Set the User and DeliveryOption in the Order entity
        Order order = new Order();
        order.setUser(user);
        order.setDeliveryOption(deliveryOption);
        order.setOrderNumber(generateOrderCode());
        order.setTimeStamp(merchantRequestId);
        order.setOrderType("Credit");
        order.setDateTimePosted(String.valueOf(new Date()));
        order.setStatus("pending");
        order.setConfirmationStatus("incomplete");

        // Fetch products by their IDs and set them in the Order entity
        Set<Product> products = orderProductMaps.stream()
                .map(orderRequest -> {
                    Product product = productRepository.findById(orderRequest.getProductId()).orElse(null);
                    if (product != null) {

                    }
                    return product;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        order.setProducts(products);
        double sum = calculateTotalPrice(orderProductMaps) + deliveryOption.getCharges();
        order.setTotalPrice(sum);
        order.setProductPrice(calculateTotalPrice(orderProductMaps));
        order.setDeliveryPrice(deliveryOption.getCharges());

        return orderRepository.save(order);
    }

    // Helper class to store order similarity information
    private static class OrderSimilarity {
        private final Order order1;
        private final Order order2;
        private final double similarityScore;

        public OrderSimilarity(Order order1, Order order2, double similarityScore) {
            this.order1 = order1;
            this.order2 = order2;
            this.similarityScore = similarityScore;
        }

        public Order getOrder1() {
            return order1;
        }

        public Order getOrder2() {
            return order2;
        }

        public double getSimilarityScore() {
            return similarityScore;
        }
    }
}

