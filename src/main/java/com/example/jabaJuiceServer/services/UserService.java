package com.example.jabaJuiceServer.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.config.StorageConfig;
import com.example.jabaJuiceServer.entities.Follow;
import com.example.jabaJuiceServer.entities.StoriYaJabaItem;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.entities.UserDTO;
import com.example.jabaJuiceServer.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private static final String S3_BUCKET_NAME = "naturaw1";
    private final String FOLDER_PATH = "C:\\\\Users\\\\User\\\\Documents\\\\jxtsam\\\\jabaJuiceServer\\\\src\\\\main\\\\resources\\\\static";
    private UserRepository userRepository;
    private FollowRepository followsRepository;
    private RelatesRepository relatesRepository;
    private LikesRepository likesRepository;
    private StoriYaJabaRepository storiYaJabaRepository;
    private AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private FollowService followService;
    private AmazonS3 s3Client;
    private StorageConfig storageConfig;

    @Autowired
    public UserService(UserRepository userRepository, FollowRepository followsRepository,
                       RelatesRepository relatesRepository, LikesRepository likesRepository,
                       StoriYaJabaRepository storiYaJabaRepository,
                       AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder,
                       FollowService followService, StorageConfig storageConfig) {
        this.userRepository = userRepository;
        this.followsRepository = followsRepository;
        this.relatesRepository = relatesRepository;
        this.likesRepository = likesRepository;
        this.storiYaJabaRepository = storiYaJabaRepository;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.followService = followService;
        this.storageConfig = storageConfig;
        this.s3Client = storageConfig.s3Client();
    }

    public void createUser(User user, MultipartFile file) {
        if (user == null || file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Invalid user or file");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Attempt to save the file to S3 first
        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            String s3FilePath = "profile_images/" + fileName;

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, s3FilePath, inputStream, metadata));
            }

            String fileUrl = s3Client.getUrl(S3_BUCKET_NAME, s3FilePath).toString();
            user.setFilePath(fileUrl);
            userRepository.save(user);
        } catch (AmazonS3Exception | IOException e) {
            // Fall back to saving the file in local storage
            try {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String fileName = UUID.randomUUID().toString() + "." + extension;

                String localFilePath = FOLDER_PATH + "/profile_images/" + fileName;
                File localFile = new File(localFilePath);

                try (InputStream inputStream = file.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(localFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                user.setFilePath(localFilePath);
                userRepository.save(user);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to save file. Please try again.");
            }
        }
    }
    public String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    public String generateReferenceNumber() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 12);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<UserDTO> getAllUsersDTOS() {
        List<User> users = userRepository.findAll();
        List<UserDTO> result = new ArrayList<>();
        for (User user : users){
            UserDTO userDTO = new UserDTO(user.getId(), user.getUserName(), user.getFullName(),
                    user.getEmail(), user.getRole(), user.getGender(), user.getAddress(), user.getAge(),
                    user.getFilePath(), user.getDateCreated());
            result.add(userDTO);
        }
        return result;
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return (user != null) ? user.getId() : null;
    }
    public List<StoriYaJabaItem> getStorisByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new ArrayList<>(user.getStoriYaJabaItems());
        }
        return new ArrayList<>();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User syncAccount(String email) {
        AdminUser adminUser = adminUserRepository.findByEmail(email);
        if (adminUser != null){
            User user = userRepository.findByEmail(email);
            if (user != null && user.getRole().isEmpty() && !user.getRole().equals("ADMIN")){
                user.setRole("ADMIN");
                userRepository.save(user);
                return user;
            }else {
                return user;
            }
        }else {
            return userRepository.findByEmail(email);
        }
    }
    public Long getUserIdByFileName(String fileName) {
        StoriYaJabaItem storiYaJabaItem = storiYaJabaRepository.findByFileName(fileName);
        User user = storiYaJabaItem.getUser();
        return user.getId();
    }

    public List<UserDTO> searchUsers(String email,String query) {

        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be empty or null.");
        }

        // Convert the query to lowercase for case-insensitive search
        String lowerQuery = "%" + query.toLowerCase() + "%";

        User requestUser = userRepository.findByEmail(email);
        List<User> allUsers = userRepository.searchUsersByQuery(lowerQuery);
        List<UserDTO> result = new ArrayList<>();
        for (User user : allUsers) {
                UserDTO userDTO = new UserDTO(user.getId(), user.getUserName(), user.getFullName(),
                        user.getEmail(), user.getRole(), user.getGender(), user.getAddress(), user.getAge(),
                        user.getFilePath(), user.getDateCreated(), user.getFcmToken(), "false");

                if (requestUser != null) {
                    userDTO.setIsFollowing(String.valueOf(followService.isFollowing(requestUser,user)));
                    result.add(userDTO);
                }
        }
        return result;
    }

    public void followUser(Long userId, Long followedUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found."));
        User followedUser = userRepository.findById(followedUserId).orElseThrow(() -> new EntityNotFoundException("Followed user not found."));

        if (followService.isFollowing(user, followedUser)) {
            throw new IllegalArgumentException("Already following this user");
        }

        followService.followUser(user, followedUser);
    }

    public void unfollowUser(Long userId, Long unfollowedUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found."));
        User unfollowedUser = userRepository.findById(unfollowedUserId).orElseThrow(() -> new EntityNotFoundException("Unfollowed user not found."));

        Follow follow = followService.getFollowByUserAndFollowedUser(user, unfollowedUser);
        if (follow == null) {
            throw new IllegalArgumentException("Not following this user");
        }

        followService.unfollowUser(follow);
    }

    public List<UserDTO> getFollowers(Long userId) {
        User user1 = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found."));
        List<User> followers = followService.getFollowersByUser(user1);

        List<UserDTO> result = new ArrayList<>();

        for (User follower : followers) {
            boolean isFollowing = followService.getFollowsByUser(user1).contains(follower);
            UserDTO userDTO = new UserDTO(follower.getId(), follower.getUserName(), follower.getFullName(),
                    follower.getEmail(), follower.getRole(), follower.getGender(), follower.getAddress(), follower.getAge(),
                    follower.getFilePath(), follower.getDateCreated(), follower.getFcmToken(), String.valueOf(isFollowing));
            result.add(userDTO);
        }
        return result;
    }

    public List<UserDTO> getFollows(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found."));
        List<User> follows = followService.getFollowsByUser(user);

        List<UserDTO> result = new ArrayList<>();

        for (User followedUser : follows) {
            boolean isFollowing = followService.getFollowersByUser(user).contains(followedUser);
            UserDTO userDTO = new UserDTO(followedUser.getId(), followedUser.getUserName(), followedUser.getFullName(),
                    followedUser.getEmail(), followedUser.getRole(), followedUser.getGender(), followedUser.getAddress(), followedUser.getAge(),
                    followedUser.getFilePath(), followedUser.getDateCreated(), followedUser.getFcmToken(), String.valueOf(isFollowing));
            result.add(userDTO);
        }
        return result;
    }
    public User login(String email, String password) {
        // Find the user by email
        User user = userRepository.findByEmail(email);

        // Check if the user exists and the provided password matches the stored password
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Decoded Existing Password: " + user.getPassword());

            return user; // Login successful, return the user object
        }

        return null; // Login failed, return null
    }
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeUsername(Long userId, String newUsername) {
        // Check if the new username is available
        User existingUser = userRepository.findByUserName(newUsername);
        if (existingUser != null && !existingUser.getId().equals(userId)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        user.setUserName(newUsername);
        userRepository.save(user);
    }
    public void changeAddress(Long userId, String newAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        user.setAddress(newAddress);
        userRepository.save(user);
    }

    public void changeFullname(Long userId, String newFullname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        user.setFullName(newFullname);
        userRepository.save(user);
    }
    public User updateFirebaseToken(String email, String firebaseToken) {
        User optionalAccount = userRepository.findByEmail(email);
        if (!(optionalAccount == null)) {
            if (!firebaseToken.equals(optionalAccount.getFcmToken())) {
                optionalAccount.setFcmToken(firebaseToken);
                return userRepository.save(optionalAccount);
            }
        }
        return null; // Account not found or token already matches
    }
}

