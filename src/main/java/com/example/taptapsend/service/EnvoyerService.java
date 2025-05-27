package com.example.taptapsend.service;

import com.example.taptapsend.model.EnvoyerDTO;
import com.example.taptapsend.model.EnvoyerSelectDTO;
import com.example.taptapsend.model.RecetteModel;
import com.example.taptapsend.model.Envoyer;

import java.math.BigDecimal;
import java.util.List;


public interface EnvoyerService {
    List <EnvoyerSelectDTO> getAllEnvoyer();
    List<EnvoyerSelectDTO> getEnvoyerByDate(String date);
    BigDecimal getRecette();
    EnvoyerDTO saveEnvoyer(EnvoyerDTO EnvoyerDTO);
    EnvoyerSelectDTO updateEnvoyer(String idEnv,EnvoyerSelectDTO EnvoyerDTO);
    void deleteEnvoyer(String idEnv);
}
