package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Idiomas;
import com.aluracursos.literalura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libros, Long> {

    @Query(value = "SELECT * FROM libros", nativeQuery = true)
    List<Libros> findAllLibros();

    Libros findByTitulo(String titulo);

    @Query(value = "SELECT * FROM libros WHERE idioma = :idioma", nativeQuery = true)
    List<Libros> findByIdioma(@Param("idioma") String idioma);

    @Query("SELECT DISTINCT l.idioma FROM Libros l")
    List<Idiomas> findDistinctIdiomas();
}