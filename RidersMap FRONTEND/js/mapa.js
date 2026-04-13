/** MAPA.JS - Leaflet y GPS */

let map, gpsInterval, puntosGPS = [], polyline, tracking = false;
let marcadores = [];

// Variables para simular la posición y el marcador móvil
let simLat, simLng;
let motoMarker; 
let tramoActual = []; // Array de puntos de la calle actual
let indiceTramo = 0;   // Punto actual dentro del tramo

// Icono actualizado para usar el estilo CSS personalizado y emoji de moto
const motoIcon = L.divIcon({
    html: `<div class="custom-moto-icon-inner">🏍️</div>`,
    className: 'leaflet-marker-icon',
    iconSize: [38, 38],
    iconAnchor: [19, 19]
});

function inicializarMapa() {
    try {
        map = L.map('map').setView([40.41, -3.70], 6);
        
        // Capa base de OpenStreetMap
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

        // NUEVO: Capa de TRÁFICO en tiempo real (Google Hybrid Traffic)
        L.tileLayer('https://{s}.google.com/vt/lyrs=m,traffic&x={x}&y={y}&z={z}', {
            maxZoom: 20,
            subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
        }).addTo(map);

        solicitarUbicacionInicial();

        // Escuchar cambios de pantalla completa para ajustar el mapa automáticamente
        document.addEventListener('fullscreenchange', () => {
            const btn = document.getElementById('btn-fs');
            
            // Si el usuario sale de pantalla completa (ya sea con Esc o con el botón)
            if (!document.fullscreenElement) {
                if(btn) btn.innerText = "⛶ PANTALLA COMPLETA";
                // Pequeño retardo para que el navegador termine de redimensionar la ventana
                setTimeout(() => { map.invalidateSize(); }, 300);
            } else {
                if(btn) btn.innerText = "✖ SALIR";
                setTimeout(() => { map.invalidateSize(); }, 300);
            }
        });

    } catch (e) { console.error("Error al cargar Leaflet"); }
}

// NUEVA FUNCIÓN: Toggle Pantalla Completa
function toggleMapFullscreen() {
    const mapElement = document.getElementById('map');
    const btn = document.getElementById('btn-fs');

    if (!document.fullscreenElement) {
        if (mapElement.requestFullscreen) {
            mapElement.requestFullscreen();
        } else if (mapElement.webkitRequestFullscreen) {
            mapElement.webkitRequestFullscreen();
        } else if (mapElement.mozRequestFullScreen) {
            mapElement.mozRequestFullScreen();
        }
        // El texto del botón se cambia ahora automáticamente mediante el event listener de 'fullscreenchange'
    } else {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        } else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        }
    }
}

function solicitarUbicacionInicial() {
    navigator.geolocation.getCurrentPosition(pos => {
        const { latitude, longitude } = pos.coords;
        simLat = latitude;
        simLng = longitude;
        map.setView([latitude, longitude], 13);
        
        // Creamos el marcador que se va a mover con el nuevo icono
        motoMarker = L.marker([latitude, longitude], { icon: motoIcon }).addTo(map).bindPopup("Estás aquí").openPopup();

        // NUEVO: Llamamos a la función del clima al obtener la ubicación
        obtenerClima(latitude, longitude);
    });
}

