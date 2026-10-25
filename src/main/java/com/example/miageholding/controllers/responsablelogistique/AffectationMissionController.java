package com.example.miageholding.controllers.responsablelogistique;

import com.example.miageholding.core.dao.MissionDAO;
import com.example.miageholding.core.dao.AffectationMissionDAO;
import com.example.miageholding.core.dao.PersonnelDAO;
import com.example.miageholding.models.dto.AffectationDTO;
import com.example.miageholding.utils.ToastService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AffectationMissionController {

    @FXML private ComboBox<String> comboPersonnel;
    @FXML private ComboBox<String> comboMission;
    @FXML private Button btnAttribuer;
    @FXML private TableView<AffectationDTO> tableAffectations;
    @FXML private TableColumn<AffectationDTO, String> colPersonnel;
    @FXML private TableColumn<AffectationDTO, String> colMission;

    @FXML
    public void initialize() {
        chargerCombos();
        chargerTableAffectations();
    }

    private void chargerCombos() {
        comboPersonnel.setItems(FXCollections.observableArrayList(PersonnelDAO.getAllNomsComplets()));
        comboMission.setItems(FXCollections.observableArrayList(MissionDAO.getAllLibelles()));
    }

    private void chargerTableAffectations() {
        List<AffectationDTO> affectations = AffectationMissionDAO.getAllAffectations();
        ObservableList<AffectationDTO> data = FXCollections.observableArrayList(affectations);

        colPersonnel.setCellValueFactory(new PropertyValueFactory<>("nomPersonnel"));
        colMission.setCellValueFactory(new PropertyValueFactory<>("libelleMission"));
        tableAffectations.setItems(data);
    }

    @FXML
    private void handleAttribuer() {
        String nomComplet = comboPersonnel.getValue();
        String libelleMission = comboMission.getValue();

        if (nomComplet == null || libelleMission == null) {
            ToastService.showWarning("Veuillez sélectionner un personnel et une mission.");
            return;
        }

        int idPersonnel = PersonnelDAO.getIdByNomComplet(nomComplet);
        int idMission = MissionDAO.getIdByLibelle(libelleMission);

        if (AffectationMissionDAO.dejaAttribue(idPersonnel, idMission)) {
            ToastService.showWarning("Cette mission est déjà attribuée à ce personnel.");
            return;
        }

        boolean success = AffectationMissionDAO.attribuerMission(idPersonnel, idMission);
        if (success) {
            ToastService.showSuccess("Mission attribuée avec succès !");
            comboPersonnel.getSelectionModel().clearSelection();
            comboMission.getSelectionModel().clearSelection();
            chargerTableAffectations();
        } else {
            ToastService.showError("Erreur lors de l'attribution.");
        }
    }
}
