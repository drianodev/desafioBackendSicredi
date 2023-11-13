package br.com.drianodev.backendapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name="pautas")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pauta")
    private Long id;

    @Column(name = "titulo_pauta", nullable = false)
    private String titulo;

    @Column(name = "descricao_pauta", nullable = false)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_associado")
    private Associado associado;
}
