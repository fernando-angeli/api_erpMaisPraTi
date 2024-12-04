package com.erp.maisPraTi.model;

import com.erp.maisPraTi.enums.SupplierStatus;
import com.erp.maisPraTi.enums.TypePfOrPj;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "typePfOrPj")
    private TypePfOrPj typePfPj;

    @Column(unique = true)
    private String cpfCnpj;

    private String stateRegistration;

    private String phoneNumber;

    private String email;

    private String address;

    private String number;

    private String district;

    private String zipCode;

    private String city;

    private String state;

    private String country;

    private BigDecimal creditLimit;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    private SupplierStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "suppliers", fetch = FetchType.LAZY)
    private List<Product> productList = new ArrayList<>();
}
