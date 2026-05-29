package auth.salud.elian.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String mensaje;
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String token;
}
