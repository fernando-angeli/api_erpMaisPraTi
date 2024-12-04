package com.erp.maisPraTi.model;

import com.erp.maisPraTi.enums.PartyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String fullName;

    private LocalDate birthDate;

    private String phoneNumber;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String email;

    private String address;

    private String number;

    private String district;

    private String zipCode;

    private String city;

    private String state;

    private String country;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PartyStatus status;

    private String resetPasswordToken;

    private LocalDateTime tokenExpiration;

    @ElementCollection
    @CollectionTable(name = "tb_user_cards", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "card_name")
    @Column(name = "card_description")
    private Map<String, String> cards = new HashMap<>();

    public void addCards(String slot, String cardName){
        this.cards.put(slot, cardName);
    }
}
