package br.com.juristrack.Juris.Track.model.repository;

import br.com.juristrack.Juris.Track.model.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
}
