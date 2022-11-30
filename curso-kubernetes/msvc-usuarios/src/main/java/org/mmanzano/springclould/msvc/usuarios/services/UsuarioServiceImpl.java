package org.mmanzano.springclould.msvc.usuarios.services;

import org.mmanzano.springclould.msvc.usuarios.clients.CursoClientRest;
import org.mmanzano.springclould.msvc.usuarios.models.entity.Usuario;
import org.mmanzano.springclould.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private CursoClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> byId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario save(Usuario user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
        client.deleteCursoUsuario(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> byIds(Iterable<Long> ids) {
        return (List<Usuario>) repository.findAllById(ids);
    }

    @Override
    @Transactional
    public Optional<Usuario> byEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
