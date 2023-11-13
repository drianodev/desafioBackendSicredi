package br.com.drianodev.backendapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessoes")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sessao")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_pauta", unique = true)
    private Pauta pauta;

    @Column(name = "data_sessao", nullable = false)
    private LocalDateTime dataSessao;

    @Column(name = "duracao_sessao", nullable = false)
    private Duration duracao;
}
