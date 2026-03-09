package com.iglesia;

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

    @GetMapping("/{id}")
    public String buscarUsuario(@PathVariable Long id) {

        if (id == 999) {
            throw new RuntimeException("Usuario no encontrado");
        }

        return "Usuario encontrado";
    }
}