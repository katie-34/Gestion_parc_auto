package com.example.miageholding.core.dao;

import com.example.miageholding.models.Assurance;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssuranceDAO {

    private static final String TABLE_NAME = "assurance"; // Table utilisée

    // ➡ Ajouter une assurance (avec retour succès)
    public static boolean insertAssurance(Assurance assurance) {
        String query = "INSERT INTO " + TABLE_NAME +
                " (NUM_CARTE_ASSURANCE, DATE_DEBUT_ASSURANCE, DATE_FIN_ASSURANCE, AGENCE, COUT_ASSURANCE, IMMATRICULATION_VEHICULE) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, assurance.getNumCarteAssurance());
            preparedStatement.setDate(2, assurance.getDateDebut() != null ? Date.valueOf(assurance.getDateDebut()) : null);
            preparedStatement.setDate(3, assurance.getDateFin() != null ? Date.valueOf(assurance.getDateFin()) : null);
            preparedStatement.setString(4, assurance.getAgence());
            preparedStatement.setInt(5, assurance.getCout());
            preparedStatement.setString(6, assurance.getImmatriculation());

            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println("Résultat insertion assurance : " + rowsInserted + " ligne(s) insérée(s).");

            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'insertion d'une assurance : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ➡ Modifier une assurance
    public static void updateAssurance(Assurance assurance) {
        String query = "UPDATE " + TABLE_NAME +
                " SET DATE_DEBUT_ASSURANCE = ?, DATE_FIN_ASSURANCE = ?, AGENCE = ?, COUT_ASSURANCE = ?, IMMATRICULATION_VEHICULE = ? " +
                "WHERE NUM_CARTE_ASSURANCE = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setDate(1, assurance.getDateDebut() != null ? Date.valueOf(assurance.getDateDebut()) : null);
            ps.setDate(2, assurance.getDateFin() != null ? Date.valueOf(assurance.getDateFin()) : null);
            ps.setString(3, assurance.getAgence());
            ps.setInt(4, assurance.getCout());
            ps.setString(5, assurance.getImmatriculation());
            ps.setInt(6, assurance.getNumCarteAssurance());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la modification d'une assurance : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ➡ Supprimer une assurance
    public static void deleteAssurance(int numCarteAssurance) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE NUM_CARTE_ASSURANCE = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, numCarteAssurance);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression d'une assurance : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ➡ Récupérer toutes les assurances
    public static List<Assurance> getAllAssurances() {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getInt("NUM_CARTE_ASSURANCE"),
                        rs.getDate("DATE_DEBUT_ASSURANCE") != null ? rs.getDate("DATE_DEBUT_ASSURANCE").toLocalDate() : null,
                        rs.getDate("DATE_FIN_ASSURANCE") != null ? rs.getDate("DATE_FIN_ASSURANCE").toLocalDate() : null,
                        rs.getString("AGENCE"),
                        rs.getInt("COUT_ASSURANCE"),
                        rs.getString("IMMATRICULATION_VEHICULE")
                );
                assurances.add(assurance);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des assurances : " + e.getMessage());
            e.printStackTrace();
        }

        return assurances;
    }

    // ➡ Vérifier si une assurance existe (par numéro de carte)
    public static boolean assuranceExists(int numCarteAssurance) {
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE NUM_CARTE_ASSURANCE = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, numCarteAssurance);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la vérification d'une assurance : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ➡ Vérifier si un véhicule est déjà assuré
    public static boolean vehiculeAlreadyInsured(String immatriculation) {
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE IMMATRICULATION_VEHICULE = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, immatriculation);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la vérification de l'assurance d'un véhicule : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Compte le nombre d’assurances encore valides (c’est-à-dire dont la date de fin est postérieure ou égale à aujourd’hui).
     *
     * @return le nombre d’assurances valides
     */
    public static int countValides() {
        String query = "SELECT COUNT(*) FROM assurance WHERE DATE_FIN_ASSURANCE >= CURDATE()";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans countValides : " + e.getMessage());
        }

        return 0;
    }

    public static List<Assurance> getAssurancesExpirantSous7Jours() {
        List<Assurance> liste = new ArrayList<>();
        String query = """
            SELECT NUM_CARTE_ASSURANCE, DATE_DEBUT_ASSURANCE, DATE_FIN_ASSURANCE, AGENCE, COUT_ASSURANCE
            FROM assurance
            WHERE DATE_FIN_ASSURANCE BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
            ORDER BY DATE_FIN_ASSURANCE ASC
        """;

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getInt("NUM_CARTE_ASSURANCE"),
                        rs.getDate("DATE_DEBUT_ASSURANCE") != null ? rs.getDate("DATE_DEBUT_ASSURANCE").toLocalDate() : null,
                        rs.getDate("DATE_FIN_ASSURANCE") != null ? rs.getDate("DATE_FIN_ASSURANCE").toLocalDate() : null,
                        rs.getString("AGENCE"),
                        rs.getInt("COUT_ASSURANCE"),
                        "" // Tu peux adapter ici pour récupérer l’immatriculation via jointure si besoin
                );
                liste.add(assurance);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans getAssurancesExpirantSous7Jours : " + e.getMessage());
        }

        return liste;
    }

}
