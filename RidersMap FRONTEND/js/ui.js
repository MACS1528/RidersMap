/** UI.JS - Renderizado de Interfaz */

function actualizarInterfaz() {
    renderizar('motosList', datosCache.motos, 'motos');
    renderizar('rutasList', datosCache.rutas, 'rutas');
    renderizar('meetupsList', datosCache.encuentros, 'encuentros');
    
    // --- REFUERZO: Gestión de visibilidad del buscador para ADMIN ---
    const navSearch = document.getElementById('navAdminSearch');
    if (isAdmin && navSearch) {
        navSearch.style.display = 'block';
    } else if (navSearch) {
        navSearch.style.display = 'none';
    }

    if (isAdmin) renderizarUsuarios(datosCache.usuarios);
}

function renderizar(id, lista, tipo) {
    const contenedor = document.getElementById(id);
    if (!contenedor) return;
    const miId = Number(localStorage.getItem('userId'));
    const miNombreSesion = (localStorage.getItem('userName') || "").toLowerCase();

    const listaFiltrada = lista.filter(item => {
        if (!filtroBusqueda) return true;
        const contenido = JSON.stringify(item).toLowerCase();
        return contenido.includes(filtroBusqueda);
    });

    contenedor.innerHTML = listaFiltrada.map(item => {
        const ownerId = item.usuario?.id || item.creador?.id || item.userId;
        const ownerName = item.usuario?.nombre || item.creador?.nombre || "Rider";
        
        // Lógica de detección: coincidencia de ID, coincidencia de nombre o marca como "Rider" por defecto
        const esMio = (Number(ownerId) === miId) || 
                      (ownerName.toLowerCase() === miNombreSesion) ||
                      (ownerName.toLowerCase() === "rider");

        if (tipo === 'motos') {
            return `<div class="moto-card">
                <div style="flex-grow: 1;">
                    <span class="moto-brand">${item.marca.toUpperCase()}</span>
                    <h3 style="color:var(--naranja); margin: 5px 0;">${item.modelo}</h3>
                    <p style="font-size: 0.9rem; color: #bbb;">CC: <b style="color:white;">${item.cilindrada || '--'}</b></p>
                    <p>👤 <span style="color:white; font-size: 0.8rem;">${ownerName.toUpperCase()}</span></p>
                </div>
                <div class="card-actions">
                    ${(isAdmin || esMio) ? `<button onclick="eliminarItem('motos', ${item.id})" class="btn btn-rojo">🗑️ BORRAR</button>` : ''}
                </div>
            </div>`;
        }

        if (tipo === 'encuentros') {
            const asistentes = item.asistentes || [];
            const yaApuntado = asistentes.some(a => Number(a.id) === miId);
            const nombresAsistentes = asistentes.length > 0 
                ? asistentes.map(a => `<span class="rider-name">${a.nombre}</span>`).join(', ')
                : 'Esperando riders...';

            return `<div class="meetup-card" style="border-left: 4px solid var(--verde);">
                <div style="flex-grow: 1;">
                    <h3 class="meetup-title">${item.titulo}</h3>
                    <p class="meetup-description">${item.descripcion || 'Sin detalles.'}</p>
                    
                    <p style="font-size: 0.8rem; color: #888; margin-bottom: 10px;">
                        <i class="fas fa-crown" style="color: var(--naranja);"></i> Creado por: 
                        <span style="color: var(--verde); font-weight: bold;">${ownerName.toUpperCase()}</span>
                    </p>

                    <div class="asistentes-container">
                        <label class="asistentes-label">🔥 RIDERS:</label>
                        <div class="asistentes-list">${nombresAsistentes}</div>
                    </div>
                </div>
                <div class="card-actions">
                    ${(!esMio && !yaApuntado) ? `<button onclick="this.disabled=true; this.innerText='...'; unirmeEncuentro(${item.id})" class="btn btn-verde">➕ UNIRME</button>` : ''}
                    ${yaApuntado ? `<button onclick="this.disabled=true; this.innerText='...'; cancelarAsistencia(${item.id})" class="btn" style="background:#e67e22; color:white;">❌ CANCELAR</button>` : ''}
                    ${(isAdmin || esMio) ? `<button onclick="eliminarItem('encuentros', ${item.id})" class="btn btn-rojo">🗑️ BORRAR</button>` : ''}
                </div>
            </div>`;
        }

        if (tipo === 'rutas') {
            const d = item.distanciaKm || item.distancia || item.kilometros || 0;
            const kms = d > 500 ? (d / 1000).toFixed(1) : Number(d).toFixed(1);

            return `<div class="route-card">
                <div style="flex-grow: 1;">
                    <h4 style="color: var(--naranja);">📍 ${item.nombre}</h4>
                    <p style="margin: 5px 0; color: #2ecc71; font-weight: bold; font-size: 1.1rem;">🏁 ${kms} km</p>
                    <p style="font-size: 0.85rem; color: #888;">Rider: ${ownerName}</p>
                </div>
                <div class="card-actions">
                    ${(isAdmin || esMio) ? `<button onclick="eliminarItem('rutas', ${item.id})" class="btn btn-rojo">🗑️ BORRAR</button>` : ''}
                </div>
            </div>`;
        }
    }).join('');

    if (tipo === 'rutas' && typeof actualizarMapa === 'function') actualizarMapa(listaFiltrada);
}

function renderizarUsuarios(usuarios) {
    const contenedor = document.getElementById('usuariosList');
    if (!contenedor) return;

    // --- FILTRADO DE USUARIOS ---
    const usuariosFiltrados = usuarios.filter(u => {
        if (!filtroBusqueda) return true;
        // Buscamos coincidencia en nombre o email
        const matchNombre = u.nombre.toLowerCase().includes(filtroBusqueda);
        const matchEmail = u.email.toLowerCase().includes(filtroBusqueda);
        return matchNombre || matchEmail;
    });

    contenedor.innerHTML = usuariosFiltrados.map(u => `
        <div class="user-card" style="border: 1px solid #444; padding: 15px; border-radius: 8px; background: #1a1a1a; margin-bottom:10px;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div>
                    <h4 style="margin:0; color: var(--naranja);">${u.nombre.toUpperCase()}</h4>
                    <small style="color: #888;">${u.email}</small>
                </div>
                <select onchange="cambiarRol(${u.id}, this.value)" style="background: #333; color: white;">
                    <option value="ROLE_USER" ${u.rol === 'USER' || u.rol === 'ROLE_USER' ? 'selected' : ''}>USER</option>
                    <option value="ROLE_ADMIN" ${u.rol === 'ADMIN' || u.rol === 'ROLE_ADMIN' ? 'selected' : ''}>ADMIN</option>
                </select>
            </div>
            <button onclick="eliminarItem('usuarios', ${u.id})" class="btn btn-rojo" style="width: 100%; margin-top: 10px;">ELIMINAR</button>
        </div>
    `).join('');
}

function filtrarContenido(valor) {
    filtroBusqueda = valor.toLowerCase();
    actualizarInterfaz();
}

function openModal(id) { document.getElementById(id).style.display = 'block'; }
function closeModal(id) { document.getElementById(id).style.display = 'none'; }
function logout() { localStorage.clear(); window.location.href = 'index.html'; }