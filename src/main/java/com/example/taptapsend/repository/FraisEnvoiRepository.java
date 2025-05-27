package com.example.taptapsend.repository;

import com.example.taptapsend.model.FraisEnvoi;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FraisEnvoiRepository extends JpaRepository<FraisEnvoi,String> {
    boolean existsByIdFrais(String idFrais);
    Optional<FraisEnvoi> findByIdFrais(String idFrais);
    @Query(value="select * from frais_envoi where id_frais like concat('%',:search,'%') and :amount>=montant1 and :amount<=montant2",nativeQuery = true)
    FraisEnvoi getFrais(@Param("search") String search,BigDecimal amount);
}
