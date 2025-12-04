package org.projetosyslocacar.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.projetosyslocacar.model.Veiculo;
import org.projetosyslocacar.model.Veiculo.StatusVeiculo; // Importa o Enum
import org.projetosyslocacar.model.Modelo;
import org.projetosyslocacar.dao.VeiculoDAO;
import org.projetosyslocacar.dao.ModeloDAO;

public class VeiculoFormularioController implements Initializable {

    @FXML private TextField txtId;
    @FXML private ComboBox<Modelo> cmbModelo;
    @FXML private ComboBox<StatusVeiculo> cmbStatus; // Usa o Enum
    @FXML private TextField txtPlaca;
    @FXML private TextField txtChassi;
    @FXML private TextField txtRenavam;
    @FXML private TextField txtCor;
    @FXML private TextField txtKm;

    private Veiculo veiculo;
    private final VeiculoDAO veiculoDAO = new VeiculoDAO(Veiculo.class);
    private final ModeloDAO modeloDAO = new ModeloDAO(Modelo.class);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Carrega os ComboBoxes com dados do banco e valores do Enum
        cmbModelo.setItems(FXCollections.observableArrayList(modeloDAO.buscarTodos()));

        // Carrega todos os valores do Enum StatusVeiculo no ComboBox
        cmbStatus.setItems(FXCollections.observableArrayList(Arrays.asList(StatusVeiculo.values())));
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        if (veiculo != null) {
            txtId.setText(String.valueOf(veiculo.getId()));
            txtPlaca.setText(veiculo.getPlaca());
            txtChassi.setText(veiculo.getChassi());
            txtRenavam.setText(veiculo.getRenavam());
            txtCor.setText(veiculo.getCor());
            txtKm.setText(veiculo.getKm());

            // Seleciona o item correto nos ComboBoxes
            cmbModelo.getSelectionModel().select(veiculo.getModelo());
            cmbStatus.getSelectionModel().select(veiculo.getStatus());
        } else {
            // Define um valor inicial para o status em novos cadastros
            cmbStatus.getSelectionModel().select(StatusVeiculo.DISPONIVEL);
        }
    }

    @FXML
    void handleSalvar(ActionEvent event) {
        try {
            if (veiculo == null) {
                veiculo = new Veiculo();
            }

            // 1. Coleta e validação de dados
            veiculo.setModelo(cmbModelo.getSelectionModel().getSelectedItem());
            veiculo.setStatus(cmbStatus.getSelectionModel().getSelectedItem());
            veiculo.setPlaca(txtPlaca.getText());
            veiculo.setChassi(txtChassi.getText());
            veiculo.setRenavam(txtRenavam.getText());
            veiculo.setCor(txtCor.getText());
            veiculo.setKm(txtKm.getText());

            // Validações
            if (cmbModelo.getValue() == null) {
                new Alert(AlertType.ERROR, "O Modelo é obrigatório.", ButtonType.OK).show();
                return;
            }
            if (txtPlaca.getText().isEmpty() || txtChassi.getText().isEmpty()) {
                new Alert(AlertType.ERROR, "Placa e Chassi são obrigatórios.", ButtonType.OK).show();
                return;
            }

            // 2. Salva (Create/Update)
            veiculoDAO.salvar(veiculo);

            new Alert(AlertType.INFORMATION, "Veículo salvo com sucesso!", ButtonType.OK).show();
            fecharJanela();

        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar o veículo: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtPlaca.getScene().getWindow();
        stage.close();
    }
}