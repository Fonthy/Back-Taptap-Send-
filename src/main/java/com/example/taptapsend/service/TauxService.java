package com.example.taptapsend.service;

import com.example.taptapsend.model.TauxDTO;

import java.util.List;
import java.util.Optional;

public interface TauxService {
    List <TauxDTO> getAllTaux();
    Optional<TauxDTO> getTauxByIdTaux(String idTaux);
    TauxDTO saveTaux(TauxDTO tauxDTO);
    TauxDTO updateTaux(String idTaux,TauxDTO tauxDTO);
    void deleteTaux(String idTaux);
}
