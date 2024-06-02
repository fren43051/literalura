package com.aluracursos.literalura.model;

import java.util.Arrays;

public enum Idiomas {
    INGLES("en", "english"),
    ALEMAN("de", "german"),
    FRANCES("fr", "french"),
    ESPAÑOL("es", "spanish"),
    ITALIANO("it", "italian"),
    NEERLANDES("nl", "dutch"),
    PORTUGUES("pt", "portuguese"),
    SUECO("sv", "swedish"),
    DANES("da", "danish"),
    NORUEGO("no", "norwegian"),
    FINLANDES("fi", "finnish"),
    LATIN("la", "latin"),
    RUSO("ru", "russian"),
    POLACO("pl", "polish"),
    CHINO("zh", "chinese"),
    JAPONES("ja", "japanese"),
    OTRO("other", "other");

    private final String codigo;
    private final String nombreCompleto;

    Idiomas(String codigo, String nombreCompleto) {
        this.codigo = codigo;
        this.nombreCompleto = nombreCompleto;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getNombreCompleto() {
        return this.nombreCompleto;
    }

    public static Idiomas fromCodigo(String codigo) {
        return Arrays.stream(Idiomas.values())
                .filter(idioma -> idioma.getCodigo().equals(codigo))
                .findFirst()
                .orElse(OTRO);
    }

    public static Idiomas fromNombreCompleto(String nombreCompleto) {
        return Arrays.stream(Idiomas.values())
                .filter(idioma -> idioma.getNombreCompleto().equalsIgnoreCase(nombreCompleto))
                .findFirst()
                .orElse(OTRO);
    }
}