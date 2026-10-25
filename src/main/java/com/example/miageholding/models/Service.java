package com.example.miageholding.models;

public class Service {
    private int idService;
    private String libelle;
    private String localisation;

    public Service() {
    }

    public Service(int idService, String libelle, String localisation) {
        this.idService = idService;
        this.libelle = libelle;
        this.localisation = localisation;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    @Override
    public String toString() {
        return libelle + " (" + localisation + ")";
    }
}
