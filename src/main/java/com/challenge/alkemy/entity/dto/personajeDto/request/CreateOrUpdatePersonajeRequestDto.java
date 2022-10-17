package com.challenge.alkemy.entity.dto.personajeDto.request;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateOrUpdatePersonajeRequestDto {

    @NotNull
    private String nombre;
    @NotNull
    private String imagen;
    @NotNull
    private int edad;
    @NotNull
    private double peso;
    @NotBlank
    private String historia;
}
