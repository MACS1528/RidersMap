 package com.ridersmap.mapa_riders_backend.controller;



import com.ridersmap.mapa_riders_backend.model.Ruta;

import com.ridersmap.mapa_riders_backend.model.Usuario;

import com.ridersmap.mapa_riders_backend.repository.RutaRepository;

import com.ridersmap.mapa_riders_backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;



import java.util.List;



@RestController

@RequestMapping("/api/rutas")

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})

public class RutaController {



    @Autowired

    private RutaRepository rutaRepository;

    

    @Autowired

    private UsuarioRepository usuarioRepository; 



    /**

     * MODIFICADO: GET /api/rutas

     * El Administrador ve TODAS las rutas de la DB.

     * El Usuario estándar ve solo las suyas (para evitar que el Dashboard se colapse).

     */

    @GetMapping

    public ResponseEntity<List<Ruta>> getRutas() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Usuario currentUser = usuarioRepository.findByEmail(authentication.getName())

                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));



        if ("ADMIN".equalsIgnoreCase(currentUser.getRol())) {

            return new ResponseEntity<>(rutaRepository.findAll(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(rutaRepository.findByCreador(currentUser), HttpStatus.OK);

        }

    }



    @PostMapping

    public ResponseEntity<Ruta> createRuta(@RequestBody Ruta ruta) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Usuario currentUser = usuarioRepository.findByEmail(authentication.getName())

                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        

        ruta.setCreador(currentUser);

        return new ResponseEntity<>(rutaRepository.save(ruta), HttpStatus.CREATED);

    }



    // --- MÉTODOS DE BORRADO Y ACTUALIZACIÓN ---



    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteRuta(@PathVariable Long id) {

        // Al no validar el creador aquí, el ADMIN puede borrar cualquier ruta por ID

        if (rutaRepository.existsById(id)) {

            rutaRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }



    @PutMapping("/{id}")

    public ResponseEntity<Ruta> updateRuta(@PathVariable Long id, @RequestBody Ruta updatedRuta) {

        return rutaRepository.findById(id)

                .map(ruta -> {

                    ruta.setNombre(updatedRuta.getNombre());

                    ruta.setDescripcion(updatedRuta.getDescripcion());

                    ruta.setDistanciaKm(updatedRuta.getDistanciaKm());

                    ruta.setLatInicio(updatedRuta.getLatInicio());

                    ruta.setLngInicio(updatedRuta.getLngInicio());

                    return new ResponseEntity<>(rutaRepository.save(ruta), HttpStatus.OK);

                })

                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }



    // --- FAVORITOS Y OTROS (Se mantienen igual) ---



    @GetMapping("/favoritas")

    public List<Ruta> getMisFavoritas() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())

                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return usuario.getRutasFavoritas();

    }

    

    @GetMapping("/{id}")

    public ResponseEntity<Ruta> getRutaById(@PathVariable Long id) {

        return rutaRepository.findById(id)

                .map(ruta -> new ResponseEntity<>(ruta, HttpStatus.OK))

                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}