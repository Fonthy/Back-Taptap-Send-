package com.example.taptapsend.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Taux {

    @Id
    private String idTaux;
    private BigDecimal montant1;
    private BigDecimal montant2;
    
    public String getIdTaux(){return idTaux;}
    public void setIdTaux(String idTaux){this.idTaux=idTaux;}

    public BigDecimal getMontant1(){return montant1;}
    public void setMontant1(BigDecimal montant1){this.montant1=montant1;}

    public BigDecimal getMontant2(){return montant2;}
    public void setMontant2(BigDecimal montant2){this.montant2=montant2;}
    
}
