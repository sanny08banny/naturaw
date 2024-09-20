package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.entities.MusicItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicItemsRepository extends JpaRepository<MusicItem, Long> {
}
