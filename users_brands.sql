CREATE DATABASE IF NOT EXISTS autodrive_db;
USE autodrive_db;

CREATE TABLE IF NOT EXISTS users (
    user_id    INT            NOT NULL AUTO_INCREMENT,
    full_name  VARCHAR(100)   NOT NULL,
    email      VARCHAR(150)   NOT NULL,
    password   VARCHAR(255)   NOT NULL,
    phone      VARCHAR(20)    DEFAULT NULL,
    role       ENUM('admin','customer') NOT NULL DEFAULT 'customer',
    created_at DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active  TINYINT(1)     NOT NULL DEFAULT 1,
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (email)
);
CREATE INDEX idx_users_role ON users (role);
CREATE INDEX idx_users_email ON users (email);


CREATE TABLE IF NOT EXISTS brands (
    brand_id   INT            NOT NULL AUTO_INCREMENT,
    brand_name VARCHAR(100)   NOT NULL,
    country    VARCHAR(100)   DEFAULT NULL,
    logo_url   VARCHAR(255)   DEFAULT NULL,
    CONSTRAINT pk_brands PRIMARY KEY (brand_id),
    CONSTRAINT uq_brands_name UNIQUE (brand_name)
);
CREATE INDEX idx_brands_name ON brands (brand_name);
