package br.com.zup.edu.propostas.repository;

import br.com.zup.edu.propostas.controller.StatusDaProposta;
import br.com.zup.edu.propostas.model.Proposta;
import org.hibernate.LockOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    boolean existsByDocumento(String documento);

    @QueryHints({
       @QueryHint(
           name = "javax.persistence.lock.timeout",
           value = (LockOptions.SKIP_LOCKED + "")
       )
    })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Proposta> findTop5ByStatusOrderByCriadaEmAsc(StatusDaProposta status);

}
