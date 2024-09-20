package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.Order;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.NotificationRepository;
import com.example.jabaJuiceServer.repositories.UserRepository;
import com.google.firebase.messaging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

// 3. Send notifications using FCM
@Service
public class NotificationService {

    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    @Autowired
    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(Long recipientId, String messageContent) {
        User recipient = userRepository.findById(recipientId).orElse(null);

        if (recipient != null && recipient.getFcmToken() != null) {
            // Construct the FCM message
            Message message = Message.builder()
                    .putData("content", messageContent)
                    .setToken(recipient.getFcmToken())
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Successfully sent message: " + response);
            } catch (FirebaseMessagingException e) {
                System.err.println("Error sending message: " + e.getMessage());
            }
        }
    }

    public void sendFollowNotification(Long recipientId, User follower) {
        User recipient = userRepository.findById(recipientId).orElse(null);

        if (recipient != null && recipient.getFcmToken() != null) {
            Message message = Message.builder()
                    .setToken(recipient.getFcmToken())
                    .putData("title","New follower")
                    .putData("content", follower.getUserName() + " started following you!")
                    .putData("largeIcon", follower.getFilePath())
                    .build();
            com.example.jabaJuiceServer.entities.Notification message1 = new com.example.jabaJuiceServer.entities.Notification();
            message1.setTitle("New follower");
            message1.setContent(follower.getUserName() + " started following you!");
            message1.setLargeIcon(follower.getFilePath());
            message1.setUser(recipient);
            notificationRepository.save(message1);

            sendNotification(message);
        }
    }
    public void sendNewMessageNotification(Long recipientId, User sendingUser, com.example.jabaJuiceServer.entities.Notification notification) {
        // Similar to sendFollowNotification, but with different content
        User recipient = userRepository.findById(recipientId).orElse(null);

        if (recipient != null && recipient.getFcmToken() != null) {
            Message message = Message.builder()
                    .setToken(recipient.getFcmToken())
                    .putData("title","New message")
                    .putData("content", notification.getContent())
                    .putData("largeIcon", notification.getLargeIcon())
                    .build();
            notificationRepository.save(notification);

            sendNotification(message);
        }
    }
    public void sendLikeNotification(Long recipientId, User liker) {
        // Similar to sendFollowNotification, but with different content
        User recipient = userRepository.findById(recipientId).orElse(null);

        if (recipient != null && recipient.getFcmToken() != null) {
            Message message = Message.builder()
                    .setToken(recipient.getFcmToken())
                    .putData("title","New like")
                    .putData("content", liker.getUserName() + " liked your stori!")
                    .putData("largeIcon", liker.getFilePath())
                    .build();
            com.example.jabaJuiceServer.entities.Notification message1 = new com.example.jabaJuiceServer.entities.Notification();
            message1.setTitle("New like");
            message1.setContent(liker.getUserName() + " liked your stori!");
            message1.setLargeIcon(liker.getFilePath());
            message1.setUser(recipient);
            notificationRepository.save(message1);

            sendNotification(message);
        }
    }

    public void sendRelateNotification(Long recipientId, User relater) {
        // Similar to sendFollowNotification, but with different content
        User recipient = userRepository.findById(recipientId).orElse(null);

        if (recipient != null && recipient.getFcmToken() != null) {
            Message message = Message.builder()
                    .setToken(recipient.getFcmToken())
                    .putData("title","New relate")
                    .putData("content", relater.getUserName() + " related to your stori!")
                    .putData("largeIcon", relater.getFilePath())
                    .build();
            com.example.jabaJuiceServer.entities.Notification message1 = new com.example.jabaJuiceServer.entities.Notification();
            message1.setTitle("New relate");
            message1.setContent(relater.getUserName() + " related to your stori!");
            message1.setLargeIcon(relater.getFilePath());
            message1.setUser(recipient);
            notificationRepository.save(message1);

            sendNotification(message);
        }
    }
    public void sendNewAdminNotification(Long recipientId) {
        // Similar to sendFollowNotification, but with different content
        User recipient = userRepository.findById(recipientId).orElse(null);

        if (recipient != null && recipient.getFcmToken() != null) {
            Message message = Message.builder()
                    .setToken(recipient.getFcmToken())
                    .putData("title","New Admin")
                    .putData("content", "Congratulations " + recipient.getUserName() + ", you're now an admin. Sync your " +
                            "account in the app settings to load your account.")
                    .putData("largeIcon", recipient.getFilePath())
                    .build();
            com.example.jabaJuiceServer.entities.Notification message1 = new com.example.jabaJuiceServer.entities.Notification();
            message1.setTitle("New Admin");
            message1.setContent("Congratulations " + recipient.getUserName() + ", you're now an admin. Sync your " +
                    "account in the app settings to load your account.");
            message1.setLargeIcon(recipient.getFilePath());
            message1.setUser(recipient);
            notificationRepository.save(message1);

            sendNotification(message);
        }
    }
    public void sendNewOrderNotification(Order order) {
        // Similar to sendFollowNotification, but with different content
        User recipient = userRepository.findByEmail(order.getDeliveryOption().getAdminUser().getEmail());
        Set<Product> products = order.getProducts();

        if (recipient != null && recipient.getFcmToken() != null) {
            Message message = Message.builder()
                    .setToken(recipient.getFcmToken())
                    .putData("title","New order request")
                    .putData("content", order.getOrderType() + " order: " +
                            order.getOrderNumber() + " has been requested.")
                    .putData("largeIcon", order.getUser().getFilePath())
                    .putData("image",products.stream().findFirst().get().getFilePath())
                    .build();
            com.example.jabaJuiceServer.entities.Notification message1 = new com.example.jabaJuiceServer.entities.Notification();
            message1.setTitle("New order request");
            message1.setContent(order.getOrderType() + " order: " +
                    order.getOrderNumber() + " has been requested.");
            message1.setLargeIcon(order.getUser().getFilePath());
            message1.setUser(recipient);
            notificationRepository.save(message1);

            sendNotification(message);
        }
    }
    public void sendNewProductNotification(Long recipientId, Product product) {
        // Similar to sendFollowNotification, but with different content
        User recipient = userRepository.findById(recipientId).orElse(null);

        List<User> users = userRepository.findAll();
        for (User user: users){
            if (user.getFcmToken() != null) {
                Message message = Message.builder()
                        .setToken(user.getFcmToken())
                        .putData("title", "Naturaw")
                        .putData("content", "New product, " + product.getName() + " has been created.")
                        .putData("largeIcon", product.getUserDTO().getFilePath())
                        .putData("image", product.getFilePath())
                        .build();
                com.example.jabaJuiceServer.entities.Notification message1 = new com.example.jabaJuiceServer.entities.Notification();
                message1.setTitle("Naturaw");
                message1.setContent("New product, " + product.getName() + " has been created.");
                message1.setLargeIcon(product.getUserDTO().getFilePath());
                message1.setImage(product.getFilePath());
                message1.setUser(user);
                notificationRepository.save(message1);

                sendNotification(message);
            }
        }
    }
    // Implement similar methods for other notification types

    public void subscribeToTopic(Long userId, String topicName) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            try {
                FirebaseMessaging.getInstance().subscribeToTopic(Arrays.asList(user.getFcmToken()), topicName);
                System.out.println("Subscribed to topic: " + topicName);
            } catch (FirebaseMessagingException e) {
                System.err.println("Error subscribing to topic: " + e.getMessage());
            }
        }
    }

    public void unsubscribeFromTopic(Long userId, String topicName) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            try {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Arrays.asList(user.getFcmToken()), topicName);
                System.out.println("Unsubscribed from topic: " + topicName);
            } catch (FirebaseMessagingException e) {
                System.err.println("Error unsubscribing from topic: " + e.getMessage());
            }
        }
    }

    public void sendTopicNotification(String topicName, String notificationTitle, String notificationBody, String largeIcon,String tokenId) {
        Message message = Message.builder()
                .setToken(tokenId)
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(notificationTitle)
                        .setBody(notificationBody)
                        .setImage(largeIcon)  // Set large icon URL
                        .build())
                .build();

        sendNotification(message);
    }

    private void sendNotification(Message message) {
        try {
            FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + message);
        } catch (FirebaseMessagingException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}

