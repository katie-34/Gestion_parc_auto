// === 1. MODELE : Assurance.java ===
package com.example.miageholding.models;

import java.time.LocalDate;

public class Assurance {
    private int numCarteAssurance;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String agence;
    private int cout;
    private String immatriculation; // ✔ Ajout

    public Assurance() {}

    public Assurance(int numCarteAssurance, LocalDate dateDebut, LocalDate dateFin, String agence, int cout, String immatriculation) {
        this.numCarteAssurance = numCarteAssurance;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.agence = agence;
        this.cout = cout;
        this.immatriculation = immatriculation;
    }

    public int getNumCarteAssurance() { return numCarteAssurance; }
    public void setNumCarteAssurance(int numCarteAssurance) { this.numCarteAssurance = numCarteAssurance; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getAgence() { return agence; }
    public void setAgence(String agence) { this.agence = agence; }

    public int getCout() { return cout; }
    public void setCout(int cout) { this.cout = cout; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
}
