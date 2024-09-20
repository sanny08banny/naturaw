package com.example.jabaJuiceServer.services;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.jabaJuiceServer.ProductNotFoundException;
import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.config.StorageConfig;
import com.example.jabaJuiceServer.entities.Comment;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.AdminUserRepository;
import com.example.jabaJuiceServer.repositories.ProductRepository;
import com.example.jabaJuiceServer.repositories.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ProductService {

    private static final String S3_BUCKET_NAME = "naturaw1";
    private final String FOLDER_PATH = "C:\\\\Users\\\\User\\\\Documents\\\\jxtsam\\\\jabaJuiceServer\\\\src\\\\main\\\\resources\\\\static";
    private final ProductRepository productRepository;
    private String imageFilePath;
    private final UserRepository userRepository;
    private UserService userService;
    private AdminUserRepository adminUserRepository;
    private LikeService likeService;
    private RelateService relateService;
    private CommentService commentService;
    private StorageConfig storageConfig;
    private AmazonS3 s3Client;
    private FollowService followService;

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository, UserService userService, AdminUserRepository adminUserRepository, LikeService likeService, RelateService relateService, CommentService commentService, StorageConfig storageConfig, FollowService followService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.adminUserRepository = adminUserRepository;
        this.likeService = likeService;
        this.relateService = relateService;
        this.commentService = commentService;
        this.storageConfig = storageConfig;
        this.s3Client = storageConfig.s3Client();
        this.followService = followService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Page<Product> getAllProdustssForEmail(int page, int pageSize, String email) {
        User user = userRepository.findByEmail(email);
        List<Product> products = productRepository.findByUserId(user.getId());
        for (Product product : products) {
            boolean isRelated = relateService.isRelatedToProduct(user, product);
            boolean isLiked = likeService.isLikedProduct(user, product);
            boolean isFollowing = followService.isFollowing(user, product.getUser());
            product.setIsRelated(String.valueOf(isRelated));
            product.setIsLiked(String.valueOf(isLiked));
            product.setIsFollowing(String.valueOf(isFollowing));
        }
        // Get the total count of posts for the given subcounty
        long totalCount = products.size();

        // Calculate the maximum random page number
        int maxRandomPageNumber = (int) Math.ceil((double) totalCount / pageSize);

        // Generate a random page number within the valid range
        int randomPageNumber = totalCount <= pageSize ? 1 : new Random().nextInt(maxRandomPageNumber) + 1;

        // Create a PageRequest for the random page
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        // Fetch the posts for the given subcounty and random page
        return new PageImpl<>(products, pageRequest, totalCount);
    }

    public Page<Product> getRandomProducts(String email, int pageSize) {
        User user = userService.getUserByEmail(email);
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            boolean isRelated = relateService.isRelatedToProduct(user, product);
            boolean isLiked = likeService.isLikedProduct(user, product);
            product.setIsRelated(String.valueOf(isRelated));
            product.setIsLiked(String.valueOf(isLiked));
        }
        // Get the total count of posts for the given subcounty
        long totalCount = products.size();

        // Calculate the maximum random page number
        int maxRandomPageNumber = (int) Math.ceil((double) totalCount / pageSize);

        // Generate a random page number within the valid range
        int randomPageNumber = totalCount <= pageSize ? 1 : new Random().nextInt(maxRandomPageNumber) + 1;

        // Create a PageRequest for the random page
        PageRequest pageRequest = PageRequest.of(randomPageNumber - 1, pageSize);

        // Fetch the posts for the given subcounty and random page
        return new PageImpl<>(products, pageRequest, totalCount);
    }
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product getProductByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public void saveProduct(Product product, MultipartFile image,String email) {
       AdminUser adminUser = adminUserRepository.findByEmail(email);
        if (adminUser != null){
            // Save the image file to the file system
            String fileUrl = saveImageToFileSystem(image);
            // Create a new Product object
//            product.setAdminUser(user);
            product.setComments(new ArrayList<>());
            product.setFileName(fileUrl);
            product.setFilePath(imageFilePath);
            product.setUI(generateReferenceNumber());
            product.setAdminUser(adminUser);

            // Save the product in the repository
            productRepository.save(product);
        }
    }

    private String saveImageToFileSystem(MultipartFile file) {
        try {
            // Generate a unique file name
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + extension;

            // Create object metadata and set content length
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize()); // Set the content length

            // Upload the file to Amazon S3
            String s3FilePath = "product_images/" + fileName;
            s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, s3FilePath, file.getInputStream(), metadata));

            // Get the URL of the uploaded file from S3
            return s3Client.getUrl(S3_BUCKET_NAME, s3FilePath).toString();
        } catch (AmazonS3Exception | IOException e) {
            try {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String fileName = UUID.randomUUID().toString() + "." + extension;

                String localFilePath = FOLDER_PATH + "/Product_images/" + fileName;
                File localFile = new File(localFilePath);

                try (InputStream inputStream = file.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(localFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                return fileName;
            } catch (IOException ex) {
                throw new RuntimeException("Failed to save file. Please try again.");
            }
        }
    }
//    private String generateUniqueFileName() {
//        // Generate a unique file name using a combination of timestamp and UUID
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String uuid = UUID.randomUUID().toString();
//        String fileName = timeStamp + "_" + uuid + ".jpg";
//
//        return fileName;
//    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException("Product not found");
        }
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        if (productRepository.existsById(id)) {
            updatedProduct.setId(id);
            return productRepository.save(updatedProduct);
        } else {
            throw new ProductNotFoundException("Product not found");
        }
    }
    public String generateReferenceNumber() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 12);
    }
    public void relateProduct(Long itemId, Long userId) {
        Optional<Product> optionalItem = productRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isPresent() && optionalUser.isPresent()) {
            Product item = optionalItem.get();
            User user = optionalUser.get();

            if (relateService.isRelatedToProduct(optionalUser.get(),optionalItem.get())) {
                throw new IllegalArgumentException("Already related");
            }

            relateService.relateProducts(user, item);
        }
    }
    public void unRelateProduct(Long itemId, Long userId) {
        Optional<Product> optionalItem = productRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isPresent() && optionalUser.isPresent()) {
            Product item = optionalItem.get();
            User user = optionalUser.get();

            if (!relateService.isRelatedToProduct(optionalUser.get(),optionalItem.get())) {
                throw new IllegalArgumentException("Not related");
            }

            relateService.removeRelateProduct(user, item);
        }
    }

    // Method to like a StoriYaJabaItem
    public void likeProduct(Long itemId, Long userId) {
        Optional<Product> optionalItem = productRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isPresent() && optionalUser.isPresent()) {
            Product item = optionalItem.get();
            User user = optionalUser.get();
            if (likeService.isLikedProduct(user,item)) {
                throw new IllegalArgumentException("Already liked");
            }
            likeService.likeProduct(user, item);
        }
    }
    public void unLikeProduct(Long itemId, Long userId) {
        Optional<Product> optionalItem = productRepository.findById(itemId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalItem.isPresent() && optionalUser.isPresent()) {
            Product item = optionalItem.get();
            User user = optionalUser.get();
            if (!likeService.isLikedProduct(user,item)) {
                throw new IllegalArgumentException("Not liked");
            }
            likeService.removeLikeProduct(user, item);
        }
    }


    // Add any necessary methods for StoriYaJabaItem-related operations
    public void createComment(Comment comment, String email, Long id) {
        commentService.createProductComment(comment.getText(), id, email);
    }
}
