package com.example.miageholding.core.dao;

import com.example.miageholding.models.Fonction;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FonctionDAO {

    private static final String TABLE_NAME = "fonction";

    public static boolean insertFonction(Fonction fonction) {
        String query = "INSERT INTO " + TABLE_NAME + " (ID_FONCTION, LIB_FONCTION) VALUES (?, ?)";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, fonction.getIdFonction());
            ps.setString(2, fonction.getLibFonction());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL - insertion fonction : " + e.getMessage());
            return false;
        }
    }

    public static boolean updateFonction(Fonction fonction) {
        String query = "UPDATE " + TABLE_NAME + " SET LIB_FONCTION = ? WHERE ID_FONCTION = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, fonction.getLibFonction());
            ps.setInt(2, fonction.getIdFonction());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL - mise à jour fonction : " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteFonction(int idFonction) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE ID_FONCTION = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idFonction);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL - suppression fonction : " + e.getMessage());
            return false;
        }
    }

    public static Fonction getFonctionById(int idFonction) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ID_FONCTION = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idFonction);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Fonction(
                        rs.getInt("ID_FONCTION"),
                        rs.getString("LIB_FONCTION")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL - getById fonction : " + e.getMessage());
        }
        return null;
    }

    public static List<Fonction> getAllFonctions() {
        List<Fonction> fonctions = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                fonctions.add(new Fonction(
                        rs.getInt("ID_FONCTION"),
                        rs.getString("LIB_FONCTION")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL - getAll fonctions : " + e.getMessage());
        }
        return fonctions;
    }

    public static int countFonctions() {
        String query = "SELECT COUNT(*) FROM fonction";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du comptage des fonctions : " + e.getMessage());
        }
        return 0;
    }

    public static int countFonctionsByService() {
        String query = "SELECT COUNT(*) FROM personnel WHERE ID_FONCTION IS NOT NULL AND ID_SERVICE IS NOT NULL";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du comptage des fonctions par service : " + e.getMessage());
        }
        return 0;
    }

    public static List<String> getAllLibelles() {
        List<String> libelles = new ArrayList<>();
        String query = "SELECT LIB_FONCTION FROM fonction";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libelles.add(rs.getString("LIB_FONCTION"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des libellés de fonction : " + e.getMessage());
        }

        return libelles;
    }

    public static int getIdByLibelle(String libelle) {
        String query = "SELECT ID_FONCTION FROM fonction WHERE LIB_FONCTION = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, libelle);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ID_FONCTION");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
        return -1;
    }

    public static String getLibelleById(int idFonction) {
        String query = "SELECT LIB_FONCTION FROM fonction WHERE ID_FONCTION = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idFonction);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("LIB_FONCTION");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
        return "";
    }
}
