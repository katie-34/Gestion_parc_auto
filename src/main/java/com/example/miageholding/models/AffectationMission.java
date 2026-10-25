package com.example.miageholding.models;

public class AffectationMission {

        private int idPersonnel;
        private int idMission;

        public AffectationMission(int idPersonnel, int idMission) {
            this.idPersonnel = idPersonnel;
            this.idMission = idMission;
        }

        public int getIdPersonnel() {
            return idPersonnel;
        }

        public void setIdPersonnel(int idPersonnel) {
            this.idPersonnel = idPersonnel;
        }

        public int getIdMission() {
            return idMission;
        }

        public void setIdMission(int idMission) {
            this.idMission = idMission;
        }
    }


