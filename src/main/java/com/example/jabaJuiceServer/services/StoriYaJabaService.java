package com.example.jabaJuiceServer.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.jabaJuiceServer.config.StorageConfig;
import com.example.jabaJuiceServer.entities.*;
import com.example.jabaJuiceServer.repositories.MusicItemsRepository;
import com.example.jabaJuiceServer.repositories.StoriYaJabaRepository;
import com.example.jabaJuiceServer.repositories.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.*;

@Service
public class StoriYaJabaService {
    private static final String S3_BUCKET_NAME = "naturaw1";
    private final StoriYaJabaRepository storiYaJabaRepository;
    private UserRepository userRepository;
    private MusicItemsRepository musicItemsRepository;
    private String imageFilePath;
    private CommentService commentService;
    private RelateService relatedItemService;
    private LikeService likeService;
    private UserService userService;
    private FollowService followService;
    private StorageConfig storageConfig;
    private AmazonS3 s3Client;

    @Autowired
    public StoriYaJabaService(StoriYaJabaRepository storiYaJabaRepository, UserRepository userRepository,
                              MusicItemsRepository musicItemsRepository, CommentService commentService,
                              RelateService relatedItemService, LikeService likeService, UserService userService, FollowService followService, StorageConfig storageConfig) {
        this.storiYaJabaRepository = storiYaJabaRepository;
        this.userRepository = userRepository;
        this.musicItemsRepository = musicItemsRepository;
        this.commentService = commentService;
        this.relatedItemService = relatedItemService;
        this.likeService = likeService;
        this.userService = userService;
        this.followService = followService;
        this.storageConfig = storageConfig;
        this.s3Client = storageConfig.s3Client();
    }

    public void createStoriYaJabaItem(StoriYaJabaItem item, MultipartFile file, String email) {
        try {
            // Define the folder inside the static folder where you want to save the file
            if (item.getStoriType().matches("image")) {
                // Generate a unique file name
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String fileName = UUID.randomUUID().toString() + "." + extension;

                // Create object metadata and set content length
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize()); // Set the content length

                // Upload the file to Amazon S3
                String s3FilePath = "storis/" + fileName;
                s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, s3FilePath, file.getInputStream(), metadata));

                // Get the URL of the uploaded file from S3
                String fileUrl = s3Client.getUrl(S3_BUCKET_NAME, s3FilePath).toString();

