package br.com.drianodev.backendapi.repository;

import br.com.drianodev.backendapi.model.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    List<Voto> findAllByPautaId(Long idPauta);
    boolean existsByCpfAssociadoAndPautaId(String cpfAssociado, Long idPauta);
}
