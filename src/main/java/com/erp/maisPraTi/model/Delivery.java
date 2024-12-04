package com.erp.maisPraTi.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tb_deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    private LocalDateTime dateDelivery;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeliveryItem> deliveryItems = new ArrayList<>();

}
