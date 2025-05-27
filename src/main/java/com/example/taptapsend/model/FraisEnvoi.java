package com.example.taptapsend.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FraisEnvoi {

    @Id
    private String idFrais;
    private BigDecimal montant1;
    private BigDecimal montant2;
    private BigDecimal frais;
    
    public String getIdFrais(){return idFrais;}
    public void setIdFrais(String idFrais){this.idFrais=idFrais;}

    public BigDecimal getMontant1(){return montant1;}
    public void setMontant1(BigDecimal montant1){this.montant1=montant1;}

    public BigDecimal getMontant2(){return montant2;}
    public void setMontant2(BigDecimal montant2){this.montant2=montant2;}

    public BigDecimal getFrais(){return frais;}
    public void setFrais(BigDecimal frais){this.frais=frais;}
}
