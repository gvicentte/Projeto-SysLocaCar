package org.projetosyslocacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ocorrencia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Boa prática: nomes de campos devem começar com letra minúscula (Java convention)
    private String descricao;

    private float valor;

    // Relacionamento Muitos-para-Um: Muitas Ocorrências pertencem a Uma Locação.
    // Ocorrencia é o lado dono e contém a FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locacao_id", nullable = false) // 'locacao_id' será a chave estrangeira (FK)
    private Locacao locacao;
}