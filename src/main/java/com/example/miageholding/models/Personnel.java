package com.example.miageholding.models;

import java.time.LocalDateTime;

public class Personnel {

    private int idPersonnel;
    private int idVehicule;
    private int idFonction;
    private int idService;
    private String nom;
    private String prenom;
    private String contact;
    private LocalDateTime dateAttribution;

    public Personnel(int idPersonnel, int idVehicule, int idFonction, int idService, String nom, String prenom, String contact, LocalDateTime dateAttribution) {
        this.idPersonnel = idPersonnel;
        this.idVehicule = idVehicule;
        this.idFonction = idFonction;
        this.idService = idService;
        this.nom = nom;
        this.prenom = prenom;
        this.contact = contact;
        this.dateAttribution = dateAttribution;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public int getIdFonction() {
        return idFonction;
    }

    public void setIdFonction(int idFonction) {
        this.idFonction = idFonction;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDateTime getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(LocalDateTime dateAttribution) {
        this.dateAttribution = dateAttribution;
    }
}

