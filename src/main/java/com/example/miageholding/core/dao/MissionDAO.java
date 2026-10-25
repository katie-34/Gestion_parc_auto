package com.example.miageholding.core.dao;

import com.example.miageholding.models.Mission;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissionDAO {

    private static final String TABLE_NAME = "mission";

    public static boolean insertMission(Mission mission) {
        String query = "INSERT INTO " + TABLE_NAME + " (ID_MISSION, IMMATRICULATION_VEHICULE, LIB_MISSION, DATE_DEBUT_MISSION, DATE_FIN_MISSION, COUT_MISSION, COUT_CARBURANT, OBSERVATION_MISSION, CIRCUIT_MISSION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, mission.getIdMission());
            ps.setString(2, mission.getImmatriculationVehicule());
            ps.setString(3, mission.getLibMission());
            ps.setTimestamp(4, mission.getDateDebutMission() != null ? Timestamp.valueOf(mission.getDateDebutMission()) : null);
            ps.setTimestamp(5, mission.getDateFinMission() != null ? Timestamp.valueOf(mission.getDateFinMission()) : null);
            ps.setInt(6, mission.getCoutMission());
            ps.setInt(7, mission.getCoutCarburant());
            ps.setString(8, mission.getObservationMission());
            ps.setString(9, mission.getCircuitMission());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'insertion d'une mission : " + e.getMessage());
            return false;
        }
    }

    public static boolean updateMission(Mission mission) {
        String query = "UPDATE " + TABLE_NAME + " SET IMMATRICULATION_VEHICULE = ?, LIB_MISSION = ?, DATE_DEBUT_MISSION = ?, DATE_FIN_MISSION = ?, COUT_MISSION = ?, COUT_CARBURANT = ?, OBSERVATION_MISSION = ?, CIRCUIT_MISSION = ? WHERE ID_MISSION = ?";
        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, mission.getImmatriculationVehicule());
            ps.setString(2, mission.getLibMission());
            ps.setTimestamp(3, mission.getDateDebutMission() != null ? Timestamp.valueOf(mission.getDateDebutMission()) : null);
            ps.setTimestamp(4, mission.getDateFinMission() != null ? Timestamp.valueOf(mission.getDateFinMission()) : null);
            ps.setInt(5, mission.getCoutMission());
            ps.setInt(6, mission.getCoutCarburant());
            ps.setString(7, mission.getObservationMission());
            ps.setString(8, mission.getCircuitMission());
            ps.setInt(9, mission.getIdMission());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la modification d'une mission : " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteMission(int idMission) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE ID_MISSION = ?";
        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idMission);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression d'une mission : " + e.getMessage());
            return false;
        }
    }

    public static List<Mission> getAllMissions() {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                missions.add(new Mission(
                        rs.getInt("ID_MISSION"),
                        rs.getString("IMMATRICULATION_VEHICULE"),
                        rs.getString("LIB_MISSION"),
                        rs.getTimestamp("DATE_DEBUT_MISSION") != null ? rs.getTimestamp("DATE_DEBUT_MISSION").toLocalDateTime() : null,
                        rs.getTimestamp("DATE_FIN_MISSION") != null ? rs.getTimestamp("DATE_FIN_MISSION").toLocalDateTime() : null,
                        rs.getInt("COUT_MISSION"),
                        rs.getInt("COUT_CARBURANT"),
                        rs.getString("OBSERVATION_MISSION"),
                        rs.getString("CIRCUIT_MISSION")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des missions : " + e.getMessage());
        }
        return missions;
    }

    // 🆕 Ajout de cette méthode :
    public static boolean vehiculeAlreadyAssigned(String immatriculation) {
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE IMMATRICULATION_VEHICULE = ?";
        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, immatriculation);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la vérification d'affectation du véhicule : " + e.getMessage());
            return false;
        }
    }

    public static List<String> getAllLibelles() {
        List<String> libelles = new ArrayList<>();
        String query = "SELECT LIB_MISSION FROM mission";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libelles.add(rs.getString("LIB_MISSION"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des libellés de mission : " + e.getMessage());
        }

        return libelles;
    }

    public static int getIdByLibelle(String libelle) {
        String query = "SELECT ID_MISSION FROM mission WHERE LIB_MISSION = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, libelle);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ID_MISSION");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans getIdByLibelle : " + e.getMessage());
        }

        return -1; // Retourne -1 si non trouvé
    }

    public static List<Mission> getRecentes() {
        List<Mission> missions = new ArrayList<>();
        String query = """
        SELECT m.ID_MISSION, v.IMMATRICULATION, m.LIB_MISSION, 
               m.DATE_DEBUT_MISSION, m.DATE_FIN_MISSION, 
               m.COUT_MISSION, m.COUT_CARBURANT, 
               m.OBSERVATION_MISSION, m.CIRCUIT_MISSION
        FROM mission m
        JOIN vehicules v ON m.ID_VEHICULE = v.ID_VEHICULE
        ORDER BY m.DATE_DEBUT_MISSION DESC
        LIMIT 5
    """;





        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                missions.add(new Mission(
                        rs.getInt("ID_MISSION"),
                        rs.getString("IMMATRICULATION"),
                        rs.getString("LIB_MISSION"),
                        rs.getTimestamp("DATE_DEBUT_MISSION") != null ? rs.getTimestamp("DATE_DEBUT_MISSION").toLocalDateTime() : null,
                        rs.getTimestamp("DATE_FIN_MISSION") != null ? rs.getTimestamp("DATE_FIN_MISSION").toLocalDateTime() : null,
                        rs.getInt("ID_SERVICE"),
                        rs.getInt("ID_PERSONNEL"),
                        null, // service libelle si besoin
                        null  // nom complet si besoin
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans getRecentes : " + e.getMessage());
        }

        return missions;
    }


}
