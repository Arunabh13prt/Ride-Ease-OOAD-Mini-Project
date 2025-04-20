package com.rideease.model;

import com.rideease.model.enums.PaymentMethod;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private String transactionId;

    private boolean completed;

    private LocalDateTime paymentTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", ride=" + ride.getId() +
                ", amount=" + amount +
                ", method=" + method +
                ", transactionId='" + transactionId + '\'' +
                ", completed=" + completed +
                ", paymentTime=" + paymentTime +
                '}';
    }
}
