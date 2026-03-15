package br.com.juristrack.Juris.Track.model.entity;

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
public class DeclarationTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String filePath;

    private Instant createdAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public void linkClient(Client client) {
        this.setClient(client);
        client.getDeclarationTerms().add(this);
    }

}
