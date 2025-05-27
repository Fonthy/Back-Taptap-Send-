package com.example.taptapsend.model;

import java.io.Serializable;
import java.util.Objects;

public class EnvoyerId implements Serializable {
    private String idEnv;
    private String numEnvoyeur;
    private String numRecepteur;

    public EnvoyerId() {}

    public EnvoyerId(String idEnv, String numEnvoyeur, String numRecepteur) {
        this.idEnv = idEnv;
        this.numEnvoyeur = numEnvoyeur;
        this.numRecepteur = numRecepteur;
    }
    public String getIdEnv(){
        return idEnv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnvoyerId)) return false;
        EnvoyerId that = (EnvoyerId) o;
        return Objects.equals(idEnv, that.idEnv) &&
               Objects.equals(numEnvoyeur, that.numEnvoyeur) &&
               Objects.equals(numRecepteur, that.numRecepteur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEnv, numEnvoyeur, numRecepteur);
    }
}
