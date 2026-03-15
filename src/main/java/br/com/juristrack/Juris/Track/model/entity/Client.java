package br.com.juristrack.Juris.Track.model.entity;

import br.com.juristrack.Juris.Track.enums.MaritalStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "")
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String cpf;

    private String nationality;

    @Enumerated(value = EnumType.STRING)
    private MaritalStatusType maritalStatus;

    private String work;

    private String phone;

    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "lawyer_id")
    private Lawyer lawyer;

    @OneToOne
    @JoinColumn(name = "address_id", unique = true)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PowerOfAttorney> powerOfAttorneys = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeclarationTerm> declarationTerms = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contract> contracts = new HashSet<>();

    public void linkAddressAndAttorney(Address address, Lawyer lawyer) {
        this.address = address;
        this.lawyer = lawyer;
    }
}
