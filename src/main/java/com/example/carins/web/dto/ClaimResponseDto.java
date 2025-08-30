package com.example.carins.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClaimResponseDto {
    private Long id;
    private Long carId;
    private LocalDate claimDate;
    private String description;
    private BigDecimal amount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
    public LocalDate getClaimDate(){return claimDate;}
    public void setClaimDate(LocalDate claimDate){this.claimDate=claimDate;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal amount){this.amount=amount;}
}
