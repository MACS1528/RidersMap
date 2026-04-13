 RidersMap - Proyecto de Fin de Grado
RidersMap es una plataforma web integral diseñada específicamente para la comunidad motociclista. La aplicación combina la gestión técnica del vehículo con el componente social de las rutas, ofreciendo una experiencia 360º para el motorista moderno.

 Funcionalidades Principales
 
 Gestión de Rutas: Creación, visualización y guardado de rutas automáticas utilizando tecnología GPS y mapas interactivos.

 Garaje Virtual: Registro detallado de motos, permitiendo llevar un control del mantenimiento y especificaciones técnicas.

 Encuentros (Meetups): Sistema social para crear u unirse a salidas grupales, fomentando la comunidad rider.

 Panel de Administración: Gestión total de usuarios, roles y contenidos para moderadores.

☁️ Clima en Tiempo Real: Integración de meteorología para planificar salidas seguras.

 Tecnologías Utilizadas
Backend
Lenguaje: Java 17+

Framework: Spring Boot (Spring Security, Spring Data JPA)

Gestión de Dependencias: Maven

Frontend
Lenguaje: JavaScript (ES6+), HTML5, CSS3.

Mapas: Leaflet.js & OSRM (Open Source Routing Machine).

APIs: OpenWeatherMap API para datos meteorológicos.

Base de Datos
Motor: MySQL

 Instrucciones de Instalación
Sigue estos pasos para desplegar el proyecto en tu entorno local:

1. Base de Datos (MySQL)
Localiza el volcado de datos en la carpeta /database/mapaderiders.sql.

Impórtalo en tu gestor (Workbench, phpMyAdmin) para generar las tablas y datos iniciales.

2. Backend (Spring Boot)
Importa la carpeta /backend en tu IDE (recomendado Eclipse o IntelliJ).

Configura tus credenciales de MySQL en el archivo:
src/main/resources/application.properties

Ejecuta como Spring Boot App. El servidor levantará en: http://localhost:8080.

3. Frontend
Accede a la carpeta /frontend.

Abre el archivo index.html en tu navegador.
