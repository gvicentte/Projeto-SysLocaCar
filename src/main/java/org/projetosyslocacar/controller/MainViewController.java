package org.projetosyslocacar.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors; // Import necessário para a filtragem em cascata

import javafx.util.StringConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.time.ZoneId; // Necessário para converter Date <-> LocalDate
import java.time.LocalDate;

// Importe todos os seus modelos e DAOs
import org.projetosyslocacar.model.*;
import org.projetosyslocacar.dao.*;

// O controlador principal unificado
public class MainViewController implements Initializable {

    // =================================================================
    // DAOs
    // =================================================================
    // Assumindo que seus DAOs tem construtores que permitem a inicialização
    private final ModeloDAO modeloDAO = new ModeloDAO(Modelo.class);
    private final MarcaDAO marcaDAO = new MarcaDAO(Marca.class);
    private final CategoriaDAO categoriaDAO = new CategoriaDAO(Categoria.class);
    private final VeiculoDAO veiculoDAO = new VeiculoDAO(Veiculo.class);
    private final ClienteDAO clienteDAO = new ClienteDAO(Cliente.class);
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO(Funcionario.class);
    private final UsuarioDAO usuarioDAO = new UsuarioDAO(Usuario.class);
    private final ContratoLocacaoDAO contratoLocacaoDAO = new ContratoLocacaoDAO(ContratoLocacao.class);
    private final LocacaoDAO locacaoDAO = new LocacaoDAO(Locacao.class);
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO(Pagamento.class);
    private final ManutencaoDAO manutencaoDAO = new ManutencaoDAO(Manutencao.class);
    private final OcorrenciaDAO ocorrenciaDAO = new OcorrenciaDAO(Ocorrencia.class);

    // =================================================================
    // NAVEGAÇÃO / CONTAINERS
    // =================================================================
    @FXML private Button btnModelos;
    @FXML private Button btnMarcas;
    @FXML private Button btnCategorias;
    @FXML private Button btnVeiculos;
    @FXML private Button btnClientes;
    @FXML private Button btnFuncionarios;
    @FXML private Button btnUsuarios;
    @FXML private Button btnContratos;
    @FXML private Button btnLocacoes;
    @FXML private Button btnPagamentos;
    @FXML private Button btnManutencao;
    @FXML private Button btnOcorrencias;

    @FXML private VBox containerModelos;
    @FXML private VBox containerMarcas;
    @FXML private VBox containerCategorias;
    @FXML private VBox containerVeiculos;
    @FXML private VBox containerClientes;
    @FXML private VBox containerFuncionarios;
    @FXML private VBox containerUsuarios;
    @FXML private VBox containerContratos;
    @FXML private VBox containerLocacoes;
    @FXML private VBox containerPagamentos;
    @FXML private VBox containerManutencao;
    @FXML private VBox containerOcorrencias;
    private VBox containerAtual;



    // =================================================================
    // 1. MODELO COMPONENTES
    // =================================================================
    @FXML private TextField txtIdModelo;
    @FXML private TextField txtNomeModelo;
    @FXML private TextField txtAnoModelo;
    @FXML private ComboBox<Marca> cmbMarcaModelo;
    @FXML private ComboBox<Categoria> cmbCategoriaModelo;
    @FXML private TextField txtCodModelo;
    @FXML private TableView<Modelo> tabelaModelos;
    @FXML private TableColumn<Modelo, Long> colIdModelo;
    @FXML private TableColumn<Modelo, String> colNomeModelo;
    @FXML private TableColumn<Modelo, Date> colAnoModelo;
    @FXML private TableColumn<Modelo, String> colMarcaModelo;
    @FXML private TableColumn<Modelo, String> colCategoriaModelo;
    @FXML private TableColumn<Modelo, Integer> colCodModeloTabela;
    @FXML private TableColumn<Modelo, Void> colAcoesModelo;

    private Modelo modeloSelecionado;


    // =================================================================
    // 2. MARCA COMPONENTES
    // =================================================================
    @FXML private TextField txtIdMarca;
    @FXML private TextField txtNomeMarca;
    @FXML private TableView<Marca> tabelaMarcas;
    @FXML private TableColumn<Marca, Long> colIdMarca;
    @FXML private TableColumn<Marca, String> colNomeMarca;
    private Marca marcaSelecionada;


    // =================================================================
    // 3. CATEGORIA COMPONENTES
    // =================================================================
    @FXML private TextField txtIdCategoria;
    @FXML private TextField txtNomeCategoria;
    @FXML private TextField txtValorLocacaoCategoria;
    @FXML private TableView<Categoria> tabelaCategorias;
    @FXML private TableColumn<Categoria, Long> colIdCategoria;
    @FXML private TableColumn<Categoria, String> colNomeCategoria;
    @FXML private TableColumn<Categoria, Float> colValorLocacaoCategoria;
    private Categoria categoriaSelecionada;


