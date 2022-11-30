package org.mmanzano.springcloud.msvc.cursos.services;

import org.mmanzano.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.mmanzano.springcloud.msvc.cursos.models.Usuario;
import org.mmanzano.springcloud.msvc.cursos.models.entity.Curso;
import org.mmanzano.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.mmanzano.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository repository;

    @Autowired
    private UsuarioClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> getCourses() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> getCourse(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso save(Curso course) {
        return repository.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCursoUsuarioByUserId(Long id) {
        repository.deleteCursoUsuarioById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> getCourseByIdWithUsers(Long id) {
        Optional<Curso> oc = repository.findById(id);
        if (oc.isPresent()){
            Curso curso = oc.get();

            if (!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios().stream().map(cu -> cu.getUsuarioId() )
                        .collect(Collectors.toList());

                curso.setUsuarios(client.getUsersByCourse(ids));
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> assigngUser(Usuario user, Long courseId) {
        Optional<Curso> oc = repository.findById(courseId);

        if(oc.isPresent()){
            Usuario usurioMsvc = client.getUser(user.getId());
            Curso curso = oc.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usurioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usurioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> createUser(Usuario user, Long courseId) {
        Optional<Curso> oc = repository.findById(courseId);

        if (oc.isPresent()){
            Usuario usuarioMsvc = client.CreateUser(user);
            Curso curso = oc.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> deleteUser(Usuario user, Long courseId) {
        Optional<Curso> oc = repository.findById(courseId);

        if(oc.isPresent()){
            Usuario usurioMsvc = client.getUser(user.getId());
            Curso curso = oc.get();

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usurioMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usurioMsvc);
        }
        return Optional.empty();
    }
}
