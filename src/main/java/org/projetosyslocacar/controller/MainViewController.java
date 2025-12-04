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
    private final ContratoLocacaoDAO contratoDAO = new ContratoLocacaoDAO(ContratoLocacao.class);
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

    /*
    // -----------------------------------------------------------------
    // 2. CLIENTE COMPONENTES (EXPANDIDO)
    // -----------------------------------------------------------------
    @FXML private TextField txtIdCliente;
    @FXML private TextField txtNomeCliente;
    @FXML private TextField txtCpfCliente;
    @FXML private TextField txtCnhCliente;
    @FXML private TextField txtRgCliente;
    @FXML private TextField txtEmailCliente;
    @FXML private TextField txtCepCliente;
    @FXML private TextField txtLogradouroCliente;
    @FXML private TextField txtNumeroCliente;
    // ... (Campos de Contato/Complemento podem ser adicionados se o FXML suportar)
    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Long> colIdCliente;
    @FXML private TableColumn<Cliente, String> colNomeCliente;
    @FXML private TableColumn<Cliente, String> colCpfCliente;
    @FXML private TableColumn<Cliente, String> colCnhCliente;
    @FXML private TableColumn<Cliente, String> colRgCliente;
    @FXML private TableColumn<Cliente, String> colEmailCliente;
    @FXML private TableColumn<Cliente, String> colLogradouroCliente; // Acessa Endereco
    private Cliente clienteSelecionado;

    // -----------------------------------------------------------------
    // 3. FUNCIONÁRIO COMPONENTES
    // -----------------------------------------------------------------
    @FXML private TextField txtIdFuncionario;
    @FXML private TextField txtNomeFuncionario;
    @FXML private TextField txtMatriculaFuncionario;
    @FXML private TextField txtCpfFuncionario;
    @FXML private TextField txtEmailFuncionario;
    @FXML private TextField txtCepFuncionario; // Endereço
    @FXML private TextField txtLogradouroFuncionario; // Endereço
    @FXML private TextField txtNumeroFuncionario; // Endereço
    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, Long> colIdFuncionario;
    @FXML private TableColumn<Funcionario, String> colNomeFuncionario;
    @FXML private TableColumn<Funcionario, String> colMatriculaFuncionario;
    @FXML private TableColumn<Funcionario, String> colCpfFuncionario;
    @FXML private TableColumn<Funcionario, String> colEmailFuncionario;
    private Funcionario funcionarioSelecionado;


    // -----------------------------------------------------------------
    // 4. USUÁRIO COMPONENTES
    // -----------------------------------------------------------------
    @FXML private TextField txtIdUsuario;
    @FXML private TextField txtNomeUsuario;
    @FXML private TextField txtCpfUsuario;
    @FXML private TextField txtLoginUsuario;
    @FXML private PasswordField pswSenhaUsuario;
    @FXML private TextField txtCepUsuario; // Endereço
    @FXML private TextField txtLogradouroUsuario; // Endereço
    @FXML private TextField txtNumeroUsuario; // Endereço
    // ComboBox para selecionar o Contato Principal (se for um objeto)
    // @FXML private ComboBox<Contato> cmbContatoPrincipalUsuario;
    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, Long> colIdUsuario;
    @FXML private TableColumn<Usuario, String> colNomeUsuario;
    @FXML private TableColumn<Usuario, String> colCpfUsuario;
    @FXML private TableColumn<Usuario, String> colLoginUsuario;
    private Usuario usuarioSelecionado;


    // -----------------------------------------------------------------
    // 5. CONTRATO DE LOCAÇÃO COMPONENTES
    // -----------------------------------------------------------------
    @FXML private TextField txtIdContrato;
    @FXML private DatePicker dtpDataContrato;
    @FXML private TextField txtValorCaucaoContrato;
    @FXML private ComboBox<ContratoLocacao.StatusLocacao> cmbStatusContrato; // Enum
    @FXML private TextField txtValorTotalContrato;
    @FXML private ComboBox<Cliente> cmbClienteContrato;
    @FXML private ComboBox<Usuario> cmbUsuarioCriadorContrato;
    @FXML private TableView<ContratoLocacao> tabelaContratos;
    @FXML private TableColumn<ContratoLocacao, Long> colIdContrato;
    @FXML private TableColumn<ContratoLocacao, Date> colDataContrato;
    @FXML private TableColumn<ContratoLocacao, String> colClienteContrato; // Acessa Cliente
    @FXML private TableColumn<ContratoLocacao, String> colStatusContrato;
    @FXML private TableColumn<ContratoLocacao, Float> colValorTotalContrato;
    private ContratoLocacao contratoSelecionado;


    // -----------------------------------------------------------------
    // 6. LOCAÇÃO COMPONENTES
    // -----------------------------------------------------------------
    @FXML private TextField txtIdLocacao;
    @FXML private DatePicker dtpDataRetiradaLocacao;
    @FXML private DatePicker dtpDataDevolucaoLocacao;
    @FXML private TextField txtValorLocacao;
    @FXML private ComboBox<ContratoLocacao> cmbContratoLocacao;
    @FXML private ComboBox<Veiculo> cmbVeiculoLocacao;
    @FXML private TableView<Locacao> tabelaLocacoes;
    @FXML private TableColumn<Locacao, Long> colIdLocacao;
    @FXML private TableColumn<Locacao, Date> colDataRetiradaLocacao;
    @FXML private TableColumn<Locacao, Date> colDataDevolucaoLocacao;
    @FXML private TableColumn<Locacao, String> colVeiculoLocacao; // Acessa Veiculo
    @FXML private TableColumn<Locacao, Long> colContratoLocacao; // Acessa Contrato ID
    private Locacao locacaoSelecionada;

    // -----------------------------------------------------------------
    // 7. PAGAMENTO COMPONENTES
    // -----------------------------------------------------------------
    @FXML private TextField txtIdPagamento;
    @FXML private ComboBox<Pagamento.TipoPagamento> cmbTipoPagamento; // Enum
    @FXML private TextField txtValorTotalPagamento;
    @FXML private ComboBox<ContratoLocacao> cmbContratoPagamento;
    @FXML private TableView<Pagamento> tabelaPagamentos;
    @FXML private TableColumn<Pagamento, Long> colIdPagamento;
    @FXML private TableColumn<Pagamento, Long> colContratoPagamento; // Acessa Contrato ID
    @FXML private TableColumn<Pagamento, String> colTipoPagamento;
    private Pagamento pagamentoSelecionado;


    // -----------------------------------------------------------------
    // 8. MANUTENÇÃO COMPONENTES
    // -----------------------------------------------------------------
    @FXML private TextField txtIdManutencao;
    @FXML private TextField txtDescricaoManutencao;
    @FXML private DatePicker dtpDataManutencao; // TIMESTAMP
    @FXML private TextField txtCustoManutencao;
    @FXML private ComboBox<Veiculo> cmbVeiculoManutencao;
    @FXML private TableView<Manutencao> tabelaManutencao;
    @FXML private TableColumn<Manutencao, Long> colIdManutencao;
    @FXML private TableColumn<Manutencao, String> colVeiculoManutencao; // Acessa Veiculo (Placa ou Modelo)
    @FXML private TableColumn<Manutencao, Date> colDataManutencao;
    @FXML private TableColumn<Manutencao, Float> colCustoManutencao;
    @FXML private TableColumn<Manutencao, String> colDescricaoManutencao;
    private Manutencao manutencaoSelecionada;


    // -----------------------------------------------------------------
    // 9. OCORRÊNCIA COMPONENTES
    // -----------------------------------------------------------------
    @FXML private TextField txtIdOcorrencia;
    @FXML private TextField txtDescricaoOcorrencia;
    @FXML private TextField txtValorOcorrencia;
    @FXML private ComboBox<Locacao> cmbLocacaoOcorrencia;
    @FXML private TableView<Ocorrencia> tabelaOcorrencias;
    @FXML private TableColumn<Ocorrencia, Long> colIdOcorrencia;
    @FXML private TableColumn<Ocorrencia, Long> colLocacaoOcorrencia; // Acessa Locação ID
    @FXML private TableColumn<Ocorrencia, String> colDescricaoOcorrencia;
    @FXML private TableColumn<Ocorrencia, Float> colValorOcorrencia;
    private Ocorrencia ocorrenciaSelecionada;

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
        }
    }

    private void mostrarContainer(VBox container, Button botao) {
        VBox[] containers = {containerModelos, containerMarcas, containerCategorias, containerVeiculos};
        Button[] botoes = {btnModelos, btnMarcas, btnCategorias, btnVeiculos};

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
}