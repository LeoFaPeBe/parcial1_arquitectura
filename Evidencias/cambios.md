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