/** API.JS - Comunicación con el servidor */

async function cargarSistema() {
    try {
        const respUser = await fetch(`${API_BASE_URL}/usuarios/perfil/${email}`, {
            headers: { 'Authorization': auth }
        });
        if (!respUser.ok) throw new Error("Error al obtener perfil");
        
        const usuarioActual = await respUser.json();
        localStorage.setItem('userId', usuarioActual.id); 
        localStorage.setItem('userRole', usuarioActual.rol);
        localStorage.setItem('userName', usuarioActual.nombre);

        // Detecta el rol correctamente aunque tenga el prefijo ROLE_
        isAdmin = (usuarioActual.rol === 'ADMIN' || usuarioActual.rol === 'ADMINISTRADOR' || usuarioActual.rol === 'ROLE_ADMIN');
        document.getElementById('userName').innerText = `RIDER: ${usuarioActual.nombre.toUpperCase()}`;
        
        if (isAdmin) {
            document.getElementById('roleBadge').style.display = 'inline';
            document.getElementById('adminUserSection').style.display = 'block';
            document.getElementById('adminSearchSection').style.display = 'block';
            await cargarAdminData();
        } else {
            const enc = await fetch(`${API_BASE_URL}/encuentros`, { 
                headers: { 'Authorization': auth } 
            }).then(r => r.ok ? r.json() : []);
            
            datosCache.motos = usuarioActual.motos || [];
            datosCache.rutas = usuarioActual.rutas || [];
            datosCache.encuentros = enc;
            actualizarInterfaz();
        }

        if (usuarioActual.rutas?.length > 0) {
            const ultimaRuta = usuarioActual.rutas[usuarioActual.rutas.length - 1];
            if (ultimaRuta.latInicio) map.flyTo([ultimaRuta.latInicio, ultimaRuta.lngInicio], 13);
        }
    } catch (e) { console.error("Error de sincronización:", e); }
}

async function cargarAdminData() {
    try {
        const [m, r, e, u] = await Promise.all([
            fetch(`${API_BASE_URL}/motos`, { headers: { 'Authorization': auth } }).then(res => res.json()),
            fetch(`${API_BASE_URL}/rutas`, { headers: { 'Authorization': auth } }).then(res => res.json()),
            fetch(`${API_BASE_URL}/encuentros`, { headers: { 'Authorization': auth } }).then(res => res.json()),
            fetch(`${API_BASE_URL}/usuarios`, { headers: { 'Authorization': auth } }).then(res => res.json())
        ]);
        datosCache = { motos: m, rutas: r, encuentros: e, usuarios: u };
        actualizarInterfaz();
    } catch (e) { console.error("Error en panel admin:", e); }
}

async function guardarMoto() {
    const moto = {
        marca: document.getElementById('marca').value,
        modelo: document.getElementById('modelo').value,
        cilindrada: parseInt(document.getElementById('cc').value),
        userId: localStorage.getItem('userId')
    };
    if (!moto.marca || !moto.modelo) return alert("Rellena los datos");
    try {
        const resp = await fetch(`${API_BASE_URL}/motos`, {
            method: 'POST',
            headers: { 'Authorization': auth, 'Content-Type': 'application/json' },
            body: JSON.stringify(moto)
        });
        if (resp.ok) { closeModal('motoModal'); await cargarSistema(); }
    } catch (e) { alert("Error al guardar moto"); }
}

async function guardarMeetup() {
    const meetup = {
        titulo: document.getElementById('titM').value,
        descripcion: document.getElementById('desM').value
    };
    if (!meetup.titulo) return alert("Falta título");
    try {
        const resp = await fetch(`${API_BASE_URL}/encuentros`, {
            method: 'POST',
            headers: { 'Authorization': auth, 'Content-Type': 'application/json' },
            body: JSON.stringify(meetup)
        });
        if (resp.ok) { closeModal('meetupModal'); await cargarSistema(); }
    } catch (e) { alert("Error al publicar"); }
}

