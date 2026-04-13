// Configuración Global de la API
const API_BASE_URL = "http://localhost:8080/api";
const auth = localStorage.getItem('userAuth');
const email = localStorage.getItem('userEmail');

// Estado global de la aplicación
let isAdmin = false;
let filtroBusqueda = "";
let datosCache = { motos: [], rutas: [], encuentros: [], usuarios: [] };