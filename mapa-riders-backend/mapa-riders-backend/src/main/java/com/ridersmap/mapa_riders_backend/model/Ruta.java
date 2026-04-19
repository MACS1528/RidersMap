package com.ridersmap.mapa_riders_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "ruta")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private double distanciaKm;
    private double latInicio;
    private double lngInicio;
    private double latFin;
    private double lngFin;

    @ManyToOne
    @JoinColumn(name = "creador_id")
    // Añadimos "encuentros" y "rol" para evitar recursión infinita
    @JsonIgnoreProperties({"rutas", "rutasFavoritas", "motos", "encuentros", "password", "rol"})
    private Usuario creador;

    @ManyToMany(mappedBy = "rutasFavoritas")
    //  Añadimos "encuentros" y "rol" aquí también
    @JsonIgnoreProperties({"rutasFavoritas", "rutas", "motos", "encuentros", "password", "rol"})
    private List<Usuario> usuariosFavoritos;

    public Ruta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }
    public double getLatInicio() { return latInicio; }
    public void setLatInicio(double latInicio) { this.latInicio = latInicio; }
    public double getLngInicio() { return lngInicio; }
    public void setLngInicio(double lngInicio) { this.lngInicio = lngInicio; }
    public double getLatFin() { return latFin; }
    public void setLatFin(double latFin) { this.latFin = latFin; }
    public double getLngFin() { return lngFin; }
    public void setLngFin(double lngFin) { this.lngFin = lngFin; }
    public Usuario getCreador() { return creador; }
    public void setCreador(Usuario creador) { this.creador = creador; }
    public List<Usuario> getUsuariosFavoritos() { return usuariosFavoritos; }
    public void setUsuariosFavoritos(List<Usuario> usuariosFavoritos) { this.usuariosFavoritos = usuariosFavoritos; }
}
