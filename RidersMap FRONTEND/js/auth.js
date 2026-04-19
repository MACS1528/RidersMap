/** AUTH.JS - Gestión de Acceso y Registro */

const API_BASE_URL = "http://localhost:8080/api";

async function login() {
    const email = document.getElementById('loginEmail').value;
    const pass = document.getElementById('loginPass').value; 
    const auth = 'Basic ' + btoa(email + ":" + pass);

    try {
        const resp = await fetch(`${API_BASE_URL}/usuarios/perfil/${email}`, {
            headers: { 'Authorization': auth }
        });
        if (resp.ok) {
            // --- REFUERZO: Capturamos los datos que faltaban ---
            const usuario = await resp.json();
            
            localStorage.setItem('userAuth', auth);
            localStorage.setItem('userEmail', email);
            localStorage.setItem('userId', usuario.id);     // Guardamos ID real
            localStorage.setItem('userName', usuario.nombre); // Guardamos Nombre real
            localStorage.setItem('userRol', usuario.rol);   // Guardamos Rol real
            
            window.location.href = 'dashboard.html';
        } else { 
            alert("Credenciales incorrectas. Verifica tu email y contraseña."); 
        }
    } catch (e) { 
        alert("Error de conexión con el servidor. ¿Está el backend encendido?"); 
    }
}

function abrirModalRegistro() {
    // CAMBIO: Usamos 'flex' en lugar de 'block' para que el CSS de centrado funcione
    document.getElementById('modalRegistro').style.display = 'flex';
}

function cerrarModalRegistro() {
    document.getElementById('modalRegistro').style.display = 'none';
    
    // Limpieza de campos opcional para cuando el usuario vuelve atrás
    document.getElementById('regNombre').value = "";
    document.getElementById('regEmail').value = "";
    document.getElementById('regPass').value = "";
    document.getElementById('regPassConfirm').value = "";
    
    // Limpiar mensaje de error al cerrar
    const errorDiv = document.getElementById('registroError');
    if(errorDiv) { errorDiv.style.display = 'none'; errorDiv.innerText = ""; }
}

async function registrarUsuario() {
    const nombre = document.getElementById('regNombre').value;
    const email = document.getElementById('regEmail').value;
    const pass = document.getElementById('regPass').value;
    const passConfirm = document.getElementById('regPassConfirm').value;
    const errorDiv = document.getElementById('registroError');

    // Función interna para mostrar errores en el modal
    const mostrarError = (msg) => {
        if(errorDiv) {
            errorDiv.innerText = msg;
            errorDiv.style.display = 'block';
        } else {
            alert(msg);
        }
    };

    if (!nombre || !email || !pass || !passConfirm) {
        mostrarError("Por favor, rellena todos los campos");
        return;
    }

    // --- VALIDACIÓN DE EMAIL (Debe tener @ y un punto después) ---
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        mostrarError("Email no válido (ejemplo: usuario@test.com)");
        return;
    }

    // --- VALIDACIÓN DE CONTRASEÑA (Mínimo 4 caracteres) ---
    if (pass.length < 4) {
        mostrarError("La contraseña debe tener al menos 4 caracteres");
        return;
    }

    if (pass !== passConfirm) {
        mostrarError("Las contraseñas no coinciden");
        return;
    }

    const nuevoUsuario = {
        nombre: nombre,
        email: email,
        password: pass,
        rol: 'RIDER' 
    };

    try {
        const resp = await fetch(`${API_BASE_URL}/usuarios/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nuevoUsuario)
        });

        if (resp.ok) {
            alert("¡Cuenta creada con éxito! Ya puedes iniciar sesión.");
            cerrarModalRegistro();
            document.getElementById('loginEmail').value = email;
        } else {
            mostrarError("No se pudo completar el registro. ¿Email ya en uso?");
        }
    } catch (e) {
        alert("Error de conexión al registrar.");
    }
}

window.onclick = function(event) {
    const modal = document.getElementById('modalRegistro');
    if (event.target == modal) cerrarModalRegistro();
}
