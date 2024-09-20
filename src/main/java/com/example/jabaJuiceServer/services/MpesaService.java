package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.MyDAOs.OrderProductMap;
import com.example.jabaJuiceServer.MyDAOs.PaidOrderRequest;
import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import com.example.jabaJuiceServer.adminDTOs.PayLink;
import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.*;
import com.example.jabaJuiceServer.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MpesaService {
    @Value("${mpesa.api.url}")
    private String mpesaApiUrl;
    @Value("${mpesa.access.token}")
    private String mpesaAccessToken; // Configure this in your application.properties
    @Value("${mpesa.short.code}")
    private String Shortcode;
    private PayLinkRepository payLinkRepository;

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private OrderService orderService;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private DeliveryOptionRepository deliveryOptionRepository;
    private ProductRepository productRepository;

    @Autowired
    public MpesaService(PayLinkRepository payLinkRepository, TransactionService transactionService, TransactionRepository transactionRepository, OrderService orderService, OrderRepository orderRepository, UserRepository userRepository, DeliveryOptionRepository deliveryOptionRepository, ProductRepository productRepository) {
        this.payLinkRepository = payLinkRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.deliveryOptionRepository = deliveryOptionRepository;
        this.productRepository = productRepository;
    }

    public MpesaResponse makeMpesaApiCall(MpesaRequest request, String token) {

        // Make API call to Lipa na M-PESA online API
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Authorization", "Bearer " + token); // Add the access token

        HttpEntity<MpesaRequest> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MpesaResponse> responseEntity = restTemplate.exchange(
                mpesaApiUrl, HttpMethod.POST, entity, MpesaResponse.class);

        MpesaResponse mpesaResponse = responseEntity.getBody();

        // Handle response and save transaction item if successful
        if (mpesaResponse != null && "0".equals(mpesaResponse.getResponseCode())) {
            saveTransactionItem(request, mpesaResponse);
        } else {
            // Handle error case, e.g., throw custom exception or log the error
        }


        return mpesaResponse;
    }

    private void saveTransactionItem(MpesaRequest request, MpesaResponse response) {
        // Create and save a transaction entity to the database
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateReferenceNumber());
        transaction.setCustomerMessage(response.getCustomerMessage());
        transaction.setResponseCode(response.getResponseCode());
        transaction.setResponseDescription(response.getResponseDescription());
        transaction.setTimestamp(request.getTimestamp());
        transaction.setMerchantRequestID(response.getMerchantRequestId());
        transaction.setCheckoutRequestID(response.getCheckoutRequestId());
        transaction.setTransactionAmount(Double.parseDouble(request.getAmount()));
        // Set other transaction properties as needed
        transactionService.saveTransaction(transaction);
    }
    public String generateReferenceNumber() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 12);
    }
    public void handleResultCallback(Map<String, Object> resultBody) {
        Map<String, Object> stkCallback = (Map<String, Object>) resultBody.get("stkCallback");

        int resultCode = (int) stkCallback.get("ResultCode");
        Transaction transaction = new Transaction();
        transaction.setMerchantRequestID(String.valueOf(stkCallback.get("MerchantRequestId")));
        transaction.setCheckoutRequestID(String.valueOf(stkCallback.get("CheckoutRequestId")));
        transaction.setResponseDescription(String.valueOf(stkCallback.get("ResultDescription")));

        if (resultCode == 0) {
            Map<String, Object> callbackMetadata = (Map<String, Object>) stkCallback.get("CallbackMetadata");
            List<Map<String, Object>> callbackItems = (List<Map<String, Object>>) callbackMetadata.get("Item");

            for (Map<String, Object> callbackItem : callbackItems) {
                String name = (String) callbackItem.get("Name");

                Object valueObject = callbackItem.get("Value");
                if (valueObject instanceof String) {
                    String value = (String) valueObject;
                    // Handle the string value
                } else if (valueObject instanceof Number) {
                    // If it's a number, you might want to convert it to a suitable data type
                    // For example, if you expect it to be a timestamp:
                    Long timestamp = ((Number) valueObject).longValue();
                    // Handle the timestamp
                }

                if ("Amount".equals(name)) {
                    // Set transaction amount
                    transaction.setTransactionAmount(Double.parseDouble(String.valueOf(valueObject)));
                } else if ("MpesaReceiptNumber".equals(name)) {
                    // Set receipt number
                    transaction.setTransactionId(String.valueOf(valueObject));
                }


                // Handle each callback metadata item as needed
            }

            transactionRepository.save(transaction);

            // Save the successful transaction to the database
            // transactionRepository.save(transaction);
        } else {
            // Transaction is unsuccessful
            // Handle error case here
        }
    }
    public MpesaResponse makeMpesaApiCallForPhoneNumber(String phoneNumber, String token, String amount,String pay_link_U) {
        PayLink payLink = payLinkRepository.findByUI(pay_link_U);
        MpesaRequest request = new MpesaRequest();
        request.setBusinessShortCode(Shortcode);
        request.setPassword("MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTYwMjE2MTY1NjI3");
        request.setTimestamp("20160216165627");
        request.setTransactionType("CustomerPayBillOnline");
        request.setAmount("1");
        request.setPartyA(phoneNumber);
        request.setPartyB(Shortcode);
        request.setPhoneNumber(phoneNumber);
        request.setCallBackURL("http://192.168.1.45:5000/sanny/callback");
        request.setAccountReference("Naturaw pay");
        request.setTransactionDesc("Make payment");

        // Make API call to Lipa na M-PESA online API
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Authorization", "Bearer " + token); // Add the access token

        HttpEntity<MpesaRequest> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MpesaResponse> responseEntity = restTemplate.exchange(
                mpesaApiUrl, HttpMethod.POST, entity, MpesaResponse.class);

        MpesaResponse mpesaResponse = responseEntity.getBody();

        // Handle response and save transaction item if successful
        if (mpesaResponse != null && "0".equals(mpesaResponse.getResponseCode())) {
            saveTransactionItem(request, mpesaResponse);
        } else {
            // Handle error case, e.g., throw custom exception or log the error
        }


        return mpesaResponse;
    }
    public String generatePassword(String shortcode, String passkey) {
        long timestamp = getCurrentTimestamp(); // Implement your getCurrentTimestamp() method

        String combinedString = shortcode + passkey + timestamp;
        byte[] data = combinedString.getBytes();

        byte[] encodedBytes = Base64.getEncoder().encode(data);
        return new String(encodedBytes);
    }

    // You need to implement this method to get the current timestamp
    private long getCurrentTimestamp() {
        // Implement your logic to get the current timestamp here
        return System.currentTimeMillis();
    }

    public MpesaResponse makeMpesaApiCallForDeliveryPayment(PaidOrderRequest paidOrderRequest) {
        PayLink payLink = payLinkRepository.findByUI(paidOrderRequest.getPayLinkUI());
        MpesaRequest request = new MpesaRequest();
        request.setBusinessShortCode(Shortcode);
        request.setPassword("MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTYwMjE2MTY1NjI3");
        request.setTimestamp("20160216165627");
        request.setTransactionType("CustomerPayBillOnline");
        request.setAmount("1");
        request.setPartyA(paidOrderRequest.getPhoneNumber());
        request.setPartyB(Shortcode);
        request.setPhoneNumber(paidOrderRequest.getPhoneNumber());
        request.setCallBackURL("http://sanny-tech-env.eba-e5g3udca.af-south-1.elasticbeanstalk.com/sanny/callback");
        request.setAccountReference("Naturaw pay");
        request.setTransactionDesc("Make payment");

        // Make API call to Lipa na M-PESA online API
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paidOrderRequest.getToken());
        headers.set("Authorization", "Bearer " + paidOrderRequest.getToken()); // Add the access token

        HttpEntity<MpesaRequest> entity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                mpesaApiUrl, HttpMethod.POST, entity, String.class);

        String responseBody = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MpesaResponse mpesaResponse = objectMapper.readValue(responseBody, MpesaResponse.class);

            // Handle response and save transaction item if successful
            if (mpesaResponse != null && "0".equals(mpesaResponse.getResponseCode())) {
                System.out.println(mpesaResponse.getCustomerMessage());

                createOrderWithUserAndDeliveryOptionPending(paidOrderRequest.getEmail(),
                        Long.parseLong(paidOrderRequest.getDeliveryOptionId()),
                        paidOrderRequest.getOrderProductMaps(),mpesaResponse.getMerchantRequestId());

                return mpesaResponse;
            } else {
                // Handle error case, e.g., throw custom exception or log the error
                if (mpesaResponse != null) {
                    System.err.println("M-PESA API Error: " + mpesaResponse.getResponseDescription());
                } else {
                    System.err.println("M-PESA API Error: No response received");
                }
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error parsing API response: " + e.getMessage());
            return null;
        }
    }
    public void createOrderWithUserAndDeliveryOptionPending(String email, Long deliveryOptionId,
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

        orderRepository.save(order);
    }
    private double calculateTotalPrice(List<OrderProductMap> orderProductMaps) {
        double totalPrice = 0.0;
        for (OrderProductMap orderProductMap : orderProductMaps) {
            Product product = productRepository.findById(orderProductMap.getProductId()).orElse(null);
            if (product != null) {
                totalPrice += Integer.parseInt(product.getPrice()) * orderProductMap.getQuantity();
            }
        }
        return totalPrice;
    }
    // Method to generate an order code of 12 characters
    public String generateOrderCode() {
        // You can modify the characters set as per your requirements
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 12;
        return RandomStringUtils.random(length, characters);
    }
}

