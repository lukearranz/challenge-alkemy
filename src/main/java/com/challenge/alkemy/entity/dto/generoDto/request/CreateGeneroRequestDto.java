package com.challenge.alkemy.entity.dto.generoDto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateGeneroRequestDto {
    @NotBlank
    private String nombre;
    @NotBlank
    private String imagen;
    @NotEmpty
    private List<Long> peliculasId;
}