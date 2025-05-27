package com.example.taptapsend.repository;

import com.example.taptapsend.model.Client;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client,String> {
    Optional<Client> findByNumTel(String numTel);
    List<Client> findByNomContains(String nom);
    boolean existsByNumTel(String numTel);
}
