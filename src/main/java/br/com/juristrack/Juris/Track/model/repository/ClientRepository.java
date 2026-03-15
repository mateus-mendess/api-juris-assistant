package br.com.juristrack.Juris.Track.model.repository;

import br.com.juristrack.Juris.Track.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Boolean existsByCpf(String cpf);

    Boolean existsByPhone(String phone);
}
