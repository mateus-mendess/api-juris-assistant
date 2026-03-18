package br.com.juristrack.Juris.Track.model.repository;

import br.com.juristrack.Juris.Track.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
