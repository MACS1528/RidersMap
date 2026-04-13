RidersMap - Proyecto Fin de Grado
RidersMap es una aplicación web integral diseñada para la comunidad motociclista, permitiendo la gestión de rutas, mantenimiento de vehículos (garaje) y la creación de encuentros sociales entre usuarios.

Instrucciones de Inicio
Para ejecutar este proyecto en un entorno local, sigue estos pasos:

1. Base de Datos (MySQL)
Localiza el archivo .sql en la carpeta /database.
Impórtalo en tu gestor de MySQL (Workbench, phpMyAdmin, etc.) para crear la estructura de tablas necesaria.
2. Backend (Spring Boot)
Abre la carpeta /backend en tu IDE (Se ha desarrollado nativamente en Eclipse).
Asegúrate de que las credenciales de la base de datos en src/main/resources/application.properties coinciden con tu configuración local.
Ejecuta la aplicación como un Spring Boot App. El servidor se iniciará por defecto en el puerto 8080.
3. Frontend
Accede a la carpeta (Se ha desarrollado nativamente en Visual Studio Code) /frontend.
Abre el archivo index.html en cualquier navegador moderno.
Nota: Para probar las funcionalidades, puedes registrarte como nuevo usuario desde la pantalla de "Sign Up".
Tecnologías Utilizadas
Backend: Java, Spring Boot, Spring Security, Maven.
Frontend: HTML5, CSS3 (Modern UI/UX), JavaScript.
Base de Datos: MySQL.
APIs Externas: Leaflet (Mapas), OSRM (Rutas) y OpenWeatherMap (Clima).
Documentación
La documentación técnica detallada, incluyendo el diario de desarrollo y el manual de usuario, se encuentra en el archivo PDF entregado a través del Aula Virtual de CESUR por motivos de privacidad.
