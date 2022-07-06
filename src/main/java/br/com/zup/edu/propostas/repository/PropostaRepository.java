package br.com.zup.edu.propostas.repository;

import br.com.zup.edu.propostas.controller.StatusDaProposta;
import br.com.zup.edu.propostas.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    boolean existsByDocumento(String documento);

    List<Proposta> findAllByStatusOrderByCriadaEmAsc(StatusDaProposta status);

    List<Proposta> findAllByStatusAndCartaoIsNullOrderByCriadaEmAsc(StatusDaProposta elegivel);
}
