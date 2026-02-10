package br.com.juristrack.Juris.Track.model.repository;

import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LawyerRepository extends JpaRepository<Lawyer, UUID> {
    Boolean existsByCpf(String cpf);

    Boolean existsByPhone(String phone);

    Boolean existsByOabNumberAndOabState(String oab, String state);
}
