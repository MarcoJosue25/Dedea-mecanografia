package com.dedea.backend.service;

import com.dedea.backend.dto.LoginRequest;
import com.dedea.backend.dto.RegistroRequest;
import com.dedea.backend.dto.TokenResponse;
import com.dedea.backend.dto.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse registrar(RegistroRequest request);

    TokenResponse login(LoginRequest request);

    UsuarioResponse obtener(Long id);
}
