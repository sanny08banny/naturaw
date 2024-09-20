package com.example.jabaJuiceServer.controllers;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.AdminUserRepository;
import com.example.jabaJuiceServer.repositories.UserRepository;
import com.example.jabaJuiceServer.services.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sanny/admin_users")
public class AdminUserController {
    private final AdminUserService adminUserService;
    private AdminUserRepository adminUserRepository;
    private UserRepository userRepository;

    @Autowired
    public AdminUserController(AdminUserService adminUserService, AdminUserRepository adminUserRepository, UserRepository userRepository) {
        this.adminUserService = adminUserService;
        this.adminUserRepository = adminUserRepository;
        this.userRepository = userRepository;
    }

    // Endpoint to create an AdminUser
    @PostMapping("/create")
    public ResponseEntity<String> saveNewUser(@RequestPart("item") AdminUser user, @RequestPart("file") MultipartFile file) {
        String userEmail = user.getEmail();
        AdminUser existingUser = adminUserRepository.findByEmail(userEmail);
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("User with the same email already exists.");
        }

        adminUserService.createUser(user, file);
        return ResponseEntity.ok("User saved successfully.");
    }
    @PostMapping("/create-admin")
    public ResponseEntity<String> saveNewAdminUser(@RequestParam("email") String email) {
        AdminUser existingUser = adminUserRepository.findByEmail(email);
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("User with the same email already exists.");
        }
        User user = userRepository.findByEmail(email);
        if (user != null) {
            adminUserService.createAdminAccess(user.getId());
        }else {
            return ResponseEntity.badRequest().body("User with the same email already exists.You must be registered");
        }
        return ResponseEntity.ok("Admin User saved successfully.");
    }

    // Endpoint to delete an AdminUser by ID
    @DeleteMapping("/delete/{id}")
    public void deleteAdminUser(@PathVariable Long id) {
        adminUserService.delete(id);
    }

    // Endpoint to update an AdminUser
    @PutMapping("/update")
    public AdminUser updateAdminUser(@RequestBody AdminUser adminUser) {
        return adminUserService.update(adminUser);
    }

    // Endpoint to find an AdminUser by ID
    @GetMapping("/findbyid/{id}")
    public AdminUser findAdminUserById(@PathVariable Long id) {
        return adminUserService.findById(id);
    }

    // Endpoint to find an AdminUser by email
    @GetMapping("/findbyemail/{email}")
    public AdminUser findAdminUserByEmail(@PathVariable String email) {
        return adminUserService.findByEmail(email);
    }

    // Endpoint to add follow relationship between two AdminUsers
    @PostMapping("/addfollow")
    public void addFollow(@RequestParam Long followerId, @RequestParam Long followeeId) {
        // Get the AdminUser instances based on their IDs
        AdminUser follower = adminUserService.findById(followerId);
        AdminUser followee = adminUserService.findById(followeeId);

        // Check if the users are valid and then add the follow relationship
        if (follower != null && followee != null) {
            adminUserService.addFollow(follower, followee);
        }
    }

    // Endpoint to unfollow a user
    @PostMapping("/unfollow")
    public void unfollow(@RequestParam Long followerId, @RequestParam Long followeeId) {
        // Get the AdminUser instances based on their IDs
        AdminUser follower = adminUserService.findById(followerId);
        AdminUser followee = adminUserService.findById(followeeId);

        // Check if the users are valid and then remove the follow relationship
        if (follower != null && followee != null) {
            adminUserService.unfollow(follower, followee);
        }
    }

//    // Endpoint to get followers of an AdminUser
//    @GetMapping("/{id}/followers")
//    public List<AdminUser> getFollowers(@PathVariable Long id) {
//        AdminUser adminUser = adminUserService.findById(id);
//        if (adminUser != null) {
//            return adminUserService.getFollowers(adminUser);
//        }
//        return new ArrayList<>();
//    }
//
//    // Endpoint to get the AdminUsers an AdminUser is following
//    @GetMapping("/{id}/follows")
//    public List<AdminUser> getFollows(@PathVariable Long id) {
//        AdminUser adminUser = adminUserService.findById(id);
//        if (adminUser != null) {
//            return adminUserService.getFollows(adminUser);
//        }
//        return new ArrayList<>();
//    }

    // Endpoint to get all AdminUsers
    @GetMapping("/getall")
    public List<AdminUser> getAllAdminUsers() {
        return adminUserService.getAll();
    }
}

