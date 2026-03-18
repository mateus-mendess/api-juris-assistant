package br.com.juristrack.Juris.Track.model.repository;

import br.com.juristrack.Juris.Track.model.entity.Attorney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttorneyRepository extends JpaRepository<Attorney, UUID> {
    Boolean existsByCpf(String cpf);

    Boolean existsByPhone(String phone);

    Boolean existsByOabNumberAndOabState(String oab, String state);

    Optional<Attorney> findByUserId(UUID id);
}
