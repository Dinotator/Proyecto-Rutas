package com.upv.pm_2022.iti_27849_u2_equipo_02;

import java.io.Serializable;

public class Entidad implements Serializable {

    private int imgFoto;
    private String titulo;
    private String contenido;
    private String contenidoDos;

    public Entidad(int imgFoto, String titulo, String contenido, String contenidoDos) {
        this.imgFoto = imgFoto;
        this.titulo = titulo;
        this.contenido = contenido;
        this.contenidoDos = contenidoDos;
    }

    public int getImgFoto() {
        return imgFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public String getContenidoDos() {
        return contenidoDos;
    }
}
