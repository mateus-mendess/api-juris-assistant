package br.com.juristrack.Juris.Track.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Lawyer {
    @Id
    private UUID id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", unique = true)
    private UserAccount userAccount;

    private String name;

    private String cpf;

    @Column(name = "oab_number")
    private String oabNumber;

    @Column(name = "oab_state")
    private String oabState;

    @Column(name = "phone_code")
    private String phoneCode;

    private String phone;

    @Column(name = "profile_photo")
    private String profilePhoto;
}
