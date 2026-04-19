package com.ridersmap.mapa_riders_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    // Solo permite escribir la contraseña (al registrar), nunca leerla (GET)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nombre;

    //  Campo de rol necesario para Spring Security
    private String rol; 

    // Añadido orphanRemoval=true para asegurar que las motos se borren al borrar el usuario
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("usuario")
    private List<Moto> motos = new ArrayList<>();

    // Añadido orphanRemoval=true para asegurar que las rutas se borren al borrar el usuario
    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("creador")
    private List<Ruta> rutas = new ArrayList<>();

    //  Añadido CascadeType.REMOVE para limpiar la tabla intermedia de favoritos
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
      name = "usuarios_rutas_favoritas", 
      joinColumns = @JoinColumn(name = "usuario_id"), 
      inverseJoinColumns = @JoinColumn(name = "ruta_id"))
    @JsonIgnoreProperties({"creador", "usuariosFavoritos"}) 
    private List<Ruta> rutasFavoritas = new ArrayList<>();

    // Relación con Encuentros (usando "usuario" para coincidir con Encuentro.java)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("usuario")
    private List<Encuentro> encuentros = new ArrayList<>();

    // --- AÑADIDO PARA SOLUCIONAR ERROR DE BORRADO Y RECURSIÓN ---
    // Esta relación permite mapear la asistencia a otros encuentros y limpiar la tabla intermedia
    @ManyToMany(mappedBy = "asistentes", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    // Añadimos "asistentes" y "usuario" para evitar que Jackson entre en bucle infinito
    @JsonIgnoreProperties({"asistentes", "usuario"}) 
    private List<Encuentro> encuentrosAsistidos = new ArrayList<>();

    public Usuario() {}

    public Usuario(String email, String password, String nombre) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Getter y Setter para el nuevo campo Rol
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public List<Moto> getMotos() { return motos; }
    public void setMotos(List<Moto> motos) { this.motos = motos; }

    public List<Ruta> getRutas() { return rutas; }
    public void setRutas(List<Ruta> rutas) { this.rutas = rutas; }

    public List<Ruta> getRutasFavoritas() { return rutasFavoritas; }
    public void setRutasFavoritas(List<Ruta> rutasFavoritas) { this.rutasFavoritas = rutasFavoritas; }

    // NUEVOS Getter y Setter para Encuentros
    public List<Encuentro> getEncuentros() { return encuentros; }
    public void setEncuentros(List<Encuentro> encuentros) { this.encuentros = encuentros; }

    // --- NUEVOS GETTER Y SETTER PARA ASISTENCIAS ---
    public List<Encuentro> getEncuentrosAsistidos() { return encuentrosAsistidos; }
    public void setEncuentrosAsistidos(List<Encuentro> encuentrosAsistidos) { this.encuentrosAsistidos = encuentrosAsistidos; }

    public void addRutaFavorita(Ruta ruta) {
        this.rutasFavoritas.add(ruta);
    }
}
