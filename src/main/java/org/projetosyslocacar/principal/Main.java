package org.projetosyslocacar.principal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.projetosyslocacar.dao.CategoriaDAO;
import org.projetosyslocacar.dao.MarcaDAO;
import org.projetosyslocacar.dao.ModeloDAO;
import org.projetosyslocacar.model.Categoria;
import org.projetosyslocacar.model.Marca;
import org.projetosyslocacar.model.Modelo;
import org.projetosyslocacar.utils.PathFXML;
import org.projetosyslocacar.utils.HibernateUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        HibernateUtil.getSessionFactory();
        seedDatabase();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(new FileInputStream(PathFXML.pathFXML() + "\\ModeloFormulario.fxml"));
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Sistema de Locacao de Veiculos!");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> {
            HibernateUtil.shutdown();
            System.out.println("Hibernate SessionFactory encerrada.");
        });

    }
    private void seedDatabase() {
        MarcaDAO marcaDao = new MarcaDAO(Marca.class);
        CategoriaDAO categoriaDao = new CategoriaDAO(Categoria.class);
        ModeloDAO modeloDao = new ModeloDAO(Modelo.class);

        // Verificação para evitar salvar dados duplicados a cada inicialização
        if (marcaDao.buscarTodos().isEmpty()) {
            System.out.println("Populando o banco de dados com dados iniciais...");

            // --- A. Marcas ---
            Marca chevrolet = new Marca(0, "Chevrolet", null);
            Marca volkswagen = new Marca(0, "Volkswagen", null);

            Marca savedChevrolet = marcaDao.salvar(chevrolet);
            Marca savedVolkswagen = marcaDao.salvar(volkswagen);

            // --- B. Categorias ---
            Categoria popular = new Categoria(0, "Popular", 100.00f, null);
            Categoria luxo = new Categoria(0, "Luxo", 350.00f, null);

            Categoria savedPopular = categoriaDao.salvar(popular);
            Categoria savedLuxo = categoriaDao.salvar(luxo);

            // --- C. Modelos (Relacionados) ---

            // 🚨 Nota: Use new Date(System.currentTimeMillis()) ou a nova API java.time
            // Se você usar new Date(year, month, day), o ano é baseado em 1900.

            Modelo onix = new Modelo();
            onix.setNome("Onix Plus");
            onix.setAno(new Date()); // Data atual como exemplo
            onix.setIdModelo(101); // Código do Modelo
            onix.setMarca(savedChevrolet);
            onix.setCategoria(savedPopular);
            modeloDao.salvar(onix);

            Modelo jetta = new Modelo();
            jetta.setNome("Jetta GLI");
            jetta.setAno(new Date());
            jetta.setIdModelo(202);
            jetta.setMarca(savedVolkswagen);
            jetta.setCategoria(savedLuxo);
            modeloDao.salvar(jetta);

            System.out.println("População inicial concluída.");
        } else {
            System.out.println("Dados iniciais já existem. Ignorando população.");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}