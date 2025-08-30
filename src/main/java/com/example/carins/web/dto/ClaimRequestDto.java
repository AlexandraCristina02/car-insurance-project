package com.example.carins.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClaimRequestDto {
    @NotNull(message = "Claim date is required")
    @PastOrPresent(message = "Claim date cannot be in the future")
    private LocalDate claimDate;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    public LocalDate getClaimDate(){return claimDate;}
    public void setClaimDate(LocalDate claimDate){this.claimDate=claimDate;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal amount){this.amount=amount;}
}
