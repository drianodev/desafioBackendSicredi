package br.com.drianodev.backendapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "votos")
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voto")
    private Long id;

    @Column(name = "id_associado")
    private Long idAssociado;

    @Column(name = "cpf_associado")
    private String cpfAssociado;

    @ManyToOne
    @JoinColumn(name = "id_pauta")
    private Pauta pauta;

    @Column(name = "voto")
    private Boolean voto;
}
