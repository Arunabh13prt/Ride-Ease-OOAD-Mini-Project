package com.rideease.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer userRating;

    @Min(1)
    @Max(5)
    private Integer driverRating;

    private String userComment;
    private String driverComment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", ride=" + ride.getId() +
                ", userRating=" + userRating +
                ", driverRating=" + driverRating +
                ", userComment='" + userComment + '\'' +
                ", driverComment='" + driverComment + '\'' +
                '}';
    }
}
