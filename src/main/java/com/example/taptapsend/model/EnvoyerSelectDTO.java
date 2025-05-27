package com.example.taptapsend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EnvoyerSelectDTO(String idEnv,String numEnvoyeur,String numRecepteur,LocalDateTime date,BigDecimal montant,String raison) {}
