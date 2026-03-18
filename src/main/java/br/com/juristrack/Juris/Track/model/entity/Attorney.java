package br.com.juristrack.Juris.Track.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Attorney {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String name;

    private String cpf;

    @Column(name = "oab_number")
    private String oabNumber;

    @Column(name = "oab_state")
    private String oabState;

    private String phone;

    @Column(name = "profile_photo")
    private String profilePhotoPath;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "attorney", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Client> clients = new HashSet<>();

    public void linkUserAccountAndAddress(User user, Address address) {
        this.setUser(user);
        user.setAttorney(this);
        this.setAddress(address);
    }
}
