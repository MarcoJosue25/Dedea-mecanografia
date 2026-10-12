package com.dedea.backend.service;

import com.dedea.backend.dto.RegistroRequest;
import com.dedea.backend.dto.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse registrar(RegistroRequest request);
}
