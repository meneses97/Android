package com.example.meneses.entities;

import androidx.annotation.NonNull;

public class Rota {
    String origem, destino;
    Integer idrota;

    public Rota(String origem, String destino, Integer idrota) {
        this.origem = origem;
        this.destino = destino;
        this.idrota = idrota;
    }

    public Rota(){
    }

    public Integer getIdrota() {
        return idrota;
    }

    public void setIdrota(Integer idrota) {
        this.idrota = idrota;
    }

    public String getOrigem(){
        return origem;
    }

    public String getDestino(){
        return destino;
    }

    public void setOrigem(String origem){
        this.origem = origem;
    }

    public void setDestino(String destino){
        this.destino = destino;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
