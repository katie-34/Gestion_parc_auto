package com.example.miageholding.core.dao;

import com.example.miageholding.models.Service;
import com.example.miageholding.core.bd.connexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class ServicesDAO {

        public static List<Service> getAllServices() {
            List<Service> services = new ArrayList<>();
            String query = "SELECT * FROM service";

            try (Connection connection = connexionBD.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    services.add(new Service(
                            rs.getInt("ID_SERVICE"),
                            rs.getString("LIB_SERVICE"),
                            rs.getString("LOCALISATION_SERVICE")
                    ));
                }

            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la récupération des services : " + e.getMessage());
            }
            return services;
        }

        public static boolean insertService(Service service) {
            String query = "INSERT INTO service (ID_SERVICE, LIB_SERVICE, LOCALISATION_SERVICE) VALUES (?, ?, ?)";
            try (Connection connection = connexionBD.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setInt(1, service.getIdService());
                ps.setString(2, service.getLibelle());
                ps.setString(3, service.getLocalisation());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de l'insertion du service : " + e.getMessage());
                return false;
            }
        }

        public static boolean updateService(Service service) {
            String query = "UPDATE service SET LIB_SERVICE = ?, LOCALISATION_SERVICE = ? WHERE ID_SERVICE = ?";
            try (Connection connection = connexionBD.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, service.getLibelle());
                ps.setString(2, service.getLocalisation());
                ps.setInt(3, service.getIdService());
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la mise à jour du service : " + e.getMessage());
                return false;
            }
        }

        public static boolean deleteService(int idService) {
            String query = "DELETE FROM service WHERE ID_SERVICE = ?";
            try (Connection connection = connexionBD.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setInt(1, idService);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la suppression du service : " + e.getMessage());
                return false;
            }
        }

        public static int countServices() {
            String query = "SELECT COUNT(*) FROM service";
            try (Connection connection = connexionBD.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors du comptage des services : " + e.getMessage());
            }
            return 0;
        }

        public static List<String> getAllLibelles() {
            List<String> libelles = new ArrayList<>();
            String query = "SELECT LIB_SERVICE FROM service";

            try (Connection conn = connexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    libelles.add(rs.getString("LIB_SERVICE"));
                }

            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la récupération des libellés de service : " + e.getMessage());
            }

            return libelles;
        }

        public static int getIdByLibelle(String libelle) {
            String query = "SELECT ID_SERVICE FROM service WHERE LIB_SERVICE = ?";
            try (Connection conn = connexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, libelle.trim());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getInt("ID_SERVICE");
                }

            } catch (SQLException e) {
                System.err.println("Erreur SQL dans getIdByLibelle (service) : " + e.getMessage());
            }
            return -1; // Retourne -1 si non trouvé
        }

        public static String getLibelleById(int id) {
            String query = "SELECT LIB_SERVICE FROM service WHERE ID_SERVICE = ?";
            try (Connection conn = connexionBD.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("LIB_SERVICE");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erreur SQL dans getLibelleById (Service) : " + e.getMessage());
            }
            return "Inconnu";
        }




    }