// NUEVA FUNCIÓN: Obtener datos de OpenWeatherMap
async function obtenerClima(lat, lon) {
    const apiKey = '671f725237130b6bc6016dd1eef6b8dd';
    const url = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&lang=es&appid=${apiKey}`;

    try {
        const response = await fetch(url);
        const data = await response.json();
        if (data.cod === 200) {
            const temp = Math.round(data.main.temp);
            const desc = data.weather[0].description;
            const icono = data.weather[0].icon;
            
            document.getElementById('weather-info').innerHTML = `
                <img src="https://openweathermap.org/img/wn/${icono}@2x.png" alt="clima">
                <div style="font-size: 1.1rem; font-weight: bold; margin-top: -5px;">${temp}°C</div>
                <div style="font-size: 0.7rem; text-transform: capitalize; opacity: 0.8;">${desc}</div>
            `;
        }
    } catch (e) {
        console.error("Error Clima:", e);
        document.getElementById('weather-info').innerText = "Error clima";
    }
}

function toggleGPS() {
    const btn = document.getElementById('btnGPS');
    tracking = !tracking;
    if (tracking) {
        btn.innerText = "🛑 PARAR RUTA";
        // Aplicamos el color rojo directamente para feedback visual inmediato
        btn.style.setProperty('background', 'var(--rojo)', 'important');
        
        // Inicializamos puntos
        puntosGPS = [[simLat, simLng]]; 
        tramoActual = [];
        indiceTramo = 0;
        
        if (polyline) map.removeLayer(polyline);
        polyline = L.polyline(puntosGPS, { color: 'var(--naranja)', weight: 5, opacity: 0.8 }).addTo(map);
        
        // Movimiento cada segundo para mayor fluidez
        gpsInterval = setInterval(rastrearSimulado, 1000);
    } else {
        btn.innerText = "🏁 INICIAR RUTA";
        // Volvemos al verde cuando paramos
        btn.style.setProperty('background', 'var(--verde)', 'important');
        clearInterval(gpsInterval);
        finalizarRuta();
    }
}

// FUNCIÓN ACTUALIZADA: Navegación aleatoria por tramos reales
async function rastrearSimulado() {
    // Si no tenemos tramo o hemos llegado al final del actual, buscamos una nueva dirección
    if (tramoActual.length === 0 || indiceTramo >= tramoActual.length) {
        await calcularSiguienteTramoAleatorio();
    }

    if (tramoActual.length > 0) {
        const punto = tramoActual[indiceTramo];
        simLng = punto[0];
        simLat = punto[1];
        const p = [simLat, simLng];

        puntosGPS.push(p);
        
        if (motoMarker) motoMarker.setLatLng(p);
        polyline.setLatLngs(puntosGPS);
        
        // Centramos el mapa suavemente en el marcador
        map.panTo(p);

        indiceTramo++; // Avanzamos al siguiente metro de la calle
    }
}

// Busca una dirección aleatoria en el mapa y obtiene la ruta por calles hasta allí
async function calcularSiguienteTramoAleatorio() {
    // Generamos un destino a unos 1.5km de distancia para que el tramo sea estable
    const offsetLat = (Math.random() - 0.5) * 0.02;
    const offsetLng = (Math.random() - 0.5) * 0.02;
    const destLat = simLat + offsetLat;
    const destLng = simLng + offsetLng;

    const url = `https://router.project-osrm.org/route/v1/driving/${simLng},${simLat};${destLng},${destLat}?overview=full&geometries=geojson`;
    
    try {
        const response = await fetch(url);
        const data = await response.json();
        if (data.routes && data.routes.length > 0) {
            tramoActual = data.routes[0].geometry.coordinates;
            indiceTramo = 0;
        }
    } catch (e) {
        console.error("Error en OSRM:", e);
        // Fallback lineal si falla la API
        tramoActual = [[simLng + 0.0005, simLat + 0.0005]];
        indiceTramo = 0;
    }
}

function rastrear() {
    navigator.geolocation.getCurrentPosition(pos => {
        const p = [pos.coords.latitude, pos.coords.longitude];
        puntosGPS.push(p);
        polyline.setLatLngs(puntosGPS);
        map.panTo(p);
    });
}

async function finalizarRuta() {
    const nombre = prompt("Nombre de la ruta:");
    if (!nombre || puntosGPS.length < 2) return;
    
    let metrosTotales = 0;
    for (let i = 0; i < puntosGPS.length - 1; i++) {
        const p1 = L.latLng(puntosGPS[i][0], puntosGPS[i][1]);
        const p2 = L.latLng(puntosGPS[i+1][0], puntosGPS[i+1][1]);
        metrosTotales += p1.distanceTo(p2);
    }
    
    const totalKm = parseFloat((metrosTotales / 1000).toFixed(2));

    const ruta = {
        nombre: nombre,
        latInicio: puntosGPS[0][0],
        lngInicio: puntosGPS[0][1],
        latFin: puntosGPS[puntosGPS.length - 1][0],
        lngFin: puntosGPS[puntosGPS.length - 1][1],
        distanciaKm: totalKm,
        puntos: puntosGPS,
        userId: localStorage.getItem('userId')
    };

    await fetch(`${API_BASE_URL}/rutas`, {
        method: 'POST',
        headers: { 'Authorization': auth, 'Content-Type': 'application/json' },
        body: JSON.stringify(ruta)
    });
    cargarSistema();
}

function actualizarMapa(rutas) {
    marcadores.forEach(m => map.removeLayer(m));
    marcadores = [];
    rutas.forEach(r => {
        if (r.latInicio) {
            const m = L.marker([r.latInicio, r.lngInicio]).addTo(map).bindPopup(`<b>${r.nombre}</b><br>${r.distanciaKm} km`);
            marcadores.push(m);
            
            if (r.latFin) {
                const lineaRuta = L.polyline([[r.latInicio, r.lngInicio], [r.latFin, r.lngFin]], {
                    color: '#3498db',
                    weight: 3,
                    opacity: 0.6,
                    dashArray: '5, 10'
                }).addTo(map);
                marcadores.push(lineaRuta);
            }
        }
    });
}