package com.rideease.model;

import com.rideease.model.enums.VehicleType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Registration number is required")
    @Column(unique = true)
    private String registrationNumber;

    @NotNull(message = "Vehicle type is required")
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    private String color;

    private int capacity;

    @OneToOne(mappedBy = "vehicle")
    private Driver driver;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", type=" + type +
                ", color='" + color + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
