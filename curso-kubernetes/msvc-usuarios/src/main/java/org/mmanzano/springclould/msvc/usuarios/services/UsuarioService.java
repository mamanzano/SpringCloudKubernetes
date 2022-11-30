package org.mmanzano.springclould.msvc.usuarios.services;

import org.mmanzano.springclould.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> byId(Long id);
    Usuario save(Usuario user);
    void delete(Long id);

    List<Usuario> byIds(Iterable<Long> ids);

    Optional<Usuario> byEmail(String email);

    boolean existsByEmail(String email);

}
