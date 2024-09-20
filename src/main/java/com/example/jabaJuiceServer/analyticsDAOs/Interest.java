package com.example.jabaJuiceServer.analyticsDAOs;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.entities.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "interests")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "interest_subtopics", joinColumns = @JoinColumn(name = "interest_id"))
    @Column(name = "subtopic")
    private Set<String> subtopics = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-interests")
    private User user;
    @Transient
    private UserDTO userDTO;

    // Constructors, getters, setters

    public Interest() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(Set<String> subtopics) {
        this.subtopics = subtopics;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

