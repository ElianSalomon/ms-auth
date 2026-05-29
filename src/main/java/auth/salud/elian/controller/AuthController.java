package auth.salud.elian.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import auth.salud.elian.dto.AuthResponse;
import auth.salud.elian.dto.LoginRequest;
import auth.salud.elian.dto.RegisterRequest;
import auth.salud.elian.entity.Usuario;
import auth.salud.elian.repository.UsuarioRepository;
import auth.salud.elian.security.JwtService;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse("El correo ya esta registrado", null, null, null, request.getEmail(), null, null));
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol() == null || request.getRol().isBlank() ? "MEDICO" : request.getRol());
        usuario.setActivo(true);

        Usuario savedUsuario = usuarioRepository.save(usuario);

        AuthResponse response = new AuthResponse(
                "Usuario registrado correctamente",
                savedUsuario.getIdUsuario(),
                savedUsuario.getNombre(),
                savedUsuario.getApellido(),
                savedUsuario.getEmail(),
                savedUsuario.getRol(),
                null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);

        if (usuario == null || Boolean.FALSE.equals(usuario.getActivo())
                || !passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Credenciales invalidas", null, null, null, request.getEmail(), null, null));
        }

        String token = jwtService.generateToken(usuario);

        AuthResponse response = new AuthResponse(
                "Login correcto",
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol(),
                token);

        return ResponseEntity.ok(response);
    }
}
