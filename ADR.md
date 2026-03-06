## ADR 1 — Implementación de endpoint de prueba del backend

**Título:** Implementación de endpoint de verificación del backend

**Estado:** Implementado

**Contexto:**

El sistema de gestión parroquial utiliza una arquitectura cliente-servidor compuesta por un frontend desarrollado en Angular y un backend desarrollado en Spring Boot con el lenguaje Java. Durante el proceso de desarrollo y pruebas del sistema, se identificó la necesidad de contar con un mecanismo sencillo que permitiera verificar rápidamente el funcionamiento del backend de forma independiente al frontend.

Debido a que el sistema también utiliza contenedores mediante Docker y orquestación con Docker Compose, resulta útil disponer de un endpoint que confirme que el servicio backend se encuentra activo y respondiendo correctamente.

Este tipo de endpoint facilita las pruebas funcionales, el diagnóstico de errores y la verificación del correcto despliegue del sistema.

**Diagrama MER:**

Las principales entidades identificadas en la base de datos son:

- Usuarios

- Personas

- Cursos

- Inscripciones

- Ofrendas

- Pagos

Relaciones principales:

- Persona 1 — N Inscripciones

- Curso 1 — N Inscripciones

- Persona 1 — N Ofrendas

- Persona 1 — N Pagos

**NOTA: Utilizamos el mismo diagrama para todos los ADR, ya que los cambios realizados corresponden a la arquitectura del sistema y no a modificaciones en la base de datos.**

**Decisión:**

Se decidió implementar un endpoint de prueba dentro del backend que permita verificar de manera rápida si el servicio se encuentra funcionando correctamente.

Para ello se creó un nuevo controlador denominado TestController, el cual expone el endpoint /api/test. Este endpoint devuelve un mensaje simple confirmando que el backend se encuentra activo.

El controlador fue ubicado en el paquete:

backend/src/main/java/com/iglesia/controller/

**Consecuencias:**

Impacto positivo:

- Permite verificar rápidamente el funcionamiento del backend.

- Facilita las pruebas funcionales del sistema.

- Ayuda en el diagnóstico de errores durante el desarrollo o despliegue.

- Permite validar la conectividad entre servicios.

Trade-offs:

- Se agrega un endpoint adicional al sistema que no forma parte de la lógica principal de negocio.

Debe controlarse su uso en entornos de producción si no es necesario.

## ADR 2 — Aplicación del principio SRP (Single Responsibility Principle)

**Título:** Aplicación del principio de responsabilidad única (SRP)

**Estado:** Propuesto

**Contexto:**
El sistema actual presenta controladores que pueden contener múltiples responsabilidades, incluyendo manejo de solicitudes HTTP, lógica de negocio y acceso a datos.

Según los principios SOLID, una clase debe tener una única responsabilidad dentro del sistema. La falta de este principio puede generar código difícil de mantener y escalar.

**Decisión:**
Se aplicará el principio SRP separando responsabilidades en tres capas principales:

- Controller: manejo de solicitudes HTTP

- Service: lógica de negocio

- Repository: acceso a base de datos

Esto permitirá que cada componente tenga una función específica dentro de la arquitectura.

**Cambios implementados:**

- Creación de la capa service.

- Traslado de la lógica de negocio desde los controladores hacia los servicios.

- Uso de repositorios para el acceso a datos.

**Pruebas funcionales:**

Se verificó el funcionamiento correcto del sistema realizando operaciones de autenticación y navegación entre módulos.

**Consecuencias:**

Impacto positivo:

- Mejor organización del código

- Mayor mantenibilidad

- Reducción del acoplamiento

Trade-offs:

- Mayor cantidad de archivos en el proyecto.

## ADR 3 — Implementación de Service Layer

**Título:** Implementación de una capa de servicios (Service Layer)

**Estado:** Propuesto

**Contexto:**
Durante el diagnóstico del sistema se identificó que la lógica de negocio puede encontrarse mezclada con los controladores. Esto dificulta la reutilización de la lógica y aumenta el acoplamiento.

**Decisión:**
Se implementará una capa de servicios encargada de manejar la lógica de negocio del sistema.

Ejemplo de servicio:

UserService.java
CourseService.java
PaymentService.java

Los controladores únicamente delegarán las operaciones a estos servicios.

**Cambios implementados:**

- Creación de UserService.

- Refactorización de métodos de autenticación.

- Uso de servicios desde los controladores.

**Pruebas funcionales:**

Se realizaron pruebas de inicio de sesión y navegación del sistema para validar que la funcionalidad permanece intacta.

**Consecuencias:**

Impacto positivo:

- Mejor separación de responsabilidades

- Mayor reutilización del código

- Arquitectura más limpia

Trade-offs:

- Mayor complejidad inicial en la estructura del proyecto.

## ADR 4 — Manejo global de excepciones

**Título:** Implementación de manejo global de excepciones

**Estado:** Propuesto

**Contexto:**
Actualmente el sistema puede manejar errores directamente en los controladores, lo que genera repetición de código y dificulta el mantenimiento.

**Decisión:**
Se implementará un manejador global de excepciones utilizando @ControllerAdvice en Spring Boot.

**Archivo propuesto:**

- GlobalExceptionHandler.java

