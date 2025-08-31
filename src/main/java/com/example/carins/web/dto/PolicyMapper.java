package com.example.carins.web.dto;

import com.example.carins.model.InsurancePolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PolicyMapper {
    @Mapping(source = "car.id", target = "carId")
    PolicyDto toDto(InsurancePolicy policy);
}
