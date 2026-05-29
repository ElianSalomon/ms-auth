package auth.salud.elian.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import auth.salud.elian.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
