package org.mmanzano.springcloud.msvc.cursos.clients;

import org.mmanzano.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "msvc-usuarios:8001")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario getUser(@PathVariable Long id);

    @PostMapping("/")
    Usuario CreateUser(@RequestBody Usuario user);

    @GetMapping("/usuarios-curso")
    List<Usuario> getUsersByCourse(@RequestParam Iterable<Long> ids);
}
