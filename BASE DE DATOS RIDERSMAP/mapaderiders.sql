-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-03-2026 a las 09:03:13
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `mapaderiders`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistencias_encuentros`
--

CREATE TABLE `asistencias_encuentros` (
  `encuentro_id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asistencias_encuentros`
--

INSERT INTO `asistencias_encuentros` (`encuentro_id`, `usuario_id`) VALUES
(4, 3),
(4, 4),
(4, 1),
(6, 5),
(6, 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `encuentro`
--

CREATE TABLE `encuentro` (
  `id` bigint(20) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha` datetime(6) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `encuentro`
--

INSERT INTO `encuentro` (`id`, `descripcion`, `fecha`, `titulo`, `usuario_id`) VALUES
(4, 'Ruta de medio día en dirección Marbella,comenzaremos desde Málaga y acabaremos en Marbella Playa.Con parada a comer sobre las 15:00 en el restaurante El Oro en Marbella Costa.', NULL, '07/02/2026 12:00 MÁLAGA  - GASOLINERA LAS CHAPAS', 5),
(6, 'Salimos a quemar ruedaa!!', NULL, '04/01/2026 09:00 MÁLAGA  - CALLE ATARAZANAS 29', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `motos`
--

CREATE TABLE `motos` (
  `id` bigint(20) NOT NULL,
  `anio` int(11) NOT NULL,
  `cilindrada` int(11) NOT NULL,
  `marca` varchar(255) DEFAULT NULL,
  `modelo` varchar(255) DEFAULT NULL,
  `usuario_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `motos`
--

INSERT INTO `motos` (`id`, `anio`, `cilindrada`, `marca`, `modelo`, `usuario_id`) VALUES
(1, 2023, 689, 'Yamaha', 'MT-07', 1),
(4, 0, 1300, 'Suzuki', 'Hayabusa', 4),
(5, 0, 125, 'Zontes', 'C125', 5),
(6, 0, 500, 'Kawasaki ', 'Eliminator', 5),
(7, 0, 450, 'BMW', 'F 450 GS', 3),
(8, 0, 1900, 'Indian', 'Springfield', 4),
(9, 0, 1200, 'Harley Davidson', 'Sporters S', 3),
(11, 0, 500, 'Honda ', 'Rebel Special Edition', 6),
(12, 0, 1200, 'Indian', 'Scout Bobber', 5),
(13, 0, 1900, 'Indian', 'Persuit Dark Horse', 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ruta`
--

CREATE TABLE `ruta` (
  `id` bigint(20) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `distancia_km` double NOT NULL,
  `lat_fin` double NOT NULL,
  `lat_inicio` double NOT NULL,
  `lng_fin` double NOT NULL,
  `lng_inicio` double NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `creador_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ruta`
--

INSERT INTO `ruta` (`id`, `descripcion`, `distancia_km`, `lat_fin`, `lat_inicio`, `lng_fin`, `lng_inicio`, `nombre`, `creador_id`) VALUES
(2, 'Curvas cerradas y vistas espectaculares', 65.5, 40.5921, 40.4167, -4.1483, -3.7033, 'Ruta por la Sierra', 1),
(13, NULL, 1.2, 36.72651999999998, 36.71652, -4.302422435365453, -4.3074224353654635, 'Ruta carrefour', 5),
(15, NULL, 14.37, 36.717147, 36.71652, -4.332187, -4.3074224353654635, 'Ruta de las minas de cemento', 3),
(16, NULL, 9.08, 36.72262, 36.71652, -4.313964, -4.3074224353654635, 'Ruta de mañana hacia Carrefour La Cala', 5),
(17, NULL, 8.02, 36.712644, 36.71652, -4.322989, -4.3074224353654635, 'Paseo museo arqueológico', 6),
(18, NULL, 5.56, 36.723051, 36.71652, -4.288514, -4.3074224353654635, 'Paseo campo de futbol', 1),
(19, NULL, 0.56, 36.716391, 36.71652, -4.311531, -4.3074224353654635, 'Urgencia a centro de salud', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `rol` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `email`, `nombre`, `password`, `rol`) VALUES
(1, 'rider@test.com', 'Manuel Herrero', '$2a$10$QUNPRjPT59zZ8azoUbBC3.RowjiIUTW58H5CwPIS5UHldBIH6yd9u', ''),
(2, 'admin@test.com', 'Admin Rider', '$2a$10$tuB2SL9egM9c1mjgaVM8aexYU0bgZodSIt2jG9OHQcPnJhNvxJuEm', 'ADMIN'),
(3, 'rider.rol@test.com', 'Pepa Mariñas', '$2a$10$nYubuC.OXJijBqQ7gkIbS.jWWEAnQpP5IIlJMxDHToZ0zEcqDeSIO', 'ROLE_USER'),
(4, 'Carlos@test.com', 'Carlos Garcia', '$2a$10$lpleYjk5sbTtDOYeZVK4d.R1eEE6spz/R25kMZB04AU02yB/HkkO.', 'ROLE_USER'),
(5, 'Isa_13@test.com', 'Isabel Molina', '$2a$10$EROJ6Ux.ohbdpFQN4iXFIOJ4RS8LVSDmKo0YtABt3ewnlFCvWNg4G', 'ROLE_USER'),
(6, 'miguel15@test.com', 'Miguel Castillo', '$2a$10$E7c9T/OVllA62AbCjS/w.uqhAy6vJ9PlKVP.aZhzpeCupRhmwEeHa', 'ROLE_USER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios_rutas_favoritas`
--

CREATE TABLE `usuarios_rutas_favoritas` (
  `usuario_id` bigint(20) NOT NULL,
  `ruta_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios_rutas_favoritas`
--

INSERT INTO `usuarios_rutas_favoritas` (`usuario_id`, `ruta_id`) VALUES
(1, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asistencias_encuentros`
--
ALTER TABLE `asistencias_encuentros`
  ADD KEY `FKmnflim595sfbbsj7nl3qmev40` (`usuario_id`),
  ADD KEY `FKl5h75rul7an4ol4ejv1bin2da` (`encuentro_id`);

--
-- Indices de la tabla `encuentro`
--
ALTER TABLE `encuentro`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKtoj7mpv87pqm1mi9kg2rb57gu` (`usuario_id`);

--
-- Indices de la tabla `motos`
--
ALTER TABLE `motos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKaxg1447bil7ul9kij3sfe22eo` (`usuario_id`);

--
-- Indices de la tabla `ruta`
--
ALTER TABLE `ruta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKy6bpgvy0hqlfkpbpmr332nu7` (`creador_id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios_rutas_favoritas`
--
ALTER TABLE `usuarios_rutas_favoritas`
  ADD KEY `FKccogl65s7avtmkmmsan981h65` (`ruta_id`),
  ADD KEY `FKefilp4ys6rvo7dh0pnx86r3a2` (`usuario_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `encuentro`
--
ALTER TABLE `encuentro`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `motos`
--
ALTER TABLE `motos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `ruta`
--
ALTER TABLE `ruta`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asistencias_encuentros`
--
ALTER TABLE `asistencias_encuentros`
  ADD CONSTRAINT `FKl5h75rul7an4ol4ejv1bin2da` FOREIGN KEY (`encuentro_id`) REFERENCES `encuentro` (`id`),
  ADD CONSTRAINT `FKmnflim595sfbbsj7nl3qmev40` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Filtros para la tabla `encuentro`
--
ALTER TABLE `encuentro`
  ADD CONSTRAINT `FKtoj7mpv87pqm1mi9kg2rb57gu` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Filtros para la tabla `motos`
--
ALTER TABLE `motos`
  ADD CONSTRAINT `FKaxg1447bil7ul9kij3sfe22eo` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Filtros para la tabla `ruta`
--
ALTER TABLE `ruta`
  ADD CONSTRAINT `FKy6bpgvy0hqlfkpbpmr332nu7` FOREIGN KEY (`creador_id`) REFERENCES `usuario` (`id`);

--
-- Filtros para la tabla `usuarios_rutas_favoritas`
--
ALTER TABLE `usuarios_rutas_favoritas`
  ADD CONSTRAINT `FKccogl65s7avtmkmmsan981h65` FOREIGN KEY (`ruta_id`) REFERENCES `ruta` (`id`),
  ADD CONSTRAINT `FKefilp4ys6rvo7dh0pnx86r3a2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
