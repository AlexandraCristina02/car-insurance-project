package com.example.carins.web.dto;

import com.example.carins.model.InsuranceClaim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClaimMapper {
    @Mapping(source = "car.id", target = "carId")
    ClaimResponseDto toDto(InsuranceClaim claim);

    List<ClaimResponseDto> toDtoList(List<InsuranceClaim> claims);

}
