package com.example.taptapsend.repository;


import com.example.taptapsend.model.Taux;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TauxRepository extends JpaRepository<Taux,String> {
    boolean existsByIdTaux(String idTaux);
    Optional<Taux> findByIdTaux(String idTaux);
}
