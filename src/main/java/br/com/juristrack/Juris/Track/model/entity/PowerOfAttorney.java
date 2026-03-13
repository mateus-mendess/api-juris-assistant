package br.com.juristrack.Juris.Track.model.entity;

import br.com.juristrack.Juris.Track.enums.PowerOfAttorneyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "")
@Entity
public class PowerOfAttorney {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String filePath;

    @Enumerated(value = EnumType.STRING)
    private PowerOfAttorneyType type;

    private Instant createdAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
