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
@Table(name = "client")
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String cpf;

    private String nationality;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatusType maritalStatus;

    private String work;

    private String phone;

    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "attorney_id")
    private Attorney attorney;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Document> documents = new HashSet<>();

    public void linkAddressAndAttorney(Address address, Attorney attorney) {
        this.setAddress(address);
        this.setAttorney(attorney);
        attorney.getClients().add(this);
    }
}