async function unirmeEncuentro(meetupId) {
    try {
        const resp = await fetch(`${API_BASE_URL}/encuentros/${meetupId}/asistencia`, {
            method: 'POST', 
            headers: { 'Authorization': auth }
        });
        
        if (resp.ok) {
            await cargarSistema(); 
        } else {
            const errorMsg = await resp.text();
            console.error("Error al unirse:", errorMsg);
            actualizarInterfaz();
        }
    } catch (e) { 
        console.error("Error de red:", e); 
        actualizarInterfaz(); 
    }
}

async function cancelarAsistencia(meetupId) {
    if (!confirm("¿Seguro que quieres cancelar tu asistencia?")) {
        actualizarInterfaz();
        return;
    }
    
    try {
        const resp = await fetch(`${API_BASE_URL}/encuentros/${meetupId}/asistencia`, {
            method: 'DELETE', 
            headers: { 'Authorization': auth }
        });
        
        if (resp.ok) {
            await cargarSistema();
        } else {
            console.error("Error al cancelar asistencia");
            actualizarInterfaz();
        }
    } catch (e) { 
        console.error("Error de red:", e); 
        actualizarInterfaz(); 
    }
}

// Función optimizada para el borrado total de usuarios y otros items
async function eliminarItem(tipo, id) {
    const item = (datosCache[tipo] || []).find(i => i.id === id);
    
    const ownerId = item?.usuario?.id || item?.creador?.id || item?.userId || (item?.user ? item.user.id : null);
    const miId = Number(localStorage.getItem('userId'));

    if (!isAdmin && ownerId && Number(ownerId) !== miId) {
        alert("No tienes permiso para borrar este elemento.");
        return;
    }

    // ---  Mensaje detallado según el tipo de borrado ---
    let confirmacion;
    if (tipo === 'usuarios') {
        confirmacion = "¿ESTÁS SEGURO? Se eliminará definitivamente al usuario y TODA su información: motos, rutas creadas y encuentros. Esta acción no se puede deshacer.";
    } else {
        confirmacion = `¿Estás seguro de que quieres eliminar este elemento de ${tipo}?`;
    }

    if (!confirm(confirmacion)) return;
    // --------------------------------------------------------------

    try {
        console.log(`Petición DELETE a: ${API_BASE_URL}/${tipo}/${id}`);
        
        const resp = await fetch(`${API_BASE_URL}/${tipo}/${id}`, { 
            method: 'DELETE', 
            headers: { 
                'Authorization': auth,
                'Content-Type': 'application/json' 
            } 
        });

        if (resp.ok) {
            console.log(`${tipo} eliminado exitosamente.`);
            await cargarSistema();
        } else {
            const errorData = await resp.text();
            console.error("Error en el borrado:", errorData);
            alert("No se pudo eliminar: " + (errorData || "Error de permisos"));
        }
    } catch (e) { 
        console.error("Error de conexión:", e);
        alert("Error de conexión con el servidor"); 
    }
}

async function cambiarRol(id, nuevoRol) {
    try {
        await fetch(`${API_BASE_URL}/usuarios/${id}/rol`, {
            method: 'PUT', headers: { 'Authorization': auth, 'Content-Type': 'application/json' },
            body: JSON.stringify({ rol: nuevoRol })
        });
        cargarSistema();
    } catch (e) { console.error(e); }
}

/** GESTIÓN DE RUTAS */

async function guardarRutaAutomatica(nombre, metros, lat, lng) {
    const calculoKm = parseFloat((metros / 1000).toFixed(2));

    const ruta = {
        nombre: nombre || "Nueva Ruta",
        distancia: calculoKm,
        latInicio: lat,
        lngInicio: lng
    };

    try {
        const resp = await fetch(`${API_BASE_URL}/rutas`, {
            method: 'POST',
            headers: { 
                'Authorization': auth, 
                'Content-Type': 'application/json' 
            },
            body: JSON.stringify(ruta)
        });
        
        if (resp.ok) {
            await cargarSistema(); 
        }
    } catch (e) {
        console.error("Error al guardar la ruta:", e);
    }
}
