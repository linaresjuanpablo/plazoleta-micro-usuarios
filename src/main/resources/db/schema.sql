-- Script to create the bd_usuarios database schema
-- Run this script manually before starting the application with ddl-auto: validate

CREATE DATABASE IF NOT EXISTS bd_usuarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_usuarios;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL UNIQUE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    phone VARCHAR(13) NOT NULL,
    birth_date DATE NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert initial roles
INSERT IGNORE INTO roles (name) VALUES
    ('ADMINISTRADOR'),
    ('PROPIETARIO'),
    ('EMPLEADO'),
    ('CLIENTE');
