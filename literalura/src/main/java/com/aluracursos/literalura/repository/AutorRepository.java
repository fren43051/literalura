package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombreAutor);

    @Query(value = "SELECT * FROM autores", nativeQuery = true)
    List<Autor> findAllAutores();

    @Query(value = "SELECT * FROM autores WHERE fecha_nacimiento <= :year AND (fecha_fallecimiento >= :year OR fecha_fallecimiento IS NULL)", nativeQuery = true)
    List<Autor> findAutoresVivosEnUnAño(int year);

    @Query(value = "SELECT MIN(fecha_nacimiento) FROM autores", nativeQuery = true)
    Integer findEarliestBirthYear();

    @Query(value = "SELECT MAX(fecha_fallecimiento) FROM autores WHERE fecha_fallecimiento IS NOT NULL", nativeQuery = true)
    Integer findLatestDeathYear();
}
