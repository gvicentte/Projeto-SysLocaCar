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
@Table(name = "contato")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;
    private String telefone;
    private String celular;

    // Relacionamento Muitos-para-Um: Muitos Contatos pertencem a Um Cliente.
    // Contato é o lado dono do relacionamento, por isso contém o @JoinColumn.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false) // 'cliente_id' será a chave estrangeira (FK)
    private Cliente cliente;
}