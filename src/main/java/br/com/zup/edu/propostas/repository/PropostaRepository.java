package br.com.zup.edu.propostas.repository;

import br.com.zup.edu.propostas.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    boolean existsByDocumento(String documento);
}
