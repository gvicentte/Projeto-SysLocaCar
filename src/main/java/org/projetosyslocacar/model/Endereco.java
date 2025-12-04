package org.projetosyslocacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "endereco")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cep;
    private String logradouro;
    private String complemento;
    private String numero;
    private String referencia;

    // Relacionamento Um-para-Um Reverso (Lado não-dono)
    // Mapeamento para Usuario (se Usuario tiver um Endereco)
    // 'mappedBy' aponta para o campo 'endereco' na classe Usuario
    @OneToOne(mappedBy = "endereco", fetch = FetchType.LAZY)
    private Usuario usuario;

    // Relacionamento Um-para-Um Reverso (Lado não-dono)
    // Mapeamento para Cliente (se Cliente tiver um Endereco)
    // 'mappedBy' aponta para o campo 'endereco' na classe Cliente
    @OneToOne(mappedBy = "endereco", fetch = FetchType.LAZY)
    private Cliente cliente;
}