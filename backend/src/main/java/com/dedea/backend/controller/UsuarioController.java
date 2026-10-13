package com.dedea.backend.controller;

import com.dedea.backend.dto.LoginRequest;
import com.dedea.backend.dto.RegistroRequest;
import com.dedea.backend.dto.TokenResponse;
import com.dedea.backend.dto.UsuarioResponse;
import com.dedea.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController (UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse registrar(@Valid @RequestBody RegistroRequest request){
        return usuarioService.registrar(request);
    }

    @PostMapping("/login")
    public TokenResponse login (@Valid @RequestBody LoginRequest request){
        return usuarioService.login(request);
    }

    // El id sale del token firmado, nunca de la URL: así nadie puede pedir los datos de otro
    @GetMapping("/me")
    public UsuarioResponse me(@AuthenticationPrincipal Long usuarioId) {
        return usuarioService.obtener(usuarioId);
    }

}
