## Cambio 1 — Endpoint de prueba del backend

*Ubicación*

backend/src/main/java/com/iglesia/controller/TestController.java

*Código*

package com.iglesia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String test(){
        return "Backend funcionando correctamente";
    }

}

*Prueba funcional*

GET
http://localhost:8080/api/test

*Resultado esperado*

Backend funcionando correctamente

Esto demuestra que el backend sigue funcionando.

## Cambio 2 — Crear la capa Service y mover lógica fuera del controller

*Ubicación*

backend/src/main/java/com/iglesia/service/UserService.java

backend/src/main/java/com/iglesia/controller/UserController.java

*Código*
UserService.java

package com.iglesia.service;

import com.iglesia.model.AppUser;
import com.iglesia.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser crearUsuario(String email, String password) {

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));

        return appUserRepository.save(user);
    }
}

UserController

package com.iglesia.controller;

import com.iglesia.model.AppUser;
import com.iglesia.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public AppUser crearUsuario(@RequestParam String email, @RequestParam String password) {
        return userService.crearUsuario(email, password);
    }
}

*Prueba funcional*

GET
http://localhost:8080/api/users

*Resultado esperado*

Se crea correctamente un usuario en la base de datos.

Esto demuestra que la lógica de negocio fue movida desde el controlador hacia una capa de servicio.