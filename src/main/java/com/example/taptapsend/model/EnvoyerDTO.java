package com.example.taptapsend.model;

import java.math.BigDecimal;

public record EnvoyerDTO(String idEnv,String numEnvoyeur,String numRecepteur,BigDecimal montant,String raison,String idFrais) {}
