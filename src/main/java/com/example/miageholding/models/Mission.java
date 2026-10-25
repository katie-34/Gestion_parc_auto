package com.example.miageholding.models;

import java.time.LocalDateTime;

public class Mission {
    private int idMission;
    private String immatriculationVehicule;
    private String libMission;
    private LocalDateTime dateDebutMission;
    private LocalDateTime dateFinMission;
    private int coutMission;
    private int coutCarburant;
    private String observationMission;
    private String circuitMission;

    // Constructeur
    public Mission(int idMission, String immatriculationVehicule, String libMission,
                   LocalDateTime dateDebutMission, LocalDateTime dateFinMission,
                   int coutMission, int coutCarburant, String observationMission, String circuitMission) {
        this.idMission = idMission;
        this.immatriculationVehicule = immatriculationVehicule;
        this.libMission = libMission;
        this.dateDebutMission = dateDebutMission;
        this.dateFinMission = dateFinMission;
        this.coutMission = coutMission;
        this.coutCarburant = coutCarburant;
        this.observationMission = observationMission;
        this.circuitMission = circuitMission;
    }

    // Getters et Setters
    public int getIdMission() { return idMission; }
    public void setIdMission(int idMission) { this.idMission = idMission; }

    public String getImmatriculationVehicule() { return immatriculationVehicule; }
    public void setImmatriculationVehicule(String immatriculationVehicule) { this.immatriculationVehicule = immatriculationVehicule; }

    public String getLibMission() { return libMission; }
    public void setLibMission(String libMission) { this.libMission = libMission; }

    public LocalDateTime getDateDebutMission() { return dateDebutMission; }
    public void setDateDebutMission(LocalDateTime dateDebutMission) { this.dateDebutMission = dateDebutMission; }

    public LocalDateTime getDateFinMission() { return dateFinMission; }
    public void setDateFinMission(LocalDateTime dateFinMission) { this.dateFinMission = dateFinMission; }

    public int getCoutMission() { return coutMission; }
    public void setCoutMission(int coutMission) { this.coutMission = coutMission; }

    public int getCoutCarburant() { return coutCarburant; }
    public void setCoutCarburant(int coutCarburant) { this.coutCarburant = coutCarburant; }

    public String getObservationMission() { return observationMission; }
    public void setObservationMission(String observationMission) { this.observationMission = observationMission; }

    public String getCircuitMission() { return circuitMission; }
    public void setCircuitMission(String circuitMission) { this.circuitMission = circuitMission; }
}