    // =================================================================
    // 4. VEÍCULO COMPONENTES (COM NOVA MARCA PARA FILTRAGEM)
    // =================================================================
    @FXML private TextField txtIdVeiculo;
    @FXML private TextField txtPlacaVeiculo;
    @FXML private TextField txtChassiVeiculo;
    @FXML private TextField txtKmVeiculo;
    @FXML private TextField txtCorVeiculo;

    // NOVO: ComboBox para a Marca do Veículo
    @FXML private ComboBox<Marca> cmbMarcaVeiculo;

    @FXML private ComboBox<Modelo> cmbModeloVeiculo;
    @FXML private ComboBox<Veiculo.StatusVeiculo> cmbStatusVeiculo;
    @FXML private TableView<Veiculo> tabelaVeiculos;
    @FXML private TableColumn<Veiculo, Long> colIdVeiculo;
    @FXML private TableColumn<Veiculo, String> colPlacaVeiculo;
    @FXML private TableColumn<Veiculo, String> colModeloVeiculo;
    @FXML private TableColumn<Veiculo, String> colStatusVeiculo;
    @FXML private TableColumn<Veiculo, String> colKmVeiculo;
    @FXML private TableColumn<Veiculo, String> colChassiVeiculo;
    private Veiculo veiculoSelecionado;


    // =================================================================
    // CLIENTE (FXML)
    // =================================================================
    @FXML private TextField txtIdCliente;
    @FXML private TextField txtNomeCliente;
    @FXML private TextField txtCpfCliente;
    @FXML private TextField txtEmailCliente;
    @FXML private TextField txtCnhCliente;
    @FXML private TextField txtRgCliente;
    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Long> colIdCliente;
    @FXML private TableColumn<Cliente, String> colNomeCliente;
    @FXML private TableColumn<Cliente, String> colCpfCliente;
    @FXML private TableColumn<Cliente, String> colEmailCliente;
    @FXML private TableColumn<Cliente, String> colCnhCliente;
    @FXML private TableColumn<Cliente, String> colRgCliente;
    private Cliente clienteSelecionado;
/*
    // =================================================================
// CONTRATO LOCAÇÃO (FXML)
// =================================================================

    @FXML private TextField txtIdContrato;
    @FXML private DatePicker dpDataContrato;
    @FXML private TextField txtValorCaucao;
    @FXML private ComboBox<ContratoLocacao.StatusLocacao> cmbStatusContrato;
    @FXML private TextField txtValorTotalContrato;
    @FXML private ComboBox<Cliente> cmbClienteContrato; // Para selecionar o cliente responsável

    @FXML private TableView<ContratoLocacao> tabelaContratos;
    @FXML private TableColumn<ContratoLocacao, Long> colIdContrato;
    @FXML private TableColumn<ContratoLocacao, Date> colDataContrato;
    @FXML private TableColumn<ContratoLocacao, ContratoLocacao.StatusLocacao> colStatusContrato;
    @FXML private TableColumn<ContratoLocacao, Float> colValorTotalContrato;
    @FXML private TableColumn<ContratoLocacao, String> colClienteContrato; // Exibir o nome do Cliente

    private ContratoLocacao contratoSelecionado;

    // =================================================================
// ENDEREÇO (FXML) - Associado ao Cliente
// =================================================================
    @FXML private TextField txtCepCliente;
    @FXML private TextField txtLogradouroCliente;
    @FXML private TextField txtNumeroCliente;
    @FXML private TextField txtComplementoCliente;
    @FXML private TextField txtReferenciaCliente;
*/
    // =================================================================
    // INICIALIZAÇÃO E NAVEGAÇÃO
    // =================================================================

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarModeloCRUD();
        inicializarMarcaCRUD();
        inicializarCategoriaCRUD();
        inicializarVeiculoCRUD();
        inicializarClienteCRUD();
        //inicializarLocacaoCRUD();

