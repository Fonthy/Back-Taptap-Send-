package com.example.taptapsend.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client {

    @Id
    private String numTel;
    private String mail;
    private String nom;
    private String sexe;
    private String pays;
    private BigDecimal solde;
    
    public String getNumTel(){return numTel;}
    public void setNumTel(String numTel){this.numTel=numTel;}

    public String getMail(){return mail;}
    public void setMail(String mail){this.mail=mail;}

    public String getNom(){return nom;}
    public void setNom(String nom){this.nom=nom;}

    public String getSexe(){return sexe;}
    public void setSexe(String sexe){this.sexe=sexe;}

    public String getPays(){return pays;}
    public void setPays(String pays){this.pays=pays;}

    public BigDecimal getSolde(){return solde;}
    public void setSolde(BigDecimal solde){this.solde=solde;}
}
