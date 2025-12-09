package org.projetosyslocacar.controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import org.w3c.dom.Text;

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
    @FXML private Button btnEndereco;

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
    @FXML private VBox containerEndereco;
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
    @FXML private ComboBox<Endereco> cmbEnderecoCliente;
    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Long> colIdCliente;
    @FXML private TableColumn<Cliente, String> colNomeCliente;
    @FXML private TableColumn<Cliente, String> colCpfCliente;
    @FXML private TableColumn<Cliente, String> colEmailCliente;
    @FXML private TableColumn<Cliente, String> colCnhCliente;
    @FXML private TableColumn<Cliente, String> colRgCliente;
    private Cliente clienteSelecionado;


    // Funcionarios

    @FXML private TextField txtIdFuncionario;
    @FXML private TextField txtNomeFuncionario;
    @FXML private TextField txtMatriculaFuncionario;
    @FXML private TextField txtCpfFuncionario;
    @FXML private ComboBox<Endereco> cmbEnderecoFuncionario;
    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, String> colIdFuncionario;
    @FXML private TableColumn<Funcionario, String> colNomeFuncionario;
    @FXML private TableColumn<Funcionario, String> colMatriculaFuncionario;
    @FXML private TableColumn<Funcionario, String> colLogradouroFuncionario;
    private Funcionario funcionarioSelecionado;


    // USUARIOS

    @FXML private TextField txtIdUsuario;
    @FXML private TextField txtNomeUsuario;
    @FXML private TextField txtCpfUsuario;
    @FXML private TextField txtLoginUsuario;
    @FXML private TextField txtSenhaUsuario;
    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, String> colIdUsuario;
    @FXML private TableColumn<Usuario, String> colNomeUsuario;
    @FXML private TableColumn<Usuario, String> colLoginUsuario;
    @FXML private TableColumn<Usuario, String> colCpfUsuario;
    private Usuario usuarioSelecionado;

    // CONTRATOS

    @FXML private TextField txtIdContrato;
    @FXML private ComboBox<Cliente> cmbClienteContrato;
    @FXML private ComboBox<Usuario> cmbUsuarioCriadorContrato;
    @FXML private DatePicker dtpDataContrato;
    @FXML private TextField txtValorCaucaoContrato;
    @FXML private TextField txtValorTotalContrato;
    @FXML private ComboBox<ContratoLocacao.StatusLocacao> cmbStatusContrato;
    @FXML private TableView<ContratoLocacao> tabelaContratos;
    @FXML private TableColumn<ContratoLocacao,String> colIdContrato;
    @FXML private TableColumn<ContratoLocacao,String> colClienteContrato;
    @FXML private TableColumn<ContratoLocacao,String> colUsuarioCriadorContrato;
    @FXML private TableColumn<ContratoLocacao,String> colDataContrato;
    @FXML private TableColumn<ContratoLocacao,String> colStatusContrato;
    @FXML private TableColumn<ContratoLocacao,String> colValorTotalContrato;
    private ContratoLocacao contratoLocacaoSelecionado;


    // LOCACAO

    @FXML private TextField txtIdLocacao;
    @FXML private ComboBox<ContratoLocacao> cmbContratoLocacao;
    @FXML private ComboBox<Veiculo> cmbVeiculoLocacao;
    @FXML private TextField txtDataRetiradaLocacao; // Deveria ser DatePicker
    @FXML private TextField txtDataDevolucaoLocacao; // Deveria ser DatePicker
    @FXML private TextField txtValorLocacaoBase;
    @FXML private DatePicker dpDataContrato; // Este campo deve ser usado para exibição do Contrato
    @FXML private TextField txtValorCaucao; // Este campo deve ser usado para exibição do Contrato
    @FXML private TableView<Locacao> tabelaLocacoes;
    @FXML private TableColumn<Locacao, Long> colIdLocacao;
    @FXML private TableColumn<Locacao, Long> colContratoLocacao;
    @FXML private TableColumn<Locacao, String> colVeiculoLocacao;
    @FXML private TableColumn<Locacao, Date> colDataRetiradaLocacao;
    @FXML private TableColumn<Locacao, Date> colDataDevolucaoLocacao;
    private ObservableList<Locacao> listaLocacoes = FXCollections.observableArrayList();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    // PAGAMENTO
    @FXML private TextField txtIdPagamento;
    @FXML private ComboBox<ContratoLocacao> cmbContratoPagamento;
    @FXML private ComboBox<Pagamento.TipoPagamento> cmbTipoPagamento;
    @FXML private TextField txtValorTotalPagamento;
    @FXML private TableView<Pagamento> tabelaPagamentos;
    @FXML private TableColumn<Pagamento, Long> colIdPagamento;
    @FXML private TableColumn<Pagamento, String> colContratoPagamento;
    @FXML private TableColumn<Pagamento, Pagamento.TipoPagamento> colTipoPagamento;
    @FXML private TableColumn<Pagamento, Float> colValorPagamento;
    private ObservableList<Pagamento> listaPagamentos = FXCollections.observableArrayList();


    // MANUTENCAO

    @FXML private TextField txtIdManutencao;
    @FXML private ComboBox<Veiculo> cmbVeiculoManutencao;
    @FXML private TextField txtDataManutencao;
    @FXML private TextField txtCustoManutencao;
    @FXML private TextField txtDescricaoManutencao;
    @FXML private TableView<Manutencao> tabelaManutencoes;
    @FXML private TableColumn<Manutencao, Long> colIdManutencao;
    @FXML private TableColumn<Manutencao, String> colVeiculoManutencao; // Usaremos a placa ou ID
    @FXML private TableColumn<Manutencao, String> colDataManutencao;
    @FXML private TableColumn<Manutencao, Float> colCustoManutencao;
    @FXML private TableColumn<Manutencao, String> colDescricaoManutencao;

    //private GenericDAO<Manutencao, Long> manutencaosDAO = new GenericDAO<>(Manutencao.class);
    //private GenericDAO<Veiculo, Long> veiculosDAO = new GenericDAO<>(Veiculo.class);
    private ObservableList<Manutencao> listaManutencoes = FXCollections.observableArrayList();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    // OCORRENCIAS

    @FXML private TextField txtIdOcorrencia;
    @FXML private ComboBox<Locacao> cmbLocacaoOcorrencia;
    @FXML private TextField txtDescricaoOcorrencia;
    @FXML private TextField txtValorOcorrencia;
    @FXML private TableView<Ocorrencia> tabelaOcorrencias;
    @FXML private TableColumn<Ocorrencia, Long> colIdOcorrencia;
    @FXML private TableColumn<Ocorrencia, String> colLocacaoOcorrencia;
    @FXML private TableColumn<Ocorrencia, Float> colValorOcorrencia;
    @FXML private TableColumn<Ocorrencia, String> colDescricaoOcorrencia;

    // --- DAOs e Listas ---
    private OcorrenciaDAO ocorrenciasDAO = new OcorrenciaDAO(Ocorrencia.class);
    private LocacaoDAO locacaosDAO = new LocacaoDAO(Locacao.class); // Use o DAO específico
    private ObservableList<Ocorrencia> listaOcorrencias = FXCollections.observableArrayList();


    // ENDERECO

    //@FXML private VBox containerEndereco;
    @FXML private TextField txtIdEndereco;
    @FXML private TextField txtCepEndereco;
    @FXML private TextField txtLogradouroEndereco;
    @FXML private TextField txtComplementoEndereco;
    @FXML private TextField txtNumeroEndereco;
    @FXML private TextField txtReferenciaEndereco;
    @FXML private TableView<Endereco> tabelaEnderecos;
    @FXML private TableColumn<Endereco, Long> colIdEndereco;
    @FXML private TableColumn<Endereco, String> colCepEndereco;
    @FXML private TableColumn<Endereco, String> colLogradouroEndereco;
    @FXML private TableColumn<Endereco, String> colComplementoEndereco;
    @FXML private TableColumn<Endereco, String> colNumeroEndereco;
    @FXML private TableColumn<Endereco, String> colReferenciaEndereco;

    // --- DAO e Lista Endereço ---
    private EnderecoDAO enderecoDAO = new EnderecoDAO(Endereco.class);
    private ObservableList<Endereco> listaEnderecos = FXCollections.observableArrayList();


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
        inicializarFuncionarioCRUD();
        inicializarUsuarioCRUD();
        inicializarContratoCRUD();
        initializeLocacoes();
        initializePagamentos();
        initializeManutencao();
        initializeOcorrencias();
        initializeEndereco();

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
        } else if (botaoClicado == btnEndereco){
            mostrarContainer(containerEndereco, btnEndereco);
        }
    }

    private void mostrarContainer(VBox container, Button botao) {
        VBox[] containers = {containerModelos, containerMarcas, containerCategorias, containerVeiculos, containerClientes, containerContratos, containerFuncionarios, containerLocacoes, containerManutencao, containerOcorrencias, containerPagamentos, containerUsuarios, containerEndereco};
        Button[] botoes = {btnModelos, btnMarcas, btnCategorias, btnVeiculos, btnClientes, btnContratos, btnFuncionarios, btnLocacoes, btnManutencao, btnOcorrencias, btnPagamentos, btnUsuarios, btnEndereco};

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

            carregarComboBoxEnderecoCliente();
            handleAtualizarListaCliente(); // Carrega os dados iniciais
        }
    }

    private void mostrarDetalhesCliente(Cliente cliente) {
        clienteSelecionado = cliente;
        if (cliente != null) {
            txtIdCliente.setText(String.valueOf(cliente.getId()));
            txtNomeCliente.setText(cliente.getNome());
            txtCpfCliente.setText(cliente.getCpf());
            cmbEnderecoCliente.setValue(cliente.getEndereco());
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
            cliente.setEndereco(cmbEnderecoCliente.getSelectionModel().getSelectedItem());
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
        cmbEnderecoCliente.getSelectionModel().clearSelection();
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

    private void carregarComboBoxEnderecoCliente() {
        List<Endereco> listaEnderecos = enderecoDAO.buscarTodos();
        cmbEnderecoCliente.setItems(FXCollections.observableArrayList(listaEnderecos));

        cmbEnderecoCliente.setConverter(new StringConverter<Endereco>() {
            @Override
            public String toString(Endereco endereco) { return endereco == null ? "" : endereco.getCep(); }
            @Override
            public Endereco fromString(String string) { return null; }
        });
    }

    // FUNCIONARIO METODOS

    private void inicializarFuncionarioCRUD(){
        if (tabelaFuncionarios != null) {
            colIdFuncionario.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNomeFuncionario.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colMatriculaFuncionario.setCellValueFactory(new PropertyValueFactory<>("matricula"));
            colLogradouroFuncionario.setCellValueFactory(new PropertyValueFactory<>("Endereco"));

            // Listener para preencher o formulário ao selecionar um cliente
            tabelaFuncionarios.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> mostrarDetalhesFuncionario(newValue));

            carregarComboBoxEnderecoFuncionario();
            handleAtualizarListaFuncionario(); // Carrega os dados iniciais
        }
    }

    private void mostrarDetalhesFuncionario(Funcionario funcionario) {
        funcionarioSelecionado = funcionario;
        if (funcionario != null) {
            txtIdFuncionario.setText(String.valueOf(funcionario.getId()));
            txtNomeFuncionario.setText(funcionario.getNome());
            txtMatriculaFuncionario.setText(funcionario.getMatricula());
            txtCpfFuncionario.setText(funcionario.getCpf());
            cmbEnderecoFuncionario.setValue(funcionario.getEndereco());
            // Obs: Endereço e Contatos são objetos complexos e exigem lógica adicional.
        } else {
            handleLimparFormularioFuncionario();
        }
    }

    @FXML
    private void handleSalvarFuncionario() {
        try {
            Funcionario funcionario = (funcionarioSelecionado == null) ? new Funcionario() : funcionarioSelecionado;

            // 1. Recuperar dados do formulário
            funcionario.setNome(txtNomeFuncionario.getText());
            funcionario.setMatricula(txtMatriculaFuncionario.getText());
            funcionario.setCpf(txtCpfFuncionario.getText());
            funcionario.setEndereco(cmbEnderecoFuncionario.getSelectionModel().getSelectedItem());
            // 2. Validação básica
            if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty() ||
                    funcionario.getCpf() == null || funcionario.getCpf().trim().isEmpty()) {
                new Alert(AlertType.WARNING, "Nome e CPF são campos obrigatórios.", ButtonType.OK).show();
                return;
            }

            // 3. Salvar ou Atualizar
            if (funcionario.getId() == 0) {
                funcionarioDAO.salvar(funcionario);
                new Alert(AlertType.INFORMATION, "Funcionario salvo com sucesso!", ButtonType.OK).show();
            } else {
                funcionarioDAO.salvar(funcionario);
                new Alert(AlertType.INFORMATION, "Funcionario atualizado com sucesso!", ButtonType.OK).show();
            }

            handleLimparFormularioFuncionario();
            handleAtualizarListaFuncionario();

        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar/atualizar Funcionario: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAtualizarListaFuncionario() {
        List<Funcionario> lista = funcionarioDAO.buscarTodos();
        tabelaFuncionarios.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void handleLimparFormularioFuncionario() {
        funcionarioSelecionado = null;
        txtIdFuncionario.setText("Gerado Automaticamente");
        txtNomeFuncionario.setText("");
        txtCpfFuncionario.setText("");
        txtMatriculaFuncionario.setText("");
        cmbEnderecoFuncionario.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarFuncionario() {
        Funcionario funcionarioDeletar = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (funcionarioDeletar != null) {
            try {
                funcionarioDAO.excluir(funcionarioDeletar);
                handleAtualizarListaFuncionario();
                handleLimparFormularioFuncionario();
                new Alert(AlertType.INFORMATION, "Funcionario excluído com sucesso!", ButtonType.OK).show();
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Erro ao excluir Funcionario: " + e.getMessage(), ButtonType.OK).show();
            }
        } else {
            new Alert(AlertType.WARNING, "Selecione um Funcionario para deletar.", ButtonType.OK).show();
        }
    }

    private void carregarComboBoxEnderecoFuncionario() {
        List<Endereco> listaEnderecos = enderecoDAO.buscarTodos();
        cmbEnderecoFuncionario.setItems(FXCollections.observableArrayList(listaEnderecos));

        cmbEnderecoFuncionario.setConverter(new StringConverter<Endereco>() {
            @Override
            public String toString(Endereco endereco) { return endereco == null ? "" : endereco.getCep(); }
            @Override
            public Endereco fromString(String string) { return null; }
        });
    }

    // USUARIOS METODOS

    private void inicializarUsuarioCRUD(){
        if (tabelaUsuarios != null) {
            colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNomeUsuario.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colCpfUsuario.setCellValueFactory(new PropertyValueFactory<>("cpf"));
            colLoginUsuario.setCellValueFactory(new PropertyValueFactory<>("login"));

            // Listener para preencher o formulário ao selecionar um cliente
            tabelaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> mostrarDetalhesUsuario(newValue));

            handleAtualizarListaUsuario(); // Carrega os dados iniciais
        }
    }

    private void mostrarDetalhesUsuario(Usuario usuario) {
        usuarioSelecionado = usuario;
        if (usuario != null) {
            txtIdUsuario.setText(String.valueOf(usuario.getId()));
            txtNomeUsuario.setText(usuario.getNome());
            txtLoginUsuario.setText(usuario.getLogin());
            txtCpfUsuario.setText(usuario.getCpf());
            txtSenhaUsuario.setText(usuario.getSenha());
            // Obs: Endereço e Contatos são objetos complexos e exigem lógica adicional.
        } else {
            handleLimparFormularioUsuario();
        }
    }

    @FXML
    private void handleSalvarUsuario() {
        try {
            Usuario usuario = (usuarioSelecionado == null) ? new Usuario() : usuarioSelecionado;

            // 1. Recuperar dados do formulário
            usuario.setNome(txtNomeUsuario.getText());
            usuario.setLogin(txtLoginUsuario.getText());
            usuario.setCpf(txtCpfUsuario.getText());
            usuario.setSenha(txtSenhaUsuario.getText());
            //funcionario.setEndereco(txtLogradouroFuncionario.getText());

            // 2. Validação básica
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty() ||
                    usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
                new Alert(AlertType.WARNING, "Nome e CPF são campos obrigatórios.", ButtonType.OK).show();
                return;
            }

            // 3. Salvar ou Atualizar
            if (usuario.getId() == 0) {
                usuarioDAO.salvar(usuario);
                new Alert(AlertType.INFORMATION, "Usuario salvo com sucesso!", ButtonType.OK).show();
            } else {
                usuarioDAO.salvar(usuario);
                new Alert(AlertType.INFORMATION, "Usuario atualizado com sucesso!", ButtonType.OK).show();
            }

            handleLimparFormularioUsuario();
            handleAtualizarListaUsuario();

        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar/atualizar Usuario: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAtualizarListaUsuario() {
        List<Usuario> lista = usuarioDAO.buscarTodos();
        tabelaUsuarios.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void handleLimparFormularioUsuario() {
        usuarioSelecionado = null;
        txtIdUsuario.setText("Gerado Automaticamente");
        txtNomeUsuario.setText("");
        txtCpfUsuario.setText("");
        txtLoginUsuario.setText("");
        txtSenhaUsuario.setText("");
    }

    @FXML
    private void handleDeletarUsuario() {
        Usuario usuarioDeletar = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioDeletar != null) {
            try {
                usuarioDAO.excluir(usuarioDeletar);
                handleAtualizarListaUsuario();
                handleLimparFormularioUsuario();
                new Alert(AlertType.INFORMATION, "Usuario excluído com sucesso!", ButtonType.OK).show();
            } catch (Exception e) {
                new Alert(AlertType.ERROR, "Erro ao excluir Usuario: " + e.getMessage(), ButtonType.OK).show();
            }
        } else {
            new Alert(AlertType.WARNING, "Selecione um Usuario para deletar.", ButtonType.OK).show();
        }
    }


    // CONTRATO METODOS

    private void inicializarContratoCRUD(){
        if (tabelaUsuarios != null) {
            colIdContrato.setCellValueFactory(new PropertyValueFactory<>("id"));
            colClienteContrato.setCellValueFactory(new PropertyValueFactory<>("cliente"));
            colUsuarioCriadorContrato.setCellValueFactory(new PropertyValueFactory<>("usuarioCriador"));
            colDataContrato.setCellValueFactory(new PropertyValueFactory<>("DataContrato"));
            colStatusContrato.setCellValueFactory(new PropertyValueFactory<>("status"));
            colValorTotalContrato.setCellValueFactory(new PropertyValueFactory<>("ValorTotal"));

            // Listener para preencher o formulário ao selecionar um cliente
            tabelaContratos.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> mostrarDetalhesContrato(newValue));
            configurarComboBoxClienteContrato();
            configurarComboBoxUsuarioContrato();
            carregarComboBoxStatusContrato();
            handleAtualizarListaContrato(); // Carrega os dados iniciais
        }
    }

    private void mostrarDetalhesContrato(ContratoLocacao contratoLocacao) {
        contratoLocacaoSelecionado = contratoLocacao;
        if (contratoLocacao != null) {
            txtIdContrato.setText(String.valueOf(contratoLocacao.getId()));
            cmbClienteContrato.setValue(contratoLocacao.getCliente());
            cmbUsuarioCriadorContrato.setValue(contratoLocacao.getUsuarioCriador());
            java.sql.Date dataDoContrato = (java.sql.Date) contratoLocacao.getDataContrato();
            LocalDate dataContrato = dataDoContrato.toLocalDate();
            dtpDataContrato.setValue(dataContrato);
            txtValorCaucaoContrato.setText(String.valueOf(contratoLocacao.getValorCaucao()));
            txtValorTotalContrato.setText(String.valueOf(contratoLocacao.getValorTotal()));
            cmbStatusContrato.setValue(contratoLocacao.getStatus());
            // Obs: Endereço e Contatos são objetos complexos e exigem lógica adicional.
        } else {
            handleLimparFormularioContrato();
        }
    }

    @FXML
    private void handleSalvarContrato() {
        try {
            ContratoLocacao contratoLocacao = (contratoLocacaoSelecionado == null) ? new ContratoLocacao() : contratoLocacaoSelecionado;

            // 1. Recuperar dados do formulário
            contratoLocacao.setCliente(cmbClienteContrato.getSelectionModel().getSelectedItem());
            contratoLocacao.setUsuarioCriador(cmbUsuarioCriadorContrato.getSelectionModel().getSelectedItem());
            String dateString = dtpDataContrato.getValue().toString(); // e.g., "2025-12-11"
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // Incorrect format
            Date dataContrato = format.parse(dateString);
            contratoLocacao.setDataContrato(dataContrato);
            contratoLocacao.setValorCaucao(Float.parseFloat(txtValorCaucaoContrato.getText()));
            contratoLocacao.setValorTotal(Float.parseFloat(txtValorTotalContrato.getText()));
            contratoLocacao.setStatus(cmbStatusContrato.getSelectionModel().getSelectedItem());


            // 2. Validação básica
            if (contratoLocacao.getCliente() == null  ||
                    contratoLocacao.getUsuarioCriador() == null ) {
                new Alert(AlertType.WARNING, "Usuario e Cliente são campos obrigatórios.", ButtonType.OK).show();
                return;
            }

            // 3. Salvar ou Atualizar
            if (contratoLocacao.getId() == 0) {
                contratoLocacaoDAO.salvar(contratoLocacao);
                new Alert(AlertType.INFORMATION, "Contrato salvo com sucesso!", ButtonType.OK).show();
            } else {
                contratoLocacaoDAO.salvar(contratoLocacao);
                new Alert(AlertType.INFORMATION, "Contrato atualizado com sucesso!", ButtonType.OK).show();
            }

            handleLimparFormularioContrato();
            handleAtualizarListaContrato();

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
        contratoLocacaoSelecionado = null;
        txtIdContrato.setText("Gerado Automaticamente");
        cmbClienteContrato.getSelectionModel().clearSelection();
        cmbUsuarioCriadorContrato.getSelectionModel().clearSelection();
        dtpDataContrato.setValue(null);
        txtValorCaucaoContrato.setText("");
        txtValorTotalContrato.setText("");
        cmbStatusContrato.getSelectionModel().clearSelection();
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
    }

    private void configurarComboBoxClienteContrato() {
        cmbClienteContrato.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) { return cliente == null ? "" : cliente.getNome(); }
            @Override
            public Cliente fromString(String string) { return null; }
        });
    }

    private void configurarComboBoxUsuarioContrato() {
        cmbUsuarioCriadorContrato.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario usuario) { return usuario == null ? "" : usuario.getNome(); }
            @Override
            public Usuario fromString(String string) { return null; }
        });
    }

    private void carregarComboBoxStatusContrato() {
        List<Cliente> listaClientes = clienteDAO.buscarTodos();
        cmbClienteContrato.setItems(FXCollections.observableArrayList(listaClientes));

        List<Usuario> listaUsuarios = usuarioDAO.buscarTodos();
        cmbUsuarioCriadorContrato.setItems(FXCollections.observableArrayList(listaUsuarios));

        cmbStatusContrato.setItems(FXCollections.observableArrayList(ContratoLocacao.StatusLocacao.values()));
    }

    // Locacoes Metodos

    @FXML
    public void initializeLocacoes() {
        configurarTabela();
        carregarComboboxes();
        handleAtualizarListaLocacao(null);

        tabelaLocacoes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> preencherFormulario(newValue));
    }

    private void configurarTabela() {
        /*colIdLocacao.setCellValueFactory(new PropertyValueFactory<>("id"));

        if(!tabelaLocacoes.getItems().isEmpty()) {
            colIdLocacao.setCellValueFactory(new PropertyValueFactory<>("id"));

            // Exibe o ID do Contrato
            colContratoLocacao.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleObjectProperty<>(
                            cellData.getValue().getContratoLocacao() != null ?
                                    cellData.getValue().getContratoLocacao().getId() : null));

            // Exibe a Placa do Veículo
            colVeiculoLocacao.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getVeiculo() != null ?
                                    cellData.getValue().getVeiculo().getPlaca() : "N/A"));

            colDataRetiradaLocacao.setCellValueFactory(new PropertyValueFactory<>("dataRetirada"));
            colDataDevolucaoLocacao.setCellValueFactory(new PropertyValueFactory<>("dataDevolucao"));

            tabelaLocacoes.setItems(listaLocacoes);
        }*/
        // Removido o 'if' que impedia a configuração na inicialização.

        // 1. Configuração de Propriedades Diretas
        colIdLocacao.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDataRetiradaLocacao.setCellValueFactory(new PropertyValueFactory<>("dataRetirada"));
        colDataDevolucaoLocacao.setCellValueFactory(new PropertyValueFactory<>("dataDevolucao"));

        // 2. Configuração de Propriedades Aninhadas (com verificação de null)

        // Exibe o ID do Contrato
        colContratoLocacao.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        cellData.getValue().getContratoLocacao() != null ?
                                cellData.getValue().getContratoLocacao().getId() : null));

        // Exibe a Placa do Veículo
        colVeiculoLocacao.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getVeiculo() != null ?
                                cellData.getValue().getVeiculo().getPlaca() : "N/A"));

        // 3. Vinculação da Lista (só deve ser executada se for a primeira vez)
        // Se esta função for chamada apenas uma vez na inicialização, esta linha está OK.
        // Se esta função for chamada repetidamente, esta linha é redundante.
        tabelaLocacoes.setItems(listaLocacoes);
    }

    private void carregarComboboxes() {
        try {
            // Buscando dados reais (ou mockados se o banco estiver vazio)
            List<ContratoLocacao> contratoLocacaos = contratoLocacaoDAO.buscarTodos();
            cmbContratoLocacao.setItems(FXCollections.observableArrayList(contratoLocacaos));

            List<Veiculo> veiculos = veiculoDAO.buscarTodos();
            cmbVeiculoLocacao.setItems(FXCollections.observableArrayList(veiculos));

            cmbContratoLocacao.setConverter(new StringConverter<ContratoLocacao>() {
                @Override
                public String toString(ContratoLocacao object) {
                    return object != null ? "Contrato #" + object.getId() : "";
                }
                @Override
                public ContratoLocacao fromString(String string) { return null; }
            });

            cmbVeiculoLocacao.setConverter(new StringConverter<Veiculo>() {
                @Override
                public String toString(Veiculo object) {
                    String modeloNome = (object.getModelo() != null) ? object.getModelo().getNome() : "Modelo N/A";

                    // Retorna uma string que o usuário entenda (Ex: PLACA - Modelo)
                    return object.getPlaca() + " - " + modeloNome;
                }
                @Override
                public Veiculo fromString(String string) { return null; }
            });
        } catch (Exception e) {
            exibirAlerta("Erro de Conexão/Dados", "Não foi possível carregar Contratos e Veículos: " + e.getMessage(), Alert.AlertType.ERROR);
            // Em produção, você trataria melhor a falha no carregamento.
        }
    }

    private void preencherFormulario(Locacao locacao) {
        // 1. Limpa o formulário primeiro para evitar dados residuais
        handleLimparFormularioLocacao(null);

        if (locacao != null) {

            // --- Preenchimento da Locação ---
            txtIdLocacao.setText(String.valueOf(locacao.getId()));

            // Seleção de ComboBox: O ContratoLocacao e o Veiculo DEVEM ser EAGER FETCH ou inicializados (Hibernate.initialize())
            // antes de você passar a 'locacao' para este método, para evitar LazyInitializationException.
            cmbContratoLocacao.getSelectionModel().select(locacao.getContratoLocacao());
            cmbVeiculoLocacao.getSelectionModel().select(locacao.getVeiculo());

            txtDataRetiradaLocacao.setText(locacao.getDataRetirada() != null ? dateFormat.format(locacao.getDataRetirada()) : "");
            txtDataDevolucaoLocacao.setText(locacao.getDataDevolucao() != null ? dateFormat.format(locacao.getDataDevolucao()) : "");

            txtValorLocacaoBase.setText(String.valueOf(locacao.getValorLocacao()));

            // --- Preenchimento do Contrato ---
            if (locacao.getContratoLocacao() != null) {

                ContratoLocacao contrato = locacao.getContratoLocacao();

                // ⚠️ CORREÇÃO: Conversão de data segura para evitar UnsupportedOperationException
                if (contrato.getDataContrato() instanceof java.sql.Date) {
                    java.sql.Date dataDoContratoSql = (java.sql.Date) contrato.getDataContrato();

                    // Conversão correta de java.sql.Date para java.time.LocalDate
                    LocalDate dataContrato = dataDoContratoSql.toLocalDate();

                    dpDataContrato.setValue(dataContrato);
                }
                // Adicionado um ELSE caso 'dataContrato' não seja do tipo java.sql.Date (ex: java.util.Date)
                else if (contrato.getDataContrato() instanceof java.util.Date) {
                    java.util.Date utilDate = (java.util.Date) contrato.getDataContrato();
                    LocalDate dataContrato = utilDate.toInstant()
                            .atZone(ZoneId.systemDefault()) // Necessário importar java.time.ZoneId
                            .toLocalDate();
                    dpDataContrato.setValue(dataContrato);
                }


                txtValorCaucao.setText(String.valueOf(contrato.getValorCaucao()));
            }
        }
    }

    @FXML
    private void handleLimparFormularioLocacao(ActionEvent event) {
        txtIdLocacao.clear();
        cmbContratoLocacao.getSelectionModel().clearSelection();
        cmbVeiculoLocacao.getSelectionModel().clearSelection();
        txtDataRetiradaLocacao.clear();
        txtDataDevolucaoLocacao.clear();
        txtValorLocacaoBase.clear();
        dpDataContrato.setValue(null);
        txtValorCaucao.clear();
        tabelaLocacoes.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSalvarLocacao(ActionEvent event) {
        try {
            if (cmbContratoLocacao.getValue() == null || cmbVeiculoLocacao.getValue() == null || txtValorLocacaoBase.getText().isEmpty()) {
                exibirAlerta("Erro de Validação", "Preencha Contrato, Veículo e Valor Base.", Alert.AlertType.WARNING);
                return;
            }

            Locacao locacao;
            if (!txtIdLocacao.getText().isEmpty()) {
                long id = Long.parseLong(txtIdLocacao.getText());
                // Busca a entidade no banco para garantir que ela esteja "managed"
                locacao = locacaoDAO.buscarPorId(id);
                if (locacao == null) throw new RuntimeException("Locação não encontrada.");
            } else {
                locacao = new Locacao();
            }

            locacao.setContratoLocacao(cmbContratoLocacao.getValue());
            locacao.setVeiculo(cmbVeiculoLocacao.getValue());
            locacao.setDataRetirada(converterStringParaDate(txtDataRetiradaLocacao.getText()));
            locacao.setDataDevolucao(converterStringParaDate(txtDataDevolucaoLocacao.getText()));
            locacao.setValorLocacao(Float.parseFloat(txtValorLocacaoBase.getText().replace(",", ".")));

            locacaoDAO.salvar(locacao); // Usa o GenericDAO

            exibirAlerta("Sucesso", "Locação salva/atualizada com sucesso!", Alert.AlertType.INFORMATION);
            handleLimparFormularioLocacao(null);
            handleAtualizarListaLocacao(null);

        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Formato", "Valor da locação inválido ou ID incorreto.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            exibirAlerta("Erro", "Erro ao salvar Locação: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeletarLocacao(ActionEvent event) {
        Locacao selecionado = tabelaLocacoes.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                    "Tem certeza que deseja deletar a Locação ID: " + selecionado.getId() + "?",
                    ButtonType.YES, ButtonType.NO).showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                try {
                    locacaoDAO.excluir(selecionado); // Usa o GenericDAO
                    exibirAlerta("Sucesso", "Locação deletada com sucesso!", Alert.AlertType.INFORMATION);
                    handleLimparFormularioLocacao(null);
                    handleAtualizarListaLocacao(null);
                } catch (Exception e) {
                    exibirAlerta("Erro de Exclusão", "Erro ao deletar Locação: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            exibirAlerta("Seleção", "Selecione uma Locação na tabela para deletar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleAtualizarListaLocacao(ActionEvent event) {
        try {
            listaLocacoes.clear();
            // MUDAR AQUI:
            //listaLocacoes.addAll(locacaoDAO.buscarTodosComVeiculo()); // Usa o novo método
            listaLocacoes.addAll(locacaoDAO.buscarTodosComRelacionamentos());
            tabelaLocacoes.refresh();
        } catch (Exception e) {
            exibirAlerta("Erro", "Erro ao carregar lista de Locações: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // --- Métodos Auxiliares ---
    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private Date converterStringParaDate(String dataString) {
        if (dataString == null || dataString.trim().isEmpty()) {
            return null;
        }
        try {
            return dateFormat.parse(dataString);
        } catch (java.text.ParseException e) {
            exibirAlerta("Erro de Data", "Formato de data inválido. Use DD/MM/AAAA.", Alert.AlertType.ERROR);
            return null;
        }
    }

    // PAGAMENTO

    private void initializePagamentos() {

        // 1. Configurar Colunas
        colIdPagamento.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipoPagamento.setCellValueFactory(new PropertyValueFactory<>("tipoPagamento"));
        colValorPagamento.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        // Coluna Contrato (Exibe o ID do Contrato)
        colContratoPagamento.setCellValueFactory(cellData -> {
            ContratoLocacao contrato = cellData.getValue().getContratoLocacao();
            // Acessa o ID do Contrato
            return new SimpleStringProperty(String.valueOf(contrato.getId()));
        });

        // 2. Preencher ComboBoxes
        cmbTipoPagamento.getItems().addAll(Pagamento.TipoPagamento.values()); // Enum

        carregarContratosParaPagamento();

        // 3. Listener para seleção na Tabela
        tabelaPagamentos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        preencherFormularioPagamento(newSelection);
                    }
                });

        // 4. Carregar dados iniciais
        handleAtualizarListaPagamento();
    }

    // --- Métodos de Apoio ---

    private void carregarContratosParaPagamento() {
        try {
            // Requer que o ContratoLocacaoDAO tenha um método buscarTodos() que retorna List<ContratoLocacao>
            List<ContratoLocacao> contratos = contratoLocacaoDAO.buscarTodos();
            cmbContratoPagamento.getItems().clear();
            cmbContratoPagamento.getItems().addAll(contratos);
        } catch (Exception e) {
            exibirAlertaErro("Erro ao carregar Contratos", "Não foi possível carregar a lista de Contratos de Locação: " + e.getMessage());
        }
    }

    private void preencherFormularioPagamento(Pagamento pagamento) {
        txtIdPagamento.setText(String.valueOf(pagamento.getId()));
        txtValorTotalPagamento.setText(String.valueOf(pagamento.getValorTotal()));
        cmbTipoPagamento.setValue(pagamento.getTipoPagamento());

        // Selecionar o Contrato no ComboBox:
        // Isso pressupõe que o objeto ContratoLocacao no Pagamento tem o mesmo ID do objeto na lista do ComboBox.
        // Se a correção do Lombok (EqualsAndHashCode.Exclude) foi feita, isso funciona.
        cmbContratoPagamento.getSelectionModel().select(pagamento.getContratoLocacao());
    }

    // --- Métodos de Ação do FXML ---

    @FXML
    private void handleAtualizarListaPagamento() {
        try {
            // Usa o GenericDAO.buscarTodos() para carregar a lista
            List<Pagamento> pagamentos = pagamentoDAO.buscarTodos();
            listaPagamentos.setAll(pagamentos);
            tabelaPagamentos.setItems(listaPagamentos);
        } catch (Exception e) {
            exibirAlertaErro("Erro de Atualização", "Não foi possível carregar a lista de pagamentos: " + e.getMessage());
        }
    }

    @FXML
    private void handleSalvarPagamento() {
        try {
            Pagamento pagamento;
            long id = txtIdPagamento.getText().isEmpty() ? 0 : Long.parseLong(txtIdPagamento.getText());

            if (id == 0) {
                // Novo Pagamento
                pagamento = new Pagamento();
            } else {
                // Atualizar Pagamento (opcionalmente buscar para garantir que não é um objeto detached)
                pagamento = tabelaPagamentos.getSelectionModel().getSelectedItem();
                if (pagamento == null) {
                    pagamento = pagamentoDAO.buscarPorId(id); // Busca no banco
                }
            }

            // Preenche os dados
            pagamento.setContratoLocacao(cmbContratoPagamento.getSelectionModel().getSelectedItem());
            pagamento.setTipoPagamento(cmbTipoPagamento.getSelectionModel().getSelectedItem());
            pagamento.setValorTotal(Float.parseFloat(txtValorTotalPagamento.getText().replace(",", ".")));

            // Salva e atualiza a lista
            pagamentoDAO.salvar(pagamento);
            handleAtualizarListaPagamento();
            handleLimparFormularioPagamento();

            exibirAlertaSucesso("Sucesso", "Pagamento salvo com sucesso!");

        } catch (NumberFormatException e) {
            exibirAlertaErro("Erro de Formato", "O valor total deve ser um número válido.");
        } catch (Exception e) {
            exibirAlertaErro("Erro ao Salvar", "Não foi possível salvar o pagamento: " + e.getMessage());
        }
    }

    @FXML
    private void handleLimparFormularioPagamento() {
        txtIdPagamento.setText("");
        txtValorTotalPagamento.setText("");
        cmbTipoPagamento.getSelectionModel().clearSelection();
        cmbContratoPagamento.getSelectionModel().clearSelection();
        tabelaPagamentos.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarPagamento() {
        Pagamento selecionado = tabelaPagamentos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                pagamentoDAO.excluir(selecionado); // Usa o método excluir do GenericDAO
                handleAtualizarListaPagamento();
                handleLimparFormularioPagamento();
                exibirAlertaSucesso("Sucesso", "Pagamento excluído.");
            } catch (Exception e) {
                exibirAlertaErro("Erro ao Excluir", "Não foi possível excluir o pagamento: " + e.getMessage());
            }
        } else {
            exibirAlertaErro("Seleção Inválida", "Por favor, selecione um pagamento na tabela para deletar.");
        }
    }

    // Adicione um método auxiliar para exibir alertas
    private void exibirAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirAlertaSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // MainViewController.java (Trecho)

    private void initializeManutencao() {

        // 1. Configurar Colunas
        colIdManutencao.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescricaoManutencao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colCustoManutencao.setCellValueFactory(new PropertyValueFactory<>("custo"));

        // Coluna Data: Formatação customizada (Date para String)
        colDataManutencao.setCellValueFactory(cellData -> {
            Date data = cellData.getValue().getData();
            if (data == null) return new SimpleStringProperty("");
            // Converte Date para LocalDate, formata para String
            LocalDate localDate = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return new SimpleStringProperty(dateFormatter.format(localDate));
        });

        // Coluna Veículo: Exibir o ID/representação do Veículo (usaremos ID por segurança)
        colVeiculoManutencao.setCellValueFactory(cellData -> {
            Veiculo veiculo = cellData.getValue().getVeiculo(); //
            // Se o Veículo tiver um campo "placa", use-o. Ex: veiculo.getPlaca()
            return new SimpleStringProperty(veiculo != null ? String.valueOf(veiculo.getId()) : "N/A");
        });

        // 2. Carregar ComboBox (Lista de Veículos)
        carregarVeiculosParaManutencao();

        // 3. Listener para seleção na Tabela
        tabelaManutencoes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        preencherFormularioManutencao(newSelection);
                    }
                });

        // 4. Carregar dados iniciais
        handleAtualizarListaManutencao();
    }

    private void carregarVeiculosParaManutencao() {
        try {
            // Assume que VeiculoDAO.buscarTodos() retorna List<Veiculo>
            List<Veiculo> veiculos = veiculoDAO.buscarTodos();
            cmbVeiculoManutencao.getItems().clear();
            cmbVeiculoManutencao.setItems(FXCollections.observableArrayList(veiculos));
            cmbVeiculoManutencao.setConverter(new StringConverter<Veiculo>() {
                @Override
                public String toString(Veiculo veiculo) {
                    if (veiculo == null) { // ⬅️ CORREÇÃO CRÍTICA
                        return null; // ou "" ou "Selecione um Veículo"
                    }
                    // Assumindo que você tem os métodos getModelo() e getPlaca()
                    // e que getModelo() retorna um objeto que tem o método getNome().
                    return veiculo.getPlaca() + " - " + veiculo.getModelo().getNome();
                }

                @Override
                public Veiculo fromString(String string) {
                    return null; // Geralmente não é necessário para ComboBoxes de entidade
                }
            });
        } catch (Exception e) {
            exibirAlertaErro("Erro de Carregamento", "Não foi possível carregar a lista de Veículos: " + e.getMessage());
        }
    }

    // MainViewController.java (Trecho)

    private void preencherFormularioManutencao(Manutencao manutencao) {
        txtIdManutencao.setText(String.valueOf(manutencao.getId()));
        txtDescricaoManutencao.setText(manutencao.getDescricao());
        txtCustoManutencao.setText(String.format("%.2f", manutencao.getCusto()).replace(",", "."));

        // Data (Date para String)
        if (manutencao.getData() != null) {
            LocalDate localDate = manutencao.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            txtDataManutencao.setText(dateFormatter.format(localDate));
        } else {
            txtDataManutencao.setText("");
        }

        // Selecionar Veículo (exige a correção @EqualsAndHashCode.Exclude no Veiculo)
        cmbVeiculoManutencao.getSelectionModel().select(manutencao.getVeiculo());
    }

    @FXML
    private void handleAtualizarListaManutencao() {
        try {
            // Se você criou um DAO customizado para JOIN FETCH:
            // List<Manutencao> manutencoes = ((ManutencaoDAO)manutencaoDAO).buscarTodosComVeiculo();

            // Usando o GenericDAO simples (pode causar LazyException se Veiculo for acessado):
            List<Manutencao> manutencoes = manutencaoDAO.buscarTodos();

            listaManutencoes.setAll(manutencoes);
            tabelaManutencoes.setItems(listaManutencoes);
        } catch (Exception e) {
            exibirAlertaErro("Erro de Atualização", "Não foi possível carregar a lista de manutenções: " + e.getMessage());
        }
    }

    @FXML
    private void handleSalvarManutencao() {
        try {
            Manutencao manutencao;
            long id = txtIdManutencao.getText().isEmpty() ? 0 : Long.parseLong(txtIdManutencao.getText());

            if (id == 0) {
                manutencao = new Manutencao();
            } else {
                manutencao = tabelaManutencoes.getSelectionModel().getSelectedItem();
                if (manutencao == null) {
                    manutencao = manutencaoDAO.buscarPorId(id);
                }
            }

            // Conversão de Data e Custo
            LocalDate localDate = LocalDate.parse(txtDataManutencao.getText(), dateFormatter);
            Date data = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            float custo = Float.parseFloat(txtCustoManutencao.getText().replace(",", "."));

            // Preenche os dados
            manutencao.setDescricao(txtDescricaoManutencao.getText());
            manutencao.setCusto(custo);
            manutencao.setData(data);
            manutencao.setVeiculo(cmbVeiculoManutencao.getSelectionModel().getSelectedItem()); //

            // Salva
            manutencaoDAO.salvar(manutencao); // Usa o GenericDAO
            handleAtualizarListaManutencao();
            handleLimparFormularioManutencao();

            exibirAlertaSucesso("Sucesso", "Manutenção salva com sucesso!");

        } catch (java.time.format.DateTimeParseException e) {
            exibirAlertaErro("Erro de Data", "A data deve estar no formato DD/MM/AAAA.");
        } catch (NumberFormatException e) {
            exibirAlertaErro("Erro de Formato", "O custo deve ser um número válido (Ex: 500.00).");
        } catch (Exception e) {
            exibirAlertaErro("Erro ao Salvar", "Não foi possível salvar a manutenção: " + e.getMessage());
        }
    }

    @FXML
    private void handleLimparFormularioManutencao() {
        txtIdManutencao.setText("");
        txtDescricaoManutencao.setText("");
        txtCustoManutencao.setText("");
        txtDataManutencao.setText("");
        cmbVeiculoManutencao.getSelectionModel().clearSelection();
        tabelaManutencoes.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarManutencao() {
        Manutencao selecionado = tabelaManutencoes.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                manutencaoDAO.excluir(selecionado); // Usa o GenericDAO
                handleAtualizarListaManutencao();
                handleLimparFormularioManutencao();
                exibirAlertaSucesso("Sucesso", "Manutenção excluída.");
            } catch (Exception e) {
                exibirAlertaErro("Erro ao Excluir", "Não foi possível excluir a manutenção: " + e.getMessage());
            }
        } else {
            exibirAlertaErro("Seleção Inválida", "Por favor, selecione uma manutenção na tabela para deletar.");
        }
    }

    private void initializeOcorrencias() {

        // 1. Configurar Colunas
        colIdOcorrencia.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescricaoOcorrencia.setCellValueFactory(new PropertyValueFactory<>("descricao")); //
        colValorOcorrencia.setCellValueFactory(new PropertyValueFactory<>("valor")); //

        // Coluna Locação: Exibir o ID da Locação
        colLocacaoOcorrencia.setCellValueFactory(cellData -> {
            Locacao locacao = cellData.getValue().getLocacao();
            // O JOIN FETCH garante que 'locacao' não seja um proxy sem sessão
            return new SimpleStringProperty(locacao != null ? String.valueOf(locacao.getId()) : "N/A");
        });

        // 2. Configurar o ComboBox para exibição e prevenção de NullPointerException
        configurarCmbLocacaoOcorrencia();
        carregarLocacoesParaOcorrencia();

        // 3. Listener para seleção na Tabela
        tabelaOcorrencias.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        preencherFormularioOcorrencia(newSelection);
                    }
                });

        // 4. Carregar dados iniciais (usando o método seguro)
        handleAtualizarListaOcorrencia();
    }

    private void configurarCmbLocacaoOcorrencia() {
        // Implementação do StringConverter para exibir Locação (ID + Datas) e evitar NullPointer
        cmbLocacaoOcorrencia.setConverter(new StringConverter<Locacao>() {
            @Override
            public String toString(Locacao locacao) {
                if (locacao == null) { // ⬅️ CORREÇÃO DE NULL POINTER
                    return null;
                }
                // Exibe o ID da Locação e as datas
                // Use o formato de data desejado (Datas estão como Date, ajuste a conversão se necessário)
                return "Locação ID: " + locacao.getId();
                // Se Locacao.toString() não tentar inicializar um campo LAZY, ele é seguro.
            }

            @Override
            public Locacao fromString(String string) {
                return null;
            }
        });
    }

    private void carregarLocacoesParaOcorrencia() {
        try {
            // Se Locacao tiver relacionamentos LAZY acessados no toString(), você precisará de JOIN FETCH aqui também.
            List<Locacao> locacoes = locacaoDAO.buscarTodos();
            cmbLocacaoOcorrencia.getItems().clear();
            cmbLocacaoOcorrencia.getItems().addAll(locacoes);
        } catch (Exception e) {
            exibirAlertaErro("Erro de Carregamento", "Não foi possível carregar a lista de Locações: " + e.getMessage());
        }
    }

    private void preencherFormularioOcorrencia(Ocorrencia ocorrencia) {
        txtIdOcorrencia.setText(String.valueOf(ocorrencia.getId()));
        txtDescricaoOcorrencia.setText(ocorrencia.getDescricao());
        txtValorOcorrencia.setText(String.format("%.2f", ocorrencia.getValor()).replace(",", "."));

        // Selecionar Locação
        cmbLocacaoOcorrencia.getSelectionModel().select(ocorrencia.getLocacao());
    }

    @FXML
    private void handleAtualizarListaOcorrencia() {
        try {
            // 🚨 Usa o método seguro JOIN FETCH
            List<Ocorrencia> ocorrencias = ocorrenciaDAO.buscarTodosComLocacao();

            listaOcorrencias.setAll(ocorrencias);
            tabelaOcorrencias.setItems(listaOcorrencias);
        } catch (Exception e) {
            exibirAlertaErro("Erro de Atualização", "Não foi possível carregar a lista de ocorrências: " + e.getMessage());
        }
    }

    @FXML
    private void handleSalvarOcorrencia() {
        try {
            Ocorrencia ocorrencia;
            long id = txtIdOcorrencia.getText().isEmpty() ? 0 : Long.parseLong(txtIdOcorrencia.getText());

            if (id == 0) {
                ocorrencia = new Ocorrencia();
            } else {
                ocorrencia = tabelaOcorrencias.getSelectionModel().getSelectedItem();
                if (ocorrencia == null) {
                    // Busca por ID apenas se não houver seleção na tabela (caso de nova criação com ID preenchido)
                    ocorrencia = ocorrenciaDAO.buscarPorId(id);
                }
            }

            // Validação básica
            if (cmbLocacaoOcorrencia.getSelectionModel().isEmpty()) {
                exibirAlertaErro("Erro de Validação", "Selecione uma Locação.");
                return;
            }

            // Conversão de Valor
            float valor = Float.parseFloat(txtValorOcorrencia.getText().replace(",", "."));

            // Preenche os dados
            ocorrencia.setDescricao(txtDescricaoOcorrencia.getText());
            ocorrencia.setValor(valor);
            ocorrencia.setLocacao(cmbLocacaoOcorrencia.getSelectionModel().getSelectedItem());

            // Salva
            ocorrenciaDAO.salvar(ocorrencia);
            handleAtualizarListaOcorrencia();
            handleLimparFormularioOcorrencia();

            exibirAlertaSucesso("Sucesso", "Ocorrência salva com sucesso!");

        } catch (NumberFormatException e) {
            exibirAlertaErro("Erro de Formato", "O valor deve ser um número válido (Ex: 100.00).");
        } catch (Exception e) {
            exibirAlertaErro("Erro ao Salvar", "Não foi possível salvar a ocorrência: " + e.getMessage());
        }
    }

    @FXML
    private void handleLimparFormularioOcorrencia() {
        txtIdOcorrencia.setText("");
        txtDescricaoOcorrencia.setText("");
        txtValorOcorrencia.setText("");
        cmbLocacaoOcorrencia.getSelectionModel().clearSelection();
        tabelaOcorrencias.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarOcorrencia() {
        Ocorrencia selecionada = tabelaOcorrencias.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                ocorrenciaDAO.excluir(selecionada); // Usa o GenericDAO.excluir()
                handleAtualizarListaOcorrencia();
                handleLimparFormularioOcorrencia();
                exibirAlertaSucesso("Sucesso", "Ocorrência excluída.");
            } catch (Exception e) {
                exibirAlertaErro("Erro ao Excluir", "Não foi possível excluir a ocorrência: " + e.getMessage());
            }
        } else {
            exibirAlertaErro("Seleção Inválida", "Por favor, selecione uma ocorrência na tabela para deletar.");
        }
    }

    // MainViewController.java (Trecho de métodos)

    private void initializeEndereco() {

        // 1. Configurar Colunas
        colIdEndereco.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCepEndereco.setCellValueFactory(new PropertyValueFactory<>("cep"));
        colLogradouroEndereco.setCellValueFactory(new PropertyValueFactory<>("logradouro"));
        colComplementoEndereco.setCellValueFactory(new PropertyValueFactory<>("complemento"));
        colNumeroEndereco.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colReferenciaEndereco.setCellValueFactory(new PropertyValueFactory<>("referencia"));

        // 2. Listener para seleção na Tabela
        tabelaEnderecos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        preencherFormularioEndereco(newSelection);
                    }
                });

        // 3. Carregar dados iniciais
        handleAtualizarListaEndereco();
    }

    private void preencherFormularioEndereco(Endereco endereco) {
        txtIdEndereco.setText(String.valueOf(endereco.getId()));
        txtCepEndereco.setText(endereco.getCep());
        txtLogradouroEndereco.setText(endereco.getLogradouro());
        txtComplementoEndereco.setText(endereco.getComplemento());
        txtNumeroEndereco.setText(endereco.getNumero());
        txtReferenciaEndereco.setText(endereco.getReferencia());
    }

    // MainViewController.java (Trecho de métodos @FXML)

    @FXML
    private void handleAtualizarListaEndereco() {
        try {
            // Usa o método buscarTodos do GenericDAO
            List<Endereco> enderecos = enderecoDAO.buscarTodos();

            listaEnderecos.setAll(enderecos);
            tabelaEnderecos.setItems(listaEnderecos);
        } catch (Exception e) {
            exibirAlertaErro("Erro de Atualização", "Não foi possível carregar a lista de endereços: " + e.getMessage());
        }
    }

    @FXML
    private void handleSalvarEndereco() {
        try {
            Endereco endereco;
            long id = txtIdEndereco.getText().isEmpty() ? 0 : Long.parseLong(txtIdEndereco.getText());

            if (id == 0) {
                endereco = new Endereco();
            } else {
                // Tenta pegar o selecionado, ou busca no BD
                endereco = tabelaEnderecos.getSelectionModel().getSelectedItem();
                if (endereco == null) {
                    endereco = enderecoDAO.buscarPorId(id);
                }
            }

            // Preenche os dados
            endereco.setCep(txtCepEndereco.getText());
            endereco.setLogradouro(txtLogradouroEndereco.getText());
            endereco.setComplemento(txtComplementoEndereco.getText());
            endereco.setNumero(txtNumeroEndereco.getText());
            endereco.setReferencia(txtReferenciaEndereco.getText());

            // Salva
            enderecoDAO.salvar(endereco);
            handleAtualizarListaEndereco();
            handleLimparFormularioEndereco();

            exibirAlertaSucesso("Sucesso", "Endereço salvo com sucesso!");

        } catch (NumberFormatException e) {
            exibirAlertaErro("Erro de Formato", "O ID deve ser um número válido.");
        } catch (Exception e) {
            exibirAlertaErro("Erro ao Salvar", "Não foi possível salvar o endereço: " + e.getMessage());
        }
    }

    @FXML
    private void handleLimparFormularioEndereco() {
        txtIdEndereco.setText("");
        txtCepEndereco.setText("");
        txtLogradouroEndereco.setText("");
        txtComplementoEndereco.setText("");
        txtNumeroEndereco.setText("");
        txtReferenciaEndereco.setText("");
        tabelaEnderecos.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleDeletarEndereco() {
        Endereco selecionado = tabelaEnderecos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                enderecoDAO.excluir(selecionado); // Usa o GenericDAO.excluir()
                handleAtualizarListaEndereco();
                handleLimparFormularioEndereco();
                exibirAlertaSucesso("Sucesso", "Endereço excluído.");
            } catch (Exception e) {
                exibirAlertaErro("Erro ao Excluir", "Não foi possível excluir o endereço: " + e.getMessage());
            }
        } else {
            exibirAlertaErro("Seleção Inválida", "Por favor, selecione um endereço na tabela para deletar.");
        }
    }

}