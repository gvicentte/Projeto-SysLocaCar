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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "veiculo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Veiculo {

    // Define o Enum StatusVeiculo (dentro ou fora da classe Veiculo, mas aqui é mais limpo)
    public enum StatusVeiculo {
        DISPONIVEL,
        LOCADO,
        MANUTENCAO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Mapeamento do Enum: usa o nome da constante (String) no BD
    @Enumerated(EnumType.STRING)
    private StatusVeiculo status;

    // O diagrama indica 'km: String', mas geralmente é um número.
    // Vou usar 'String' conforme o diagrama, mas 'int' ou 'float' seriam mais comuns.
    private String km;

    private String placa;
    private String chassi;
    private String renavam;
    private String cor;

    // Relacionamento Muitos-para-Um: Muitos Veículos pertencem a Um Modelo
    // O Veículo é o lado "dono" (ManyToOne) e contém a chave estrangeira (JoinColumn)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false) // 'modelo_id' será a FK
    private Modelo modelo;

    // Relacionamento Um-para-Muitos: Um Veículo pode ter Muitas Manutenções
    // mappedBy aponta para o campo 'veiculo' na classe Manutencao (se ela existir)
    @OneToMany(mappedBy = "veiculo")
    private List<Manutencao> manutencoes;

    // Relacionamento Um-para-Muitos: Um Veículo pode ter Muitas Locações
    // mappedBy aponta para o campo 'veiculo' na classe Locacao (se ela existir)
    @OneToMany(mappedBy = "veiculo")
    private List<Locacao> locacoes;
}