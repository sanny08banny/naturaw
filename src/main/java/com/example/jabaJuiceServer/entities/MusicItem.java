package com.example.jabaJuiceServer.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "music_items")
public class MusicItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String artist;
    @OneToMany(mappedBy = "musicItem", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<StoriYaJabaItem> storiYaJabaItems;

    public MusicItem() {
    }

    public MusicItem(String title, String artist, List<StoriYaJabaItem> storiYaJabaItems) {
        this.title = title;
        this.artist = artist;
        this.storiYaJabaItems = storiYaJabaItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<StoriYaJabaItem> getStoriYaJabaItems() {
        return storiYaJabaItems;
    }

    public void setStoriYaJabaItems(List<StoriYaJabaItem> storiYaJabaItems) {
        this.storiYaJabaItems = storiYaJabaItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
