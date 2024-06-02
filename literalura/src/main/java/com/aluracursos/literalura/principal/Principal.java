package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Locale.filter;

@Service
public class Principal {
    private static final String URL_BASE = "http://gutendex.com/books/?search=";
    private Scanner teclado;
    private ConsumoAPI consumoApi;
    private ConvierteDatos conversor;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository, ConsumoAPI consumoApi, ConvierteDatos conversor) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.consumoApi = consumoApi;
        this.conversor = conversor;
        this.teclado = new Scanner(System.in);
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                ################### Menú ###################
                1 - Buscar libro por titulo
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un año
                5 - Listar libros por idiomas
                                    
                                    
                              
                0 - Salir
                ############################################
                """;
            System.out.println(menu);
            try {
                System.out.print("Elige una opción valida: ");
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnUnAño();
                        break;
                    case 5:
                        listarLibrosPorIdiomas();
                        break;

                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, elige una opción válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Opción inválida. Por favor, introduce un número.");
                teclado.nextLine(); // Limpiar el buffer del scanner
            }
        }
        teclado.close();
    }

    private void buscarLibroPorTitulo() {
        String json = "";
        do {
            try {
                System.out.print("Introduce el título del libro: ");
                String titulo = teclado.nextLine();
                if (titulo.isEmpty() || titulo.trim().length() == 0) {
                    System.out.println("Introduzca un título válido.");
                    continue;
                }
                String url = URL_BASE + URLEncoder.encode(titulo, StandardCharsets.UTF_8);

                json = consumoApi.get(url);
                System.out.println(json);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = null;
                try {
                    jsonNode = objectMapper.readTree(json);
                } catch (JsonProcessingException e) {
                    System.err.println("Error al procesar el JSON: " + e.getMessage());
                    continue;
                }

                // Extrae los datos del libro del JSON
                JsonNode resultsNode = jsonNode.get("results");
                if (resultsNode != null && resultsNode.isArray() && resultsNode.size() > 0) {
                    JsonNode firstResultNode = resultsNode.get(0);
                    if (firstResultNode != null) {
                        procesarLibro(firstResultNode);
                    } else {
                        System.out.println("No se encontraron resultados.");
                    }
                } else {
                    System.out.println("No se encontraron resultados.");
                }
            } catch (Exception e) {
                System.out.println("Opción incorrecta. Por favor, introduce el título del libro correctamente.");
            }
        } while (json.equals("{\"count\":0,\"next\":null,\"previous\":null,\"results\":[]}"));
    }

    private void procesarLibro(JsonNode firstResultNode) {
        String tituloLibro = firstResultNode.get("title").asText();
        String nombreAutor = firstResultNode.get("authors").get(0).get("name").asText();
        String idioma = firstResultNode.get("languages").get(0).asText();
        Double numeroDeDescargas = firstResultNode.get("download_count").asDouble();

        // Verifica si el libro ya existe en la base de datos
        Libros libroExistente = libroRepository.findByTitulo(tituloLibro);
        if (libroExistente != null) {
            System.out.println("Este libro ya se encuentra registrado en la base de datos.");
            return;
        }

        // Extrae las fechas de nacimiento y fallecimiento del autor del JSON
        int birthYear = firstResultNode.get("authors").get(0).get("birth_year").asInt();
        int deathYear = firstResultNode.get("authors").get(0).get("death_year").asInt();

        // Crea el objeto Autor si no existe ya en la base de datos
        Autor autor = autorRepository.findByNombre(nombreAutor);
        if (autor == null) {
            autor = new Autor();
            autor.setNombre(nombreAutor);
            autor.setFechaNacimiento(birthYear != 0 ? birthYear : null);
            autor.setFechaFallecimiento(deathYear != 0 ? deathYear : null);
            autor = autorRepository.save(autor);
        }

        // Convierte el idioma a la enumeración Idiomas
        Idiomas idiomaEnum = convertirStringAIdiomas(idioma);

        // Crea el objeto Libros
        Libros libro = new Libros(tituloLibro, nombreAutor, idiomaEnum, numeroDeDescargas);

        // Asigna el Autor al Libro
        libro.setAutor(autor);

        // Guarda el Libro en la base de datos
        Libros save = libroRepository.save(libro);

        // Imprime los detalles del libro
        System.out.println(libro);
    }

    private Idiomas convertirStringAIdiomas(String idioma) {
        return switch (idioma) {
            case "en" -> Idiomas.INGLES;
            case "es" -> Idiomas.ESPAÑOL;
            case "fr" -> Idiomas.FRANCES;
            case "de" -> Idiomas.ALEMAN;
            default -> Idiomas.OTRO;
        };
    }

    private void listarLibrosRegistrados() {
        // Obtenemos todos los libros del repositorio
        List<Libros> libros = libroRepository.findAll();

        // Si la lista de libros está vacía, imprimimos un mensaje indicando que no hay libros registrados
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            // Si la lista de libros no está vacía, imprimimos un mensaje indicando que se listarán los libros registrados
            System.out.println("Sus libros registrados son:");

            // Recorremos cada libro en la lista de libros
            libros.forEach(libro -> {
                // Imprimimos los detalles del libro en el formato especificado
                System.out.println("----------------------------LIBROS-----------------------------------------");
                System.out.println("Titulo: " + libro.getTitulo());
                System.out.println("Autor: " + libro.getNombreAutor());
                System.out.println("Idioma: " + libro.getIdioma());
                System.out.println("Numero de descargas: " + libro.getNumeroDeDescargas());
                System.out.println("----------------------------------------------------------------------------");
            });
        }
    }

    private void listarAutoresRegistrados() {
        // Obtenemos todos los autores del repositorio
        List<Autor> autores = autorRepository.findAllAutores();

        // Si la lista de autores está vacía, imprimimos un mensaje indicando que no hay autores registrados
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            // Si la lista de autores no está vacía, imprimimos un mensaje indicando que se listarán los autores registrados
            System.out.println("Sus autores registrados son:");

            // Recorremos cada autor en la lista de autores
            autores.forEach(autor -> {
                // Obtenemos los libros del autor
                List<Libros> libros = autor.getLibros();

                // Convertimos la lista de libros en una cadena
                String librosStr = libros.stream()
                        .map(Libros::getTitulo)
                        .collect(Collectors.joining(", "));

                // Imprimimos los detalles del autor en el formato especificado
                System.out.println("----------------------------AUTORES----------------------------------------");
                System.out.println("Nombre: " + autor.getNombre());
                System.out.println("Fecha de nacimiento: " + autor.getFechaNacimiento());
                System.out.println("Fecha de fallecimiento: " + autor.getFechaFallecimiento());
                System.out.println("Libros: [" + librosStr + "]");
                System.out.println("----------------------------------------------------------------------------");
            });
        }
    }

    private void listarAutoresVivosEnUnAño() {
        // Obtenemos el año de nacimiento más temprano y el año de fallecimiento más reciente de la base de datos
        Integer earliestBirthYear = autorRepository.findEarliestBirthYear();
        Integer latestDeathYear = autorRepository.findLatestDeathYear();

        int year;
        do {
            // Solicitamos al usuario que introduzca un año
            System.out.print("Introduce un año entre " + earliestBirthYear + " y " + latestDeathYear + ": ");
            year = teclado.nextInt();
            teclado.nextLine();

            // Si el año introducido no está dentro del rango válido, imprimimos un mensaje de error
            if (year < earliestBirthYear || year > latestDeathYear) {
                System.out.println("Año inválido. Por favor, introduce un año entre " + earliestBirthYear + " y " + latestDeathYear + ".");
            }
        } while (year < earliestBirthYear || year > latestDeathYear);

        // Obtenemos todos los autores que estuvieron vivos en el año introducido por el usuario
        List<Autor> autoresVivosEnUnAño = autorRepository.findAutoresVivosEnUnAño(year);

        // Si la lista de autores está vacía, imprimimos un mensaje indicando que no hay autores vivos en el año introducido
        if (autoresVivosEnUnAño.isEmpty()) {
            System.out.println("No hay autores vivos en el año " + year + ".");
        } else {
            // Si la lista de autores no está vacía, imprimimos un mensaje indicando que se listarán los autores vivos en el año introducido
            System.out.println("Los autores vivos en el año " + year + " son:");

            // Recorremos cada autor en la lista de autores vivos en el año introducido
            autoresVivosEnUnAño.forEach(autor -> {
                // Obtenemos los libros del autor
                List<Libros> libros = autor.getLibros();

                // Convertimos la lista de libros en una cadena
                String librosStr = libros.stream()
                        .map(Libros::getTitulo)
                        .collect(Collectors.joining(", "));

                // Imprimimos los detalles del autor en el formato especificado
                System.out.println("----------------------------AUTORES----------------------------------------");
                System.out.println("Nombre: " + autor.getNombre());
                System.out.println("Fecha de nacimiento: " + autor.getFechaNacimiento());
                System.out.println("Fecha de fallecimiento: " + autor.getFechaFallecimiento());
                System.out.println("Libros: [" + librosStr + "]");
                System.out.println("----------------------------------------------------------------------------");
            });
        }
    }

    private void listarLibrosPorIdiomas() {
        // Obtenemos todos los idiomas únicos de la base de datos
        List<Idiomas> idiomas = libroRepository.findDistinctIdiomas();

        // Si no hay idiomas, informamos al usuario y salimos del método
        if (idiomas.isEmpty()) {
            System.out.println("No hay idiomas registrados en la base de datos.");
            return;
        }

        // Solicitamos al usuario que introduzca un idioma
        System.out.println("Ingrese el idioma para buscar los libros:");

        // Presentamos las opciones de idioma al usuario
        for (int i = 0; i < idiomas.size(); i++) {
            System.out.println((i + 1) + ". " + idiomas.get(i));
        }

        int opcion = -1;
        String idiomaSeleccionado = "";
        while (opcion < 1 || opcion > idiomas.size()) {
            try {
                // Solicitamos al usuario que introduzca un número de opción
                System.out.print("Por favor introduzca la opción numérica correspondiente: ");
                opcion = teclado.nextInt();
                teclado.nextLine();

                // Obtenemos el idioma correspondiente a la opción seleccionada por el usuario
                idiomaSeleccionado = idiomas.get(opcion - 1).toString();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Por favor introduzca una opción numérica correspondiente al idioma.");
                opcion = -1; // Reset the option to invalid so the loop continues
            }
        }

        // Imprimimos el mensaje inicial
        System.out.println("Los libros registrados por idioma son:");

        // Obtenemos todos los libros del idioma seleccionado por el usuario
        List<Libros> librosPorIdioma = libroRepository.findByIdioma(idiomaSeleccionado);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros registrados en el idioma " + idiomaSeleccionado + ".");
        } else {
            System.out.println("Los libros registrados en el idioma " + idiomaSeleccionado + " son:");
            librosPorIdioma.forEach(libro -> System.out.println(libro));
        }
    }}