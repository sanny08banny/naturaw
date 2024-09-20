package com.example.jabaJuiceServer.controllers;


import com.example.jabaJuiceServer.entities.Comment;
import com.example.jabaJuiceServer.entities.StoriYaJabaItem;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.services.StoriYaJabaService;
import com.example.jabaJuiceServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stori-ya-jaba-items")
public class StoriYaJabaController {
    private final StoriYaJabaService storiYaJabaService;
    private UserService userService;

    @Autowired
    public StoriYaJabaController(StoriYaJabaService storiYaJabaService, UserService userService) {
        this.storiYaJabaService = storiYaJabaService;
        this.userService = userService;
    }

    @PostMapping("/newStori")
    public ResponseEntity<String> saveStoriYaJabaItem(@RequestPart("item") StoriYaJabaItem item,
                                                      @RequestPart("file") MultipartFile file,
                                                      @RequestParam("email") String email) {
        // Save the item using the repository
        storiYaJabaService.createStoriYaJabaItem(item, file,email);
        return ResponseEntity.ok("StoriYaJabaItem saved successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStoriYaJabaItem(@PathVariable Long id) {
        storiYaJabaService.deleteStoriYaJabaItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<StoriYaJabaItem>> getStoriYaJabaItemById(@PathVariable Long id) {
        Optional<StoriYaJabaItem> item = storiYaJabaService.getStoriYaJabaItemById(id);
        if (item.isPresent()) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<StoriYaJabaItem>> getAllStoriYaJabaItems() {
        List<StoriYaJabaItem> items = storiYaJabaService.getAllStoris();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @GetMapping("/all-images")
    public ResponseEntity<List<StoriYaJabaItem>> getAllImageStoriYaJabaItems() {
        List<StoriYaJabaItem> items = storiYaJabaService.getAllImageStoris();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @GetMapping("/all-videos")
    public ResponseEntity<List<StoriYaJabaItem>> getAllVideoStoriYaJabaItems() {
        List<StoriYaJabaItem> items = storiYaJabaService.getAllVideoStoris();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @GetMapping("/video/{id}")
    public ResponseEntity<InputStreamResource> getVideoStream(@PathVariable Long id) {
        Optional<StoriYaJabaItem> item = storiYaJabaService.getStoriYaJabaItemById(id);
        StoriYaJabaItem item1 = item.get();
        if ("video".equals(item1.getStoriType())) {
            File videoFile = new File(item1.getFilePath());
            try {
                InputStream inputStream = new FileInputStream(videoFile);
                InputStreamResource resource = new InputStreamResource(inputStream);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentLength(videoFile.length());
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + videoFile.getName() + "\"");

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } catch (IOException e) {
                // Handle exception, log or return an error response
            }
        }

        // Return a 404 response if the item is not found or is not a video
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/commentStori")
    public ResponseEntity<?> commentOnStori(@RequestPart("comment") Comment comment,
                                        @RequestParam("email") String email,
                                        @RequestParam("id") Long storiId) {
        storiYaJabaService.createComment(comment,email,storiId);
        return ResponseEntity.ok("Product saved successfully.");
    }
    @PostMapping("/{itemId}/relate/{email}")
    public ResponseEntity<String> relateStoriYaJabaItem(@PathVariable Long itemId, @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        storiYaJabaService.relateStoriYaJabaItem(itemId, user.getId());
        return ResponseEntity.ok("Item related successfully.");
    }

    @PostMapping("/{itemId}/like/{email}")
    public ResponseEntity<String> likeStoriYaJabaItem(@PathVariable Long itemId, @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        storiYaJabaService.likeStoriYaJabaItem(itemId, user.getId());
        return ResponseEntity.ok("Item liked successfully.");
    }
    @DeleteMapping("/{itemId}/unRelate/{email}")
    public ResponseEntity<String> unRelateStoriYaJabaItem(@PathVariable Long itemId, @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        storiYaJabaService.unRelateStoriYaJabaItem(itemId, user.getId());
        return ResponseEntity.ok("Item unRelated successfully.");
    }

    @DeleteMapping("/{itemId}/unLike/{email}")
    public ResponseEntity<String> unLikeStoriYaJabaItem(@PathVariable Long itemId, @PathVariable String email) {
        User user = userService.getUserByEmail(email);
        storiYaJabaService.unLikeStoriYaJabaItem(itemId, user.getId());
        return ResponseEntity.ok("Item unLiked successfully.");
    }
    @GetMapping("/storis")
    public Page<StoriYaJabaItem> getAllStorisPaginated(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        return storiYaJabaService.getAllStorisPaginated(page, pageSize);
    }

    @GetMapping("/video_storis")
    public Page<StoriYaJabaItem> getAllVideoStorisPaginated(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int pageSize) {
        return storiYaJabaService.getAllVideoStorisPaginated(page, pageSize);
    }

    @GetMapping("/image_storis")
    public Page<StoriYaJabaItem> getAllImageStorisPaginated(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int pageSize) {
        return storiYaJabaService.getAllImageStorisPaginated(page, pageSize);
    }
    @GetMapping("/random/{email}")
    public ResponseEntity<Page<StoriYaJabaItem>> getRandomPosts(
            @PathVariable String email,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<StoriYaJabaItem> randomPosts = storiYaJabaService.getRandomPosts(email, pageSize);
        return ResponseEntity.ok(randomPosts);
    }
    @GetMapping("/random-videos/{email}")
    public ResponseEntity<Page<StoriYaJabaItem>> getRandomVideoPosts(
            @PathVariable String email,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<StoriYaJabaItem> randomPosts = storiYaJabaService.getRandomVideoPosts(email, pageSize);
        return ResponseEntity.ok(randomPosts);
    }
    @GetMapping("/for-email")
    public ResponseEntity<Page<StoriYaJabaItem>> getStorisForEmail(
            @RequestParam("email") String email,
            @RequestParam("page")int page,
            @RequestParam(defaultValue = "10", name = "pageSize") int pageSize) {
        Page<StoriYaJabaItem> randomPosts = storiYaJabaService.getAllStorisForEmail(page, pageSize,email);
        return ResponseEntity.ok(randomPosts);
    }
    // Add API endpoints for StoriYaJabaItem operations
}
