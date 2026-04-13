package com.ridersmap.mapa_riders_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importación añadida

@Entity
@Table(name = "motos")
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;

    private String modelo;

    private int cilindrada;

    private int anio;
    
    // --- RELACIÓN MANY-TO-ONE CON USUARIO (Propietario) ---
    @ManyToOne 
    @JoinColumn(name = "usuario_id", nullable = false)
    // MODIFICADO: Añadimos ignore para evitar bucles infinitos con las nuevas listas del Usuario
    @JsonIgnoreProperties({"motos", "rutas", "rutasFavoritas", "encuentros", "password", "rol"})
    private Usuario usuario; 

    // --- CONSTRUCTORES ---
    
    public Moto() {
    }

    // Constructor con campos básicos
    public Moto(String marca, String modelo, int cilindrada, int anio) {
        this.marca = marca;
        this.modelo = modelo;
        this.cilindrada = cilindrada;
        this.anio = anio;
    }

    // --- GETTERS y SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}