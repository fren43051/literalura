package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Libros;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libros> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    public Libros obtenerLibroPorId(Long id) {
        return libroRepository.findById(id).orElse(null);
    }

    public Libros guardarLibro(Libros libro) {
        return libroRepository.save(libro);
    }

    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }
}