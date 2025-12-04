package org.projetosyslocacar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "modelo") // Recomenda-se sempre definir o nome da tabela
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    @Temporal(TemporalType.DATE) // Para salvar apenas a data, se for o caso
    private Date ano;

    // O diagrama indica 'idModelo: int', que eu interpretei como 'qtModelo'
    // ou um código de modelo. Usei 'idModelo' para ser mais fiel ao diagrama.
    private int idModelo;

    // Relacionamento Muitos-para-Um: Muitos Modelos pertencem a Uma Marca
    // O lado 'dono' do relacionamento @ManyToOne sempre contém @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    // Relacionamento Muitos-para-Um: Muitos Modelos podem ter Uma Categoria
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Relacionamento Um-para-Muitos: Um Modelo tem Muitos Veículos
    // mappedBy aponta para o campo 'modelo' na classe Veiculo
    // CASCADE.ALL garante que ao salvar um Modelo, os Veiculos associados sejam salvos
    @OneToMany(mappedBy = "modelo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Veiculo> listVeiculo;

    // Nota: O diagrama original tinha 'idModelo: int' e 'listModelos: List<Modelo>'
    // em Categoria, e 'listVeiculo: List<Veiculo>' em Modelo. Ajustei a variável
    // 'qtModelo' para 'idModelo' para ser mais fiel ao nome do atributo no diagrama.
}