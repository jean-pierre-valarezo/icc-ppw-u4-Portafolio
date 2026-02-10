Informe Técnico: Módulo de Gestión y Reportes (Backend)
Proyecto: Portafolio - Dashboard del Programador

Tecnología Principal: Java 17, Spring Boot 4.0, PostgreSQL (Docker)

1. Objetivo General
Implementar una infraestructura robusta en el lado del servidor para centralizar la gestión de citas de asesoría, permitiendo la visualización de estadísticas en tiempo real y la exportación de datos en formatos profesionales (PDF/Excel).

Arquitectura y Capas del Sistema
Se utilizó una arquitectura en capas para garantizar la escalabilidad y el mantenimiento:

Capa de Controlador (DashboardController): Exposición de endpoints REST seguros.

GET /resumen: Retorna el conteo de citas filtrado por estado (Pendiente, Aprobada, Rechazada).

GET /reporte/pdf y /excel: Generación dinámica de archivos.

Capa de Servicio (AppointmentService): Lógica de negocio avanzada. Implementa el filtrado por UUID del programador autenticado y la construcción de documentos usando flujos de salida (OutputStream).

Capa de Persistencia (Repository): Consultas optimizadas en JPA/Hibernate para extraer información específica del programador desde la base de datos PostgreSQL alojada en Docker.

Funcionalidades Destacadas
Generación de Reportes Dinámicos
Se integraron librerías especializadas para la conversión de datos relacionales a documentos:

PDF: Uso de OpenPDF/iText para crear tablas con formato profesional, incluyendo encabezados y estados de citas.

Excel: Implementación de Apache POI para la creación de libros de trabajo (SXSSF) optimizados para el manejo de grandes volúmenes de datos.

Seguridad y Control de Acceso (JWT)
El sistema utiliza JSON Web Tokens (JWT) para proteger los datos.

Se configuró un filtro de seguridad que valida el rol del usuario (ROLE_PROGRAMMER) antes de permitir el acceso a los métodos de descarga.

Protección de Endpoints: Uso de SecurityFilterChain para prevenir accesos no autorizados a la información privada de los asesores.

Infraestructura y Despliegue (DevOps)
Base de Datos Local: Configuración mediante Docker Compose, asegurando un entorno de desarrollo aislado y replicable.

CI/CD (Render): Sincronización automática con GitHub. El backend fue configurado para manejar variables de entorno dinámicas, permitiendo que el sistema funcione tanto en localhost como en producción de manera transparente.
