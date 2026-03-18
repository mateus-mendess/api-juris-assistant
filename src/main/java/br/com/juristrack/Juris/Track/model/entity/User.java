package br.com.juristrack.Juris.Track.model.entity;

import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "user")
    private Attorney attorney;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private AuthProviderType authProviderType;

    @Column(name = "provider_user_id")
    private String providerUserId;

    private String password;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