                // Update the item's filePath field with the file reference
                item.setFilePath(fileUrl);
                item.setFileName(fileName + "." + extension);
                item.setComments(new HashSet<>());
                User user = userRepository.findByEmail(email);
                item.setUser(user);
                MusicItem musicItem = item.getMusicItem();
                if (musicItem != null) {
                    musicItemsRepository.save(musicItem);
                }
                item.setMusicItem(musicItem);
                item.setLikedItems(new HashSet<>());
                item.setRelatedItems(new HashSet<>());
                storiYaJabaRepository.save(item);
            } else if (item.getStoriType().matches("video")) {

                String fileUrl = saveVideoToFileSystem(file);
                item.setFilePath(fileUrl);
                item.setFileName(imageFilePath);
                item.setComments(new HashSet<>());
                MusicItem musicItem = item.getMusicItem();
                if (musicItem != null) {
                    musicItemsRepository.save(musicItem);
                }
                User user = userRepository.findByEmail(email);
                item.setLikedItems(new HashSet<>());
                item.setRelatedItems(new HashSet<>());
                item.setUser(user);
                item.setMusicItem(musicItem);
                storiYaJabaRepository.save(item);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStoriYaJabaItem(Long id) {
        storiYaJabaRepository.deleteById(id);
    }

    public List<StoriYaJabaItem> getAllStoris() {
        return storiYaJabaRepository.findAll();
    }
    public List<StoriYaJabaItem> getAllStorisSorted(String field) {
        return storiYaJabaRepository.findAll(Sort.by(Sort.Direction.ASC,field));
    }


    public Optional<StoriYaJabaItem> getStoriYaJabaItemById(Long id) {
        return storiYaJabaRepository.findById(id);
    }

    private String saveVideoToFileSystem(MultipartFile file) {
        try {

            // Generate a unique file name
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + extension;

            // Create object metadata and set content length
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize()); // Set the content length

            // Upload the file to Amazon S3
            String s3FilePath = "video_storis/" + fileName;
            s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, s3FilePath, file.getInputStream(), metadata));

            // Get the URL of the uploaded file from S3
            String fileUrl = s3Client.getUrl(S3_BUCKET_NAME, s3FilePath).toString();



            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the image file.", e);
        }
    }

    public List<StoriYaJabaItem> getAllVideoStoris() {
        return storiYaJabaRepository.findByStoriType("video");
    }

    public List<StoriYaJabaItem> getAllImageStoris() {
        return storiYaJabaRepository.findByStoriType("image");
    }

    public StreamingResponseBody getVideoStreamById(Long id) {
        Optional<StoriYaJabaItem> optionalItem = storiYaJabaRepository.findById(id);
        if (optionalItem.isPresent()) {
            StoriYaJabaItem item = optionalItem.get();
            if ("video".equals(item.getStoriType())) {
                String filePath = item.getFilePath();
                return outputStream -> {
                    try (InputStream inputStream = new FileInputStream(filePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read and stream the video file.", e);
                    }
                };
            }
        }
        // Return an empty stream if the item is not found or not a video
        return outputStream -> {
        };
    }

    // Method to relate a StoriYaJabaItem
    public void relateStoriYaJabaItem(Long itemId, Long userId) {
        Optional<StoriYaJabaItem> optionalItem = storiYaJabaRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isPresent() && optionalUser.isPresent()) {
            StoriYaJabaItem item = optionalItem.get();
            User user = optionalUser.get();

            if (relatedItemService.isRelatedToStori(optionalUser.get(),optionalItem.get())) {
                throw new IllegalArgumentException("Already related");
            }

            relatedItemService.relateStori(user, item);
        }
    }
    public void unRelateStoriYaJabaItem(Long itemId, Long userId) {
        Optional<StoriYaJabaItem> optionalItem = storiYaJabaRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isPresent() && optionalUser.isPresent()) {
            StoriYaJabaItem item = optionalItem.get();
            User user = optionalUser.get();

            if (!relatedItemService.isRelatedToStori(optionalUser.get(),optionalItem.get())) {
                throw new IllegalArgumentException("Not related");
            }

            relatedItemService.removeRelateStori(user, item);
        }
    }

    // Method to like a StoriYaJabaItem
    public void likeStoriYaJabaItem(Long itemId, Long userId) {
        Optional<StoriYaJabaItem> optionalItem = storiYaJabaRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isPresent() && optionalUser.isPresent()) {
            StoriYaJabaItem item = optionalItem.get();
            User user = optionalUser.get();
            if (likeService.isLikedStori(user,item)) {
                throw new IllegalArgumentException("Already liked");
            }
            likeService.likeStori(user, item);
        }
    }
    public void unLikeStoriYaJabaItem(Long itemId, Long userId) {
        Optional<StoriYaJabaItem> optionalItem = storiYaJabaRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isPresent() && optionalUser.isPresent()) {
            StoriYaJabaItem item = optionalItem.get();
            User user = optionalUser.get();
            if (!likeService.isLikedStori(user,item)) {
                throw new IllegalArgumentException("Not liked");
            }
            likeService.removeLikeStori(user, item);
        }
    }


    // Add any necessary methods for StoriYaJabaItem-related operations
    public void createComment(Comment comment, String email, Long id) {
        commentService.createStoriComment(comment.getText(), id, email);
    }
    public Page<StoriYaJabaItem> getAllStorisPaginated(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return storiYaJabaRepository.findAll(pageRequest);
    }

    public Page<StoriYaJabaItem> getAllVideoStorisPaginated(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return storiYaJabaRepository.findByStoriType("video", pageRequest);
    }

    public Page<StoriYaJabaItem> getAllImageStorisPaginated(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return storiYaJabaRepository.findByStoriType("image", pageRequest);
    }
    public Page<StoriYaJabaItem> getRandomPosts(String email, int pageSize) {
        User user = userService.getUserByEmail(email);
        List<StoriYaJabaItem> storiYaJabaItems = storiYaJabaRepository.findByStoriType("image");
        for (StoriYaJabaItem storiYaJabaItem : storiYaJabaItems) {
            boolean isRelated = relatedItemService.isRelatedToStori(user,storiYaJabaItem);
            boolean isLiked = likeService.isLikedStori(user, storiYaJabaItem);
            boolean isFollowing = followService.isFollowing(user,storiYaJabaItem.getUser());
            storiYaJabaItem.setIsRelated(String.valueOf(isRelated));
            storiYaJabaItem.setIsLiked(String.valueOf(isLiked));
            storiYaJabaItem.setIsFollowing(String.valueOf(isFollowing));
        }
        // Get the total count of posts for the given subcounty
        long totalCount = storiYaJabaItems.size();

        // Calculate the maximum random page number
        int maxRandomPageNumber = (int) Math.ceil((double) totalCount / pageSize);

        // Generate a random page number within the valid range
        int randomPageNumber = totalCount <= pageSize ? 1 : new Random().nextInt(maxRandomPageNumber) + 1;

        // Create a PageRequest for the random page
        PageRequest pageRequest = PageRequest.of(randomPageNumber - 1, pageSize);

        // Fetch the posts for the given subcounty and random page
        return new PageImpl<>(storiYaJabaItems, pageRequest, totalCount);
    }
    public Page<StoriYaJabaItem> getRandomVideoPosts(String email, int pageSize) {
        User user = userService.getUserByEmail(email);
        List<StoriYaJabaItem> storiYaJabaItems = storiYaJabaRepository.findByStoriType("video");
        for (StoriYaJabaItem storiYaJabaItem : storiYaJabaItems) {
            boolean isRelated = relatedItemService.isRelatedToStori(user,storiYaJabaItem);
            boolean isLiked = likeService.isLikedStori(user, storiYaJabaItem);
            boolean isFollowing = followService.isFollowing(user,storiYaJabaItem.getUser());
            storiYaJabaItem.setIsRelated(String.valueOf(isRelated));
            storiYaJabaItem.setIsLiked(String.valueOf(isLiked));
            storiYaJabaItem.setIsFollowing(String.valueOf(isFollowing));
        }
        // Get the total count of posts for the given subcounty
        long totalCount = storiYaJabaItems.size();

        // Calculate the maximum random page number
        int maxRandomPageNumber = (int) Math.ceil((double) totalCount / pageSize);

        // Generate a random page number within the valid range
        int randomPageNumber = totalCount <= pageSize ? 1 : new Random().nextInt(maxRandomPageNumber) + 1;

        // Create a PageRequest for the random page
        PageRequest pageRequest = PageRequest.of(randomPageNumber - 1, pageSize);

        // Fetch the posts for the given subcounty and random page
        return new PageImpl<>(storiYaJabaItems, pageRequest, totalCount);
    }
    public Page<StoriYaJabaItem> getAllStorisForEmail(int page, int pageSize,String email) {
        User user = userRepository.findByEmail(email);
        List<StoriYaJabaItem> storiYaJabaItems = storiYaJabaRepository.findByUserId(user.getId());
        for (StoriYaJabaItem storiYaJabaItem : storiYaJabaItems) {
            boolean isRelated = relatedItemService.isRelatedToStori(user,storiYaJabaItem);
            boolean isLiked = likeService.isLikedStori(user, storiYaJabaItem);
            boolean isFollowing = followService.isFollowing(user,storiYaJabaItem.getUser());
            storiYaJabaItem.setIsRelated(String.valueOf(isRelated));
            storiYaJabaItem.setIsLiked(String.valueOf(isLiked));
            storiYaJabaItem.setIsFollowing(String.valueOf(isFollowing));
        }
        // Get the total count of posts for the given subcounty
        long totalCount = storiYaJabaItems.size();

        // Calculate the maximum random page number
        int maxRandomPageNumber = (int) Math.ceil((double) totalCount / pageSize);

        // Generate a random page number within the valid range
        int randomPageNumber = totalCount <= pageSize ? 1 : new Random().nextInt(maxRandomPageNumber) + 1;

        // Create a PageRequest for the random page
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        // Fetch the posts for the given subcounty and random page
        return new PageImpl<>(storiYaJabaItems, pageRequest, totalCount);
    }
}

