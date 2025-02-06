package dev.backend.ecommercial.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(name = "address_line", nullable = false, length = 255)
    private String addressLine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
