package com.example.taptapsend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(EnvoyerId.class)
public class Envoyer {

    @Id
    private String idEnv;
    @Id
    private String numEnvoyeur;
    @Id
    private String numRecepteur;
    private BigDecimal montant;
    private LocalDateTime date;
    private String raison;
    private String idFrais;
    
    public String getIdEnv(){return idEnv;}
    public void setIdEnv(String idEnv){this.idEnv=idEnv;}

    public String getNumEnvoyeur(){return numEnvoyeur;}
    public void setNumEnvoyeur(String numEnvoyeur){this.numEnvoyeur=numEnvoyeur;}

    public String getNumRecepteur(){return numRecepteur;}
    public void setNumRecepteur(String numRecepteur){this.numRecepteur=numRecepteur;}

    public BigDecimal getMontant(){return montant;}
    public void setMontant(BigDecimal montant){this.montant=montant;}

    public LocalDateTime getDate(){return date;}
    public void setDate(LocalDateTime date){this.date=date;}

    public String getRaison(){return raison;}
    public void setRaison(String raison){this.raison=raison;}

    public String getIdFrais(){return idFrais;}
    public void setIdFrais(String idFrais){this.idFrais=idFrais;}
}
