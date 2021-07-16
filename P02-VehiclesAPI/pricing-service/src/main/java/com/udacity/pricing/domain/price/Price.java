package com.udacity.pricing.domain.price;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Entity
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vehicle_Id;

    private String currency;
    private BigDecimal price;


    public Price() {
    }

    public Price(Long vehicleId, String currency, BigDecimal price) {
        this.vehicle_Id = vehicleId;
        this.currency = currency;
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getVehicleId() {
        return vehicle_Id;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicle_Id = vehicleId;
    }
}
