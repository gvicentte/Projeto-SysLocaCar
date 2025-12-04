package org.projetosyslocacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "manutencao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String descricao;

    // Anotação para garantir que a data seja salva corretamente (apenas data ou data e hora)
    @Temporal(TemporalType.TIMESTAMP) // TIMESTAMP para incluir data e hora da manutenção
    private Date data;

    private float custo;

    // Relacionamento Muitos-para-Um: Muitas Manutenções pertencem a Um Veículo.
    // Manutencao é o lado dono e contém a FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false) // 'veiculo_id' será a chave estrangeira (FK)
    private Veiculo veiculo;
}