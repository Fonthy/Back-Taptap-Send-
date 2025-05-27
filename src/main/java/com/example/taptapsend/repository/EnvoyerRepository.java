package com.example.taptapsend.repository;

import com.example.taptapsend.model.Envoyer;
import com.example.taptapsend.model.EnvoyerId;
import com.example.taptapsend.model.RecetteModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EnvoyerRepository extends JpaRepository<Envoyer,EnvoyerId> {
    boolean existsByIdEnv(String idEnv);
    Optional<Envoyer> findOneByIdEnv(String idEnv);

    @Query(value="select * from envoyer where date::date = :search",nativeQuery = true)
    List<Envoyer> getEnvoyerByDate(@Param("search") LocalDate search);

    @Query(value = "SELECT SUM(f.frais) AS total, e.id_frais AS idFrais FROM envoyer e JOIN frais_envoi f ON e.id_frais = f.id_frais GROUP BY e.id_frais", nativeQuery = true)
    List<RecetteModel> getRecette();

    @Query(value="select * from envoyer where num_envoyeur = :numTel and extract(month from date) = :month and extract(year from date) = :year",nativeQuery = true)
    List<Envoyer> getTransactionByMonth(@Param("numTel") String numTel,@Param("month") int month,@Param("year") int year);
}
