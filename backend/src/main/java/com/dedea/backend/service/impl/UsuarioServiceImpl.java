package com.dedea.backend.service.impl;

import com.dedea.backend.dto.RegistroRequest;
import com.dedea.backend.dto.UsuarioResponse;
import com.dedea.backend.exception.EmailYaRegistradoException;
import com.dedea.backend.model.Usuario;
import com.dedea.backend.model.enums.TipoUsuario;
import com.dedea.backend.repository.UsuarioRepository;
import com.dedea.backend.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
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
}
