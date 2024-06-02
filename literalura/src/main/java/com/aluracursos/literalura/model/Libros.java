package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @Column(name = "nombre_autor")
    private String nombreAutor;

    @Enumerated(EnumType.STRING)
    private Idiomas idioma;

    private Double numeroDeDescargas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libros() {
    }

    public Libros(String titulo, String nombreAutor, Idiomas idioma, Double numeroDeDescargas) {
        this.titulo = titulo;
        this.nombreAutor = nombreAutor;
        this.idioma = idioma;
        this.numeroDeDescargas = numeroDeDescargas;
    }

    // Getters and setters...


    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Idiomas getIdioma() {
        return idioma;
    }

    public void setIdioma(Idiomas idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return  "-------------------------LIBRO---------------------------------------\n" +
                "Titulo: " + titulo + '\n' +
                "Autor: " + (autor != null ? autor.getNombre() : nombreAutor) + '\n' +
                "Idioma: " + idioma + '\n' +
                "Numero de descargas: " + numeroDeDescargas + '\n' +
                "-----------------------------------------------------------------------";
    }
}
