package com.rideease.model;

import com.rideease.model.enums.RideStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude")),
            @AttributeOverride(name = "address", column = @Column(name = "pickup_address"))
    })
    private Location pickupLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "destination_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "destination_longitude")),
            @AttributeOverride(name = "address", column = @Column(name = "destination_address"))
    })
    private Location destinationLocation;

    private LocalDateTime pickupTime;
    private LocalDateTime dropOffTime;
    
    private double distance;
    private double fare;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @OneToOne(mappedBy = "ride", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "ride", cascade = CascadeType.ALL)
    private Rating rating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", user=" + user.getName() +
                ", driver=" + (driver != null ? driver.getName() : "Not assigned") +
                ", pickupLocation=" + pickupLocation +
                ", destinationLocation=" + destinationLocation +
                ", pickupTime=" + pickupTime +
                ", dropOffTime=" + dropOffTime +
                ", distance=" + distance +
                ", fare=" + fare +
                ", status=" + status +
                '}';
    }
}
