package com.example.taptapsend.service;

import com.example.taptapsend.model.FraisEnvoiDTO;

import java.util.List;
import java.util.Optional;

public interface FraisEnvoiService {
    List <FraisEnvoiDTO> getAllFraisEnvois();
    Optional<FraisEnvoiDTO> getFraisEnvoiByIdFrais(String idFrais);
    FraisEnvoiDTO saveFraisEnvoi(FraisEnvoiDTO FraisEnvoiDTO);
    FraisEnvoiDTO updateFraisEnvoi(String idFrais,FraisEnvoiDTO FraisEnvoiDTO);
    void deleteFraisEnvoi(String idFrais);
}
