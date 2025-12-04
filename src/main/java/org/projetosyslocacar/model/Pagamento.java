package org.projetosyslocacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "pagamento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {

    // Define o Enum para os tipos de pagamento
    public enum TipoPagamento {
        CARTAO_CREDITO, DEBITO, PIX, BOLETO, DINHEIRO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Mapeamento do Enum: usa o nome da constante (String) no BD
    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    private float valorTotal;

    // Relacionamento Muitos-para-Um: Muitos Pagamentos estão associados a Um Contrato de Locação.
    // Pagamento é o lado dono e contém a FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false) // 'contrato_id' será a chave estrangeira (FK)
    private ContratoLocacao contratoLocacao;
}