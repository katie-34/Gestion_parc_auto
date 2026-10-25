package com.example.miageholding.models.dto;

public class PersonnelAffectationDTO {
    private int idPersonnel;
    private String nomComplet;
    private String libelleFonction;
    private String libelleService;

    public PersonnelAffectationDTO(int idPersonnel, String nomComplet, String libelleFonction, String libelleService) {
        this.idPersonnel = idPersonnel;
        this.nomComplet = nomComplet;
        this.libelleFonction = libelleFonction;
        this.libelleService = libelleService;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public String getLibelleFonction() {
        return libelleFonction;
    }

    public String getLibelleService() {
        return libelleService;
    }

    // Optionnel : setters si jamais tu veux permettre la modification
    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public void setLibelleFonction(String libelleFonction) {
        this.libelleFonction = libelleFonction;
    }

    public void setLibelleService(String libelleService) {
        this.libelleService = libelleService;
    }

    // Optionnel : toString() pour affichage/debug
    @Override
    public String toString() {
        return nomComplet + " - " + libelleFonction + " - " + libelleService;
    }
}
