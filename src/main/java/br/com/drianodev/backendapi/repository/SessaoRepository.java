package br.com.drianodev.backendapi.repository;

import br.com.drianodev.backendapi.model.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    Optional<Sessao> findByPauta_Id(Long idPauta);
}
