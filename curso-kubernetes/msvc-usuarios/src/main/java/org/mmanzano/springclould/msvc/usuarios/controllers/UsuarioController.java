package org.mmanzano.springclould.msvc.usuarios.controllers;


import org.mmanzano.springclould.msvc.usuarios.models.entity.Usuario;
import org.mmanzano.springclould.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> getUsers(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = service.byId(id);

        if (usuarioOptional.isPresent())
            return ResponseEntity.ok(usuarioOptional.get());

        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody Usuario user, BindingResult result){

        if (result.hasErrors()){
            return  validate(result);
        }

        if (service.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje","Existe un usuario con el mismo email"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user)) ;
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody Usuario user, @PathVariable Long id, BindingResult result){

        Optional<Usuario> usuarioOptional = service.byId(id);

        if (result.hasErrors()){
            return  validate(result);
        }

        if (usuarioOptional.isPresent()){
            Usuario userDb = usuarioOptional.get();

            if (!user.getEmail().isEmpty() && !user.getEmail().equalsIgnoreCase(userDb.getEmail()) && service.byEmail(user.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("mensaje","Existe un usuario con el mismo email"));
            }

            userDb.setNombre(user.getNombre());
            userDb.setEmail(user.getEmail());
            userDb.setPassword(user.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userDb));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){

        Optional<Usuario> usuarioOptional = service.byId(id);

        if (usuarioOptional.isPresent()){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }

        return  ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-curso")
    public ResponseEntity<?> getUsersByCourse(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.byIds(ids));
    }

    private ResponseEntity<?> validate(BindingResult result) {
        Map<String,String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
