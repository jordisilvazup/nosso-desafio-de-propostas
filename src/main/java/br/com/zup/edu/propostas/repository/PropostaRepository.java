package br.com.zup.edu.propostas.repository;

import br.com.zup.edu.propostas.controller.StatusDaProposta;
import br.com.zup.edu.propostas.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    boolean existsByDocumento(String documento);

    List<Proposta> findTop2ByStatusOrderByCriadaEmAsc(StatusDaProposta status);

}
