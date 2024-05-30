package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosAutor;
import com.aluracursos.literalura.model.DatosLibros;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "http://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    -------------------Menú-------------------
                    1 - Buscar libro por titulo
                    
                                  
                    0 - Salir
                    ------------------------------------------
                    """;
            System.out.println(menu);
            System.out.print("Elige una opción valida: ");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;


                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Introduce el título del libro que deseas buscar:");
        var titulo = teclado.nextLine();
        var encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        var json = consumoApi.obtenerDatos(URL_BASE + encodedTitulo);

        Datos datos = conversor.obtenerDatos(json, Datos.class);

        if (datos.resultados() != null && !datos.resultados().isEmpty()) {
            var primerLibro = datos.resultados().get(0);
            var autor = primerLibro.autor().get(0);
            var nombreAutor = autor.nombre().split(",");
            var apellidoAutor = nombreAutor[0];
            var nombre = nombreAutor.length > 1 ? nombreAutor[1].trim() : "";
            System.out.println("-----------------------LIBRO---------------------------");
            System.out.println("Titulo: " + primerLibro.titulo());
            System.out.println("Autor: " + apellidoAutor.substring(0, 1).toUpperCase() + apellidoAutor.substring(1) + ", " + nombre.substring(0, 1).toUpperCase() + nombre.substring(1));
            System.out.println("Idioma: " + (primerLibro.idiomas().get(0).equals("en") ? "Inglés" : primerLibro.idiomas().get(0)));
            System.out.println("Número de Descargas: " + primerLibro.numeroDeDescargas());
            System.out.println("-------------------------------------------------------");
        } else {
            System.out.println("No se encontraron resultados");
        }
    }
}





