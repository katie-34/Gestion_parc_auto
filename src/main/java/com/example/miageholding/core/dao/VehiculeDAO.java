package com.example.miageholding.core.dao;

import com.example.miageholding.models.Vehicule;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAO {

    // Méthode pour insérer un véhicule
    public static void insertVehicule(Vehicule vehicule) {
        String query = "INSERT INTO vehicules (ID_VEHICULE, ID_ETAT_VOITURE, NUMERO_CHASSI, IMMATRICULATION, MARQUE, MODELE, NB_PLACES, ENERGIE, " +
                "DATE_ACQUISITION, DATE_AMMORTISSEMENT, DATE_MISE_EN_SERVICE, PUISSANCE, COULEUR, PRIX_VEHICULE, DATE_ETAT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, vehicule.getIdVehicule()); // 🛑 Insérer aussi l'ID !
            preparedStatement.setInt(2, vehicule.getIdEtatVoiture());
            preparedStatement.setString(3, vehicule.getNumeroChassi());
            preparedStatement.setString(4, vehicule.getImmatriculation());
            preparedStatement.setString(5, vehicule.getMarque());
            preparedStatement.setString(6, vehicule.getModele());
            preparedStatement.setInt(7, vehicule.getNbPlaces());
            preparedStatement.setString(8, vehicule.getEnergie());
            preparedStatement.setTimestamp(9, vehicule.getDateAcquisition() != null ? Timestamp.valueOf(vehicule.getDateAcquisition().atStartOfDay()) : null);
            preparedStatement.setTimestamp(10, vehicule.getDateAmortissement() != null ? Timestamp.valueOf(vehicule.getDateAmortissement().atStartOfDay()) : null);
            preparedStatement.setTimestamp(11, vehicule.getDateMiseEnService() != null ? Timestamp.valueOf(vehicule.getDateMiseEnService().atStartOfDay()) : null);
            preparedStatement.setInt(12, vehicule.getPuissance());
            preparedStatement.setString(13, vehicule.getCouleur());
            preparedStatement.setInt(14, vehicule.getPrixVehicule());
            preparedStatement.setTimestamp(15, vehicule.getDateEtat() != null ? Timestamp.valueOf(vehicule.getDateEtat().atStartOfDay()) : null);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'insertion d'un véhicule : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour un véhicule
    public static void updateVehicule(Vehicule vehicule) {
        String query = "UPDATE vehicules SET " +
                "ID_ETAT_VOITURE = ?, NUMERO_CHASSI = ?, IMMATRICULATION = ?, MARQUE = ?, MODELE = ?, NB_PLACES = ?, " +
                "ENERGIE = ?, DATE_ACQUISITION = ?, DATE_AMMORTISSEMENT = ?, DATE_MISE_EN_SERVICE = ?, PUISSANCE = ?, " +
                "COULEUR = ?, PRIX_VEHICULE = ?, DATE_ETAT = ? WHERE ID_VEHICULE = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, vehicule.getIdEtatVoiture());
            preparedStatement.setString(2, vehicule.getNumeroChassi());
            preparedStatement.setString(3, vehicule.getImmatriculation());
            preparedStatement.setString(4, vehicule.getMarque());
            preparedStatement.setString(5, vehicule.getModele());
            preparedStatement.setInt(6, vehicule.getNbPlaces());
            preparedStatement.setString(7, vehicule.getEnergie());
            preparedStatement.setTimestamp(8, vehicule.getDateAcquisition() != null ? Timestamp.valueOf(vehicule.getDateAcquisition().atStartOfDay()) : null);
            preparedStatement.setTimestamp(9, vehicule.getDateAmortissement() != null ? Timestamp.valueOf(vehicule.getDateAmortissement().atStartOfDay()) : null);
            preparedStatement.setTimestamp(10, vehicule.getDateMiseEnService() != null ? Timestamp.valueOf(vehicule.getDateMiseEnService().atStartOfDay()) : null);
            preparedStatement.setInt(11, vehicule.getPuissance());
            preparedStatement.setString(12, vehicule.getCouleur());
            preparedStatement.setInt(13, vehicule.getPrixVehicule());
            preparedStatement.setTimestamp(14, vehicule.getDateEtat() != null ? Timestamp.valueOf(vehicule.getDateEtat().atStartOfDay()) : null);
            preparedStatement.setInt(15, vehicule.getIdVehicule());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la mise à jour d'un véhicule : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un véhicule
    public static void deleteVehicule(int idVehicule) {
        String query = "DELETE FROM vehicules WHERE ID_VEHICULE = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idVehicule);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression d'un véhicule : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer tous les véhicules
    public static List<Vehicule> getAllVehicules() {
        List<Vehicule> vehiculesList = new ArrayList<>();
        String query = "SELECT * FROM vehicules";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Vehicule vehicule = new Vehicule(
                        resultSet.getInt("ID_VEHICULE"),
                        resultSet.getInt("ID_ETAT_VOITURE"),
                        resultSet.getString("NUMERO_CHASSI"),
                        resultSet.getString("IMMATRICULATION"),
                        resultSet.getString("MARQUE"),
                        resultSet.getString("MODELE"),
                        resultSet.getInt("NB_PLACES"),
                        resultSet.getString("ENERGIE"),
                        resultSet.getTimestamp("DATE_ACQUISITION") != null ? resultSet.getTimestamp("DATE_ACQUISITION").toLocalDateTime().toLocalDate() : null,
                        resultSet.getTimestamp("DATE_AMMORTISSEMENT") != null ? resultSet.getTimestamp("DATE_AMMORTISSEMENT").toLocalDateTime().toLocalDate() : null,
                        resultSet.getTimestamp("DATE_MISE_EN_SERVICE") != null ? resultSet.getTimestamp("DATE_MISE_EN_SERVICE").toLocalDateTime().toLocalDate() : null,
                        resultSet.getInt("PUISSANCE"),
                        resultSet.getString("COULEUR"),
                        resultSet.getInt("PRIX_VEHICULE"),
                        resultSet.getTimestamp("DATE_ETAT") != null ? resultSet.getTimestamp("DATE_ETAT").toLocalDateTime().toLocalDate() : null
                );
                vehiculesList.add(vehicule);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des véhicules : " + e.getMessage());
            e.printStackTrace();
        }

        return vehiculesList;
    }
    /*public static boolean vehiculeExists(int idVehicule) {
        String query = "SELECT 1 FROM vehicules WHERE ID_VEHICULE = ?";

        try (Connection connection = connexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idVehicule);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // S'il y a un résultat => l'ID existe
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la vérification d'un véhicule : " + e.getMessage());
            e.printStackTrace();
            return false; // On considère que c'est faux pour éviter de bloquer
        }
    }*/

    // 🆕 Vérifier si un véhicule existe par son IMMATRICULATION
    public static boolean vehiculeExists(String immatriculation) {
        String query = "SELECT 1 FROM vehicules WHERE IMMATRICULATION = ?";
        try (Connection connection = connexionBD.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, immatriculation);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la vérification de l'immatriculation : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    // ✅ Méthode qui retourne l'ID d'un véhicule à partir de son immatriculation
    public static int getIdByImmatriculation(String immatriculation) {
        String query = "SELECT ID_VEHICULE FROM vehicules WHERE IMMATRICULATION = ?";
        try (Connection connection = connexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, immatriculation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_VEHICULE");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans getIdByImmatriculation: " + e.getMessage());
        }

        return -1; // Retourne -1 si l'immatriculation n'est pas trouvée
    }

    public static List<String> getAllImmatriculations() {
        List<String> immatriculations = new ArrayList<>();
        String query = "SELECT IMMATRICULATION FROM vehicules";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                immatriculations.add(rs.getString("IMMATRICULATION"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des immatriculations : " + e.getMessage());
        }

        return immatriculations;
    }

    public static String getImmatriculationById(int idVehicule) {
        String query = "SELECT IMMATRICULATION FROM vehicules WHERE ID_VEHICULE = ?";
        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idVehicule);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("IMMATRICULATION");
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération de l'immatriculation : " + e.getMessage());
        }
        return null;
    }

    public static List<String> getImmatriculationsDisponibles() {
        List<String> disponibles = new ArrayList<>();
        String query = """
        SELECT IMMATRICULATION
        FROM vehicules
        WHERE ID_ETAT_VOITURE = 1
        AND IMMATRICULATION IS NOT NULL
    """;

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                disponibles.add(rs.getString("IMMATRICULATION"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules disponibles : " + e.getMessage());
        }

        return disponibles;
    }

    /**
     * Compte les véhicules en fonction de leur libellé d'état (ex: "Disponible").
     *
     * @param libelleEtat le libellé de l'état du véhicule
     * @return le nombre de véhicules correspondants
     */
    public static int countByEtat(String libelleEtat) {
        String query = """
        SELECT COUNT(*) FROM vehicules v
        JOIN etat_voiture e ON v.ID_ETAT_VOITURE = e.ID_ETAT_VOITURE
        WHERE e.LIB_ETAT_VOITURE = ?
    """;

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, libelleEtat);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du comptage par état : " + e.getMessage());
        }

        return 0;
    }

    public static int countAttribues() {
        String query = "SELECT COUNT(*) FROM vehicules WHERE ID_ETAT_VOITURE = 5";

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans countAttribues : " + e.getMessage());
        }

        return 0;
    }

    public static List<Vehicule> getVehiculesRecents(int limite) {
        List<Vehicule> liste = new ArrayList<>();
        String query = """
        SELECT * FROM vehicules
        ORDER BY DATE_MISE_EN_SERVICE DESC
        LIMIT ?
    """;

        try (Connection conn = connexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vehicule vehicule = new Vehicule(
                        rs.getInt("ID_VEHICULE"),
                        rs.getInt("ID_ETAT_VOITURE"),
                        rs.getString("NUMERO_CHASSI"),
                        rs.getString("IMMATRICULATION"),
                        rs.getString("MARQUE"),
                        rs.getString("MODELE"),
                        rs.getInt("NB_PLACES"),
                        rs.getString("ENERGIE"),
                        rs.getTimestamp("DATE_ACQUISITION") != null ? rs.getTimestamp("DATE_ACQUISITION").toLocalDateTime().toLocalDate() : null,
                        rs.getTimestamp("DATE_AMMORTISSEMENT") != null ? rs.getTimestamp("DATE_AMMORTISSEMENT").toLocalDateTime().toLocalDate() : null,
                        rs.getTimestamp("DATE_MISE_EN_SERVICE") != null ? rs.getTimestamp("DATE_MISE_EN_SERVICE").toLocalDateTime().toLocalDate() : null,
                        rs.getInt("PUISSANCE"),
                        rs.getString("COULEUR"),
                        rs.getInt("PRIX_VEHICULE"),
                        rs.getTimestamp("DATE_ETAT") != null ? rs.getTimestamp("DATE_ETAT").toLocalDateTime().toLocalDate() : null
                );
                liste.add(vehicule);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans getVehiculesRecents : " + e.getMessage());
        }

        return liste;
    }




}


