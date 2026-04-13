/** APP.JS - Controlador Principal */

document.addEventListener('DOMContentLoaded', async () => {
    // Verificación de seguridad
    if (!auth) { 
        window.location.href = 'index.html'; 
        return; 
    }
    
    // 1. Arrancar Mapa
    inicializarMapa();

    // 2. Cargar datos del servidor
    await cargarSistema();
});