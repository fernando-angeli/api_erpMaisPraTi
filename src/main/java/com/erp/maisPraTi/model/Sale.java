package com.erp.maisPraTi.model;

import com.erp.maisPraTi.enums.SaleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "tb_sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private Long saleNumber;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false)
    private LocalDate expectedDeliveryDate;

    private LocalDateTime saleDelivery;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties("sales")
    private Client client;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SaleItem> saleItems = new ArrayList<>();

    private String sellerName;

    @Transient
    private BigDecimal totalSaleValue;

    @Transient
    private BigDecimal totalPendingDelivery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus saleStatus;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Delivery> deliveries = new ArrayList<>();

    public BigDecimal getTotalSaleValue() {
        if(this.saleItems == null)
            return new BigDecimal(0);
        return this.saleItems.stream()
            .map(item -> item.getSalePrice().multiply(item.getQuantitySold()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalPendingDelivery(){
        if (this.saleItems == null)
            return new BigDecimal(0);
        return this.saleItems.stream()
                .map(SaleItem::getQuantityPending)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
