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

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "locacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date dataRetirada;

    @Temporal(TemporalType.DATE)
    private Date dataDevolucao;

    private float valorLocacao; // Valor da locação (diária, total base)

    // Relacionamento Muitos-para-Um: Locação pertence a Um ContratoLocacao
    // Locação é o lado dono e contém a FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private ContratoLocacao contratoLocacao;

    // Relacionamento Muitos-para-Um: Locação está associada a Um Veículo
    // Locação é o lado dono e contém a FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo; // Corrigido de 'veiculol' para 'veiculo'

    // Relacionamento Um-para-Muitos: Uma Locação pode ter Muitas Ocorrências (multa, avaria, etc.)
    // mappedBy aponta para o campo 'locacao' na classe Ocorrencia (que é o lado dono da FK)
    @OneToMany(mappedBy = "locacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ocorrencia> listaOcorrencias;
}