package com.example.miageholding.core.dao;

import com.example.miageholding.models.Entretien;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntretienDAO {

    private static final String TABLE_NAME = "entretien";

    public static boolean insertEntretien(Entretien entretien) {
        String query = "INSERT INTO " + TABLE_NAME + " (ID_ENTRETIEN, ID_VEHICULE, DATE_ENTREE_ENTR, DATE_SORTIE_ENTR, MOTIF_ENTR, OBSERVATION, COUT_ENTR, LIEU_ENTR) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            int idVehicule = findVehiculeIdByImmatriculation(entretien.getImmatriculationVehicule());

            if (idVehicule == -1) {
                System.err.println("Aucun véhicule trouvé pour l'immatriculation : " + entretien.getImmatriculationVehicule());
                return false;
            }

            ps.setInt(1, entretien.getIdEntretien());
            ps.setInt(2, idVehicule);
            ps.setTimestamp(3, entretien.getDateEntree() != null ? Timestamp.valueOf(entretien.getDateEntree()) : null);
            ps.setTimestamp(4, entretien.getDateSortie() != null ? Timestamp.valueOf(entretien.getDateSortie()) : null);
            ps.setString(5, entretien.getMotif());
            ps.setString(6, entretien.getObservation());
            ps.setInt(7, entretien.getCout());
            ps.setString(8, entretien.getLieu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'insertion d'un entretien : " + e.getMessage());
            return false;
        }
    }

    public static boolean updateEntretien(Entretien entretien) {
        String query = "UPDATE " + TABLE_NAME + " SET ID_VEHICULE = ?, DATE_ENTREE_ENTR = ?, DATE_SORTIE_ENTR = ?, MOTIF_ENTR = ?, OBSERVATION = ?, COUT_ENTR = ?, LIEU_ENTR = ? WHERE ID_ENTRETIEN = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            int idVehicule = findVehiculeIdByImmatriculation(entretien.getImmatriculationVehicule());
            if (idVehicule == -1) {
                System.err.println("Aucun véhicule trouvé pour l'immatriculation : " + entretien.getImmatriculationVehicule());
                return false;
            }

            ps.setInt(1, idVehicule);
            ps.setTimestamp(2, entretien.getDateEntree() != null ? Timestamp.valueOf(entretien.getDateEntree()) : null);
            ps.setTimestamp(3, entretien.getDateSortie() != null ? Timestamp.valueOf(entretien.getDateSortie()) : null);
            ps.setString(4, entretien.getMotif());
            ps.setString(5, entretien.getObservation());
            ps.setInt(6, entretien.getCout());
            ps.setString(7, entretien.getLieu());
            ps.setInt(8, entretien.getIdEntretien());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la mise à jour d'un entretien : " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteEntretien(int idEntretien) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE ID_ENTRETIEN = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idEntretien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression d'un entretien : " + e.getMessage());
            return false;
        }
    }

    public static List<Entretien> getAllEntretiens() {
        List<Entretien> entretiens = new ArrayList<>();
        String query = "SELECT e.*, v.IMMATRICULATION FROM " + TABLE_NAME + " e JOIN vehicules v ON e.ID_VEHICULE = v.ID_VEHICULE";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                entretiens.add(new Entretien(
                        rs.getInt("ID_ENTRETIEN"),
                        rs.getString("IMMATRICULATION"),
                        rs.getTimestamp("DATE_ENTREE_ENTR") != null ? rs.getTimestamp("DATE_ENTREE_ENTR").toLocalDateTime() : null,
                        rs.getTimestamp("DATE_SORTIE_ENTR") != null ? rs.getTimestamp("DATE_SORTIE_ENTR").toLocalDateTime() : null,
                        rs.getString("MOTIF_ENTR"),
                        rs.getString("OBSERVATION"),
                        rs.getInt("COUT_ENTR"),
                        rs.getString("LIEU_ENTR")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des entretiens : " + e.getMessage());
        }

        return entretiens;
    }

    private static int findVehiculeIdByImmatriculation(String immatriculation) {
        String query = "SELECT ID_VEHICULE FROM vehicules WHERE IMMATRICULATION = ?";
        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, immatriculation);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ID_VEHICULE");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche de véhicule par immatriculation : " + e.getMessage());
        }
        return -1;
    }
}
