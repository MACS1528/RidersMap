package com.ridersmap.mapa_riders_backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Encuentro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    // Añadimos 'encuentrosAsistidos' para evitar la recursión desde el creador
    @JsonIgnoreProperties({"motos", "rutas", "rutasFavoritas", "encuentros", "password", "rol", "encuentrosAsistidos"})
    private Usuario usuario;

    // Se añade CascadeType.ALL para asegurar que al borrar el encuentro se limpien las asistencias
    // Esto evita el error de clave foránea en la tabla asistencias_encuentros
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})
    @JoinTable(
        name = "asistencias_encuentros",
        joinColumns = @JoinColumn(name = "encuentro_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    //  Se añade explícitamente 'encuentrosAsistidos' para romper el bucle infinito con los asistentes
    @JsonIgnoreProperties({"motos", "rutas", "rutasFavoritas", "encuentros", "password", "rol", "encuentrosAsistidos"})
    private List<Usuario> asistentes = new ArrayList<>();

    public Encuentro() {}

    // ---  MÉTODO PARA EL FRONTEND ---
    // Jackson detectará este getter y enviará "numAsistentes" en el JSON automáticamente
    public int getNumAsistentes() {
        return (asistentes != null) ? asistentes.size() : 0;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Usuario> getAsistentes() { return asistentes; }
    public void setAsistentes(List<Usuario> asistentes) { this.asistentes = asistentes; }
    
    // MÉTODO Para limpiar asistentes manualmente si fuera necesario
    public void removerAsistentes() {
        this.asistentes.clear();
    }
}
