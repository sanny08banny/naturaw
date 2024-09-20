package com.example.jabaJuiceServer.entities;

public class UserDTO {
    private Long id;
    private String userName, fullName, email, role,gender,address;
    private int age;
    private String filePath;
    private String dateCreated,fcmToken;
    private String isFollowing = "false";

    public UserDTO(Long id, String userName, String fullName, String email, String role, String gender,
                   String address, int age, String filePath, String dateCreated) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.filePath = filePath;
        this.dateCreated = dateCreated;
    }

    public UserDTO(Long id, String userName, String fullName, String email, String role,
                   String gender, String address, int age,
                   String filePath, String dateCreated, String fcmToken, String isFollowing) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.filePath = filePath;
        this.dateCreated = dateCreated;
        this.fcmToken = fcmToken;
        this.isFollowing = isFollowing;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(String isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
