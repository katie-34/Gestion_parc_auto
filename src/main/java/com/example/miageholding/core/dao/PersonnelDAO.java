package com.example.miageholding.core.dao;

import com.example.miageholding.models.Personnel;
import com.example.miageholding.models.dto.PersonnelAffectationDTO;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDAO {

    public static boolean insertPersonnel(Personnel p) {
        String query = "INSERT INTO personnel (ID_PERSONNEL, ID_VEHICULE, ID_FONCTION, ID_SERVICE, NOM_PERSONNEL, PRENOM_PERSONNEL, CONTACT_PERSONNEL, DATE_ATTRIBUTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, p.getIdPersonnel());
            ps.setInt(2, p.getIdVehicule());
            ps.setInt(3, p.getIdFonction());
            ps.setInt(4, p.getIdService());
            ps.setString(5, p.getNom());
            ps.setString(6, p.getPrenom());
            ps.setString(7, p.getContact());
            ps.setTimestamp(8, Timestamp.valueOf(p.getDateAttribution()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL insert personnel: " + e.getMessage());
            return false;
        }
    }

    public static boolean updatePersonnel(Personnel p) {
        String query = "UPDATE personnel SET ID_VEHICULE = ?, ID_FONCTION = ?, ID_SERVICE = ?, NOM_PERSONNEL = ?, PRENOM_PERSONNEL = ?, CONTACT_PERSONNEL = ?, DATE_ATTRIBUTION = ? WHERE ID_PERSONNEL = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, p.getIdVehicule());
            ps.setInt(2, p.getIdFonction());
            ps.setInt(3, p.getIdService());
            ps.setString(4, p.getNom());
            ps.setString(5, p.getPrenom());
            ps.setString(6, p.getContact());
            ps.setTimestamp(7, Timestamp.valueOf(p.getDateAttribution()));
            ps.setInt(8, p.getIdPersonnel());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL update personnel: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePersonnel(int id) {
        String query = "DELETE FROM personnel WHERE ID_PERSONNEL = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL delete personnel: " + e.getMessage());
            return false;
        }
    }

    public static List<Personnel> getAllPersonnel() {
        List<Personnel> list = new ArrayList<>();
        String query = "SELECT * FROM personnel";
        try (Connection conn = connexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Personnel p = new Personnel(
                        rs.getInt("ID_PERSONNEL"),
                        rs.getInt("ID_VEHICULE"),
                        rs.getInt("ID_FONCTION"),
                        rs.getInt("ID_SERVICE"),
                        rs.getString("NOM_PERSONNEL"),
                        rs.getString("PRENOM_PERSONNEL"),
                        rs.getString("CONTACT_PERSONNEL"),
                        rs.getTimestamp("DATE_ATTRIBUTION") != null ? rs.getTimestamp("DATE_ATTRIBUTION").toLocalDateTime() : null
                );
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL getAll personnel: " + e.getMessage());
        }
        return list;
    }

    public static Personnel getById(int id) {
        String query = "SELECT * FROM personnel WHERE ID_PERSONNEL = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Personnel(
                        rs.getInt("ID_PERSONNEL"),
                        rs.getInt("ID_VEHICULE"),
                        rs.getInt("ID_FONCTION"),
                        rs.getInt("ID_SERVICE"),
                        rs.getString("NOM_PERSONNEL"),
                        rs.getString("PRENOM_PERSONNEL"),
                        rs.getString("CONTACT_PERSONNEL"),
                        rs.getTimestamp("DATE_ATTRIBUTION") != null ? rs.getTimestamp("DATE_ATTRIBUTION").toLocalDateTime() : null
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL getById personnel: " + e.getMessage());
        }
        return null;
    }

    public static int countPersonnels() {
        String query = "SELECT COUNT(*) FROM personnel";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du comptage des personnels : " + e.getMessage());
        }
        return 0;
    }

    public static List<PersonnelAffectationDTO> getPersonnelAvecFonctionEtService() {
        List<PersonnelAffectationDTO> resultat = new ArrayList<>();
        String query = """
                SELECT p.ID_PERSONNEL, p.NOM_PERSONNEL, p.PRENOM_PERSONNEL, f.LIB_FONCTION, s.LIB_SERVICE
                FROM personnel p
                JOIN fonction f ON p.ID_FONCTION = f.ID_FONCTION
                JOIN service s ON p.ID_SERVICE = s.ID_SERVICE
                ORDER BY p.DATE_ATTRIBUTION DESC
                """;

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultat.add(new PersonnelAffectationDTO(
                        rs.getInt("ID_PERSONNEL"),
                        rs.getString("NOM_PERSONNEL") + " " + rs.getString("PRENOM_PERSONNEL"),
                        rs.getString("LIB_FONCTION"),
                        rs.getString("LIB_SERVICE")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL chargement personnels avec fonction/service : " + e.getMessage());
        }

        return resultat;
    }

    public static boolean isVehiculeAttribue(int idVehicule, Integer excludePersonnelId) {
        String query = "SELECT COUNT(*) FROM personnel WHERE ID_VEHICULE = ?" +
                (excludePersonnelId != null ? " AND ID_PERSONNEL <> ?" : "");

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idVehicule);
            if (excludePersonnelId != null) {
                ps.setInt(2, excludePersonnelId);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du véhicule attribué : " + e.getMessage());
        }

        return false;
    }

    public static List<String> getAllNomsComplets() {
        List<String> nomsComplets = new ArrayList<>();
        String query = "SELECT CONCAT(NOM_PERSONNEL, ' ', PRENOM_PERSONNEL) AS NOM_COMPLET FROM personnel";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                nomsComplets.add(rs.getString("NOM_COMPLET"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des noms complets : " + e.getMessage());
        }

        return nomsComplets;
    }
    public static int getIdByNomComplet(String nomComplet) {
        String query = "SELECT ID_PERSONNEL, NOM_PERSONNEL, PRENOM_PERSONNEL FROM personnel";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nom = rs.getString("NOM_PERSONNEL");
                String prenom = rs.getString("PRENOM_PERSONNEL");
                String complet = nom + " " + prenom;

                if (complet.equalsIgnoreCase(nomComplet.trim())) {
                    return rs.getInt("ID_PERSONNEL");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans getIdByNomComplet : " + e.getMessage());
        }

        return -1; // -1 si aucun résultat
    }


}
