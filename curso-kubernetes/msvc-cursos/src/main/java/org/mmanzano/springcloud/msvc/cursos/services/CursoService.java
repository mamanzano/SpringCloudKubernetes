package org.mmanzano.springcloud.msvc.cursos.services;

import org.mmanzano.springcloud.msvc.cursos.models.Usuario;
import org.mmanzano.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> getCourses();
    Optional<Curso> getCourse(Long id);
    Curso save(Curso course);
    void delete(Long id);

    void deleteCursoUsuarioByUserId(Long id);

    Optional<Curso> getCourseByIdWithUsers(Long id);

    Optional<Usuario> assigngUser(Usuario user, Long courseId);
    Optional<Usuario> createUser(Usuario user, Long courseId);
    Optional<Usuario> deleteUser(Usuario user, Long courseId);
}
