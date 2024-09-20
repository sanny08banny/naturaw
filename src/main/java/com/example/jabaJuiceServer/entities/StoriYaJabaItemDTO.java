package com.example.jabaJuiceServer.entities;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.File;
import java.util.List;

public class StoriYaJabaItemDTO {
    private Long id;

    private String description;
    private int relates;
    private int likes;

    private List<Comment> comments;

    private AdminUser adminUser;

    private UserDTO userDTO;
    private String datePosted;
    private String dateTimePosted;
    private String filePath;
    private String fileName;
    private String storiType;
    private String location;
    private MusicItem musicItem;

    public StoriYaJabaItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRelates() {
        return relates;
    }

    public void setRelates(int relates) {
        this.relates = relates;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStoriType() {
        return storiType;
    }

    public void setStoriType(String storiType) {
        this.storiType = storiType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MusicItem getMusicItem() {
        return musicItem;
    }

    public void setMusicItem(MusicItem musicItem) {
        this.musicItem = musicItem;
    }
}
