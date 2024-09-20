package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.AdminUserRepository;
import com.example.jabaJuiceServer.repositories.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminUserService {
    private final String FOLDER_PATH = "C:\\\\Users\\\\dell\\\\Documents\\\\spring_projects\\\\jabaJuiceServer\\\\src\\\\main\\\\resources\\\\static";
    private final AdminUserRepository adminUserRepository;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private NotificationService notificationService;

    @Autowired
    public AdminUserService(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, NotificationService notificationService) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // Method to create an AdminUser
    public void createUser(AdminUser user, MultipartFile file) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            // Get the path of the static folder
            String staticFolderPath = FOLDER_PATH;

            // Define the folder inside the static folder where you want to save the file
            String uploadFolderPath = staticFolderPath + "/profile_images/";

            // Create the folder if it doesn't exist
            File uploadFolder = new File(uploadFolderPath);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Generate a unique file name
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString();

            // Save the file to the upload folder
            String filePath = uploadFolderPath + fileName + "." + extension;
            File dest = new File(filePath);
            file.transferTo(dest);
            // Get the URL of the uploaded file
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/profile_images/")
                    .path(fileName)
                    .toUriString();

            // Update the item's filePath field with the file reference
            user.setFilePath(filePath);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            adminUserRepository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public AdminUser createAdminAccess(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            AdminUser adminUser = createAdminItem(user.get());
            adminUserRepository.save(adminUser);
            user.get().setRole("ADMIN");
            userRepository.save(user.get());
            return adminUser;
        }else {
            return null;
        }
    }
    public AdminUser createAdminItem(User user) {
        AdminUser adminUser = new AdminUser();

// Copy attributes from the User to AdminUser
        adminUser.setUserName(user.getUserName());
        adminUser.setFullName(user.getFullName());
        adminUser.setEmail(user.getEmail());
        adminUser.setPassword(user.getPassword());
        adminUser.setGender(user.getGender());
        adminUser.setAddress(user.getAddress());
        adminUser.setAge(user.getAge());
        adminUser.setFilePath(user.getFilePath());
        adminUser.setDateCreated(user.getDateCreated());
        adminUser.setFcmToken(user.getFcmToken());

// Set the role to "ADMIN"
        adminUser.setRole("ADMIN");
        notificationService.sendNewAdminNotification(user.getId());
        return adminUser;
    }
//    public List<UserDTO> getAllUsersDTOS() {
//        List<User> users = userRepository.findAll();
//        List<UserDTO> result = new ArrayList<>();
//        for (User user : users){
//            UserDTO userDTO = new UserDTO(user.getId(), user.getUserName(), user.getFullName(),
//                    user.getEmail(), user.getRole(), user.getGender(), user.getAddress(), user.getAge(),
//                    user.getFilePath(), user.getDateCreated());
//            result.add(userDTO);
//        }
//        return result;
//    }

    // Method to delete an AdminUser by ID
    public void delete(Long id) {
        adminUserRepository.deleteById(id);
    }

    // Method to update an AdminUser
    public AdminUser update(AdminUser adminUser) {
        return adminUserRepository.save(adminUser);
    }

    // Method to find an AdminUser by ID
    public AdminUser findById(Long id) {
        return adminUserRepository.findById(id).orElse(null);
    }

    // Method to find an AdminUser by email
    public AdminUser findByEmail(String email) {
        return adminUserRepository.findByEmail(email);
    }

    // Method to add a follow relationship between two AdminUsers
    public void addFollow(AdminUser follower, AdminUser followee) {
        // Add logic to establish the follow relationship between the two users
    }

    // Method to unfollow a user
    public void unfollow(AdminUser follower, AdminUser followee) {
        // Add logic to remove the follow relationship between the two users
    }

//    // Method to get followers of an AdminUser
//    public List<AdminUser> getFollowers(AdminUser adminUser) {
//        return adminUser.getFollowers();
//    }
//
//    // Method to get the AdminUsers an AdminUser is following
//    public List<AdminUser> getFollows(AdminUser adminUser) {
//        return adminUser.getFollows();
//    }

    // Method to get all AdminUsers
    public List<AdminUser> getAll() {
        return adminUserRepository.findAll();
    }
}
