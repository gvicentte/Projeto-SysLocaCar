package org.projetosyslocacar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Atributos Pessoais (Copias de Usuario/Pessoa - conforme diagrama implícito)
    private String nome;
    private String cpf;
    private String email;
    // ... outros campos de pessoa que o cliente deve ter

    // Atributo específico do Cliente
    private String cnh;
    private String rg; // Adicionado conforme o diagrama UML

    // Relacionamento Um-para-Um: Cliente possui um Endereço (lado dono da FK)
    // O diagrama indica 1..1, mas para simplificar, usaremos OneToOne.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    // Relacionamento Um-para-Muitos: Um Cliente pode ter Muitos Contatos (Telefones)
    // 'mappedBy' aponta para o campo 'cliente' na classe Contato.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> listaContatos;

    // Relacionamento Um-para-Muitos: Um Cliente pode ter Muitos Contratos de Locação
    // 'mappedBy' aponta para o campo 'cliente' na classe ContratoLocacao.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContratoLocacao> listaContratos;
}