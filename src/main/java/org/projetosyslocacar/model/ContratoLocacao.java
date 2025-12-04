package org.projetosyslocacar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contrato_locacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContratoLocacao {

    // Enum para o status do contrato
    public enum StatusLocacao {
        ABERTO, EM_ANDAMENTO, CONCLUIDO, CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date dataContrato;

    private float valorCaucao;

    // Mapeamento do Enum: usa o nome da constante (String) no BD
    @Enumerated(EnumType.STRING)
    private StatusLocacao status;

    private float valorTotal;

    // Relacionamento Muitos-para-Um: Contrato pertence a Um Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_criador_id")
    private Usuario usuarioCriador;

    // Relacionamento Um-para-Muitos: Um Contrato possui Muitas Locações
    // mappedBy aponta para o campo 'contratoLocacao' na classe Locacao
    @OneToMany(mappedBy = "contratoLocacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Locacao> listaLocacao;
}