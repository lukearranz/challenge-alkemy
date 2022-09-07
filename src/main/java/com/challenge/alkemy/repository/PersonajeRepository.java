package com.challenge.alkemy.repository;

import com.challenge.alkemy.entity.Personaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonajeRepository extends JpaRepository<Personaje, Long> {

    List<Personaje> findByNombre(String nombre);

    List<Personaje> findByEdad(int edad);

    List<Personaje> findByPeso(Double peso);
}