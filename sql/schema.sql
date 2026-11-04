-- =====================================================
-- PUNTO DE VENTA - Script de Base de Datos
-- Base: sistema_punto_venta
-- Proyecto Escolar 2025
-- =====================================================

CREATE DATABASE IF NOT EXISTS sistema_punto_venta;
USE sistema_punto_venta;

-- Tabla de usuarios del sistema
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN','VENDEDOR') DEFAULT 'VENDEDOR',
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de productos
CREATE TABLE IF NOT EXISTS productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de ventas (encabezado)
CREATE TABLE IF NOT EXISTS ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    folio VARCHAR(20) NOT NULL UNIQUE,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT NOT NULL,
    subtotal DECIMAL(10,2) DEFAULT 0.00,
    iva DECIMAL(10,2) DEFAULT 0.00,
    total DECIMAL(10,2) DEFAULT 0.00,
    observaciones TEXT,
    estatus ENUM('COMPLETADA','CANCELADA','PENDIENTE') DEFAULT 'PENDIENTE',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla detalle de venta (productos por venta)
CREATE TABLE IF NOT EXISTS detalle_venta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- DATOS DE PRUEBA
-- =====================================================

-- Usuarios (contraseñas hasheadas con SHA-256)
INSERT INTO usuarios (nombre, usuario, contrasena, rol) VALUES
('Administrador', 'admin', SHA2('admin123', 256), 'ADMIN'),
('María López', 'vendedor', SHA2('venta123', 256), 'VENDEDOR');

-- Productos de ejemplo
INSERT INTO productos (codigo, nombre, precio_unitario, stock) VALUES
('PROD-001', 'Coca-Cola 600ml', 18.00, 50),
('PROD-002', 'Sabritas Original 45g', 15.50, 30),
('PROD-003', 'Gansito Marinela', 12.00, 40),
('PROD-004', 'Cuaderno Profesional 100H', 45.00, 25),
('PROD-005', 'Pluma BIC Azul', 8.50, 100),
('PROD-006', 'Agua Ciel 1L', 14.00, 60),
('PROD-007', 'Galletas Oreo 114g', 22.50, 35),
('PROD-008', 'Resistol Escolar 100ml', 28.00, 20);

SELECT '✅ Base de datos configurada correctamente' AS resultado;
