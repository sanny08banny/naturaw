package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.entities.StoriYaJabaItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoriYaJabaRepository extends JpaRepository<StoriYaJabaItem, Long> {
    List<StoriYaJabaItem> findByLocation(String location);


    StoriYaJabaItem findByFilePath(String fileName);
    StoriYaJabaItem findStoriById(Long id);

    List<StoriYaJabaItem> findByStoriType(String storiType);

    StoriYaJabaItem findByFileName(String fileName);
    Page<StoriYaJabaItem> findAll(Pageable pageable);

    Page<StoriYaJabaItem> findByStoriType(String storiType, Pageable pageable);

    List<StoriYaJabaItem> findByUserId(Long id);
}
