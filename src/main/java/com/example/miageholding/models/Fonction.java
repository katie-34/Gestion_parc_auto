package com.example.miageholding.models;


public class Fonction {
    private int idFonction;
    private String libFonction;

    public Fonction(int idFonction, String libFonction) {
        this.idFonction = idFonction;
        this.libFonction = libFonction;
    }

    public int getIdFonction() {
        return idFonction;
    }

    public void setIdFonction(int idFonction) {
        this.idFonction = idFonction;
    }

    public String getLibFonction() {
        return libFonction;
    }

    public void setLibFonction(String libFonction) {
        this.libFonction = libFonction;
    }
}


