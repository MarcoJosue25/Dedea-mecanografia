package com.dedea.backend.service.impl;

import com.dedea.backend.dto.LoginRequest;
import com.dedea.backend.dto.RegistroRequest;
import com.dedea.backend.dto.TokenResponse;
import com.dedea.backend.dto.UsuarioResponse;
import com.dedea.backend.exception.CredencialesInvalidasException;
import com.dedea.backend.exception.EmailYaRegistradoException;
import com.dedea.backend.model.Usuario;
import com.dedea.backend.model.enums.TipoUsuario;
import com.dedea.backend.repository.UsuarioRepository;
import com.dedea.backend.security.JwtService;
import com.dedea.backend.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository,JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    @Override
    public UsuarioResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailYaRegistradoException("El email ya está registrado");
        }
        Usuario usuario = Usuario.builder().nombre(request.getNombre()).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).tipo(TipoUsuario.EMAIL).build();
        Usuario guardado = usuarioRepository.save(usuario);
        return new UsuarioResponse(guardado.getId(), guardado.getNombre(), guardado.getEmail());
    }

    @Override
    public TokenResponse login(LoginRequest request){
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales inválidas"));
        if(!passwordEncoder.matches(request.getPassword(), usuario.getPassword())){
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }
        return new TokenResponse(jwtService.generarToken(usuario.getId()));
    }
}
