RidersMap - Proyecto Fin de Grado

RidersMap es una aplicación web integral diseñada para la comunidad motociclista, permitiendo la gestión de rutas ( configuradas automáticamente para ver el funcionamiento en un entorno real), garaje (creación y guardado de vehículos) y la creación de encuentros sociales entre usuarios mediante el tablón.

Tecnologías Utilizadas

-Backend: Java, Spring Boot, Spring Security, Maven.

-Frontend: HTML5, CSS3 (Modern UI/UX), JavaScript.

-Base de Datos: MySQL.

-APIs Externas: Leaflet (Mapas), OSRM (Rutas) y OpenWeatherMap (Clima).

Instrucciones de Inicio
Para ejecutar este proyecto en un entorno local, sigue estos pasos:

1. Base de Datos (MySQL) 
Localiza el archivo .sql en la carpeta BASE DE DATOS RIDERSMAP. Impórtalo en tu gestor de MySQL (Workbench, phpMyAdmin, etc.) para crear la estructura de tablas necesaria.

2. Backend (Spring Boot) 
Abre la carpeta mapa-riders-backend/mapa-riders-backend en tu IDE (desarrollado nativamente en Eclipse). Asegúrate de que las credenciales de la base de datos en src/main/resources/application.properties coinciden con tu configuración local. Ejecuta la aplicación . El servidor se iniciará por defecto en el puerto 8080.

3. Frontend 
Accede a la carpeta RidersMap FRONTEND (desarrollado nativamente en Visual Studio Code). Abre el archivo index.html en cualquier navegador moderno.

Para probar las funcionalidades, puede registrarse como nuevo usuario desde la pantalla principal.

*Perfil Administrador: Posee control total sobre la plataforma, incluyendo la moderación y eliminación de registros (motos y rutas) de terceros, así como la gestión integral de cuentas de usuario.

*Perfil Estándar (Rider): Gestión exclusiva de su perfil personal: creación de flota en el garaje, planificación de rutas propias y organización/asistencia a encuentros sociales.

 Documentación
La documentación técnica detallada, incluyendo el diario de desarrollo y el manual de usuario, se encuentra en el archivo PDF entregado a través del Aula Virtual de CESUR por motivos de privacidad.
