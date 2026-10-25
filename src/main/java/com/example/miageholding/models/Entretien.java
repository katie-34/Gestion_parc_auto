// Entretien.java (modèle)
package com.example.miageholding.models;

import java.time.LocalDateTime;

public class Entretien {
    private int idEntretien;
    private String immatriculationVehicule;
    private LocalDateTime dateEntree;
    private LocalDateTime dateSortie;
    private String motif;
    private String observation;
    private int cout;
    private String lieu;

    public Entretien(int idEntretien, String immatriculationVehicule, LocalDateTime dateEntree, LocalDateTime dateSortie, String motif, String observation, int cout, String lieu) {
        this.idEntretien = idEntretien;
        this.immatriculationVehicule = immatriculationVehicule;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.motif = motif;
        this.observation = observation;
        this.cout = cout;
        this.lieu = lieu;
    }

    // Getters et Setters
    public int getIdEntretien() { return idEntretien; }
    public String getImmatriculationVehicule() { return immatriculationVehicule; }
    public LocalDateTime getDateEntree() { return dateEntree; }
    public LocalDateTime getDateSortie() { return dateSortie; }
    public String getMotif() { return motif; }
    public String getObservation() { return observation; }
    public int getCout() { return cout; }
    public String getLieu() { return lieu; }
}
