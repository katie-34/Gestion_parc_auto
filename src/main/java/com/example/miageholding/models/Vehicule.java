package com.example.miageholding.models;

import java.time.LocalDate;

/**
 * Classe modèle représentant un véhicule
 */
public class Vehicule {

    private int idVehicule;
    private int idEtatVoiture;
    private String numeroChassi;
    private String immatriculation;
    private String marque;
    private String modele;
    private int nbPlaces;
    private String energie;
    private LocalDate dateAcquisition;
    private LocalDate dateAmortissement;
    private LocalDate dateMiseEnService;
    private int puissance;
    private String couleur;
    private int prixVehicule;
    private LocalDate dateEtat;

    // ================== Constructeurs ==================

    /**
     * Constructeur vide
     */
    public Vehicule() {
    }

    /**
     * Constructeur avec ID pour insertion (nouvelle version)
     */
    public Vehicule(int idVehicule, int idEtatVoiture, String numeroChassi, String immatriculation,
                    String marque, String modele, int nbPlaces, String energie,
                    LocalDate dateAcquisition, LocalDate dateAmortissement, LocalDate dateMiseEnService,
                    int puissance, String couleur, int prixVehicule, LocalDate dateEtat) {
        this.idVehicule = idVehicule;
        this.idEtatVoiture = idEtatVoiture;
        this.numeroChassi = numeroChassi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.nbPlaces = nbPlaces;
        this.energie = energie;
        this.dateAcquisition = dateAcquisition;
        this.dateAmortissement = dateAmortissement;
        this.dateMiseEnService = dateMiseEnService;
        this.puissance = puissance;
        this.couleur = couleur;
        this.prixVehicule = prixVehicule;
        this.dateEtat = dateEtat;
    }

    /**
     * Constructeur sans ID (utile si un jour on remet l'auto-incrément)
     */
    public Vehicule(int idEtatVoiture, String numeroChassi, String immatriculation,
                    String marque, String modele, int nbPlaces, String energie,
                    LocalDate dateAcquisition, LocalDate dateAmortissement, LocalDate dateMiseEnService,
                    int puissance, String couleur, int prixVehicule, LocalDate dateEtat) {
        this.idEtatVoiture = idEtatVoiture;
        this.numeroChassi = numeroChassi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.nbPlaces = nbPlaces;
        this.energie = energie;
        this.dateAcquisition = dateAcquisition;
        this.dateAmortissement = dateAmortissement;
        this.dateMiseEnService = dateMiseEnService;
        this.puissance = puissance;
        this.couleur = couleur;
        this.prixVehicule = prixVehicule;
        this.dateEtat = dateEtat;
    }

    // ================== Getters et Setters ==================

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public int getIdEtatVoiture() {
        return idEtatVoiture;
    }

    public void setIdEtatVoiture(int idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    public String getNumeroChassi() {
        return numeroChassi;
    }

    public void setNumeroChassi(String numeroChassi) {
        this.numeroChassi = numeroChassi;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public String getEnergie() {
        return energie;
    }

    public void setEnergie(String energie) {
        this.energie = energie;
    }

    public LocalDate getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public LocalDate getDateAmortissement() {
        return dateAmortissement;
    }

    public void setDateAmortissement(LocalDate dateAmortissement) {
        this.dateAmortissement = dateAmortissement;
    }

    public LocalDate getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDate dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public int getPuissance() {
        return puissance;
    }

    public void setPuissance(int puissance) {
        this.puissance = puissance;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getPrixVehicule() {
        return prixVehicule;
    }

    public void setPrixVehicule(int prixVehicule) {
        this.prixVehicule = prixVehicule;
    }

    public LocalDate getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(LocalDate dateEtat) {
        this.dateEtat = dateEtat;
    }
}
