package com.example.carins.web.dto;

import com.example.carins.model.InsuranceClaim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClaimMapper {
    @Mapping(source = "car.id", target = "carId")
    ClaimResponseDto toDto(InsuranceClaim claim);
}
