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
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "categoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    // O tipo 'float' em Java corresponde a 'float' ou 'real' no BD.
    // Mantenho 'float' como no seu código.
    private float valorLocacao;

    // Relacionamento Um-para-Muitos: Uma Categoria pode ter Muitos Modelos.
    // 'mappedBy' aponta para o campo 'categoria' na classe Modelo, que é o lado dono da FK.
    // Usamos CascadeType.PERSIST para que, ao salvar a Categoria, os Modelos novos também sejam persistidos.
    @ToString.Exclude
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Modelo> listaModelos;
}