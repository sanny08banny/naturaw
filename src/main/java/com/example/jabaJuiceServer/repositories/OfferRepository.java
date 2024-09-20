package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.adminDTOs.Offer;
import com.example.jabaJuiceServer.entities.StoriYaJabaItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    Page<Offer> findAll(Pageable pageable);
}
