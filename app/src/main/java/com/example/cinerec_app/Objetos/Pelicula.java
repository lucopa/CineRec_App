package com.example.cinerec_app.Objetos;

public class Pelicula {

    String id_peli, uid_usuario, correo_usuario, fecha_hora_actual, titulo, descripcion, fecha_peli, estado;

    public Pelicula() {

    }

    public Pelicula(String id_peli, String uid_usuario, String correo_usuario, String fecha_hora_actual, String titulo, String descripcion, String fecha_peli, String estado) {
        this.id_peli = id_peli;
        this.uid_usuario = uid_usuario;
        this.correo_usuario = correo_usuario;
        this.fecha_hora_actual = fecha_hora_actual;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_peli = fecha_peli;
        this.estado = estado;
    }

    public String getId_peli() {
        return id_peli;
    }

    public void setId_peli(String id_peli) {
        this.id_peli = id_peli;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getFecha_hora_actual() {
        return fecha_hora_actual;
    }

    public void setFecha_hora_actual(String fecha_hora_actual) {
        this.fecha_hora_actual = fecha_hora_actual;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_peli() {
        return fecha_peli;
    }

    public void setFecha_peli(String fecha_peli) {
        this.fecha_peli = fecha_peli;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
