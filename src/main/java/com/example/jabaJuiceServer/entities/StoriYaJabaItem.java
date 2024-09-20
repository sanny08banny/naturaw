package com.example.jabaJuiceServer.entities;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "stori_ya_jaba_items")
public class StoriYaJabaItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private int relates;
    private int likes;

    @OneToMany(mappedBy = "storiYaJabaItem",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("storiYaJabaItem")
    private Set<Comment> comments;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-storiYaJabaItems")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id")
    @JsonBackReference("adminUser-storiYaJabaItems")
    private AdminUser adminUser;
    @Transient
    private UserDTO userDTO;
    private String datePosted;
    private String dateTimePosted;
    private String filePath;
    private String fileName;
    private String storiType;
    private String location;
    @Transient
    private String isLiked = "false",isRelated = "false", isFollowing = "false";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_item_id")
    @JsonBackReference
    private MusicItem musicItem;

    @OneToMany(mappedBy = "storiYaJabaItem")
    @JsonIgnore
    private Set<LikedItem> likedItems;

    @OneToMany(mappedBy = "storiYaJabaItem")
    @JsonIgnore
    private Set<RelatedItem> relatedItems;

    // Constructors, getters, setters, and other methods

    public StoriYaJabaItem() {
    }

    public UserDTO getUserDTO() {
        if (user != null){
            return new UserDTO(user.getId(), user.getUserName(), user.getFullName(),
                    user.getEmail(), user.getRole(), user.getGender(), user.getAddress(), user.getAge(),
                    user.getFilePath(), user.getDateCreated(), user.getFcmToken(),"");
    }else {
            return userDTO;
        }
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRelates() {
        return relatedItems.size();
    }

    public void setRelates(int relates) {
        this.relates = relates;
    }

    public int getLikes() {
        return likedItems.size();
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getDateTimePosted() {
        return dateTimePosted;
    }

    public void setDateTimePosted(String dateTimePosted) {
        this.dateTimePosted = dateTimePosted;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStoriType() {
        return storiType;
    }

    public void setStoriType(String storiType) {
        this.storiType = storiType;
    }

    public MusicItem getMusicItem() {
        return musicItem;
    }

    public void setMusicItem(MusicItem musicItem) {
        this.musicItem = musicItem;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<LikedItem> getLikedItems() {
        return likedItems;
    }

    public void setLikedItems(Set<LikedItem> likedItems) {
        this.likedItems = likedItems;
    }

    public Set<RelatedItem> getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(Set<RelatedItem> relatedItems) {
        this.relatedItems = relatedItems;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getIsRelated() {
        return isRelated;
    }

    public void setIsRelated(String isRelated) {
        this.isRelated = isRelated;
    }

    public String getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(String isFollowing) {
        this.isFollowing = isFollowing;
    }
}
