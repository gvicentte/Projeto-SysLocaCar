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
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private String cpf;
    private String login;
    private String senha;

    // Relacionamento Um-para-Um: Usuario possui um Endereço.
    // Usuario é o lado dono e contém a chave estrangeira (FK)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    // NOTA: O diagrama original sugere que Contato está ligado a Cliente (1..*).
    // Se o Usuario DEVE ter um contato principal, use Um-para-Um (ou use os campos de Contato diretamente).
    // Se for Um-para-Um:
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contato_principal_id")
    private Contato contatoPrincipal;

    // Se você usa Funcionario separadamente, esta lista rastreia os contratos criados por este usuário/funcionário.
    @OneToMany(mappedBy = "usuarioCriador", fetch = FetchType.LAZY)
    private List<ContratoLocacao> contratosGerenciados;
}