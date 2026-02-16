package br.com.juristrack.Juris.Track.model.entity;

import br.com.juristrack.Juris.Track.enums.Provider;
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
@Table(name = "user_account")
@Entity
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "userAccount")
    private Lawyer lawyer;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Builder.Default
    @Column(name = "is_enable")
    private Boolean isEnable = true;

    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
