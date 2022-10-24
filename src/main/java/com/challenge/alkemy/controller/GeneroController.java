package com.challenge.alkemy.controller;

import com.challenge.alkemy.entity.dto.generoDto.request.CreateGeneroRequestDto;
import com.challenge.alkemy.error.genero.GeneroAlreadyInUseException;
import com.challenge.alkemy.error.genero.GeneroNotFoundException;
import com.challenge.alkemy.service.GeneroService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class GeneroController {

    private final GeneroService generoService;

    @Operation(summary = "Obtener todos los Generos")
    @GetMapping("/genero")
    public ResponseEntity getAllGeneros() {

        try {
            return ResponseEntity.ok(generoService.getAllGeneros());
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener un genero por Id")
    @GetMapping("/genero/{id}")
    public ResponseEntity fetchGeneroById(@PathVariable("id") Long generoId) {

        try {
            return ResponseEntity.ok(generoService.findGeneroById(generoId));
        } catch (GeneroNotFoundException generoNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO GENERO CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Eliminar un genero por id")
    @DeleteMapping("/genero/{id}")
    public ResponseEntity deleteGeneroById(@PathVariable("id") Long generoId) {

        try {
            generoService.deleteGeneroById(generoId);
            return ResponseEntity.ok("GENERO ELIMINADO CON EXITO");
        } catch (GeneroNotFoundException generoNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO GENERO A ELIMINAR CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Crear un nuevo Genero")
    @PostMapping("/genero")
    public ResponseEntity createGenero(@Valid @RequestBody CreateGeneroRequestDto genero) {

            try {
                return ResponseEntity.ok(generoService.saveGenero(genero));
            } catch (GeneroAlreadyInUseException generoAlreadyInUseException) {
                return new ResponseEntity("EL GENERO INDICADO YA SE ENCUENTRA EN USO", HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }
}