Esto permitirá centralizar el manejo de errores en toda la aplicación.

**Cambios implementados:**

- Creación de la clase GlobalExceptionHandler.

- Manejo de excepciones comunes como errores de autenticación o validación.

**Pruebas funcionales:**

Se realizaron pruebas generando errores de validación para verificar que el sistema responde correctamente.

**Consecuencias:**

Impacto positivo:

- Código más limpio

- Manejo consistente de errores

Trade-offs:

- Requiere definir correctamente los tipos de excepciones.

## ADR 5 — Inyección de dependencias por constructor

**Título:** Implementación de Dependency Injection mediante constructores

**Estado:** Propuesto

**Contexto:**
El sistema utiliza Spring Boot, el cual ofrece mecanismos de inyección de dependencias. Sin embargo, el uso de @Autowired en campos puede dificultar las pruebas unitarias.

**Decisión:**
Se implementará la inyección de dependencias por constructor, siguiendo el principio Dependency Inversion de SOLID.

Ejemplo:

public AuthController(AuthService authService){
    this.authService = authService;
}

**Cambios implementados:**

- Eliminación de @Autowired en atributos.

- Implementación de constructores para inyección de dependencias.

**Pruebas funcionales:**

- Se verificó el correcto funcionamiento del sistema realizando pruebas de autenticación.

**Consecuencias:**

Impacto positivo:

- Mayor facilidad para realizar pruebas unitarias

- Código más robusto

Trade-offs:

- Requiere modificar varias clases existentes.

## ADR 6 — Implementación del patrón Repository

**Título:** Uso del patrón Repository para acceso a datos

**Estado:** Propuesto

**Contexto:**
El sistema utiliza JPA para el acceso a datos. Sin embargo, es importante mantener una capa de abstracción que permita separar la lógica del dominio de la persistencia.


**Decisión:**
Se utilizará el patrón Repository mediante interfaces que extiendan JpaRepository.

Ejemplo:

UserRepository.java
PersonaRepository.java

**Cambios implementados:**

- Creación de interfaces de repositorio.

- Refactorización del acceso a base de datos.

**Pruebas funcionales:**

- Se realizaron pruebas CRUD para verificar la correcta persistencia de datos.

**Consecuencias:**

Impacto positivo:

- Abstracción de la capa de persistencia

- Mayor flexibilidad

Trade-offs:

- Dependencia del framework Spring Data.

## ADR 7 — Validación de datos en backend

**Título:** Implementación de validaciones mediante anotaciones

**Estado:** Propuesto

**Contexto:**
El sistema debe garantizar la integridad de los datos enviados desde el frontend.

**Decisión:**
Se implementarán validaciones utilizando anotaciones de Java:

@Valid
@NotNull
@Email

**Cambios implementados:**

Se agregaron validaciones en los DTO y controladores.

**Pruebas funcionales:**

Se enviaron solicitudes con datos inválidos para comprobar la respuesta del sistema.

**Consecuencias:**

Impacto positivo:

- Mayor integridad de datos

- Prevención de errores

Trade-offs:

- Mayor control en la validación de formularios.

## ADR 8 — Implementación del patrón Observer

**Título:** Implementación del patrón Observer para eventos del sistema

**Estado:** Propuesto

**Contexto:**
El sistema registra eventos importantes como pagos y ofrendas.

**Decisión:**
Se utilizará el patrón Observer para manejar eventos del sistema, permitiendo que diferentes componentes reaccionen ante estos eventos.

Ejemplo:

- Evento de registro de pago

- Evento de registro de ofrenda

**Cambios implementados:**

Creación de un sistema de eventos utilizando mecanismos de Spring.

**Pruebas funcionales:**

Se verificó que los eventos se ejecutan correctamente tras registrar pagos.

**Consecuencias:**

Impacto positivo:

- Arquitectura más desacoplada

- Mayor escalabilidad

Trade-offs:

- Mayor complejidad en la gestión de eventos.

## ADR 9 — Modularización del backend

**Título:** Organización modular del backend

**Estado:** Propuesto

**Contexto:**
El proyecto presenta varios módulos funcionales que deben mantenerse organizados para facilitar su mantenimiento.

**Decisión:**
Se organizará el backend en módulos funcionales:

auth
users
courses
payments

**Cambios implementados:**

Reorganización de paquetes en el backend.

**Pruebas funcionales:**

Se verificó que las rutas del sistema continúan funcionando correctamente.

**Consecuencias:**

Impacto positivo:

- Mejor organización del código

- Mayor escalabilidad

Trade-offs:

- Requiere refactorización inicial.

## ADR 10 — Implementación de logging centralizado

**Título:** Implementación de sistema de logging centralizado

**Estado:** Propuesto

**Contexto:**
El sistema requiere mecanismos de monitoreo para registrar eventos y errores.

**Decisión:**

Se implementará un sistema de logging centralizado utilizando SLF4J y Logback.

Esto permitirá registrar eventos importantes del sistema.

**Cambios implementados:**

Configuración de logging en el backend.

**Pruebas funcionales:**

Se verificó que las operaciones del sistema generan registros en los logs.

**Consecuencias:**

Impacto positivo:

- Mejor monitoreo del sistema

- Facilita la depuración de errores

Trade-offs:

- Generación de archivos de log adicionales.