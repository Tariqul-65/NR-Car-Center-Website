CREATE TABLE IF NOT EXISTS users (
    user_id    INT            NOT NULL AUTO_INCREMENT,
    full_name  VARCHAR(100)   NOT NULL,
    email      VARCHAR(150)   NOT NULL,
    password   VARCHAR(255)   NOT NULL,
    phone      VARCHAR(20)    DEFAULT NULL,
    role       ENUM('admin','customer')
                              NOT NULL DEFAULT 'customer',
    created_at DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active  TINYINT(1)     NOT NULL DEFAULT 1,
 
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE INDEX idx_users_role ON users (role);

CREATE INDEX idx_users_email ON users (email);

INSERT INTO users (full_name, email, password, phone, role)
VALUES
    ('Admin User',   'admin@autodrive.com',
     '$2b$12$hashedPasswordHere', '01700000001', 'admin'),
    ('Tariqul Islam','tariqul@example.com',
     '$2b$12$hashedPasswordHere', '01800000002', 'customer'),
    ('Ayesha Siddiq','ayesha@example.com',
     '$2b$12$hashedPasswordHere', '01900000003', 'customer')

     SELECT user_id, full_name, email, phone
FROM   users
WHERE  role = 'customer' AND is_active = 1
ORDER  BY full_name;

SELECT user_id, full_name, password, role
FROM   users
WHERE  email = 'tariqul@example.com' AND is_active = 1;

SELECT role, COUNT(*) AS total
FROM   users
GROUP  BY role;

UPDATE users SET is_active = 0 WHERE user_id = 2;

CREATE TABLE IF NOT EXISTS brands (
    brand_id   INT            NOT NULL AUTO_INCREMENT,
    brand_name VARCHAR(100)   NOT NULL,
    country    VARCHAR(100)   DEFAULT NULL,
    logo_url   VARCHAR(255)   DEFAULT NULL,
 
    CONSTRAINT pk_brands PRIMARY KEY (brand_id),
    CONSTRAINT uq_brands_name UNIQUE (brand_name)
);

CREATE INDEX idx_brands_name ON brands (brand_name);

INSERT INTO brands (brand_name, country, logo_url)
VALUES
    ('Toyota',   'Japan',  '/logos/toyota.png'),
    ('Honda',    'Japan',  '/logos/honda.png'),
    ('Ford',     'USA',    '/logos/ford.png'),
    ('BMW',      'Germany','/logos/bmw.png'),
    ('Mercedes', 'Germany','/logos/mercedes.png'),
    ('Nissan',   'Japan',  '/logos/nissan.png');

    SELECT brand_id, brand_name, country
FROM   brands
ORDER  BY brand_name;

SELECT brand_id, brand_name
FROM   brands
WHERE  country = 'Japan';

SELECT b.brand_name, COUNT(v.vehicle_id) AS total_vehicles
FROM   brands b
LEFT   JOIN vehicles v ON b.brand_id = v.brand_id
GROUP  BY b.brand_id, b.brand_name
ORDER  BY total_vehicles DESC;