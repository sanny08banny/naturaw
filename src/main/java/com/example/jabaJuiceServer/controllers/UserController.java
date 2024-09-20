package com.example.jabaJuiceServer.controllers;

import com.example.jabaJuiceServer.entities.StoriYaJabaItem;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.entities.UserDTO;
import com.example.jabaJuiceServer.repositories.UserRepository;
import com.example.jabaJuiceServer.services.NotificationService;
import com.example.jabaJuiceServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("jaba/users")
public class UserController {
    private final UserService userService;
    private UserRepository userRepository;
    private NotificationService notificationService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, NotificationService notificationService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.login(email, password);

        if (user != null) {
            // Login successful, return the user object (you may choose not to return the password field)
            user.setPassword(null); // Optional: Remove the password from the response for security reasons
            return ResponseEntity.ok(user);
        } else {
            // Login failed, return an error response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }
    @PostMapping("/new")
    public ResponseEntity<String> saveNewUser(@RequestPart("item") User user, @RequestPart("file") MultipartFile file) {
        String userEmail = user.getEmail();
        User existingUser = userRepository.findByEmail(userEmail);
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("User with the same email already exists.");
        }

        userService.createUser(user, file);
        return ResponseEntity.ok("User saved successfully.");
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping("/{userId}/follow/{followUserId}")
    public ResponseEntity<String> followUser(@PathVariable Long userId, @PathVariable Long followUserId) {
        userService.followUser(userId, followUserId);
        return ResponseEntity.ok("User followed successfully.");
    }

    @PostMapping("/{userId}/unfollow/{unfollowUserId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId, @PathVariable Long unfollowUserId) {
        userService.unfollowUser(userId, unfollowUserId);
        return ResponseEntity.ok("User unfollowed successfully.");
    }
    @PostMapping("/follow/byFileName")
    public ResponseEntity<String> followUserByFileName(@RequestParam("followerEmail") String followerEmail, @RequestParam("fileName") String fileName) {
        Long followerId = userService.getUserIdByEmail(followerEmail);
        Long followingId = userService.getUserIdByFileName(fileName);

        if (followerId != null && followingId != null) {
            // Perform the follow operation using the retrieved user IDs
            // Your follow logic here
            userService.followUser(followerId,followingId);
            return ResponseEntity.ok("User followed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
    }
    @PostMapping("/follow/byId")
    public ResponseEntity<String> followUserById(@RequestParam("followerEmail") String followerEmail,
                                                      @RequestParam("followingUser") Long id) {
        Long followerId = userService.getUserIdByEmail(followerEmail);

        if (followerId != null && id != null) {
            // Perform the unfollow operation using the retrieved user IDs
            // Your unfollow logic here
            userService.followUser(followerId,id);

            return ResponseEntity.ok("User unfollowed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
    }
    @PostMapping("/follow")
    public ResponseEntity<String> followUserByEmail(@RequestParam("followerEmail") String followerEmail,
                                                    @RequestParam("followingEmail") String followingEmail) {
        Long followerId = userService.getUserIdByEmail(followerEmail);
        Long followingId = userService.getUserIdByEmail(followingEmail);

        if (followerId != null && followingId != null) {
            // Perform the follow operation using the retrieved user IDs
            // Your follow logic here
            userService.followUser(followerId,followingId);
            return ResponseEntity.ok("User followed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
    }
    @PostMapping("/unfollow/byFileName")
    public ResponseEntity<String> unFollowUserByFileName(@RequestParam("followerEmail") String followerEmail, @RequestParam("fileName") String fileName) {
        Long followerId = userService.getUserIdByEmail(followerEmail);
        Long followingId = userService.getUserIdByFileName(fileName);

        if (followerId != null && followingId != null) {
            // Perform the follow operation using the retrieved user IDs
            // Your follow logic here
            userService.unfollowUser(followerId,followingId);
            return ResponseEntity.ok("User followed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUserByEmail(@RequestParam("followerEmail") String followerEmail, @RequestParam("followingEmail") String followingEmail) {
        Long followerId = userService.getUserIdByEmail(followerEmail);
        Long followingId = userService.getUserIdByEmail(followingEmail);

        if (followerId != null && followingId != null) {
            // Perform the unfollow operation using the retrieved user IDs
            // Your unfollow logic here
            userService.unfollowUser(followerId,followingId);

            return ResponseEntity.ok("User unfollowed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
    }
    @PostMapping("/unfollow/byId")
    public ResponseEntity<String> unfollowUserById(@RequestParam("followerEmail") String followerEmail,
                                                      @RequestParam("followingUser") Long id) {
        Long followerId = userService.getUserIdByEmail(followerEmail);

        if (followerId != null && id != null) {
            // Perform the unfollow operation using the retrieved user IDs
            // Your unfollow logic here
            userService.unfollowUser(followerId,id);

            return ResponseEntity.ok("User unfollowed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
    }
    @GetMapping("/followers")
    public ResponseEntity<List<UserDTO>> getFollowers(@RequestParam("email") String email) {
        Long userId = userService.getUserIdByEmail(email);
        List<UserDTO> followers = userService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }
    @GetMapping("/follows")
    public ResponseEntity<List<UserDTO>> getFollows(@RequestParam("email") String email) {
        Long userId = userService.getUserIdByEmail(email);
        List<UserDTO> followers = userService.getFollows(userId);
        return ResponseEntity.ok(followers);
    }
    @GetMapping("/{email}/followers")
    public List<UserDTO> getFollowersByEmail(@PathVariable String email) {
        Long userId = userService.getUserIdByEmail(email);
        List<UserDTO> followers = userService.getFollowers(userId);
        return followers;
    }
    @GetMapping("/{email}/follows")
    public List<UserDTO> getFollowsByEmail(@PathVariable String email) {
        Long userId = userService.getUserIdByEmail(email);
        List<UserDTO> followers = userService.getFollows(userId);
        return followers;
    }
    @GetMapping("/ByEmail")
    public List<StoriYaJabaItem> getStorisByEmail(@RequestParam("email") String email) {
        return userService.getStorisByEmail(email);
    }
    @GetMapping("/byEmail/{email}")
    public User getProfileAccountByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }
    @GetMapping("/sync-by-email/{email}")
    public User syncProfileAccountByEmail(@PathVariable String email){
        return userService.syncAccount(email);
    }
    @GetMapping("/search")
    public List<UserDTO> searchUsers(@RequestParam("email") String email,
                                     @RequestParam("query") String query) {
        List<UserDTO> users = userService.searchUsers(email,query);
        return users;
    }
    @GetMapping("/recommended_friends")
    public List<UserDTO> getRecommendedUsers(@RequestParam("email") String email) {
        List<UserDTO> users = userService.getAllUsersDTOS();
        return users;
    }
    @GetMapping("/close_friends")
    public List<UserDTO> getCloseUsers(@RequestParam("email") String email) {
        List<UserDTO> users = userService.getAllUsersDTOS();
        return users;
    }
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestParam("email") String email,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword) {
        Long userId = userService.getUserIdByEmail(email);
        userService.changePassword(userId, currentPassword, newPassword);
        return ResponseEntity.ok("Password changed successfully.");
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<String> changeUsername(
            @RequestParam("email") String email,
            @RequestParam("newUsername") String newUsername) {
        Long userId = userService.getUserIdByEmail(email);
        userService.changeUsername(userId, newUsername);
        return ResponseEntity.ok("Username changed successfully.");
    }

    @PostMapping("/changeFullname")
    public ResponseEntity<String> changeFullname(
            @RequestParam("email") String email,
            @RequestParam("newFullname") String newFullname) {
        Long userId = userService.getUserIdByEmail(email);
        userService.changeFullname(userId, newFullname);
        return ResponseEntity.ok("Fullname changed successfully.");
    }

    @PostMapping("/{userId}/changeAddress")
    public ResponseEntity<String> changeAddress(
            @RequestParam("email") String email,
            @RequestParam String newAddress) {
        Long userId = userService.getUserIdByEmail(email);
        userService.changeAddress(userId, newAddress);
        return ResponseEntity.ok("Address changed successfully.");
    }
    @PostMapping("/tokenUpdate/{email}")
    public ResponseEntity<String> updateFirebaseToken(@PathVariable String email, @RequestBody String firebaseToken) {
        User account = userService.updateFirebaseToken(email, firebaseToken);
        if (account != null) {
            return ResponseEntity.ok("Token updated successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
