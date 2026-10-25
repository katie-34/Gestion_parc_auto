package com.example.miageholding.core.dao;

import com.example.miageholding.models.AffectationMission;
import com.example.miageholding.models.dto.AffectationDTO;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AffectationMissionDAO {

        // ✅ Ajouter une attribution
        public static boolean insertAttribution(AffectationMission am) {
            String query = "INSERT INTO participer (ID_PERSONNEL, ID_MISSION) VALUES (?, ?)";
            try (Connection conn = connexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setInt(1, am.getIdPersonnel());
                ps.setInt(2, am.getIdMission());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Erreur SQL - insertAttribution : " + e.getMessage());
                return false;
            }
        }

        // ✅ Supprimer une attribution
        public static boolean deleteAttribution(int idPersonnel, int idMission) {
            String query = "DELETE FROM participer WHERE ID_PERSONNEL = ? AND ID_MISSION = ?";
            try (Connection conn = connexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setInt(1, idPersonnel);
                ps.setInt(2, idMission);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Erreur SQL - deleteAttribution : " + e.getMessage());
                return false;
            }
        }

        // ✅ Vérifier si une attribution existe
        public static boolean exists(int idPersonnel, int idMission) {
            String query = "SELECT 1 FROM participer WHERE ID_PERSONNEL = ? AND ID_MISSION = ?";
            try (Connection conn = connexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setInt(1, idPersonnel);
                ps.setInt(2, idMission);
                ResultSet rs = ps.executeQuery();
                return rs.next();

            } catch (SQLException e) {
                System.err.println("Erreur SQL - exists : " + e.getMessage());
                return false;
            }
        }

        // ✅ Liste des attributions
        public static List<AffectationDTO> getAllAffectations() {
            List<AffectationDTO> liste = new ArrayList<>();
            String query = """
        SELECT p.ID_PERSONNEL, p.NOM_PERSONNEL, p.PRENOM_PERSONNEL, m.ID_MISSION, m.LIB_MISSION
        FROM participer pa
        JOIN personnel p ON pa.ID_PERSONNEL = p.ID_PERSONNEL
        JOIN mission m ON pa.ID_MISSION = m.ID_MISSION
    """;

            try (Connection conn = connexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String nomComplet = rs.getString("NOM_PERSONNEL") + " " + rs.getString("PRENOM_PERSONNEL");
                    String mission = rs.getString("LIB_MISSION");

                    liste.add(new AffectationDTO(nomComplet, mission));
                }

            } catch (SQLException e) {
                System.err.println("Erreur lors du chargement des affectations : " + e.getMessage());
            }

            return liste;
        }


    public static boolean dejaAttribue(int idPersonnel, int idMission) {
        String query = "SELECT 1 FROM participer WHERE ID_PERSONNEL = ? AND ID_MISSION = ?";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idPersonnel);
            ps.setInt(2, idMission);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // true si au moins un résultat (déjà attribué)
        } catch (SQLException e) {
            System.err.println("Erreur SQL dans dejaAttribue : " + e.getMessage());
        }

        return false; // faux par défaut
    }

    public static boolean attribuerMission(int idPersonnel, int idMission) {
        String query = "INSERT INTO participer (ID_PERSONNEL, ID_MISSION) VALUES (?, ?)";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idPersonnel);
            ps.setInt(2, idMission);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l’attribution de mission : " + e.getMessage());
            return false;
        }
    }



}


