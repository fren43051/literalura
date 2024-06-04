Proyecto Literalura
Descripción
Literalura es una aplicación Java basada en Spring Boot diseñada para interactuar con la API de Gutendex, almacenar y gestionar datos de libros y autores, y proporcionar diversas funcionalidades de búsqueda y listado de libros y autores. Este proyecto permite a los usuarios buscar libros por título, listar libros y autores registrados, listar autores vivos en un año específico y listar libros por idiomas.

Requisitos
Java 11 o superior
Spring Boot 2.6 o superior
Maven 3.6 o superior
Conexión a internet para acceder a la API de Gutendex
Instalación
Clonar el repositorio

bash
Copiar código
git clone https://github.com/fren43051/proyecto-literalura.git
cd proyecto-literalura
Compilar y empaquetar la aplicación

bash
Copiar código
mvn clean package
Ejecutar la aplicación

bash
Copiar código
java -jar target/literalura-0.0.1-SNAPSHOT.jar
Uso
Al iniciar la aplicación, se mostrará un menú en la consola con las siguientes opciones:

shell
Copiar código
################### Menú ###################
1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un año
5 - Listar libros por idiomas
0 - Salir
############################################
Funcionalidades
Buscar libro por título

Permite buscar un libro por su título utilizando la API de Gutendex. Si el libro no está registrado en la base de datos, se solicitará el registro.

Listar libros registrados

Muestra una lista de todos los libros almacenados en la base de datos con sus detalles.

Listar autores registrados

Muestra una lista de todos los autores almacenados en la base de datos con sus detalles y libros asociados.

Listar autores vivos en un año específico

Permite ingresar un año y muestra una lista de autores que estuvieron vivos en ese año.

Listar libros por idiomas

Permite seleccionar un idioma y muestra una lista de libros registrados en ese idioma.

Ejemplo de uso
Para buscar un libro por su título:

Selecciona la opción 1 en el menú.
Ingresa el título del libro cuando se te solicite.
La aplicación buscará el libro utilizando la API de Gutendex y mostrará los resultados.

Estructura del proyecto
plaintext
Copiar código
com.aluracursos.literalura
├── config
│   ├── DatabaseConfig.java
│   └── (other configuration classes)
├── exception
│   ├── LibroNotFoundException.java
│   └── (other custom exceptions)
├── model
│   ├── Autor.java
│   ├── Libros.java
│   ├── Idiomas.java
│   └── (other data models)
├── repository
│   ├── AutorRepository.java
│   ├── LibroRepository.java
│   └── (other repository interfaces)
├── service
│   ├── ConsumoAPI.java
│   ├── ConvierteDatos.java
│   ├── IConvierteDatos.java
│   └── LibroService.java
├── util
│   ├── DateUtils.java
│   └── (other utility classes)
└── principal
    └── Principal.java
Clases principales
Principal.java
Esta clase contiene el menú principal de la aplicación y maneja la interacción con el usuario.

config/
Contiene clases de configuración, como DatabaseConfig.java.

exception/
Contiene excepciones personalizadas, como LibroNotFoundException.java.

model/
Contiene las clases de modelo Autor, Idiomas, y Libros.

repository/
Contiene las interfaces AutorRepository y LibroRepository para interactuar con la base de datos.

service/
Contiene las clases de servicio ConsumoAPI, ConvierteDatos, IConvierteDatos, y LibroService para manejar la lógica de negocio y la interacción con la API.

util/
Contiene clases utilitarias, como DateUtils.java.

Contribuciones
Si deseas contribuir a este proyecto, por favor, sigue estos pasos:

Haz un fork del repositorio.
Crea una rama (git checkout -b feature/nueva-funcionalidad).
Realiza tus cambios y haz commits descriptivos (git commit -am 'Añadir nueva funcionalidad').
Empuja tus cambios a la rama (git push origin feature/nueva-funcionalidad).
Abre un Pull Request.
Contacto
Para cualquier duda o sugerencia, por favor, contacta a fren43051@gmail.com.

Autor
Nelson Enrique Reyes