        mostrarContainer(containerModelos, btnModelos);
        handleAtualizarListaModelo();
    }

    // Lógica para alternar as telas (containers)
    @FXML
    private void handleNavegacao(ActionEvent event) {
        Button botaoClicado = (Button) event.getSource();

        if (botaoClicado == btnModelos) {
            mostrarContainer(containerModelos, btnModelos);
            handleAtualizarListaModelo();
        } else if (botaoClicado == btnMarcas) {
            mostrarContainer(containerMarcas, btnMarcas);
            handleAtualizarListaMarca();
        } else if (botaoClicado == btnCategorias) {
            mostrarContainer(containerCategorias, btnCategorias);
            handleAtualizarListaCategoria();
        } else if (botaoClicado == btnVeiculos) {
            mostrarContainer(containerVeiculos, btnVeiculos);
            handleAtualizarListaVeiculo();
        } else if (botaoClicado == btnClientes) {
            mostrarContainer(containerClientes, btnClientes);
            handleAtualizarListaCliente();
        } else if (botaoClicado == btnContratos) {
            mostrarContainer(containerContratos,btnContratos);
        } else if (botaoClicado == btnFuncionarios) {
            mostrarContainer(containerFuncionarios,btnFuncionarios);
        } else if (botaoClicado == btnLocacoes){
            mostrarContainer(containerLocacoes,btnLocacoes);
        } else if (botaoClicado == btnManutencao) {
            mostrarContainer(containerManutencao,btnManutencao);
        } else if (botaoClicado == btnOcorrencias) {
            mostrarContainer(containerOcorrencias,btnOcorrencias);
        } else if (botaoClicado == btnPagamentos) {
            mostrarContainer(containerPagamentos,btnPagamentos);
        } else if (botaoClicado == btnUsuarios) {
            mostrarContainer(containerUsuarios,btnUsuarios);
        }
    }

    private void mostrarContainer(VBox container, Button botao) {
        VBox[] containers = {containerModelos, containerMarcas, containerCategorias, containerVeiculos, containerClientes, containerContratos, containerFuncionarios, containerLocacoes, containerManutencao, containerOcorrencias, containerPagamentos, containerUsuarios};
        Button[] botoes = {btnModelos, btnMarcas, btnCategorias, btnVeiculos, btnClientes, btnContratos, btnFuncionarios, btnLocacoes, btnManutencao, btnOcorrencias, btnPagamentos, btnUsuarios};

        for (VBox box : containers) {
            if (box != null) {
                box.setVisible(false);
                box.setManaged(false);
            }
        }

        for (Button btn : botoes) {
            if (btn != null) {
                btn.setStyle(null);
            }
        }

        if (container != null) {
            container.setVisible(true);
            container.setManaged(true);
            containerAtual = container;
            botao.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        }
    }


    // =================================================================
    // 1. MÉTODOS CRUD - MODELO
    // =================================================================

    private void inicializarModeloCRUD() {
        colIdModelo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeModelo.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAnoModelo.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colCodModeloTabela.setCellValueFactory(new PropertyValueFactory<>("idModelo"));

        colMarcaModelo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMarca().getNome()));

        colCategoriaModelo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoria().getNome()));

        configurarComboBoxMarcaModelo();
        configurarComboBoxCategoriaModelo();
        carregarComboBoxesModelo();

        tabelaModelos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetalhesModelo(newValue));
    }

    private void configurarComboBoxMarcaModelo() {
        cmbMarcaModelo.setConverter(new StringConverter<Marca>() {
            @Override
            public String toString(Marca marca) { return marca == null ? "" : marca.getNome(); }
            @Override
            public Marca fromString(String string) { return null; }
        });
    }

    private void configurarComboBoxCategoriaModelo() {
        cmbCategoriaModelo.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria categoria) { return categoria == null ? "" : categoria.getNome(); }
            @Override
            public Categoria fromString(String string) { return null; }
        });
    }

    private void carregarComboBoxesModelo() {
        List<Marca> marcas = marcaDAO.buscarTodos();
        cmbMarcaModelo.setItems(FXCollections.observableArrayList(marcas));

        List<Categoria> categorias = categoriaDAO.buscarTodos();
        cmbCategoriaModelo.setItems(FXCollections.observableArrayList(categorias));
    }

    private void mostrarDetalhesModelo(Modelo modelo) {
        modeloSelecionado = modelo;
        if (modelo != null) {
            txtIdModelo.setText(String.valueOf(modelo.getId()));
            txtNomeModelo.setText(modelo.getNome());

            if (modelo.getAno() != null) {
                txtAnoModelo.setText(new SimpleDateFormat("yyyy").format(modelo.getAno()));
            } else {
                txtAnoModelo.setText("");
            }

            txtCodModelo.setText(String.valueOf(modelo.getIdModelo()));

            cmbMarcaModelo.getSelectionModel().select(modelo.getMarca());
            cmbCategoriaModelo.getSelectionModel().select(modelo.getCategoria());
        } else {
            handleLimparFormularioModelo();
        }
    }

    @FXML
    private void handleAtualizarListaModelo() {
        try {
            List<Modelo> modelos = modeloDAO.buscarTodos();
            ObservableList<Modelo> obsList = FXCollections.observableArrayList(modelos);
            tabelaModelos.setItems(obsList);
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao carregar modelos: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    @FXML
    void handleSalvarModelo(ActionEvent event) {
        if (modeloSelecionado == null) { modeloSelecionado = new Modelo(); }

        try {
            if (txtNomeModelo.getText().isEmpty() || cmbMarcaModelo.getValue() == null || cmbCategoriaModelo.getValue() == null) {
                new Alert(AlertType.ERROR, "Nome, Marca e Categoria são obrigatórios.", ButtonType.OK).show();
                return;
            }

            modeloSelecionado.setNome(txtNomeModelo.getText());
            modeloSelecionado.setMarca(cmbMarcaModelo.getValue());
            modeloSelecionado.setCategoria(cmbCategoriaModelo.getValue());

            try {
                String anoStr = txtAnoModelo.getText();
                if (!anoStr.isEmpty()) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy");
                    modeloSelecionado.setAno(df.parse(anoStr));
                }
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Formato do Ano inválido (use YYYY).", ButtonType.OK).show();
                return;
            }

            try {
                modeloSelecionado.setIdModelo(Integer.parseInt(txtCodModelo.getText()));
            } catch (NumberFormatException e) {
                new Alert(AlertType.ERROR, "O código ID do Modelo deve ser um número.", ButtonType.OK).show();
                return;
            }

            modeloDAO.salvar(modeloSelecionado);

            new Alert(AlertType.INFORMATION, "Modelo salvo com sucesso!", ButtonType.OK).show();
            handleLimparFormularioModelo();
            handleAtualizarListaModelo();

        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar o modelo: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLimparFormularioModelo() {
        modeloSelecionado = null;
        txtIdModelo.setText("Gerado Automaticamente");
        txtNomeModelo.setText("");
        txtAnoModelo.setText("");
        txtCodModelo.setText("");
        cmbMarcaModelo.getSelectionModel().clearSelection();
        cmbCategoriaModelo.getSelectionModel().clearSelection();
        tabelaModelos.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleEditarModelo() {
        mostrarDetalhesModelo(tabelaModelos.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleDeletarModelo() {
        Modelo modeloDeletar = tabelaModelos.getSelectionModel().getSelectedItem();
        if (modeloDeletar != null) {
            try {
                modeloDAO.excluir(modeloDeletar);
                handleAtualizarListaModelo();
                new Alert(AlertType.INFORMATION, "Modelo excluído com sucesso!", ButtonType.OK).show();
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Erro ao excluir: " + e.getMessage(), ButtonType.OK).show();
            }
        }
    }


    // =================================================================
    // 2. MÉTODOS CRUD - MARCA
    // =================================================================

    private void inicializarMarcaCRUD() {
        colIdMarca.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeMarca.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tabelaMarcas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetalhesMarca(newValue));
    }

    private void mostrarDetalhesMarca(Marca marca) {
        marcaSelecionada = marca;
        if (marca != null) {
            txtIdMarca.setText(String.valueOf(marca.getId()));
            txtNomeMarca.setText(marca.getNome());
        } else {
            handleLimparFormularioMarca();
        }
    }

    @FXML
    private void handleSalvarMarca() {
        if (marcaSelecionada == null) { marcaSelecionada = new Marca(); }

        try {
            if (txtNomeMarca.getText().isEmpty()) {
                new Alert(AlertType.ERROR, "O nome da Marca é obrigatório.", ButtonType.OK).show();
                return;
            }

            marcaSelecionada.setNome(txtNomeMarca.getText());
            marcaDAO.salvar(marcaSelecionada);

            new Alert(AlertType.INFORMATION, "Marca salva com sucesso!", ButtonType.OK).show();
            handleLimparFormularioMarca();
            handleAtualizarListaMarca();
            carregarComboBoxesModelo(); // Atualiza ComboBoxes relacionados
            carregarComboBoxesVeiculo(); // Atualiza ComboBoxes relacionados
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar a Marca: " + e.getMessage(), ButtonType.OK).show();
        }
    }

    @FXML
    private void handleAtualizarListaMarca() {
        List<Marca> lista = marcaDAO.buscarTodos();
        tabelaMarcas.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void handleLimparFormularioMarca() {
        marcaSelecionada = null;
        txtIdMarca.setText("Gerado Automaticamente");
        txtNomeMarca.setText("");
        tabelaMarcas.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarMarca() {
        Marca marcaDeletar = tabelaMarcas.getSelectionModel().getSelectedItem();
        if (marcaDeletar != null) {
            try {
                marcaDAO.excluir(marcaDeletar);
                handleAtualizarListaMarca();
                carregarComboBoxesModelo(); // Atualiza ComboBoxes relacionados
                carregarComboBoxesVeiculo(); // Atualiza ComboBoxes relacionados
                new Alert(AlertType.INFORMATION, "Marca excluída com sucesso!", ButtonType.OK).show();
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Erro ao excluir: " + e.getMessage(), ButtonType.OK).show();
            }
        }
    }


    // =================================================================
    // 3. MÉTODOS CRUD - CATEGORIA
    // =================================================================

    private void inicializarCategoriaCRUD() {
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeCategoria.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colValorLocacaoCategoria.setCellValueFactory(new PropertyValueFactory<>("valorLocacao"));
        tabelaCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetalhesCategoria(newValue));
    }

    private void mostrarDetalhesCategoria(Categoria categoria) {
        categoriaSelecionada = categoria;
        if (categoria != null) {
            txtIdCategoria.setText(String.valueOf(categoria.getId()));
            txtNomeCategoria.setText(categoria.getNome());
            txtValorLocacaoCategoria.setText(String.valueOf(categoria.getValorLocacao()));
        } else {
            handleLimparFormularioCategoria();
        }
    }

    @FXML
    private void handleSalvarCategoria() {
        if (categoriaSelecionada == null) { categoriaSelecionada = new Categoria(); }

        try {
            if (txtNomeCategoria.getText().isEmpty() || txtValorLocacaoCategoria.getText().isEmpty()) {
                new Alert(AlertType.ERROR, "Nome e Valor de Locação são obrigatórios.", ButtonType.OK).show();
                return;
            }

            categoriaSelecionada.setNome(txtNomeCategoria.getText());

            try {
                // Permite vírgula ou ponto como separador decimal
                float valor = Float.parseFloat(txtValorLocacaoCategoria.getText().replace(",", "."));
                categoriaSelecionada.setValorLocacao(valor);
            } catch (NumberFormatException e) {
                new Alert(AlertType.ERROR, "Valor de Locação deve ser um número válido.", ButtonType.OK).show();
                return;
            }

            categoriaDAO.salvar(categoriaSelecionada);

            new Alert(AlertType.INFORMATION, "Categoria salva com sucesso!", ButtonType.OK).show();
            handleLimparFormularioCategoria();
            handleAtualizarListaCategoria();
            carregarComboBoxesModelo(); // Atualiza ComboBoxes relacionados
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar a Categoria: " + e.getMessage(), ButtonType.OK).show();
        }
    }

    @FXML
    private void handleAtualizarListaCategoria() {
        List<Categoria> lista = categoriaDAO.buscarTodos();
        tabelaCategorias.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void handleLimparFormularioCategoria() {
        categoriaSelecionada = null;
        txtIdCategoria.setText("Gerado Automaticamente");
        txtNomeCategoria.setText("");
        txtValorLocacaoCategoria.setText("");
        tabelaCategorias.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarCategoria() {
        Categoria categoriaDeletar = tabelaCategorias.getSelectionModel().getSelectedItem();
        if (categoriaDeletar != null) {
            try {
                categoriaDAO.excluir(categoriaDeletar);
                handleAtualizarListaCategoria();
                carregarComboBoxesModelo(); // Atualiza ComboBoxes relacionados
                new Alert(AlertType.INFORMATION, "Categoria excluída com sucesso!", ButtonType.OK).show();
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Erro ao excluir: " + e.getMessage(), ButtonType.OK).show();
            }
        }
    }

    // =================================================================
    // 4. MÉTODOS CRUD - VEÍCULO
    // =================================================================

    private void inicializarVeiculoCRUD() {
        // Configuração das colunas
        colIdVeiculo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlacaVeiculo.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colKmVeiculo.setCellValueFactory(new PropertyValueFactory<>("km"));
        colChassiVeiculo.setCellValueFactory(new PropertyValueFactory<>("chassi"));

        colModeloVeiculo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getModelo().getNome()));
        colStatusVeiculo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().toString()));

        // Configurações de ComboBox
        configurarComboBoxMarcaVeiculo();
        configurarComboBoxModeloVeiculo();
        carregarComboBoxesVeiculo();

        // Listener para edição
        tabelaVeiculos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetalhesVeiculo(newValue));
    }

    private void configurarComboBoxMarcaVeiculo() {
        cmbMarcaVeiculo.setConverter(new StringConverter<Marca>() {
            @Override
            public String toString(Marca marca) { return marca == null ? "" : marca.getNome(); }
            @Override
            public Marca fromString(String string) { return null; }
        });
    }

    private void configurarComboBoxModeloVeiculo() {
        cmbModeloVeiculo.setConverter(new StringConverter<Modelo>() {
            @Override
            public String toString(Modelo modelo) { return modelo == null ? "" : modelo.getNome(); }
            @Override
            public Modelo fromString(String string) { return null; }
        });
    }

    private void carregarComboBoxesVeiculo() {
        // 1. Carrega Marcas e Status
        List<Marca> listaMarcas = marcaDAO.buscarTodos();
        cmbMarcaVeiculo.setItems(FXCollections.observableArrayList(listaMarcas));
        cmbStatusVeiculo.setItems(FXCollections.observableArrayList(Veiculo.StatusVeiculo.values()));

        // 2. ComboBox de Modelo começa vazia
        cmbModeloVeiculo.setItems(FXCollections.observableArrayList());
    }

    /**
     * NOVO MÉTODO: Filtra os modelos na cmbModeloVeiculo com base na Marca selecionada.
     */
    @FXML
    private void handleMarcaSelecionadaVeiculo(ActionEvent event) {
        Marca marcaSelecionada = cmbMarcaVeiculo.getSelectionModel().getSelectedItem();

        if (marcaSelecionada != null) {

            // 🚨 SOLUÇÃO PARA LAZY LOADING: Buscar todos os modelos e filtrar em memória.
            // Para grandes volumes de dados, é mais performático criar um método no DAO (ex: buscarPorMarca(id)).
            List<Modelo> todosModelos = modeloDAO.buscarTodos();
            List<Modelo> modelosFiltrados = todosModelos.stream()
                    .filter(m -> m.getMarca() != null && m.getMarca().getId() == marcaSelecionada.getId())
                    .collect(Collectors.toList());

            cmbModeloVeiculo.setItems(FXCollections.observableArrayList(modelosFiltrados));
        } else {
            // Limpa o ComboBox de Modelos se nenhuma Marca estiver selecionada
            cmbModeloVeiculo.setItems(FXCollections.observableArrayList());
        }

        // Limpa a seleção anterior do Modelo para forçar uma nova escolha
        cmbModeloVeiculo.getSelectionModel().clearSelection();
    }


    private void mostrarDetalhesVeiculo(Veiculo veiculo) {
        veiculoSelecionado = veiculo;
        if (veiculo != null) {
            txtIdVeiculo.setText(String.valueOf(veiculo.getId()));
            txtPlacaVeiculo.setText(veiculo.getPlaca());
            txtChassiVeiculo.setText(veiculo.getChassi());
            txtKmVeiculo.setText(veiculo.getKm());
            txtCorVeiculo.setText(veiculo.getCor());

            if (veiculo.getModelo() != null && veiculo.getModelo().getMarca() != null) {
                // 1. Seleciona a Marca (isso dispara o handleMarcaSelecionadaVeiculo e preenche a lista de Modelos)
                cmbMarcaVeiculo.getSelectionModel().select(veiculo.getModelo().getMarca());
                // 2. Agora que a lista de Modelos está filtrada, seleciona o Modelo
                cmbModeloVeiculo.getSelectionModel().select(veiculo.getModelo());
            } else {
                cmbMarcaVeiculo.getSelectionModel().clearSelection();
                cmbModeloVeiculo.getSelectionModel().clearSelection();
            }

            cmbStatusVeiculo.getSelectionModel().select(veiculo.getStatus());
        } else {
            handleLimparFormularioVeiculo();
        }
    }

    @FXML
    private void handleSalvarVeiculo() {
        if (veiculoSelecionado == null) { veiculoSelecionado = new Veiculo(); }

        try {
            if (txtPlacaVeiculo.getText().isEmpty() || txtChassiVeiculo.getText().isEmpty() || cmbModeloVeiculo.getValue() == null || cmbStatusVeiculo.getValue() == null) {
                new Alert(AlertType.ERROR, "Placa, Chassi, Modelo e Status são obrigatórios.", ButtonType.OK).show();
                return;
            }

            veiculoSelecionado.setPlaca(txtPlacaVeiculo.getText());
            veiculoSelecionado.setChassi(txtChassiVeiculo.getText());
            veiculoSelecionado.setKm(txtKmVeiculo.getText());
            veiculoSelecionado.setCor(txtCorVeiculo.getText());
            veiculoSelecionado.setModelo(cmbModeloVeiculo.getValue());
            veiculoSelecionado.setStatus(cmbStatusVeiculo.getValue());

            veiculoDAO.salvar(veiculoSelecionado);

            new Alert(AlertType.INFORMATION, "Veículo salvo com sucesso!", ButtonType.OK).show();
            handleLimparFormularioVeiculo();
            handleAtualizarListaVeiculo();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar o Veículo: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAtualizarListaVeiculo() {
        List<Veiculo> lista = veiculoDAO.buscarTodos();
        tabelaVeiculos.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void handleLimparFormularioVeiculo() {
        veiculoSelecionado = null;
        txtIdVeiculo.setText("Gerado Automaticamente");
        txtPlacaVeiculo.setText("");
        txtChassiVeiculo.setText("");
        txtKmVeiculo.setText("");
        txtCorVeiculo.setText("");
        cmbMarcaVeiculo.getSelectionModel().clearSelection(); // Limpa a Marca
        cmbModeloVeiculo.setItems(FXCollections.observableArrayList()); // Zera o Modelo
        cmbModeloVeiculo.getSelectionModel().clearSelection();
        cmbStatusVeiculo.getSelectionModel().clearSelection();
        tabelaVeiculos.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarVeiculo() {
        Veiculo veiculoDeletar = tabelaVeiculos.getSelectionModel().getSelectedItem();
        if (veiculoDeletar != null) {
            try {
                veiculoDAO.excluir(veiculoDeletar);
                handleAtualizarListaVeiculo();
                new Alert(AlertType.INFORMATION, "Veículo excluído com sucesso!", ButtonType.OK).show();
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Erro ao excluir: " + e.getMessage(), ButtonType.OK).show();
            }
        }
    }
    
    // CLIENTE
    private void inicializarClienteCRUD(){
        if (tabelaClientes != null) {
            colIdCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNomeCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colCpfCliente.setCellValueFactory(new PropertyValueFactory<>("cpf"));
            colEmailCliente.setCellValueFactory(new PropertyValueFactory<>("email"));
            colCnhCliente.setCellValueFactory(new PropertyValueFactory<>("cnh"));
            colRgCliente.setCellValueFactory(new PropertyValueFactory<>("rg"));

            // Listener para preencher o formulário ao selecionar um cliente
            tabelaClientes.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> mostrarDetalhesCliente(newValue));

            handleAtualizarListaCliente(); // Carrega os dados iniciais
        }
    }

    private void mostrarDetalhesCliente(Cliente cliente) {
        clienteSelecionado = cliente;
        if (cliente != null) {
            txtIdCliente.setText(String.valueOf(cliente.getId()));
            txtNomeCliente.setText(cliente.getNome());
            txtCpfCliente.setText(cliente.getCpf());
            txtEmailCliente.setText(cliente.getEmail());
            txtCnhCliente.setText(cliente.getCnh());
            txtRgCliente.setText(cliente.getRg());
            // Obs: Endereço e Contatos são objetos complexos e exigem lógica adicional.
        } else {
            handleLimparFormularioCliente();
        }
    }

    @FXML
    private void handleSalvarCliente() {
        try {
            Cliente cliente = (clienteSelecionado == null) ? new Cliente() : clienteSelecionado;

            // 1. Recuperar dados do formulário
            cliente.setNome(txtNomeCliente.getText());
            cliente.setCpf(txtCpfCliente.getText());
            cliente.setEmail(txtEmailCliente.getText());
            cliente.setCnh(txtCnhCliente.getText());
            cliente.setRg(txtRgCliente.getText());

            // 2. Validação básica
            if (cliente.getNome() == null || cliente.getNome().trim().isEmpty() ||
                    cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
                new Alert(AlertType.WARNING, "Nome e CPF são campos obrigatórios.", ButtonType.OK).show();
                return;
            }

            // 3. Salvar ou Atualizar
            if (cliente.getId() == 0) {
                clienteDAO.salvar(cliente);
                new Alert(AlertType.INFORMATION, "Cliente salvo com sucesso!", ButtonType.OK).show();
            } else {
                clienteDAO.salvar(cliente);
                new Alert(AlertType.INFORMATION, "Cliente atualizado com sucesso!", ButtonType.OK).show();
            }

            handleLimparFormularioCliente();
            handleAtualizarListaCliente();

        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar/atualizar Cliente: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAtualizarListaCliente() {
        List<Cliente> lista = clienteDAO.buscarTodos();
        tabelaClientes.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void handleLimparFormularioCliente() {
        clienteSelecionado = null;
        txtIdCliente.setText("Gerado Automaticamente");
        txtNomeCliente.setText("");
        txtCpfCliente.setText("");
        txtEmailCliente.setText("");
        txtCnhCliente.setText("");
        txtRgCliente.setText("");
        tabelaClientes.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarCliente() {
        Cliente clienteDeletar = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteDeletar != null) {
            try {
                clienteDAO.excluir(clienteDeletar);
                handleAtualizarListaCliente();
                handleLimparFormularioCliente();
                new Alert(AlertType.INFORMATION, "Cliente excluído com sucesso!", ButtonType.OK).show();
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Erro ao excluir Cliente: " + e.getMessage(), ButtonType.OK).show();
            }
        } else {
            new Alert(AlertType.WARNING, "Selecione um Cliente para deletar.", ButtonType.OK).show();
        }
    }

    /*
    private void inicializarLocacaoCRUD(){
        if (tabelaContratos != null) {
            // 1. Configurar ComboBoxes
            // Preenche o ComboBox com os valores do ENUM StatusLocacao
            cmbStatusContrato.setItems(FXCollections.observableArrayList(ContratoLocacao.StatusLocacao.values()));

            // Carrega a lista de clientes para a ComboBox
            handleAtualizarComboClienteContrato();

            // 2. Configurar Colunas da Tabela
            colIdContrato.setCellValueFactory(new PropertyValueFactory<>("id"));
            colDataContrato.setCellValueFactory(new PropertyValueFactory<>("dataContrato"));
            colStatusContrato.setCellValueFactory(new PropertyValueFactory<>("status"));
            colValorTotalContrato.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

            // Para mostrar o nome do Cliente (e não o objeto Cliente completo)
            colClienteContrato.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getCliente().getNome()));

            // 3. Listener para preencher o formulário
            tabelaContratos.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> mostrarDetalhesContrato(newValue));

            handleAtualizarListaContrato(); // Carrega os dados iniciais
        }
    }



    // =================================================================
    // CONTRATO LOCAÇÃO (Métodos de Ação)
    // =================================================================

    private void handleAtualizarComboClienteContrato() {
        List<Cliente> clientes = clienteDAO.buscarTodos();
        cmbClienteContrato.setItems(FXCollections.observableArrayList(clientes));

        // Configura como o Cliente é exibido na ComboBox (nome + ID)
        cmbClienteContrato.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                return (cliente != null) ? cliente.getNome() + " (ID: " + cliente.getId() + ")" : "";
            }

            @Override
            public Cliente fromString(String string) {
                // Não é necessário implementar a conversão de String para Objeto
                return null;
            }
        });
    }

    private void mostrarDetalhesContrato(ContratoLocacao contrato) {
        contratoSelecionado = contrato;
        if (contrato != null) {
            txtIdContrato.setText(String.valueOf(contrato.getId()));

            // Conversão de java.util.Date para java.time.LocalDate para o DatePicker
            dpDataContrato.setValue(contrato.getDataContrato() != null
                    ? contrato.getDataContrato().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    : null);

            // Formata os valores para exibição no TextField
            txtValorCaucao.setText(String.format("%.2f", contrato.getValorCaucao()).replace(',', '.'));
            txtValorTotalContrato.setText(String.format("%.2f", contrato.getValorTotal()).replace(',', '.'));

            cmbStatusContrato.getSelectionModel().select(contrato.getStatus());
            cmbClienteContrato.getSelectionModel().select(contrato.getCliente());
        } else {
            handleLimparFormularioContrato();
        }
    }

    @FXML
    private void handleSalvarContrato() {
        try {
            ContratoLocacao contrato = (contratoSelecionado == null) ? new ContratoLocacao() : contratoSelecionado;

            // 1. Recuperar dados do formulário
            if (dpDataContrato.getValue() != null) {
                // Conversão de LocalDate para java.util.Date
                contrato.setDataContrato(Date.from(dpDataContrato.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }

            // Trata a entrada de valores, aceitando ponto ou vírgula como separador decimal
            contrato.setValorCaucao(Float.parseFloat(txtValorCaucao.getText().trim().replace(',', '.')));
            contrato.setValorTotal(Float.parseFloat(txtValorTotalContrato.getText().trim().replace(',', '.')));

            contrato.setStatus(cmbStatusContrato.getSelectionModel().getSelectedItem());
            contrato.setCliente(cmbClienteContrato.getSelectionModel().getSelectedItem());

            // 2. Validação básica
            if (contrato.getCliente() == null || contrato.getStatus() == null || contrato.getDataContrato() == null) {
                new Alert(AlertType.WARNING, "Cliente, Status e Data do Contrato são obrigatórios.", ButtonType.OK).show();
                return;
            }

            // 3. Salvar ou Atualizar
            if (contrato.getId() == 0) {
                contratoLocacaoDAO.salvar(contrato);
                new Alert(AlertType.INFORMATION, "Contrato de Locação salvo com sucesso!", ButtonType.OK).show();
            } else {
                contratoLocacaoDAO.salvar(contrato);
                new Alert(AlertType.INFORMATION, "Contrato de Locação atualizado com sucesso!", ButtonType.OK).show();
            }

            handleLimparFormularioContrato();
            handleAtualizarListaContrato();

        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Erro de formato. Verifique se os campos de valor (Caução/Total) estão preenchidos corretamente (ex: 1500.50).", ButtonType.OK).show();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar/atualizar Contrato: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAtualizarListaContrato() {
        List<ContratoLocacao> lista = contratoLocacaoDAO.buscarTodos();
        tabelaContratos.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void handleLimparFormularioContrato() {
        contratoSelecionado = null;
        txtIdContrato.setText("Gerado Automaticamente");
        dpDataContrato.setValue(null);
        txtValorCaucao.setText("");
        cmbStatusContrato.getSelectionModel().clearSelection();
        txtValorTotalContrato.setText("");
        cmbClienteContrato.getSelectionModel().clearSelection();
        tabelaContratos.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarContrato() {
        ContratoLocacao contratoDeletar = tabelaContratos.getSelectionModel().getSelectedItem();
        if (contratoDeletar != null) {
            try {
                contratoLocacaoDAO.excluir(contratoDeletar);
                handleAtualizarListaContrato();
                handleLimparFormularioContrato();
                new Alert(AlertType.INFORMATION, "Contrato excluído com sucesso!", ButtonType.OK).show();
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Erro ao excluir Contrato: " + e.getMessage(), ButtonType.OK).show();
            }
        } else {
            new Alert(AlertType.WARNING, "Selecione um Contrato para deletar.", ButtonType.OK).show();
        }
    }*/

}