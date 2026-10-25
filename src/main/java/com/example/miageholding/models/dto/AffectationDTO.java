package com.example.miageholding.models.dto;

public class AffectationDTO {
    private String nomPersonnel;
    private String libelleMission;

    public AffectationDTO(String nomPersonnel, String libelleMission) {
        this.nomPersonnel = nomPersonnel;
        this.libelleMission = libelleMission;
    }

    public String getNomPersonnel() {
        return nomPersonnel;
    }

    public void setNomPersonnel(String nomPersonnel) {
        this.nomPersonnel = nomPersonnel;
    }

    public String getLibelleMission() {
        return libelleMission;
    }

    public void setLibelleMission(String libelleMission) {
        this.libelleMission = libelleMission;
    }
}

