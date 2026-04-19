package com.ridersmap.mapa_riders_backend.controller;



import com.ridersmap.mapa_riders_backend.model.Moto;

import com.ridersmap.mapa_riders_backend.model.Usuario;

import com.ridersmap.mapa_riders_backend.repository.MotoRepository;

import com.ridersmap.mapa_riders_backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;



import java.util.List;



@RestController

@RequestMapping("/api/motos")

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})

public class MotoController {



    @Autowired

    private MotoRepository motoRepository;

    

    @Autowired

    private UsuarioRepository usuarioRepository; 



    /**

     * MODIFICADO: GET /api/motos

     * Si es ADMIN ve todas. Si es USER ve las suyas.

     */

    @GetMapping

    public ResponseEntity<List<Moto>> getAllMotos() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUserName = authentication.getName();

        

        Usuario currentUser = usuarioRepository.findByEmail(currentUserName)

                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));



        // LÓGICA DE CONTROL TOTAL

        if ("ADMIN".equalsIgnoreCase(currentUser.getRol())) {

            // El administrador ve la lista de TODA la base de datos

            return new ResponseEntity<>(motoRepository.findAll(), HttpStatus.OK);

        } else {

            // El usuario normal solo ve su lista personal

            return new ResponseEntity<>(currentUser.getMotos(), HttpStatus.OK);

        }

    }



    /**

     * POST /api/motos : Crea una nueva moto.

     * El admin puede añadir motos a su perfil también.

     */

    @PostMapping

    public ResponseEntity<Moto> createMoto(@RequestBody Moto moto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUserName = authentication.getName(); 

        

        Usuario currentUser = usuarioRepository.findByEmail(currentUserName)

                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));



        moto.setUsuario(currentUser);

        Moto newMoto = motoRepository.save(moto);

        return new ResponseEntity<>(newMoto, HttpStatus.CREATED);

    }



    /**

     * DELETE /api/motos/{id}

     * El admin puede borrar cualquier moto por ID.

     */

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteMoto(@PathVariable Long id) {


        // Permitimos que el Admin borre cualquier cosa con solo tener el ID.

        if (motoRepository.existsById(id)) {

            motoRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }



    // Los métodos PUT y GET por ID se mantienen ...

    @GetMapping("/{id}")

    public ResponseEntity<Moto> getMotoById(@PathVariable Long id) {

        return motoRepository.findById(id)

                .map(moto -> new ResponseEntity<>(moto, HttpStatus.OK))

                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}
