package br.com.juristrack.Juris.Track.model.entity;

import br.com.juristrack.Juris.Track.enums.FileType;
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
@Table(name = "documents")
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String title;

    @Column(name = "file_path")
    private String storagePath;

    @Enumerated(value = EnumType.STRING)
    private FileType type;

    private Instant createdAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public void linkClient(Client client) {
        this.setClient(client);
        client.getDocuments().add(this);
    }
}
