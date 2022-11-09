package com.challenge.alkemy.controller;

import com.challenge.alkemy.entity.dto.peliculaDto.request.CreatePeliculaRequestDto;
import com.challenge.alkemy.entity.dto.peliculaDto.request.UpdatePeliculaRequestDto;
import com.challenge.alkemy.error.genero.GeneroNotFoundException;
import com.challenge.alkemy.error.pelicula.PeliculaAlreadyExistsException;
import com.challenge.alkemy.error.pelicula.PeliculaBuscadaPorParametroIncorrectoException;
import com.challenge.alkemy.error.pelicula.PeliculaNotFoundException;
import com.challenge.alkemy.error.personaje.PersonajeNotFoundException;
import com.challenge.alkemy.error.personaje.PersonajeNotFoundInPeliculaException;
import com.challenge.alkemy.error.personaje.PersonajeYaEnUsoException;
import com.challenge.alkemy.service.GeneroService;
import com.challenge.alkemy.service.PeliculaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
public class PeliculaController {

    private final PeliculaService peliculaService;
    private final GeneroService generoService;

    @Operation(summary = "Obtener todas las peliculas")
    @GetMapping("/pelicula")
    public ResponseEntity getAllPeliculas() {

        try {
            return ResponseEntity.ok(peliculaService.getAllPeliculas());
        } catch (Exception e) {
            return new ResponseEntity("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener una pelicula por Id")
    @GetMapping("/pelicula/{id}")
    public ResponseEntity getPeliculaById(@PathVariable("id") Long peliculaId) {

        try {
            return ResponseEntity.ok(peliculaService.getPeliculaById(peliculaId));
        } catch (PeliculaNotFoundException peliculaNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO PELICULA CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Crear nueva pelicula")
    @PostMapping("/pelicula")
    public ResponseEntity createPelicula(@Valid @RequestBody CreatePeliculaRequestDto request) throws PersonajeNotFoundException, PeliculaAlreadyExistsException, GeneroNotFoundException {

        try {
            return  ResponseEntity.ok(peliculaService.createPelicula(request));
        } catch (PeliculaAlreadyExistsException peliculaAlreadyExistsException) {
            return new ResponseEntity("LA PELICULA QUE QUIERES GUARDAR YA EXISTE", HttpStatus.BAD_REQUEST);
        } catch (PersonajeNotFoundException personajeNotFoundException) {
            return new ResponseEntity("NO SE ENCONTRARON PERSONAJES CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException noSuchElementException) {
            return new ResponseEntity<>("NO SE ENCONTRO GENERO CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar una Pelicula por Id")
    @DeleteMapping("/pelicula/{id}")
    public ResponseEntity deletePeliculaById(@PathVariable("id") Long peliculaId) {

        try {
            peliculaService.deletePeliculaById(peliculaId);
            return new  ResponseEntity<>("PELICULA ELIMINADA CON EXITO", HttpStatus.OK);
        } catch (PeliculaNotFoundException peliculaNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO NINGUNA PELICULA CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Editar una Pelicula por id")
    @PutMapping("/pelicula/{id}")
    public ResponseEntity updatePelicula(@Valid @PathVariable("id") Long peliculaId, @RequestBody UpdatePeliculaRequestDto peliculaRequest) {

        try {
            return ResponseEntity.ok(peliculaService.updatePelicula(peliculaId, peliculaRequest));
        } catch (PeliculaNotFoundException peliculaNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO PELICULA CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (PersonajeNotFoundException personajeNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO PERSONAJE CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException noSuchElementException) {
            return new ResponseEntity<>("NO SE ENCONTRO GENERO CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (PeliculaAlreadyExistsException peliculaAlreadyExistsException) {
            return new ResponseEntity<>("EL TITULO SOLICITADO YA ESTA EN USO", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Busqueda de peliculas con parametros")
    @GetMapping("/movies")
    public ResponseEntity getMoviesWithParameters(@Valid @RequestParam(required = false, name = "nombre") String nombre, @RequestParam(required = false, name = "genero") Long idGenero, @RequestParam(required = false, name = "orden") String orden
    ) throws PeliculaNotFoundException {

        if (nombre != null) {
            try {
                return ResponseEntity.ok(peliculaService.getPeliculaByTitulo(nombre));
            } catch (PeliculaNotFoundException peliculaNotFoundException) {
                return new ResponseEntity("NO SE ENCONTRO NINGUNA PELICULA CON EL TITULO INGRESADO", HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (orden != null) {
            try {
                return ResponseEntity.ok(peliculaService.getPeliculasByOrder(orden));
            } catch (PeliculaNotFoundException peliculaNotFoundException) {
                return new ResponseEntity("NO SE ENCONTRARON PELICULAS A ORDENAR", HttpStatus.NOT_FOUND);
            } catch (PeliculaBuscadaPorParametroIncorrectoException peliculaBuscadaPorParametroIncorrectoException) {
                return new ResponseEntity("EL PARAMETRO DE ORDENAMIENTO INGRESADO ES INCORRECTO", HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity("ALGO SALIO MAL",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (idGenero != null) {
            try {
                return ResponseEntity.ok(peliculaService.getPeliculasByGeneroId(idGenero));
            } catch (GeneroNotFoundException generoNotFoundException) {
                return new ResponseEntity("NO SE ENCONTRO GENERO CON ESE ID", HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Si no se ingreso ningun parametro devolvemos la lista de peliculas completa
        try {
            return ResponseEntity.ok(peliculaService.getPeliculasSinParametros());
        } catch (PeliculaNotFoundException peliculaNotFoundException) {
            return new ResponseEntity("NO SE ENCONTRARON PELICULAS EN LA DB", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Agregar personaje a una pelicula por Id")
    @PostMapping("/movies/{idMovie}/characters/{idCharacter}")
    ResponseEntity addPersonajeToPelicula(
            @PathVariable Long idMovie,
            @PathVariable Long idCharacter
    ) {
        try {
            return ResponseEntity.ok(peliculaService.addPersonajeToPelicula(idMovie, idCharacter));
        } catch (PersonajeNotFoundException personajeNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO UN PERSONJE CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (PeliculaNotFoundException peliculaNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO PELICULA CON ESE ID", HttpStatus.NOT_FOUND);
        } catch (PersonajeYaEnUsoException personajeYaEnUsoException) {
            return new ResponseEntity<>("EL PERSONAJE QUE DESEA AGREGAR YA ESTA EN LA PELICULA", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar personaje de una pelicula por Id")
    @DeleteMapping("/movies/{idMovie}/characters/{idCharacter}")
    ResponseEntity deletePersonajeDePelicula(
            @PathVariable Long idMovie,
            @PathVariable Long idCharacter
    ) {
        try {
            return ResponseEntity.ok(peliculaService.deletePersonajeDePelicula(idMovie, idCharacter));
        } catch (PeliculaNotFoundException peliculaNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO PELICULA CON ESE ID" ,HttpStatus.NOT_FOUND);
        } catch (PersonajeNotFoundException personajeNotFoundException) {
            return new ResponseEntity<>("NO SE ENCONTRO PERSONAJE CON ESE ID" ,HttpStatus.NOT_FOUND);
        } catch (PersonajeNotFoundInPeliculaException personajeNotFoundInPeliculaException) {
            return new ResponseEntity<>("NO SE ENCONTRO EL PERSONAJE EN LA PELICULA INDICADA" ,HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("ALGO SALIO MAL" ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
