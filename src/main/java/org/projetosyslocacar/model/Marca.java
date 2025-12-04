package org.projetosyslocacar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import lombok.ToString;
@Entity
@Table(name = "marca")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    // Relacionamento Um-para-Muitos: Uma Marca pode ter Muitos Modelos.
    // 'mappedBy' aponta para o campo 'marca' na classe Modelo, que é o lado dono da FK.
    // CascadeType.ALL: Operações como persistir ou deletar uma Marca serão aplicadas aos Modelos associados.
    @ToString.Exclude
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Modelo> listModelo;
}