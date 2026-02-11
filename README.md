#  Sistema de Gestión de Asesorías – Portafolio 

Este proyecto corresponde al desarrollo de un **sistema web completo de gestión de asesorías**, diseñado como un portafolio profesional.  
La aplicación permite a los usuarios **agendar asesorías**, a los **programadores gestionar sus citas**, y al sistema administrar horarios, usuarios y estados de cada asesoría.

El proyecto está desarrollado bajo una **arquitectura cliente-servidor**, utilizando **Angular** en el frontend y **Spring Boot** en el backend.

---

##  Arquitectura del Sistema

El sistema está compuesto por dos repositorios principales:

- **Frontend (Angular)**  
  Repositorio: `icc-ppw-u2-Portafolio`

- **Backend (Spring Boot)**  
  Repositorio: `icc-ppw-u4-Portafolio`

Ambos se comunican mediante una **API REST segura**.

---

## ⚙️ Backend – Spring Boot (Énfasis Principal)

### 🛠 Tecnologías Utilizadas

- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- Hibernate
- PostgreSQL
- Docker
- Render (Despliegue)


---

##  Seguridad y Autenticación

El sistema implementa **JWT (JSON Web Token)** para la autenticación y autorización:

- Login con generación de token
- Filtro `JwtFilter` para validar cada request
- Protección de endpoints por rol
- Uso de `@PreAuthorize` para control de acceso

### Roles del sistema

- `CLIENT`
- `PROGRAMMER`
- `ADMIN`

---

##  Gestión de Horarios

Los programadores pueden crear y gestionar horarios disponibles.

### Estados del horario

- `AVAILABLE`
- `BOOKED`

El backend se encarga de:
- Cambiar el estado al reservar una cita
- Liberar el horario si la cita es rechazada

---

##  Gestión de Asesorías

El flujo completo de asesorías es controlado desde el backend.

### Estados de una asesoría

- `PENDING`
- `ACCEPTED`
- `REJECTED`

### Funcionalidades principales

- Agendar asesoría (clientes)
- Ver citas propias
- Ver citas entrantes (programadores)
- Aceptar o rechazar asesorías
- Actualizar estados automáticamente

---

##  Dashboard del Programador (Backend)

Se implementó un **endpoint de resumen** que permite obtener estadísticas de asesorías por programador.

### Endpoint

```http
GET /api/appointments/summary
