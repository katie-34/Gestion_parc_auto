package com.example.miageholding.models;

public class ResponsableLogistique {

        private int idUtilisateur;
        private String nomUtilisateur;
        private String motDePasse;
        private String role;

        public ResponsableLogistique(int idUtilisateur, String nomUtilisateur, String motDePasse, String role) {
            this.idUtilisateur = idUtilisateur;
            this.nomUtilisateur = nomUtilisateur;
            this.motDePasse = motDePasse;
            this.role = role;
        }

        public int getIdUtilisateur() {
            return idUtilisateur;
        }

        public void setIdUtilisateur(int idUtilisateur) {
            this.idUtilisateur = idUtilisateur;
        }

        public String getNomUtilisateur() {
            return nomUtilisateur;
        }

        public void setNomUtilisateur(String nomUtilisateur) {
            this.nomUtilisateur = nomUtilisateur;
        }

        public String getMotDePasse() {
            return motDePasse;
        }

        public void setMotDePasse(String motDePasse) {
            this.motDePasse = motDePasse;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

