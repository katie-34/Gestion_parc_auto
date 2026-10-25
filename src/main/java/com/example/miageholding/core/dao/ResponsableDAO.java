package com.example.miageholding.core.dao;

import com.example.miageholding.models.ResponsableLogistique;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;

public class ResponsableDAO {

    private static final String ROLE = "responsable_logistique";

    public static ResponsableLogistique getResponsable() {
        String query = "SELECT * FROM utilisateurs WHERE ROLE = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, ROLE);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ResponsableLogistique(
                        rs.getInt("ID_UTILISATEUR"),
                        rs.getString("NOM_UTILISATEUR"),
                        rs.getString("MOT_DE_PASSE"),
                        rs.getString("ROLE")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du responsable logistique : " + e.getMessage());
        }
        return null;
    }

    public static boolean insertResponsable(String nom, String motDePasse) {
        // Vérifier s’il y en a déjà un
        if (getResponsable() != null) return false;

        String query = "INSERT INTO utilisateurs (ID_UTILISATEUR, NOM_UTILISATEUR, MOT_DE_PASSE, ROLE) VALUES (?, ?, ?, ?)";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            int newId = getNextId(conn);
            ps.setInt(1, newId);
            ps.setString(2, nom);
            ps.setString(3, motDePasse);
            ps.setString(4, ROLE);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l’insertion du responsable logistique : " + e.getMessage());
        }
        return false;
    }

    public static boolean updateResponsable(String nom, String motDePasse) {
        String query = "UPDATE utilisateurs SET NOM_UTILISATEUR = ?, MOT_DE_PASSE = ? WHERE ROLE = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nom);
            ps.setString(2, motDePasse);
            ps.setString(3, ROLE);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteResponsable() {
        String query = "DELETE FROM utilisateurs WHERE ROLE = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, ROLE);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
        return false;
    }

    private static int getNextId(Connection conn) throws SQLException {
        String query = "SELECT MAX(ID_UTILISATEUR) FROM utilisateurs";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1) + 1;
        }
        return 1;
    }
}
