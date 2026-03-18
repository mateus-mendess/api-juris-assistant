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

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "document_type")
    private FileType fileType;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public void linkClient(Client client) {
        this.setClient(client);
        client.getDocuments().add(this);
    }
}
