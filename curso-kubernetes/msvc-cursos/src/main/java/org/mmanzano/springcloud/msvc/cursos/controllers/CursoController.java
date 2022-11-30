package org.mmanzano.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import org.mmanzano.springcloud.msvc.cursos.models.Usuario;
import org.mmanzano.springcloud.msvc.cursos.models.entity.Curso;
import org.mmanzano.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> getCourses(){
        return ResponseEntity.ok(service.getCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id){

        Optional<Curso> c = service.getCourseByIdWithUsers(id); //service.getCourse(id);
        if (c.isPresent())
            return ResponseEntity.ok(c);

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> saveCourse(@Valid @RequestBody Curso course, BindingResult result){

        if (result.hasErrors()){
            return  validate(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCourse(@Valid @RequestBody Curso course, @PathVariable Long id, BindingResult result){

        if (result.hasErrors()){
            return  validate(result);
        }

        Optional<Curso> cursoOptional = service.getCourse(id);
        if (cursoOptional.isPresent()){
            Curso courseDb = cursoOptional.get();

            courseDb.setNombre(course.getNombre());

            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDb));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id){

        Optional<Curso> optionalCurso = service.getCourse(id);
        if (optionalCurso.isPresent()){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity deleteCursoUsuario(@PathVariable Long id){
        service.deleteCursoUsuarioByUserId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> assingUser(@RequestBody Usuario user, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.assigngUser(user,cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Collections.singletonMap("mensaje","Error: " + e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> createUser(@RequestBody Usuario user, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.createUser(user,cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Collections.singletonMap("mensaje","Error: " + e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> deleteUser(@RequestBody Usuario user, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.deleteUser(user,cursoId);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Collections.singletonMap("mensaje","Error: " + e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validate(BindingResult result) {
        Map<String,String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
