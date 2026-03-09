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

## Cambio 3 — Manejo global de excepciones

*Ubicación*

backend/src/main/java/com/iglesia/exception/GlobalExceptionHandler.java

*Código*
package com.iglesia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarError(RuntimeException ex){

        Map<String,Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}


*Prueba funcional*

GET
http://localhost:8080/api/users/999

*Resultado esperado*

{
 "timestamp": "2026-03-09T20:00:00",
 "status": 400,
 "error": "Bad Request",
 "message": "Usuario no encontrado"
}

## Cambio 4 — Validación de datos en backend

*Ubicación*

backend/src/main/java/com/iglesia/controller/AuthController.java
*Código*
package com.iglesia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request){
        return "Login procesado correctamente";
    }

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ){}
}

*Prueba funcional*

GET
http://localhost:8080/api/auth/login

*Resultado esperado*

El backend rechaza la solicitud con error 400 Bad Request debido a que los datos enviados no cumplen con las validaciones.

Esto demuestra que el backend ahora valida la integridad de los datos antes de procesarlos.